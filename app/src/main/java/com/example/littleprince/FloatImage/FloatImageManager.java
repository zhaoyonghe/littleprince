package com.example.littleprince.FloatImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
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

        //初始化悬浮图像的规格参数，接下来进行调整
        floatImageParams=testinit(context);
        floatImageParams.imagePath=imageItem.getPath();

        //有些图像无法通过cursor的方式得到高宽，必须先提取bitmap才可以
        Uri uri=Uri.parse("file://"+floatImageParams.imagePath);
        Bitmap bp=null;
        try {
            bp= MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(imageItem.getHeight()==0||imageItem.getWidth()==0){
            floatImageParams.height=bp.getHeight();
            floatImageParams.width=bp.getWidth();
        }
        else {
            floatImageParams.height=imageItem.getHeight();
            floatImageParams.width=imageItem.getWidth();
        }

        Log.d("","++++++++++++++++++");
        Log.d("tietuzhiqian",floatImageParams.toString());
        Log.d("","++++++++++++++++++");
        //先设定比例
        floatImageParams.mRatio=(float)floatImageParams.height/floatImageParams.width;

        //设定最大最小宽度
        //最小：能够放在minLevel屏幕高*minLevel屏幕宽中的最大图片
        //最小：能够放在maxLevel屏幕高*maxLevel屏幕宽中的最大图片

        final float minLevel=0.4f;
        final float maxLevel=0.95f;
        final float screenRatio=(float) floatImageParams.screenHeight/floatImageParams.screenWidth;

        if(screenRatio>floatImageParams.mRatio){
            //该图片比例比屏幕矮胖
            floatImageParams.mMinWidth=(int)(minLevel*floatImageParams.screenWidth);
            floatImageParams.mMaxWidth=(int)(maxLevel*floatImageParams.screenWidth);
        }else{
            //该图片比例不比屏幕矮胖
            floatImageParams.mMinWidth=(int)(minLevel*floatImageParams.screenHeight/floatImageParams.mRatio);
            floatImageParams.mMaxWidth=(int)(maxLevel*floatImageParams.screenHeight/floatImageParams.mRatio);
        }

        //不设这个贴图后无法立即拖动，不知道为什么
        floatImageParams.contentWidth=floatImageParams.mMinWidth;
        floatImageParams.width=floatImageParams.mMinWidth;
        floatImageParams.height=(int)(floatImageParams.width*floatImageParams.mRatio);

        Log.d("","++++++++++++++++++");
        Log.d("tietu",floatImageParams.toString());
        Log.d("","++++++++++++++++++");

        //其他设置，准备贴出
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
