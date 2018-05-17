package com.example.littleprince.FloatImage;

/**
 * Created by zhaoyonghe on 2018/5/2.
 */


import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * 关于悬浮图片的工具类
 */

public class FloatImageUtil {

    //屏幕高
    private static int screenHeight = 0;

    //屏幕宽
    private static int screenWidth = 0;

    //底部状态栏高度
    private static int statusBarHeight=0;


    /**
     *返回屏幕宽
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        if (screenWidth > 0) {
            return screenWidth;
        }

        if (context == null) {
            return 0;
        }

        return context.getResources().getDisplayMetrics().widthPixels;
    }


    /**
     * 获取屏幕高（包括底部虚拟按键）
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (screenHeight > 0) {
            return screenHeight;
        }

        if (context == null) {
            return 0;
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();

        Display display = getWindowManager(context).getDefaultDisplay();

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(displayMetrics);
            } else {
                display.getMetrics(displayMetrics);
            }
            screenHeight = displayMetrics.heightPixels;
        } catch (Exception e) {
            screenHeight = display.getHeight();
        }
        return screenHeight;
    }

    /**
     * 获取底部状态栏高度
     * @param mContext
     * @return
     */
    public static int getStatusBarHeightByReflect(Context mContext) {
        //int sbHeight;
        if (statusBarHeight > 0) {
            return statusBarHeight;
        }
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int sbHeightId = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = mContext.getResources().getDimensionPixelSize(sbHeightId);
        } catch (Exception e1) {
            e1.printStackTrace();
            statusBarHeight = 0;
        }
        return statusBarHeight;
    }


    /**
     * 获取底部状态栏高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = getStatusBarHeightByReflect(context);
        if (statusBarHeight == 0) {
            statusBarHeight = FloatImageUtil.dip2px(context, 30);
        }
        return statusBarHeight;
    }


    /**
     * 获取当前context的windowmanager
     * @param context
     * @return
     */
    public static WindowManager getWindowManager(Context context) {
        if (context == null) {
            return null;
        }

        return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }


    /**
     * dip->px，具体作用尚不明确
     * @param mContext
     * @param dipValue
     * @return
     */
    public static int dip2px(Context mContext, float dipValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

}
