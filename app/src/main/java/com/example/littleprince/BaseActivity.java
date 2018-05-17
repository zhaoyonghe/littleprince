package com.example.littleprince;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.littleprince.FloatImage.FloatImageManager;
import com.example.littleprince.FloatImage.FloatViewListener;
import com.example.littleprince.FloatImage.IFloatImageView;
import com.example.littleprince.ImageList.ImageItem;
import com.example.littleprince.utils.FileUtils;
import com.linchaolong.android.floatingpermissioncompat.FloatingPermissionCompat;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;


/**
 * 基础Activity
 */

public class BaseActivity extends AppCompatActivity {



    public static final int ACTION_REQUEST_EDITIMAGE = 9;

    public static final String EXTRA_OUTPUT = "extra_output";
    public static final String SAVE_FILE_PATH = "save_file_path";
    public static final String FILE_PATH = "file_path";


    public static final String IMAGE_IS_EDIT = "image_is_edit";

    protected String editImgPath;
    protected String editImgTaken;
    protected long editImageSize;

    public void imageSelected(ImageItem selectImage) {
        editImgPath=selectImage.getPath();
        editImgTaken=selectImage.getImageTaken();
        editImageSize=selectImage.getImageSize();
        File outputFile = FileUtils.genEditFile();
        if(TextUtils.isEmpty(editImgPath)){
            Toast.makeText(this, "Please choose a image", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent it = new Intent(this, EditImageActivity.class);
        it.putExtra(FILE_PATH, editImgPath);
        it.putExtra(EXTRA_OUTPUT, outputFile.getAbsolutePath());
        it.putExtra("IMAGE_ITEM",selectImage);
        startActivityForResult(it,ACTION_REQUEST_EDITIMAGE);
        //EditImageActivity.start(this,path,outputFile.getAbsolutePath(),ACTION_REQUEST_EDITIMAGE)
    }




    ImageLoader imageLoader;

    //应为BaseActivity类型，完成贴图的重要参数
    protected Context context;
    //应为图片列表最外层的ConstraintLayout，完成贴图的重要参数，采用该对象的post方法触发贴图线程
    protected View rootView;

    protected FloatImageManager floatImageManager;

    protected ImageItem imageItem=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*--------------------以下操作主要为贴图做准备----------------------*/

        //context=BaseActivity
        context=this;

        //获取rootView的id
        int layoutId = getLayoutResId();

        //（把rootView变得可用）以下代码机制尚不明确，不可删
        if (layoutId > 0) {
            rootView = LayoutInflater.from(context).inflate(layoutId, null);
            Log.d("rootView",rootView.toString());
            setContentView(rootView);
        }

        //生成一个FloatImageManager
        floatImageManager=new FloatImageManager();

    }


    /**
     * 获取rootView的id
     * @return rootView的id
     */
    public int getLayoutResId(){
        return R.layout.activity_list;
    }

    /*--------------------以下是有关图片列表的基础操作----------------------*/

    /**
     * 检测图片载入框架是否导入，若没有，则导入并初始化
     */
    protected void checkInitImageLoader() {
        if (!ImageLoader.getInstance().isInited()) {
            initImageLoader();
        }//end if
    }

    /**
     * 初始化图片载入框架
     */
    private void initImageLoader() {
        //
        File cacheDir = StorageUtils.getCacheDirectory(this);
        int MAXMEMONRY = (int) (Runtime.getRuntime().maxMemory());
        // System.out.println("dsa-->"+MAXMEMONRY+"   "+(MAXMEMONRY/5));//.memoryCache(new
        // LruMemoryCache(50 * 1024 * 1024))
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this).memoryCacheExtraOptions(480, 800).defaultDisplayImageOptions(defaultOptions)
                .diskCacheExtraOptions(480, 800, null).threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(MAXMEMONRY / 5))
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(this)) // default
                .imageDecoder(new BaseImageDecoder(false)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()).build();

        ImageLoader.getInstance().init(config);
    }

    /**
     * 作用尚不明确
     * @param context
     * @param titleId
     * @param canCancel
     * @return
     */
    public static Dialog getLoadingDialog(Context context, int titleId,
                                          boolean canCancel) {
        return getLoadingDialog(context, context.getString(titleId), canCancel);
    }

    /**
     * 作用尚不明确
     * @param context
     * @param title
     * @param canCancel
     * @return
     */
    public static Dialog getLoadingDialog(Context context, String title,
                                          boolean canCancel) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setCancelable(canCancel);
        dialog.setMessage(title);
        return dialog;
    }



    /*--------------------以下是有关贴图悬浮窗的基础操作----------------------*/

    /**
     * 获取悬浮窗权限并显示图片
     */
    public void checkPermissionAndShow(ImageItem imageItem){
        this.imageItem=imageItem;
        //this.context=newcontext;
        Log.d("#######",String.valueOf(context));
        // 检查是否已经授权
        if (FloatingPermissionCompat.get().check(context)) {
            Log.d("casac","checkpermission");
            showFloatImageDelay();
        } else {
            // 授权提示
            new AlertDialog.Builder(context).setTitle("悬浮窗权限未开启")
                    .setMessage("你的手机没有授权" + context.getString(R.string.app_name) + "获得悬浮窗权限，图片悬浮窗功能将无法正常使用")
                    .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 显示授权界面
                            FloatingPermissionCompat.get().apply(context);
                        }
                    })
                    .setNegativeButton("取消", null).show();
        }
    }

    /**
     * 采用rootView的post方法进行贴图操作
     */
    protected void showFloatImageDelay(){
        Log.d("2","showFloatImageDelay");
        Log.d("22",String.valueOf(rootView));
        if(rootView!=null){
            rootView.post(floatImageRunnable);
        }
    }

    //贴图线程
    protected final Runnable floatImageRunnable=new Runnable(){
        @Override
        public void run() {
            Log.d("3","run");
            showFloatImage();
        }
    };

    /**
     * 贴图
     */
    protected void showFloatImage(){
        Log.d("4","showfloatimage");
        //先把已贴的图关掉，保证只能贴一张图
        closeFloatImage();
        //windowmanager贴图
        floatImageManager.show(context,imageItem);
        //添加事件监听器，关闭按钮
        addFloatViewListener();
    }

    /**
     * 添加事件监听器
     */
    protected void addFloatViewListener(){
        IFloatImageView floatImageView = floatImageManager.getFloatImageView();
        if (floatImageView == null) {
            return;
        }
        floatImageView.setFloatViewListener(new FloatViewListener() {
            @Override
            public void onClose() {
                Log.d("dq", "onClosed");
                closeFloatImage();
            }

            @Override
            public void onClick() {

            }

            @Override
            public void onMoved() {
                Log.d("dq", "onMoved");
            }

            @Override
            public void onDragged() {
                Log.d("dq", "onDragged");
            }
        });
    }

    /**
     * 关闭贴图
     */
    protected void closeFloatImage(){
        if (rootView != null) {
            //Log.d("as","==============rootView != null============");
            rootView.removeCallbacks(floatImageRunnable);
        }
        if (floatImageManager != null) {
            //Log.d("as","==============floatImageManager != null============");
            floatImageManager.dismissFloatWindow();
        }
    }


}
