package share;

import java.util.HashMap;

/**
 * Created by wangqiang on 16/10/15.
 * 获取分享相关的各种参数
 */
public abstract class ShareParams {

    //UI相关,主界面以何种方式展示
    public static final int STYLE_VERTICAL_LIST = 1;
    public static final int STYLE_HORIZONTAL    = 2;
    public static final int STYLE_GRID          = 3;

    //TODO 分享到哪里,注意后续添加的时候以2的指数次增加
    public static final int PLATFORM_QQ        = 1;
    public static final int PLATFORM_QZONE     = (1 << 1);
    public static final int PLATFORM_WX        = (1 << 2);
    public static final int PLATFORM_WX_CIRCLE = (1 << 3);
    public static final int ALL_PLATFORM = PLATFORM_QQ | PLATFORM_QZONE | PLATFORM_WX | PLATFORM_WX_CIRCLE;

    /**
     * 分享的结果
     */
    public static final int SHARE_COMPLETE = 0;
    public static final int SHARE_CANCELED = 1;
    public static final int SHARE_ERROR    = -1;

    //分享什么
    /**
     * 默认
     */
    public static final int SHARE_NONE     = -1;
    /**
     * 分享图文
     */
    public static final int SHARE_TEXT_PIC = 0;
    /**
     * 分享纯图片
     */
    public static final int SHARE_PIC      = 1;
    /**
     * 分享音乐
     */
    public static final int SHARE_MUSIC    = 2;
    /**
     * 分享app
     */
    public static final int SHARE_APP      = 3;
    /**
     * 分享视频
     */
    public static final int SHARE_VIDEO    = 4;
    /**
     * 分享网页
     */
    public static final int SHARE_WEBPAGE  = 5;

    static int platformCount       = 4;

    public int getShareType() {
        return SHARE_NONE;
    }


    //-------------------------其他信息-------------------------

    /**
     * 获取平台(需要分享到哪些平台)
     */
    public int getPlatforms() {
        return ALL_PLATFORM;
    }

    /**
     * 分享的消息的摘要
     * @return
     */
    public String getSummary() {
        return "";
    }

    /**
     * 分享的消息的标题
     * @return
     */
    public String getTitle() {
        return "";
    }

    /**
     * 点击分享的消息后跳转的url地址或者其他需要的url可以放在这里
     * @return
     */
    public String getTargetUrl() {
        return "";
    }

    /**
     * 客户端顶部替换返回按钮的文字
     */
    public String getAppName() {
        return "";
    }

    /**
     * 分享的图片的url或者在本地的路径
     */
    public String getImageUrl() {
        return "";
    }

    /**
     * 分享的音乐的链接
     */
    public String getAudioUrl() {
        return "";
    }

    /**
     * 获取appid
     * key:平台id，value:指定平台的appid
     */
    public abstract HashMap<Integer, String> getAppId();

    /**
     * 获取额外数据
     * @return
     */
    public Object getExtra() {
        return null;
    }

    /**
     * 分享结果的回调
     * @param resultCode
     * @param resultMsg
     */
    public abstract void onShareResult(int resultCode, int errorCode, String resultMsg);
}
