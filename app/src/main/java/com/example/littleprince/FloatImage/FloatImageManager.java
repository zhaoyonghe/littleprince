package com.example.littleprince.FloatImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import com.example.littleprince.ImageList.ImageItem;

import java.io.IOException;

/**
 * Created by zhaoyonghe on 2018/5/3.
 */

/**
 * 对悬浮图像进行操作的类
 */
public class FloatImageManager {

    private WindowManager windowManager;

    //悬浮图像的规格参数
    private FloatImageParams floatImageParams;

    //该FloatImageManager掌控的悬浮图像实例
    private IFloatImageView floatImageView;

    //是否已经有悬浮图像
    private boolean isFloatImageShowing=false;

    /**
     * 初始化悬浮图像的规格参数
     * @param context
     * @return
     */
    private FloatImageParams initFloatImageParams(Context context){
        FloatImageParams params=new FloatImageParams();
        int screenWidth = FloatImageUtil.getScreenWidth(context);
        int screenHeight = FloatImageUtil.getScreenHeight(context);
        int statusBarHeight = FloatImageUtil.getStatusBarHeight(context);
        //根据实际宽高和设计稿尺寸比例适应。
        int marginBottom = FloatImageUtil.dip2px(context, 150);
//        if (float_window_type == FW_TYPE_ROOT_VIEW) {
//            marginBottom += statusBarHeight;
//        }
        //设置窗口大小，已view、视频大小做调整
//        int winWidth = LastWindowInfo.getInstance().getWidth();
//        int winHeight = LastWindowInfo.getInstance().getHeight();
        int margin = FloatImageUtil.dip2px(context, 15);
        int width = 30;
//        if (winWidth <= winHeight) {
//            //竖屏比例
//            width = (int) (screenWidth * 1.0f * 1 / 3) + margin;
//        } else {//横屏比例
//            width = (int) (screenWidth * 1.0f / 2) + margin;
//        }
        float ratio = 1.5f;
        int height = (int) (width * ratio);

        //如果上次的位置不为null，则用上次的位置
//        FloatViewParams lastParams = livePlayerWrapper.getLastParams();
//        if (lastParams != null) {
//            params.width = lastParams.width;
//            params.height = lastParams.height;
//            params.x = lastParams.x;
//            params.y = lastParams.y;
//            params.contentWidth = lastParams.contentWidth;
//        } else {
            params.width = width;
            params.height = height;
            params.x = screenWidth - width;
            params.y = screenHeight - height - marginBottom;
            params.contentWidth = width;
//        }

        params.screenWidth = screenWidth;
        params.screenHeight = screenHeight;
//        if (float_window_type == FW_TYPE_ROOT_VIEW) {
//            params.screenHeight = screenHeight - statusBarHeight;// - actionBarHeight;
//        }
        params.videoViewMargin = margin;
        params.mMaxWidth = screenWidth / 2 + margin;
        params.mMinWidth = width;
        params.mRatio = ratio;
        return params;
    }

    /**
     * 初始化悬浮图像的规格参数(测试版)
     * @return
     */
    private FloatImageParams testinit(Context context){
        FloatImageParams floatImageParams=new FloatImageParams();
        floatImageParams.width=405;
        floatImageParams.height=607;
        floatImageParams.x=675;//675
        floatImageParams.y=719;//719
        floatImageParams.contentWidth=405;
        //floatImageParams.screenWidth=1080;
        //floatImageParams.screenHeight=1776;
        floatImageParams.screenWidth=FloatImageUtil.getScreenWidth(context);
        floatImageParams.screenHeight=FloatImageUtil.getScreenHeight(context);
        floatImageParams.statusBarHeight=0;
        floatImageParams.mMinWidth=405;
        floatImageParams.mMaxWidth=585;
        floatImageParams.mRatio=1.5f;
        floatImageParams.videoViewMargin=45;


        return floatImageParams;
    }


    /**
     *
     * @param context
     */
    private void initFloatImage(Context context,ImageItem imageItem){
        //初始化悬浮图像的规格参数
        //
        floatImageParams=testinit(context);
        floatImageParams.imagePath=imageItem.getPath();
        Uri uri=Uri.parse("file://"+floatImageParams.imagePath);
        Bitmap bp=null;
        try {
            bp= MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(imageItem.getHeight()==0||imageItem.getWidth()==0){
            floatImageParams.height=bp.getHeight()/3;
            floatImageParams.width=bp.getWidth()/3;
        }
        else {
            floatImageParams.height=imageItem.getHeight()/3;
            floatImageParams.width=imageItem.getWidth()/3;
        }

        floatImageParams.mRatio=(float)floatImageParams.height/floatImageParams.width;

        if(floatImageParams.height*2>floatImageParams.screenHeight){
            floatImageParams.height=floatImageParams.screenHeight/3;
            floatImageParams.width=(int)(floatImageParams.height/floatImageParams.mRatio);
        }
        if (floatImageParams.width*2>floatImageParams.screenWidth){
            floatImageParams.width=floatImageParams.screenWidth/3;
            floatImageParams.height=(int)(floatImageParams.width*floatImageParams.mRatio);
        }
        floatImageParams.mMinWidth=floatImageParams.width;
        floatImageParams.mMaxWidth=floatImageParams.mMinWidth*2;
        //
        windowManager=FloatImageUtil.getWindowManager(context);
        WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();
        wmParams.packageName=context.getPackageName();
        wmParams.flags=WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                |WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                |WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                //|WindowManager.LayoutParams.FLAG_SCALED
                |WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                |WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;


        //
        if (Build.VERSION.SDK_INT>=26){
            wmParams.type=WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }else{
            wmParams.type=WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }

        wmParams.format= PixelFormat.RGBA_8888;
        wmParams.gravity= Gravity.START|Gravity.TOP;

        wmParams.width=floatImageParams.width;
        wmParams.height=floatImageParams.height;
        wmParams.x=floatImageParams.x;
        wmParams.y=floatImageParams.y;

        floatImageView=new FloatImageView(context,floatImageParams,wmParams);

        try {
            windowManager.addView((View) floatImageView,wmParams);
        }catch(Exception e){
            e.printStackTrace();
        }
        
    }

    /**
     *
     * @param context
     */
    public synchronized void show(Context context,ImageItem imageItem){

        //
        initFloatImage(context.getApplicationContext(),imageItem);
        //
        isFloatImageShowing=true;
    }

    /**
     * 返回当前悬浮图像实例
     * @return
     */
    public IFloatImageView getFloatImageView() {
        return floatImageView;
    }

    /**
     * 关闭当前悬浮图像
     */
    public synchronized void dismissFloatWindow() {
        if (!isFloatImageShowing) {
            return;
        }
        isFloatImageShowing = false;
        try {
            if (windowManager != null && floatImageView != null) {
                windowManager.removeViewImmediate((View) floatImageView);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        floatImageView = null;
        windowManager = null;

    }
}
