package com.cy.devandroidtools.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * dp、sp 转换为 px 的工具类
 *
 * @author cy
 * @date 2019/12/31.
 */
public class DisplayUtil {
    public static DisplayMetrics metrics;
    /**
     * 将px转成dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将dp转成px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context, float dipValue)
    {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 将px转成sp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2sp(Context context, float pxValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     *  将sp转成px
     * @param context
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue)
    {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 得到density属性
     *
     * @return
     */
    public static float getDensity(Context context) {
        return getDisplayMetrics(context).density;
    }
    /**
     * 获取DisplayMetrics对象
     *
     * @return
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        if (metrics == null) {
            metrics = context.getResources().getDisplayMetrics();
        }
        return metrics;
    }
}
