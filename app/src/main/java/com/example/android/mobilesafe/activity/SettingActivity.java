package com.example.android.mobilesafe.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdateUI();
    }

    /**
     * 更新
     */
    private void initUpdateUI() {

        //内部内使用外部的变量，加上final
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        //条目的点击事件。不是CheckBox的点击事件
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的，点击过后，变成未选中
                //如果之前是未选中的，点击过后，变成选中
                boolean check = siv_update.isChecked();
                //取反
                siv_update.setChecked(!check);
            }
        });
    }

}
