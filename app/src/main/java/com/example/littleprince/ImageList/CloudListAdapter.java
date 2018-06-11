package com.example.littleprince.ImageList;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.littleprince.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;


/**
 * Created by zhaoyonghe on 2018/6/10.
 */

public class CloudListAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private final Context context;
    private final List<CloudImageItem> cloudimages;
    private final LayoutInflater inflater;
    private static final DisplayImageOptions options = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .cacheOnDisk(false)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .showImageOnLoading(R.drawable.ic_screencapture)
            .build();
    ProgressDialog progressDialog;
    Handler handler= new Handler();
    int curposition=0;


    public CloudListAdapter(Context context, List<CloudImageItem> cloudimages) {
        this.context = context;
        this.cloudimages = cloudimages;
        this.inflater = LayoutInflater.from(context);
        this.progressDialog=new ProgressDialog(context);
    }

    @Override
    public int getCount() {
        return cloudimages.size();
    }

    @Override
    public CloudImageItem getItem(int position) {
        return cloudimages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return cloudimages.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.image_item,null);
        ImageView imageView;
        imageView=((ImageView)view.findViewById(R.id.img));
        curposition=position;
        ImageLoader.getInstance().displayImage(getItem(position).getPath(), imageView, options, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {
                Log.d("shashis",s+String.valueOf(curposition));
                if(curposition<5){
                    progressDialog.setTitle("图片加载中");
                    progressDialog.setMessage("等待中......");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setCancelable(false);
                    progressDialog.show();
                }
            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                progressDialog.dismiss();
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        }, new ImageLoadingProgressListener() {
            @Override
            public void onProgressUpdate(String s, View view, final int current, final int total) {
                Log.d("process",String.valueOf((double)(current/total))+"::::"+String.valueOf(curposition));
            }
        });
        view.setTag(R.id.selected_image_path,getItem(position).getPath());
        return view;
    }

    @Override
    public long getHeaderId(int position) {
        return cloudimages.get(position).getHeaderId();
    }

    @Override
    public View getHeaderView(int position, View view, ViewGroup viewGroup) {
        view=inflater.inflate(R.layout.header_item,null);
        ((TextView)view.findViewById(R.id.header)).setText(getItem(position).getHeader());
        return view;
    }


}
