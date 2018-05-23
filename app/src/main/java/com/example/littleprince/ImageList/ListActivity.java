package com.example.littleprince.ImageList;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.littleprince.AboutmeActivity;
import com.example.littleprince.BaseActivity;
import com.example.littleprince.DonateActivity;
import com.example.littleprince.EditImageActivity;
import com.example.littleprince.Notification.MyNotificationManager;
import com.example.littleprince.R;
import com.example.littleprince.SettingActivity;
import com.example.littleprince.utils.FileUtils;

import java.io.File;
import java.lang.reflect.Method;


/**
 * 显示图片列表Activity，主界面
 */


public class ListActivity extends BaseActivity {


    private static ListActivity listContext;
    private static String curBucket;



    public static ListActivity getListContext(){
        return listContext;
    }

    public static String getCurBucket(){
        return curBucket;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        listContext=this;

        //检测图片载入框架是否导入，若没有，则导入并初始化
        checkInitImageLoader();

        //在主界面显示ImagesFragment
        ImagesFragment imagesfragment = new ImagesFragment();

        curBucket=imagesfragment.bucketName;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content, imagesfragment);

        transaction.commit();

        //通知栏
        MyNotificationManager.showChannel2CustomNotification(getApplicationContext());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menus, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.refresh:
                refresh();
                return true;
            case R.id.bucket_changing:
                return true;
            case R.id.aboutme:
                Intent intent1=new Intent(ListActivity.this, AboutmeActivity.class);
                startActivity(intent1);
                return true;
            case R.id.donate:
                Intent intent2=new Intent(ListActivity.this, DonateActivity.class);
                startActivity(intent2);
                return true;
            case R.id.setting:
                Intent intent3=new Intent(ListActivity.this, SettingActivity.class);
                startActivity(intent3);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        Log.d("fuck","eat fuck");
        if (featureId== Window.FEATURE_ACTION_BAR&& menu!=null){
            if(menu.getClass().getSimpleName().equals("MenuBuilder")){
                try {
                    Method m=menu.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu,true);
                }catch (Exception e){

                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    public void refresh(){
        //在主界面显示ImagesFragment
        ImagesFragment imagesfragment = new ImagesFragment();

        imagesfragment.bucketName=curBucket;

        curBucket=imagesfragment.bucketName;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content, imagesfragment);

        transaction.commit();
    }

    public void refresh(String bucketName){
        //在主界面显示ImagesFragment
        ImagesFragment imagesfragment = new ImagesFragment();

        imagesfragment.bucketName=bucketName;

        curBucket=imagesfragment.bucketName;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content, imagesfragment);

        transaction.commit();
    }



}
