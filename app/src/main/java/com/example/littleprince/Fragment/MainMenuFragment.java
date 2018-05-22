package com.example.littleprince.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.littleprince.BaseActivity;
import com.example.littleprince.EditImageActivity;
import com.example.littleprince.ImageList.ImageItem;
import com.example.littleprince.R;
import com.example.littleprince.ModuleConfig;
import com.example.littleprince.utils.BitmapUtils;

import java.io.File;


/**
 * 工具栏主菜单
 *
 * @author panyi
 */
public class MainMenuFragment extends BaseEditFragment implements View.OnClickListener {
    public static final int INDEX = ModuleConfig.INDEX_MAIN;

    public static final String TAG = MainMenuFragment.class.getName();
    private View mainView;
//TODO

    private View cropBtn;// 剪裁按钮
    private View rotateBtn;// 旋转按钮
    private View mTextBtn;//文字型贴图添加
    private View mPaintBtn;//编辑按钮
    private View mShareBtn;//贴图按钮
    protected static ImageItem selectImage;
    protected SaveImageTask mySaveImageTask;

    public static MainMenuFragment newInstance(ImageItem img) {
        MainMenuFragment fragment = new MainMenuFragment();
        System.out.println("IMG============"+img);
        fragment.selectImage=img;
        System.out.println("FRAGEMT============"+fragment.selectImage);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_image_main_menu,
                null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//TODO
        System.out.println("ONCrEATEE======="+selectImage);
//        stickerBtn = mainView.findViewById(R.id.btn_stickers);
//        fliterBtn = mainView.findViewById(R.id.btn_filter);
        cropBtn = mainView.findViewById(R.id.btn_crop);
        rotateBtn = mainView.findViewById(R.id.btn_rotate);
        mTextBtn = mainView.findViewById(R.id.btn_text);
        mPaintBtn = mainView.findViewById(R.id.btn_paint);
        mShareBtn = mainView.findViewById(R.id.btn_share);

//        stickerBtn.setOnClickListener(this);
//        fliterBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        mTextBtn.setOnClickListener(this);
        mPaintBtn.setOnClickListener(this);
        mShareBtn.setOnClickListener(this);
    }

    @Override
    public void onShow() {
        // do nothing
    }

    @Override
    public void backToMain() {
        //do nothing
    }
//TODO
    @Override
    public void onClick(View v) {
        if (v == cropBtn) {
            onCropClick();
        } else if (v == rotateBtn) {
            onRotateClick();
        } else if (v == mTextBtn) {
            onAddTextClick();
        } else if (v == mPaintBtn) {
            onPaintClick();
        }
        else if (v == mShareBtn) {
            onShareClick();
        }
//        else if (v == fliterBtn) {
//            onFilterClick();
//        }
//        else if(v == mBeautyBtn){
//            onBeautyClick();
//        }
    }




    /**
     * 裁剪模式
     *
     * @author panyi
     */
    private void onCropClick() {
        activity.bottomGallery.setCurrentItem(CropFragment.INDEX);
        activity.mCropFragment.onShow();
    }

    /**
     * 图片旋转模式
     *
     * @author panyi
     */
    private void onRotateClick() {
        activity.bottomGallery.setCurrentItem(RotateFragment.INDEX);
        activity.mRotateFragment.onShow();
    }

    /**
     * 插入文字模式
     *
     * @author panyi
     */
    private void onAddTextClick() {
        activity.bottomGallery.setCurrentItem(AddTextFragment.INDEX);
        activity.mAddTextFragment.onShow();
    }

    /**
     * 自由绘制模式
     */
    private void onPaintClick() {
        activity.bottomGallery.setCurrentItem(PaintFragment.INDEX);
        activity.mPaintFragment.onShow();
    }


    private void onShareClick(){
        String savePath=activity.saveFilePath;
        if(activity.getmOpTimes()==0){
            shareAction(savePath);
        }else {
            if (mySaveImageTask != null) {
                mySaveImageTask.cancel(true);
            }

            mySaveImageTask = new SaveImageTask();
            mySaveImageTask.execute(activity.getMainBitmap());
            shareAction(savePath);

        }

    }
    private void shareAction(String savePath){
        Intent imageIntent = new Intent(Intent.ACTION_SEND);
        imageIntent.setType("image/jpeg");
        File f =new File(savePath);
        imageIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(f));
        startActivity(Intent.createChooser(imageIntent, "分享"));
    }
    //保存后不退出
    private final class SaveImageTask extends AsyncTask<Bitmap, Void, Boolean> {
        private Dialog dialog;

        @Override
        protected Boolean doInBackground(Bitmap... params) {
            if (TextUtils.isEmpty(activity.saveFilePath))
                return false;

            return BitmapUtils.saveBitmap(params[0], activity.saveFilePath);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            dialog.dismiss();
        }

        @Override
        protected void onCancelled(Boolean result) {
            super.onCancelled(result);
            dialog.dismiss();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = EditImageActivity.getLoadingDialog(activity.getmContext(), "图片保存中...", false);
            dialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            dialog.dismiss();

            if (result) {
                activity.resetOpTimes();
            } else {
                Toast.makeText(activity.getmContext(), "图片保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }//end inner class

}// end class
