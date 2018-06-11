package com.example.littleprince.ImageList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.littleprince.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by zhaoyonghe on 2018/6/10.
 */

/**
 * 该类主要重写了onCreateView
 * 定义了绘制主界面云相册图片列表的操作
 */
public class CloudImagesFragment extends Fragment {

    //相册名
    final public String bucketName = "云相册";
    //云相册照片信息
    public String imageMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_list, null);

        //获取云相册全部照片信息
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    String url = "http://39.106.150.248:3000/info";
                    URL httpUrl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();//与服务器建立连接；
                    conn.setReadTimeout(5000);
                    conn.setRequestMethod("GET");//设置请求方式为GET

                    byte[] data = new byte[1024];
                    int len = 0;
                    InputStream inputStream = conn.getInputStream();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    while ((len = inputStream.read(data)) != -1) {
                        byteArrayOutputStream.write(data, 0, len);
                    }
                    inputStream.close();

                    imageMsg = new String(byteArrayOutputStream.toByteArray());
                    Log.d("imagemsg", new String(byteArrayOutputStream.toByteArray()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        t.start();

        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Log.d("thread", "testtest");
        Log.d("asdfa", String.valueOf(imageMsg));

        //解析JSON
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(imageMsg);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final List<CloudImageItem> cloudImages = new ArrayList<CloudImageItem>(jsonArray.length());
//        String a = "as";
//        String b = "http://39.106.150.248/littleprince/images/IMG_20180605_111840.jpg";
//        String c = "aaaa";

        for (int i = 0; i < jsonArray.length(); i++) {
            String n = null;
            String p = null;
            String d = null;
            try {
                n = jsonArray.getJSONObject(i).getString("name");
                p = "http://39.106.150.248/littleprince/images/" + n;
                d = jsonArray.getJSONObject(i).getString("time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cloudImages.add(new CloudImageItem(n, p, d));
        }

        Collections.sort(cloudImages, new Comparator<CloudImageItem>() {
            @Override
            public int compare(CloudImageItem c1, CloudImageItem c2) {
                return c2.getDate().compareTo(c1.getDate());
            }
        });

        Log.d("list",cloudImages.toString());

        //StickyGridHeadersGridView
        GridView cloudimagelist = v.findViewById(R.id.imagelist);

        //设置适配器，填充数据的中间桥梁
        cloudimagelist.setAdapter(new CloudListAdapter(getActivity(), cloudImages));

        cloudimagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO 高钰洋加点击下载，或者实现长按下载
                //cloudImages.get(i) 获取点击图片
                //cloudImages.get(i).getPath() 获取点击图片存储路径
            }
        });

        cloudimagelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //TODO 长按下载?
                return true;
            }
        });

        return v;
    }
}
