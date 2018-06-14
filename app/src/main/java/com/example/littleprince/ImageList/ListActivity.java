package com.example.littleprince.ImageList;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.example.littleprince.AboutmeActivity;
import com.example.littleprince.BaseActivity;
import com.example.littleprince.Capture.CaptureService;
import com.example.littleprince.DonateActivity;
import com.example.littleprince.Notification.MyNotificationManager;
import com.example.littleprince.R;
import com.example.littleprince.SettingActivity;
import com.example.littleprince.ShotApplication;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 * 显示图片列表Activity，主界面
 */


public class ListActivity extends BaseActivity {

    public static String defaultBucket=new String("Screenshots");
    private static ListActivity listContext;
    private static String curBucket;

    private String TAG = "Service";
    private int result = 0;
    private Intent intent = null;
    private int REQUEST_MEDIA_PROJECTION = 1;
    private MediaProjectionManager mMediaProjectionManager;

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
        //投屏权限
        mMediaProjectionManager = (MediaProjectionManager)getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startIntent();
        //设置默认首页相册
        //setDefaultBucket();
        //checkPermission();
        //Log.d("sadfsadf","SADfsadfasdf");
        if (isGrantExternalRW(this,1)){
            Log.d("asdf","qiuqiunile");
        }

        defaultBucket=bu();

        //检测图片载入框架是否导入，若没有，则导入并初始化
        checkInitImageLoader();

        //在主界面显示ImagesFragment
        ImagesFragment imagesfragment = new ImagesFragment();

        curBucket=defaultBucket;

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content, imagesfragment);

        transaction.commit();



        //通知栏
        MyNotificationManager.showChannel2CustomNotification(getApplicationContext(),defaultBucket);


    }

    public static boolean isGrantExternalRW(Activity activity, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int storagePermission1 = activity.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int storagePermission2 = activity.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (storagePermission1 != PackageManager.PERMISSION_GRANTED ||
                    storagePermission2 != PackageManager.PERMISSION_GRANTED) {
                activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("aaas","aaas hhhhhhhhhhhhhhh");
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("abcding","nopermission");
                        }
                    });
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public String bu(){

//        //权限
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_DENIED) {
//
//                Log.d("permission", "permission denied to SEND_SMS - requesting it");
//                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//
//                requestPermissions(permissions, 1);
//
//            }
//        }

        Cursor cur = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME},null,null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        List<String> buckets=new ArrayList<>();

        if (cur != null) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    buckets.add(cur.getString(0));
                    cur.moveToNext();
                }
            }
            cur.close();
        }

        if (buckets.contains("Screenshots")){
            return "Screenshots";
        } else {
            return buckets.get(0);
        }

    }

    public void setDefaultBucket(){

        Cursor cur = listContext.getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.BUCKET_DISPLAY_NAME},null,null,
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        List<String> buckets=new ArrayList<>();

        if (cur != null) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    buckets.add(cur.getString(0));
                    cur.moveToNext();
                }
            }
            cur.close();
        }

        if (!buckets.contains(ListActivity.defaultBucket)){
            ListActivity.defaultBucket=buckets.get(0);
        }
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startIntent(){
        if(intent != null && result != 0){
            Log.i(TAG, "user agree the application to capture screen");
            //Service1.mResultCode = resultCode;
            //Service1.mResultData = data;
            ((ShotApplication)getApplication()).setResult(result);
            ((ShotApplication)getApplication()).setIntent(intent);
            Intent intent = new Intent(getApplicationContext(), CaptureService.class);
            startService(intent);
            Log.i(TAG, "start service CaptureService");
        }else{
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            //Service1.mMediaProjectionManager1 = mMediaProjectionManager;
            ((ShotApplication)getApplication()).setMediaProjectionManager(mMediaProjectionManager);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            }else if(data != null && resultCode != 0){
                Log.i(TAG, "user agree the application to capture screen");
                //Service1.mResultCode = resultCode;
                //Service1.mResultData = data;
                result = resultCode;
                intent = data;
                ((ShotApplication)getApplication()).setResult(resultCode);
                ((ShotApplication)getApplication()).setIntent(data);
                Intent intent = new Intent(getApplicationContext(), CaptureService.class);
                startService(intent);
                Log.i(TAG, "start service Service1");

//                finish();
            }
        }
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
//            case R.id.setting:
//                Intent intent3=new Intent(ListActivity.this, SettingActivity.class);
//                startActivity(intent3);
//                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
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
        if (curBucket=="※小王子图床※"){
            refreshcloud();
        }else{
            refresh(curBucket);
        }
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

    public void refreshcloud(){
        //在主界面显示CloudImagesFragment
        CloudImagesFragment cloudImagesFragment=new CloudImagesFragment();

        curBucket="※小王子图床※";

        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();

        transaction.replace(android.R.id.content,cloudImagesFragment);

        transaction.commit();

    }

}
