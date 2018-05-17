package com.example.littleprince.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.littleprince.BaseActivity;
import com.example.littleprince.ImageList.ImageItem;
import com.example.littleprince.R;
import com.example.littleprince.ModuleConfig;


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
//    private View stickerBtn;// 贴图按钮
//    private View fliterBtn;// 滤镜按钮
    private View cropBtn;// 剪裁按钮
    private View rotateBtn;// 旋转按钮
    private View mTextBtn;//文字型贴图添加
    private View mPaintBtn;//编辑按钮
    private View mFLoatImgBtn;//贴图按钮
    protected static ImageItem selectImage;

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
//        mFLoatImgBtn = mainView.findViewById(R.id.btn_floatImg);

//        stickerBtn.setOnClickListener(this);
//        fliterBtn.setOnClickListener(this);
        cropBtn.setOnClickListener(this);
        rotateBtn.setOnClickListener(this);
        mTextBtn.setOnClickListener(this);
        mPaintBtn.setOnClickListener(this);
//        mFLoatImgBtn.setOnClickListener(this);
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
//        else if (v == mFLoatImgBtn) {
//            onFloatImgClick();
//        }
//        else if (v == fliterBtn) {
//            onFilterClick();
//        }
//        else if(v == mBeautyBtn){
//            onBeautyClick();
//        }
    }


//TODO
//    /**
//     * 贴图模式
//     *
//     * @author panyi
//     */
//    private void onStickClick() {
//        activity.bottomGallery.setCurrentItem(StickerFragment.INDEX);
//        activity.mStickerFragment.onShow();
//    }

//    /**
//     * 滤镜模式
//     *
//     * @author panyi
//     */
//    private void onFilterClick() {
//        activity.bottomGallery.setCurrentItem(FilterListFragment.INDEX);
//        activity.mFilterListFragment.onShow();
//    }

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


//    private void onFloatImgClick(){
//    //TODO  贴图响应
//        Log.d("aasas",String.valueOf(selectImage));
//        Log.d("aasas",String.valueOf(selectImage.getPath()));
//        Log.d("wwwwwww",String.valueOf(getActivity()));
//        ((BaseActivity) getActivity()).checkPermissionAndShow(selectImage);
//
//    }

}// end class
