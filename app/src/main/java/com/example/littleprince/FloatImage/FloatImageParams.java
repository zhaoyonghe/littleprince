package com.example.littleprince.FloatImage;

/**
 * Created by zhaoyonghe on 2018/5/3.
 */

public class FloatImageParams {
    public int width;//窗口的宽
    public int height;//窗口的高
    public int x;//窗口的x坐标
    public int y;//窗口的y坐标
    public int contentWidth;//当前窗口content view的宽度

    public int screenWidth;//屏幕宽度
    public int screenHeight;//屏幕高度
    public int statusBarHeight;//状态栏高度


    //
    public int mMinWidth;//初始宽度
    public int mMaxWidth;//视频最大宽度
    public float mRatio = 1.77f;//窗口高/宽比
    public int videoViewMargin;//视频距离父view的边距

    public String imagePath;


    @Override
    public String toString() {
        return "FloatImageParams{" +
                "width=" + width +
                ", height=" + height +
                ", x=" + x +
                ", y=" + y +
                ", contentWidth=" + contentWidth +
                ", screenWidth=" + screenWidth +
                ", screenHeight=" + screenHeight +
                ", statusBarHeight=" + statusBarHeight +
                ", mMinWidth=" + mMinWidth +
                ", mMaxWidth=" + mMaxWidth +
                ", mRatio=" + mRatio +
                ", videoViewMargin=" + videoViewMargin +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
