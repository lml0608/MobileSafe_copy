package com.example.android.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.service.AddressService;
import com.example.android.mobilesafe.service.BlackNumberService;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.ServiceUtil;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.view.SettingItemView;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initUpdateUI();
        initAddress();
        initBlacknumber();
    }

    /**
     * 拦截黑名单短信电话
     */
    private void initBlacknumber() {

        final SettingItemView siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber);
        //判断服务是否开启
        boolean isRunning = ServiceUtil.isRunning(this, "com.example.android.mobilesafe.service.BlackNumberService");
        siv_blacknumber.setChecke(isRunning);

        siv_blacknumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isCheck = siv_blacknumber.isChecked();

                siv_blacknumber.setChecke(!isCheck);

                if (!isCheck) {
                    //开启拦截
                    startService(new Intent(getApplicationContext(), BlackNumberService.class));
                } else {
                    //停止服务
                    stopService(new Intent(getApplicationContext(), BlackNumberService.class));

                }
            }
        });

    }

    private void initAddress() {


        final SettingItemView siv_address = (SettingItemView) findViewById(R.id.siv_address);
        //判断服务是否开启
        boolean isRunning = ServiceUtil.isRunning(this, "com.example.android.mobilesafe.service.AddressService");
        //boolean open_address = SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_ADDRESS, false);
        siv_address.setChecke(isRunning);

        siv_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回点击前的选中状态
                boolean check = siv_address.isChecked();
                siv_address.setChecke(!check);
                if (!check) {

                    //开启服务
                    startService(new Intent(getApplicationContext(), AddressService.class));
                } else {
                    //停止服务
                    stopService(new Intent(getApplicationContext(), AddressService.class));

                }
            }
        });
    }

    /**
     * 更新
     */
    private void initUpdateUI() {

        //内部内使用外部的变量，加上final
        final SettingItemView siv_update = (SettingItemView) findViewById(R.id.siv_update);
        boolean open_update =  SpUtil.getBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, false);
        siv_update.setChecke(open_update);
        //条目的点击事件。不是CheckBox的点击事件
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //如果之前是选中的，点击过后，变成未选中
                //如果之前是未选中的，点击过后，变成选中
                boolean check = siv_update.isChecked();
                //取反
                siv_update.setChecke(!check);
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_UPDATE, !check);
            }
        });
    }

}
