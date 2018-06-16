package com.example.littleprince.Notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;


import com.example.littleprince.ImageList.ListActivity;

import java.lang.reflect.Method;

public class NotificationBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("aaa",context.toString());
        String action = intent.getAction();//动作
        //collapseStatusBar(context);//收起通知栏
        Intent i = new Intent(context, ListActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//必须添加，避免重复打开
        if (action.equals("right")) {
            MyNotificationManager.updateImage(context, "right");
        } else if (action.equals("left")) {
            MyNotificationManager.updateImage(context, "left");
        } else if (action.equals("put")) {
            collapseStatusBar(context);
            Log.d("NotificationBroadcast",String.valueOf(ListActivity.getListContext()));
            MyNotificationManager.putImage(ListActivity.getListContext());
        }
    }

    public void collapseStatusBar(Context context) {
        try {
            Object statusBarManager = context.getSystemService("statusbar");
            Method collapse;
            if (Build.VERSION.SDK_INT <= 16) {
                collapse = statusBarManager.getClass().getMethod("collapse");
            } else {
                collapse = statusBarManager.getClass().getMethod("collapsePanels");
            }
            collapse.invoke(statusBarManager);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }
}
