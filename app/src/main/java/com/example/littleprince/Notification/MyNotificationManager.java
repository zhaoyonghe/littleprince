package com.example.littleprince.Notification;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RemoteViews;


import com.example.littleprince.BaseActivity;
import com.example.littleprince.Capture.CaptureService;
import com.example.littleprince.ImageList.ImageItem;
import com.example.littleprince.ImageList.ListActivity;
import com.example.littleprince.R;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.support.v4.content.PermissionChecker.checkSelfPermission;


/**
 * Created by Tian on 17/10/21.
 */

public class MyNotificationManager {
    public static final String TAG = "MyNotificationManager";

    private static final int PERMISSION_REQUEST_CODE = 1;

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public static void initNotificationChannel(Context context) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
////
////        notificationManager.createNotificationChannelGroup(new NotificationChannelGroup("a", "a"));
////
////        NotificationChannel channel = new NotificationChannel("1",
////                "Channel1", NotificationManager.IMPORTANCE_DEFAULT);
////        channel.enableLights(true);
////        channel.setLightColor(Color.GREEN);
////        channel.setShowBadge(true);
////
////        notificationManager.createNotificationChannel(channel);
////
////        NotificationChannel channel2 = new NotificationChannel("2",
////                "Channel2", NotificationManager.IMPORTANCE_DEFAULT);
////        channel2.enableLights(true);
////        channel2.setLightColor(Color.RED);
////        channel2.setGroup("a");
////        notificationManager.createNotificationChannel(channel2);
//    }

    public static void showChannel1Notification(Context context) {
        int notificationId = 0x1234;
        Notification.Builder builder = new Notification.Builder(context);

        builder.setSmallIcon(android.R.drawable.edit_text)
                .setContentText("green light");


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        notificationManager.notify(notificationId, notification);
    }


    public static List<ImageItem> images =null;

    public static int currentindex=0;


    public static RemoteViews getDefaultRemoteView(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.notification_layout);//自定义的布局视图
        //按钮点击事件：

        PendingIntent rightIntent = PendingIntent.getBroadcast(context,1,new Intent("right"), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent leftIntent = PendingIntent.getBroadcast(context,1,new Intent("left"), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent jmpIntent = PendingIntent.getActivity(context, 1, new Intent(context, ListActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent putIntent = PendingIntent.getBroadcast(context, 1, new Intent("put"), PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent captureIntent = PendingIntent.getBroadcast(context, 1, new Intent("capture"), PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.btn_next, rightIntent);//点击的id，点击事件
        views.setOnClickPendingIntent(R.id.btn_pre, leftIntent);
        views.setOnClickPendingIntent(R.id.notification_img, jmpIntent);
        views.setOnClickPendingIntent(R.id.btn_put, putIntent);
        views.setOnClickPendingIntent(R.id.btn_capture, captureIntent);
        return views;
    }
    public static void showChannel2CustomNotification(Context context,String BucketName) {
        int notificationId = 0x1236;
        RemoteViews views = getDefaultRemoteView(context);

        images=getImages(context, BucketName);
        currentindex=0;

        views.setImageViewUri(R.id.notification_img, Uri.parse(images.get(currentindex).getPath()));
        //按钮点击事件：
        //创建通知：
        Notification.Builder mBuilder = new Notification.Builder(context);

        mBuilder.setContent(views)//设置布局
                .setOngoing(true)//设置是否常驻,true为常驻
                .setSmallIcon(R.mipmap.ic_littleprince)//设置小图标
                .setTicker("贴图小王子")//设置提示
                .setPriority(Notification.PRIORITY_MAX)//设置优先级
                .setWhen(System.currentTimeMillis())//设置展示时间
                .setContentIntent(PendingIntent.getBroadcast(context,2,new Intent("action.view"),PendingIntent.FLAG_UPDATE_CURRENT));//设置视图点击事件


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;



        notificationManager.notify(notificationId, notification);

    }


    public static void updateImage(Context context,String action){
        int notificationId = 0x1236;

        RemoteViews views = getDefaultRemoteView(context);
        if (images.equals(null)) {
            images=getImages(context,ListActivity.defaultBucket);
        }
        if (action.equals("right")) {
            if(currentindex<images.size()-1){
                currentindex += 1;
            }else{
                return;
            }
        }
        if (action.equals("left")) {
            if(currentindex > 0) {
                currentindex -= 1;
            }else{
                return;
            }
        }
        views.setImageViewUri(R.id.notification_img, Uri.parse(images.get(currentindex).getPath()));
        //按钮点击事件：
        //创建通知：
        Notification.Builder mBuilder = new Notification.Builder(context);

        mBuilder.setContent(views)//设置布局
                .setOngoing(true)//设置是否常驻,true为常驻
                .setSmallIcon(R.mipmap.ic_littleprince)//设置小图标
                .setTicker("贴图小王子")//设置提示
                .setPriority(Notification.PRIORITY_MAX)//设置优先级
                .setWhen(System.currentTimeMillis())//设置展示时间
                .setContentIntent(PendingIntent.getBroadcast(context,2,new Intent("action.view"),PendingIntent.FLAG_UPDATE_CURRENT));//设置视图点击事件


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = mBuilder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;


        notificationManager.notify(notificationId, notification);
    }

    /**
     *
     * @param context
     * @param BucketName
     * @return
     */
    public static List<ImageItem> getImages(Context context,String BucketName){
        //权限
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//
//            if (checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_DENIED) {
//                Log.d("abcding","in mynotificationmanager.java");
//                Log.d("permission", "permission denied to SEND_SMS - requesting it");
//                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
//
//                ActivityCompat.requestPermissions(ListActivity.getListContext(),permissions, PERMISSION_REQUEST_CODE);
//
//            }
//        }


        Cursor cur = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.WIDTH},
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "= ?",
                new String[]{BucketName},
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        images=new ArrayList<>(cur.getCount());

        //查询结果加入List中
        if (cur != null) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    Log.d("", "(((((((((((((((((((((((((");
                    Log.d("DISPLAY_NAME", cur.getString(0));
                    Log.d("DATA", cur.getString(1));
                    Log.d("DATE_TAKEN", cur.getString(2));
                    Log.d("SIZE", String.valueOf(cur.getLong(3)));
                    Log.d("HEIGHT", String.valueOf(cur.getLong(4)));
                    Log.d("WIDTH", String.valueOf(cur.getLong(5)));
                    images.add(new ImageItem(cur.getString(0), cur.getString(1), cur.getString(2), cur.getLong(3),cur.getInt(4),cur.getInt(5)));
                    Log.d("title", images.get(images.size() - 1).getHeader());
                    Log.d("headerid", Integer.toString(images.get(images.size() - 1).getHeaderId()));
                    Log.d("", "((((((((((((((((((((((((((((((");
                    cur.moveToNext();
                }
            }
            cur.close();
        }
        return images;
    }
    public static void putImage(Context context) {
        ((BaseActivity) context).checkPermissionAndShow(images.get(currentindex));
    }
}