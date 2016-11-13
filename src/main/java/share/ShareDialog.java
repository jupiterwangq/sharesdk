package share;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by wangqiang on 16/10/16.
 * 分享组件的UI,基于RecyclerView实现
 */
public class ShareDialog extends Dialog {

    /**
     * @param context 上下文
     * 使用此构造函数将展示全部的分享渠道
     */
    public ShareDialog(Activity context) {
        super(context);
        mActivity = context;
    }

    /**
     * 注意,在分享前必须先设置好参数
     * @param p
     */
    public void setShareParams(DefaultParams p) {
        mParams = p;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager wm = (WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        Window w = getWindow();
        setContentView(mParams.getContentViewRes());
        WindowManager.LayoutParams wp = w.getAttributes();
        wp.x = 0;
        wp.gravity = Gravity.BOTTOM;
        wp.width = wm.getDefaultDisplay().getWidth();
        w.setAttributes(wp);
        mGrid = (RecyclerView)findViewById(mParams.getRCV());
        getWindow().setGravity(Gravity.BOTTOM);
        mAdapter = new ShareAdapter(this);
        mAdapter.setPlatforms(mParams.getPlatforms());
        mGrid.setAdapter(mAdapter);
        setRcvByParams();
    }

    private void setRcvByParams() {
        RecyclerView.LayoutManager lm;
        int style = mParams.getStyle();
        if ( style == ShareParams.STYLE_GRID) {
            final GridLayoutManager tmp;
            lm = new GridLayoutManager(getContext(), mParams.getColumnCount());
            tmp = (GridLayoutManager)lm;
            tmp.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

                @Override
                public int getSpanSize(int position) {
                    return mAdapter.isBotom(position) ? tmp.getSpanCount() : 1;
                }
            });
        } else {
            if (style == ShareParams.STYLE_HORIZONTAL) {
                lm = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
            } else {
                lm = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            }
        }
        mGrid.setLayoutManager(lm);
        RecyclerView.ItemDecoration decoration = mParams.getItemDecoration();
        if (decoration != null) {
            mGrid.addItemDecoration(decoration);
        }
    }

    RecyclerView mGrid;
    ShareAdapter mAdapter;

    Activity mActivity;
    DefaultParams mParams;
}
