package com.example.littleprince;

import android.os.Bundle;
import android.view.View;

public class DonateActivity extends BaseActivity {
    private View donate_back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        donate_back_btn=findViewById(R.id.donate_back_btn);
        donate_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //Intent intent=new Intent(SettingActivity.this, ListActivity.class);
                //startActivity(intent);
            }
        });
    }
}
