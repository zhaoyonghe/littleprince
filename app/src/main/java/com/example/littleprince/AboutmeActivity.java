package com.example.littleprince;


import android.os.Bundle;
import android.view.View;

public class AboutmeActivity extends BaseActivity {
    private View aboutme_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutme);
        aboutme_back_btn=findViewById(R.id.aboutme_back_btn);
        aboutme_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Intent intent=new Intent(SettingActivity.this, ListActivity.class);
                //startActivity(intent);
            }
        });
    }
}
