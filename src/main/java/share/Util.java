package share;

import android.content.Context;

/**
 * Created by wangqiang on 16/11/13.
 */

public class Util {
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
