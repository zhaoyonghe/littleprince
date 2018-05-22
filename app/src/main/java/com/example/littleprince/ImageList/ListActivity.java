package com.example.littleprince.ImageList;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.littleprince.BaseActivity;
import com.example.littleprince.EditImageActivity;
import com.example.littleprince.Notification.MyNotificationManager;
import com.example.littleprince.utils.FileUtils;

import java.io.File;


/**
 * 显示图片列表Activity，主界面
 */


public class ListActivity extends BaseActivity {


    private static ListActivity listContext;



    public static ListActivity getListContext(){
        return listContext;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        listContext=this;
        Log.d("fuck",this.toString());

        //检测图片载入框架是否导入，若没有，则导入并初始化
        checkInitImageLoader();

        //在主界面显示ImagesFragment
        Fragment imagesfragment = new ImagesFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content, imagesfragment);

        transaction.commit();

        //通知栏
        MyNotificationManager.showChannel2CustomNotification(getApplicationContext());


    }


    public void refresh(){
        //在主界面显示ImagesFragment
        ImagesFragment imagesfragment = new ImagesFragment();
        //imagesfragment.bucketName="Camera";
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content, imagesfragment);

        transaction.commit();
    }



}
