package com.example.littleprince.Intro;

import android.Manifest;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.example.littleprince.Fragment.SampleSlide;
import com.example.littleprince.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class IntroActivity extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment1));
        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment2));
        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment3));
        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment4));
        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment5));
        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment6));
        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment7));
        addSlide(SampleSlide.newInstance(R.layout.fragment_turorial_fragment8));

        setFadeAnimation();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}