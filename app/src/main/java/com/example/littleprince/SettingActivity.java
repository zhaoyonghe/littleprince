package com.example.littleprince;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;



public class SettingActivity extends BaseActivity {

    private View setting_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setting_back_btn=findViewById(R.id.setting_back_btn);
        setting_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Intent intent=new Intent(SettingActivity.this, ListActivity.class);
                //startActivity(intent);
            }
        });
    }
}
