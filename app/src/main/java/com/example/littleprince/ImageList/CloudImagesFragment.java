package com.example.littleprince.ImageList;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.littleprince.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.example.littleprince.utils.FileUtils.genEditFile;


/**
 * Created by zhaoyonghe on 2018/6/10.
 */

/**
 * 该类主要重写了onCreateView
 * 定义了绘制主界面小王子图床图片列表的操作
 */
public class CloudImagesFragment extends Fragment {

    //相册名
    final public String bucketName = "※小王子图床※";
    //小王子图床照片信息
    public String imageMsg;
    private AlertDialog.Builder builder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_list, null);

        //获取小王子图床全部缩略版照片信息
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    String url = "http://39.106.150.248:3000/smallinfo";
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
            String rp = null;
            String d = null;
            try {
                n = jsonArray.getJSONObject(i).getString("name");
                p = "http://39.106.150.248/littleprince/smallimages/" + n;
                rp = "http://39.106.150.248/littleprince/images/" + n;
                d = jsonArray.getJSONObject(i).getString("time");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            cloudImages.add(new CloudImageItem(n, p, rp, d));
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
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //TODO 高钰洋加点击下载，或者实现长按下载
                builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("下载");
                final String realPath = cloudImages.get(position).getRealPath();
                builder.setMessage(realPath);
                //监听下方button点击事件
                builder.setPositiveButton("复制到剪贴板", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ClipboardManager cmb = (ClipboardManager)getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData cd = ClipData.newPlainText("url",realPath);
                        cmb.setPrimaryClip(cd);
                        Toast.makeText(getActivity(), "复制到剪贴板成功", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });

                //设置对话框是可取消的
                builder.setCancelable(true);
                AlertDialog dialog=builder.create();
                dialog.show();
                //cloudImages.get(i) 获取点击图片
                //cloudImages.get(i).getPath() 获取点击图片存储路径

            }
        });

//        cloudimagelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //TODO 长按下载?
//                return true;
//            }
//        });

        return v;
    }

//    public void SaveImage(Bitmap bitmap, String path){
//        File file=new File(path);
//        FileOutputStream fileOutputStream=null;
//        //文件夹不存在，则创建它
//        if(!file.exists()){
//            file.mkdir();
//        }
//        try {
//            File saveFIle = genEditFile();
//            fileOutputStream=new FileOutputStream(saveFIle);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);
//            fileOutputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public class HttpImgThread extends Thread {
//        private String url;
//        public HttpImgThread(){
//            super();
//        }
//        public HttpImgThread(String _url){
//            this.url = _url;
//        }
//        @Override
//        public void run() {
//
//            downloadImageFromCloud(url);
//            Looper.prepare();
//            Toast.makeText(getActivity(),"下载完成",Toast.LENGTH_LONG).show();
//            Looper.loop();
//
//        }
//    }

//    private void downloadImageFromCloud(String imageUrl){
//        System.out.println(1);
//        Looper.prepare();
//        System.out.println(2);
//        URL url;
//        dialog.incrementProgressBy(1);
//        HttpURLConnection connection=null;
//        Bitmap bitmap=null;
//        try {
//            url = new URL(imageUrl);
//            dialog.incrementProgressBy(1);
//            connection=(HttpURLConnection)url.openConnection();
//            connection.setConnectTimeout(6000); //超时设置
//            dialog.incrementProgressBy(1);
//            connection.setDoInput(true);
//            connection.setUseCaches(false); //设置不使用缓存
//            InputStream inputStream=connection.getInputStream();
//            dialog.incrementProgressBy(1);
//            bitmap= BitmapFactory.decodeStream(inputStream);
//            dialog.incrementProgressBy(1);
//            inputStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        dialog.incrementProgressBy(1);
//        SaveImage(bitmap,"/littleprince_cloud");
//        dialog.incrementProgressBy(1);
//        dialog.dismiss();
//        Looper.loop();
//
//
//    }
}
