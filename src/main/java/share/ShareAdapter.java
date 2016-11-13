package share;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by wangqiang on 16/10/16.
 */
class ShareAdapter extends RecyclerView.Adapter {

    public static final String TAG = "ShareAdapter";

    public static final int TYPE_ITEM = 0;
    public static final int TYPE_TAIL = 1;

    private static final int NONE = -1;

    public void setPlatforms( int targets ) {
        if ((targets & ShareParams.PLATFORM_QQ) != 0) {
            //分享到QQ
            mShares.add(ShareParams.PLATFORM_QQ);
        }
        if ((targets & ShareParams.PLATFORM_WX) != 0) {
            //分享到微信
            mShares.add(ShareParams.PLATFORM_WX);
        }
        if ((targets & ShareParams.PLATFORM_QZONE) != 0) {
            //分享到空间
            mShares.add(ShareParams.PLATFORM_QZONE);
        }
        if ((targets & ShareParams.PLATFORM_WX_CIRCLE) != 0) {
            //分享到微信朋友圈
            mShares.add(ShareParams.PLATFORM_WX_CIRCLE);
        }
        //TODO 其他渠道的分享,以后可以在这里添加

        if (mDlg.mParams.showCancelView()) {
            mShares.add(NONE);
        }
    }

    public ShareAdapter(ShareDialog dialog) {
        mDlg = dialog;
    }

    @Override
    public int getItemViewType(int pos) {
        if (isBotom(pos)) {
            return TYPE_TAIL;
        }
        return TYPE_ITEM;
    }

    public boolean isBotom(int pos) {
        if (mDlg.mParams.showCancelView()) {
            return pos == mShares.size() - 1;
        }
        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //item
            return new ItemViewHolder(mDlg.mParams.getItemContainer());
        } else {
            //底部按钮
            return new TailViewHolder(mDlg.mParams.getBottomContainer());
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (isBotom(position)) {
            //
        } else {
            TextView item = (TextView)(((ItemViewHolder)holder).mItem);
            setItemByPos(item, position);
        }
    }

    @Override
    public int getItemCount() {
        return mShares.size();
    }

    private void setItemByPos(TextView item, int pos) {
        Integer target = mShares.get(pos);
        Map<Integer,Integer> res = mDlg.mParams.getIconRes();
        if (res != null) {
            item.setCompoundDrawablePadding(mDlg.mParams.getDrawablePadding(mDlg.getContext()));
            item.setCompoundDrawablesWithIntrinsicBounds(0, res.get(target), 0, 0);
            item.setText(mDlg.mParams.getPlatformName().get(target));
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(final View itemView) {
            super(itemView);
            mItem = mDlg.mParams.getItemView(itemView);
            mItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int platform = mShares.get(getAdapterPosition());
                    Log.e(TAG, "share to:" + platform);
                    if (mDlg.isShowing()) mDlg.dismiss();
                    if (platform != NONE) {
                        mShare.share(mDlg.mActivity, platform, mDlg.mParams);
                    }
                }
            });
        }
        View mItem;
    }

    class TailViewHolder extends RecyclerView.ViewHolder {

        public TailViewHolder(View itemView) {
            super(itemView);
            mTail = mDlg.mParams.getBottomView(itemView);
            mTail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDlg.isShowing()) {
                        mDlg.dismiss();
                    }
                }
            });
            mTail.setVisibility(mDlg.mParams.showCancelView() ? View.VISIBLE : View.GONE);
        }
        View mTail;
    }

    private ArrayList<Integer> mShares = new ArrayList<>();
    private ShareDialog mDlg;
    private Share mShare = new Share();
}
