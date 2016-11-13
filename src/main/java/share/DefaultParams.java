package share;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangqiang on 16/11/13.
 * 如果使用默认的UI实现，请使用这个类.默认的UI需要提供一个RecyclerView，每个
 * 子View中需提供一个TextView
 */

public abstract class DefaultParams extends ShareParams {

    /**
     * 获取分享界面的content view,用户可自由定制分享的主界面
     */
    public abstract int getContentViewRes();

    /**
     * 获取用于展示各分享渠道的RecyclerView的id
     * 用户的主界面布局中需要提供一个RecyclerView来展示
     */
    public abstract int getRCV();

    /**
     * 获取分享列表的展示方式(水平、垂直、网格)
     */
    public int getStyle() {
        return STYLE_GRID;
    }

    /**
     * 以网格方式展示时的列数，默认一行展示4列
     * @return
     */
    public int getColumnCount() {
        return 4;
    }

    /**
     * 是否展示取消按钮
     */
    public boolean showCancelView() {
        return true;
    }

    /**
     * 获取不同的分享类型对应的图标,key取值为分享渠道对应的ID,
     * 值为对应的背景图资源id
     * @return
     */
    public abstract Map<Integer, Integer> getIconRes();

    /**
     * 获取item的父view
     * @return
     */
    public abstract View getItemContainer();

    /**
     * 获取item view,需要提供一个TextView
     * @param parent item view的父view,即#getItemContainer返回的view
     */
    public abstract TextView getItemView(View parent);

    /**
     * 获取底部按钮的容器view
     */
    public abstract View getBottomContainer();

    /**
     * 获取底部view
     * @param parent 底部view的父view,即#getBottomContainer返回的view
     */
    public abstract View getBottomView(View parent);

    /**
     * 获取分享平台的名称
     */
    public HashMap<Integer, String> getPlatformName() {
        HashMap<Integer, String> names = new HashMap<>();
        names.put(PLATFORM_QZONE, "QQ空间");
        names.put(PLATFORM_QQ,    "QQ");
        names.put(PLATFORM_WX,    "微信");
        names.put(PLATFORM_WX_CIRCLE, "朋友圈");
        return names;
    }

    /**
     * 获取文字和图片的间距
     */
    public int getDrawablePadding(Context context) {
        return Util.dip2px(context, 5);
    }

    /**
     * 分割线
     */
    public RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }
}
