package share;

import android.app.Activity;
import java.util.ArrayList;

/**
 * Created by wangqiang on 16/10/15.
 * 分享管理
 */
public class Share {

    /**
     * 分享适配器
     */
    public interface ShareImpl {
        /**
         * 是否可以处理本次分享请求
         * @param platform 分享的目标平台(QQ/微信/空间...)
         */
        boolean canHandleShareRequest(int platform);

        /**
         * 初始化分享环境
         * @param appid
         * @param context
         */
        void init(String appid, Activity context);

        /**
         * 分享
         * @param activity 上下文
         * @param p 获取用户分享相关的参数
         */
        void share(Activity activity, ShareParams p);
    }

    /**
     * 分享接口
     * @param activity activity
     * @param platform 平台
     * @param p 参数
     */
    public void share(Activity activity, int platform, ShareParams p) {
        mImpl.share(activity, platform, p);
    }

    /**
     * 增加一种分享渠道,返回新增分享渠道的id
     * @param impl 具体的实现
     */
    public int addShareImpl(ShareImpl impl) {
        ShareParams.platformCount++;
        mImpl.addShareImpl(impl);
        return 1 << (ShareParams.platformCount - 1);
    }

    /**
     * 移除某种分享渠道的实现
     */
    public void removeShareImpl(int platform) {
        mImpl.removeImpl(platform);
    }

    private class ImplSet implements ShareImpl {

        public ImplSet() {
            mImplSet.add(new QQ());
            mImplSet.add(new WeChat());
        }

        public void addShareImpl(ShareImpl impl) {
            if (mImplSet.contains(impl)) return;
            mImplSet.add(impl);
        }

        public void removeImpl(int platform) {
            for (ShareImpl impl : mImplSet) {
                if (impl.canHandleShareRequest(platform)) {
                    mImplSet.remove(impl);
                    ShareParams.platformCount--;
                }
            }
        }

        public void share(Activity activity, int platform, ShareParams p) {
            for( ShareImpl impl : mImplSet) {
                if (impl.canHandleShareRequest(platform)) {
                    impl.init(p.getAppId().get(platform), activity);
                    impl.share(activity, p);
                    break;
                }
            }
        }

        @Override
        public boolean canHandleShareRequest(int type) {
            return true;
        }

        @Override
        public void init(String appid, Activity context) {
            //什么都不做
        }

        @Override
        public void share(Activity activity, ShareParams p) {
            //
        }

        ArrayList<ShareImpl> mImplSet = new ArrayList<>();
    }

    private ImplSet mImpl = new ImplSet();
}
