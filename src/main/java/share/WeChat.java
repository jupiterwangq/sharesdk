package share;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXTextObject;
import com.tencent.mm.sdk.modelmsg.WXVideoObject;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by wangqiang on 16/11/10.
 * 微信分享的实现
 */

public class WeChat implements Share.ShareImpl {
    @Override
    public boolean canHandleShareRequest(int platform) {
        if ( platform == ShareParams.PLATFORM_WX || platform == ShareParams.PLATFORM_WX_CIRCLE) {
            mFlag = (platform == ShareParams.PLATFORM_WX ? 0 : 1);
            return true;
        }
        return false;
    }

    @Override
    public void init(String appid, Activity context) {
        mApi = WXAPIFactory.createWXAPI(context.getApplicationContext(), appid, true);
        mApi.registerApp(appid);
    }

    @Override
    public void share(Activity activity, final ShareParams p) {
        mApi.handleIntent(activity.getIntent(), new IWXAPIEventHandler() {
            @Override
            public void onReq(BaseReq baseReq) {
                Log.e("WX", "onReq");
            }

            @Override
            public void onResp(BaseResp baseResp) {
                Log.e("WX", "onResp");
                switch (baseResp.errCode) {
                    case BaseResp.ErrCode.ERR_OK:
                        p.onShareResult(ShareParams.SHARE_COMPLETE, baseResp.errCode, "");
                        break;
                    case BaseResp.ErrCode.ERR_USER_CANCEL:
                        p.onShareResult(ShareParams.SHARE_CANCELED, baseResp.errCode, baseResp.errStr);
                        break;
                    case BaseResp.ErrCode.ERR_AUTH_DENIED:
                        p.onShareResult(ShareParams.SHARE_ERROR, baseResp.errCode, baseResp.errStr);
                        break;
                }
            }
        });

        int shareType = p.getShareType();
        if (shareType == ShareParams.SHARE_NONE) {
            return;
        }
        switch (shareType) {
            case ShareParams.SHARE_TEXT_PIC:
                shareText((String)p.getExtra(), mFlag);
                break;
            case ShareParams.SHARE_PIC:
                sharePicture(activity, p, mFlag);
                break;
            case ShareParams.SHARE_VIDEO:
                shareVideo(activity, p, mFlag);
                break;
            case ShareParams.SHARE_WEBPAGE:
                shareWebPage(activity, p, mFlag);
                break;
            default:
        }
    }

    /**
     * 分享纯文本
     * @param txt 要分享的文本
     * @param flag 朋友圈/好友
     */
    protected void shareText(String txt, int flag) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = txt;
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = txt;
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("textshare");
        req.message = msg;
        req.scene = flag;
        mApi.sendReq(req);
    }

    /**
     * 分享图片
     * @param context 上下文
     * @param p 分享的参数
     * @param flag 朋友圈/好友
     */
    protected void sharePicture(Activity context, ShareParams p, int flag) {
        Bitmap bitmap = BitmapFactory.decodeFile(p.getImageUrl());
        WXImageObject imgObj = new WXImageObject(bitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(bitmap, THUMB_SIZE, THUMB_SIZE, true);
        bitmap.recycle();
        msg.thumbData = bitmapToByteArray(thumbBitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("imgshareappdata");
        req.message = msg;
        req.scene = flag;
        mApi.sendReq(req);
    }

    /**
     * 分享网页
     * @param context 上下文
     * @param p 分享参数
     * @param flag 朋友圈/好友
     */
    protected void shareWebPage(Activity context, ShareParams p, int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = p.getTargetUrl();
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title          = p.getTitle();
        msg.description    = p.getSummary();
        Bitmap thumb = BitmapFactory.decodeFile(p.getImageUrl());
        if(thumb == null) {
            //
        } else {
            msg.thumbData = bitmapToByteArray(thumb);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = flag;
        mApi.sendReq(req);
    }

    /**
     * 分享视频
     * @param context 上下文
     * @param p 参数
     * @param flag 朋友圈/好友
     */
    protected void shareVideo(Context context, ShareParams p, int flag) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = p.getTargetUrl();
        WXMediaMessage msg = new WXMediaMessage(video);
        msg.title = p.getTitle();
        msg.description = p.getSummary();
        Bitmap thumb = BitmapFactory.decodeFile(p.getImageUrl());
        Bitmap thumbBitmap = Bitmap.createScaledBitmap(thumb, THUMB_SIZE, THUMB_SIZE, true);
        msg.thumbData = bitmapToByteArray(thumbBitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene   = flag;
        mApi.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }

    private byte[] bitmapToByteArray(Bitmap bmp) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        bmp.recycle();
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private IWXAPI mApi;
    private int mFlag = 0;

    private static final int THUMB_SIZE = 150;
}
