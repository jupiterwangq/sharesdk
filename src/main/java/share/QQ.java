package share;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * Created by wangqiang on 16/10/15.
 * 分享到QQ或者QQ空间
 */
public class QQ implements Share.ShareImpl {

    public static final String TAG = "ShareQQ";

    @Override
    public boolean canHandleShareRequest(int where) {
        if (where == ShareParams.PLATFORM_QQ || where == ShareParams.PLATFORM_QZONE) {
            mTarget = where;
            return true;
        }
        return false;
    }

    @Override
    public synchronized void init(String appid, Activity context) {
        if (!mIsInitied) {
            mIsInitied = true;
            mTencent = Tencent.createInstance(appid, context.getApplicationContext());
        }
    }

    @Override
    public void share(Activity context, final ShareParams p) {
        IUiListener listener = new IUiListener() {
            @Override
            public void onComplete(Object o) {
                Log.e(TAG, "share complete");
                p.onShareResult(ShareParams.SHARE_COMPLETE, 0, "");
            }

            @Override
            public void onError(UiError uiError) {
                Log.e(TAG, "share error:" + uiError.toString());
                p.onShareResult(ShareParams.SHARE_ERROR, uiError.errorCode, uiError.errorMessage);
            }

            @Override
            public void onCancel() {
                Log.e(TAG, "share cancel");
                p.onShareResult(ShareParams.SHARE_CANCELED, 0, "");
            }
        };

        int shareType = p.getShareType();
        if (shareType == ShareParams.SHARE_NONE) {
            return;
        }
        mTencent.shareToQQ(context, getParamsByType(shareType, p), listener);
    }

    protected Bundle getParamsByType(int shareType, ShareParams p) {
        Bundle bundle = null;
        switch (shareType) {
            case ShareParams.SHARE_TEXT_PIC:
                bundle = getTextPicParam(p);
                break;
            case ShareParams.SHARE_PIC:
                bundle = getPicParam(p);
                break;
            case ShareParams.SHARE_MUSIC:
                bundle = getMusicParam(p);
                break;
            case ShareParams.SHARE_APP:
                bundle = getAppParam(p);
                break;
            default:
        }
        if (bundle != null && mTarget == ShareParams.PLATFORM_QZONE) {
            bundle.putInt(QQShare.SHARE_TO_QQ_EXT_INT,  QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        return bundle;
    }

    protected Bundle getTextPicParam(ShareParams p) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, p.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, p.getSummary());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, p.getTargetUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, p.getImageUrl());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, p.getAppName());
        return params;
    }

    protected Bundle getPicParam(ShareParams p) {
        Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, p.getImageUrl());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, p.getAppName());
        return params;
    }

    protected Bundle getMusicParam(ShareParams p) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_AUDIO);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, p.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, p.getSummary());
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, p.getTargetUrl());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, p.getImageUrl());
        params.putString(QQShare.SHARE_TO_QQ_AUDIO_URL, p.getAudioUrl());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, p.getAppName());
        return params;
    }

    protected Bundle getAppParam(ShareParams p) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_APP);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, p.getTitle());
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, p.getSummary());
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, p.getImageUrl());
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, p.getAppName());
        return params;
    }

    private Tencent mTencent;
    private boolean mIsInitied = false;
    private int mTarget = ShareParams.PLATFORM_QQ;
}
