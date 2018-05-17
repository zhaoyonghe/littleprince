package com.example.littleprince.ImageList;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.littleprince.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

/**
 * Created by zhaoyonghe on 2018/4/29.
 */


/**
 * 把List<ImageItem> images中的图片绘制到界面上
 */
public class ListAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    private final Context context;
    private final List<ImageItem> images;
    private final LayoutInflater inflater;

    private static final DisplayImageOptions options = new DisplayImageOptions.Builder()
            .resetViewBeforeLoading(true)
            .cacheInMemory(true)
            .cacheOnDisk(false)
            .considerExifParams(true)
            .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
            .bitmapConfig(Bitmap.Config.RGB_565)
            .build();

    public ListAdapter(Context context, List<ImageItem> images) {
        this.context = context;
        this.images = images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public ImageItem getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return images.get(position).hashCode();
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = inflater.inflate(R.layout.image_item, null);
        ImageView imageView;
        imageView = ((ImageView) view.findViewById(R.id.img));
        ImageLoader.getInstance().displayImage("file://" + getItem(position).getPath(), imageView, options);
        view.setTag(R.id.selected_image_path,getItem(position).getPath());
        return view;



        /*这段注释别删
        ImageView imageView;
        if (view==null){
            imageView=(ImageView) inflater.inflate(R.layout.imageitem,null);
        }
        else{
            imageView=(ImageView) view;
        }
        ImageLoader.getInstance().displayImage("file://"+getItem(position).path,imageView,options);
        //Log.d("khkhkff","file://"+getItem(i).path);
        return imageView;*/
    }

    @Override
    public long getHeaderId(int position) {
        return images.get(position).getHeaderId();
    }

    @Override
    public View getHeaderView(int position, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.header_item, null);
        ((TextView) view.findViewById(R.id.header)).setText(getItem(position).getHeader());
        return view;

    }
}
