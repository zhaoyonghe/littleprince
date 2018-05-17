package com.example.littleprince.ImageList;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


import com.example.littleprince.BaseActivity;
import com.example.littleprince.R;

import java.util.ArrayList;
import java.util.List;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * 该类主要重写了onCreateView
 * 定义了绘制主界面图片列表的操作
 */
public class ImagesFragment extends Fragment {

    private static final int PERMISSION_REQUEST_CODE = 1;
    //相册名
    public String bucketName = "Screenshots";
    //final private Handler handler=new Handler();



    /**
     * 1、onAttach(): 当该Fragment被添加到Activity时被回调。该方法只会被调用一次；
     * 2、onCreate():  当创建Fragment时被回调。该方法只会被调用一次；
     * 3、onCreateView()：每次创建、绘制该Fragment的View组件时回调该方法，Fragment将会显示该方法返回的View 组件；
     * 4、onActivityCreated(): 当Fragment的宿主Activity被启动完成后回调该方法；//单独的
     * 5、onStart(): 启动Fragment时被回调；
     * 6、onResume():  onStart()方法后一定会回调onResume()方法；
     * 7、onPause(): 暂停Fragment时被回调；
     * 8、onStop(): 停止Fragment时被回调；
     * 9、onDestroyView():  销毁该Fragment所包含的View组件时调用；
     * 10、onDestroy():  销毁Fragment时被回调。该方法只会被调用一次；
     * 11、onDetach():  将Fragment从Activity中删除、替换完成时调用该方法。onDestroy()方法后一定会回调onDetach()方法。该方法只会被调用一次。
     * 12、onInflate():
     * 13、onViewCreated():
     */

    @Nullable
    @Override
    //每次创建、绘制该Fragment的View组件时回调该方法，Fragment将会显示该方法返回的View组件
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_list, null);

        Log.d("EXTERNAL_CONTENT_URI", MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());


        /**
         * MediaStore.Images.Media.DISPLAY_NAME 图片文件名
         * MediaStore.Images.Media.DATA 图片绝对路径
         * MediaStore.Images.Media.DATE_TAKEN 图片的拍照时间（从1970年） 毫秒
         * MediaStore.Images.Media.SIZE 图片的空间占用大小
         * MediaStore.Images.Media.HEIGHT 图片高度
         * MediaStore.Images.Media.WIDTH 图片宽度
         * MediaStore.Images.Media.BUCKET_DISPLAY_NAME 相册名
         * MediaStore.Images.Media.DATE_MODIFIED 图片最后一次被修改的时间（从1970年） 秒
         */

        /**
         * query用于查询内容（内部数据），相当于数据库查表
         * cur指向查询结果，getCount方法返回查询所得记录条数
         * 参数1：提供内容的provider（在Android中有唯一标识区分provider）（from xxx）
         * 参数2：查询条目（select xxx）
         * 参数3：查询限制（where xxx）
         * 参数4：替换？
         * 参数5：结果排序（order by）
         */

        //权限
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d("permission", "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);

            }
        }

        Cursor cur = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.DATE_TAKEN, MediaStore.Images.Media.SIZE,
                        MediaStore.Images.Media.HEIGHT, MediaStore.Images.Media.WIDTH},
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "= ?",
                new String[]{bucketName},
                MediaStore.Images.Media.DATE_MODIFIED + " DESC");

        //Screenshots相册中所有图片
        final List<ImageItem> images = new ArrayList<ImageItem>(cur.getCount());

        //查询结果加入List中
        if (cur != null) {
            if (cur.moveToFirst()) {
                while (!cur.isAfterLast()) {
                    Log.d("", "===========================================================");
                    Log.d("DISPLAY_NAME", cur.getString(0));
                    Log.d("DATA", cur.getString(1));
                    Log.d("DATE_TAKEN", cur.getString(2));
                    Log.d("SIZE", String.valueOf(cur.getLong(3)));
                    Log.d("HEIGHT", String.valueOf(cur.getLong(4)));
                    Log.d("WIDTH", String.valueOf(cur.getLong(5)));
                    images.add(new ImageItem(cur.getString(0), cur.getString(1), cur.getString(2), cur.getLong(3),cur.getInt(4),cur.getInt(5)));
                    Log.d("title", images.get(images.size() - 1).getHeader());
                    Log.d("headerid", Integer.toString(images.get(images.size() - 1).getHeaderId()));
                    Log.d("", "===========================================================");
                    cur.moveToNext();
                }
            }
            cur.close();
        }

        //StickyGridHeadersGridView
        GridView imagelist = v.findViewById(R.id.imagelist);

        //设置适配器，填充数据的中间桥梁
        imagelist.setAdapter(new ListAdapter(getActivity(), images));



        //点击图片事件
        imagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //和编辑功能，贴图功能衔接
                //高钰洋
                Log.d("click",view.getTag(R.id.selected_image_path).toString());
                Log.d("very important","@@@@@@@@@@@@@@@"+String.valueOf(i)+"@@@@@@@@@@@@@@@");
                //Log.d("very important","@@@@@@@@@@@@@@@"+String.valueOf(l)+"@@@@@@@@@@@@@@@");
                //Log.d("very important",String.valueOf((view.getContext()).hashCode()));

                //((ListActivity)view.getContext()).refresh();
                ImageItem selectImage = images.get(i);
                ((BaseActivity) view.getContext()).imageSelected(selectImage);
                Log.d("view.getContext()",String.valueOf(view.getContext()));
                //((BaseActivity) view.getContext()).checkPermissionAndShow(images.get(i));
            }
        });

        imagelist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                ((BaseActivity) view.getContext()).checkPermissionAndShow(images.get(i));
                return true;
            }
        });

        return v;
    }

}
