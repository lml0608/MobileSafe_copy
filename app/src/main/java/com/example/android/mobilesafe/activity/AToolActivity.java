package com.example.android.mobilesafe.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.mobilesafe.R;

public class AToolActivity extends AppCompatActivity {

    private TextView mPhoneAddress;
    private TextView mSmsBackup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atool);

        //电话归属点查询
        initPhoneAddress();
        //短信备份

        initSmsBackUp();


    }

    private void initSmsBackUp() {

        mSmsBackup = (TextView) findViewById(R.id.tv_sms_backup);

        mSmsBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //备份
                showSmsBackUpDialog();
            }
        });

    }

    /**
     * 备份进度弹框
     */
    protected void showSmsBackUpDialog() {

        ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.setIcon(R.drawable.ic_launcher);

        progressDialog.setTitle("短信备份");
        //指定样式 水平
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        progressDialog.show();

        //获取短信

    }

    private void initPhoneAddress() {

        mPhoneAddress = (TextView) findViewById(R.id.tv_query_phone_address);

        mPhoneAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QueryAddressActivity.class));
            }
        });


    }
}
