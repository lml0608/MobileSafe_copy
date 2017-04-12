package com.example.android.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

import org.w3c.dom.Text;

public class SetupOverActivity extends AppCompatActivity {

    private TextView mPhoneView;
    private TextView mResetTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isSetupOver = SpUtil.getBoolean(this, ConstantValue.SETUP_OVER, false);
        if (isSetupOver) {
            //密码输入成功，并且4个导航界面设置完成，停留在设置完成功能列表界面
            setContentView(R.layout.activity_setup_over);

        } else {
            //密码输入成功，并且4个导航界面没有设置完成，跳转到导航页面第一个
            startActivity(new Intent(this, SetupOneActivity.class));
            finish();

        }

        initUI();

    }

    private void initUI() {

        mPhoneView = (TextView)findViewById(R.id.tv_phone);

        mPhoneView.setText(SpUtil.getString(this, ConstantValue.CONTACT_PHONE,""));

        //条目被点击的事件

        mResetTextView = (TextView)findViewById(R.id.tv_reset);
        mResetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),SetupOneActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}
