package com.example.android.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

public class Setup4Activity extends BaseSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup4);

        initUI();

    }

    @Override
    protected void showNextPage() {
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);
        if (open_security) {
            startActivity(new Intent(this, SetupOverActivity.class));
            finish();
            SpUtil.putBoolean(this, ConstantValue.SETUP_OVER, true);
            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        }else {
            ToastUtil.show(getApplicationContext(), "请开启防盗保护");
        }
    }

    @Override
    protected void showPrePage() {
        startActivity(new Intent(this, Setup3Activity.class));
        finish();

        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }

    private void initUI() {

        final CheckBox cb_box = (CheckBox) findViewById(R.id.cb_box);

        //回显
        boolean open_security = SpUtil.getBoolean(this, ConstantValue.OPEN_SECURITY, false);

        cb_box.setChecked(open_security);
        if (open_security) {

            cb_box.setText("安全设置已开启");
        } else {
            cb_box.setText("安全设置已关闭");
        }


        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SpUtil.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, isChecked);

                if (isChecked) {
                    cb_box.setText("安全设置已开启");
                } else {
                    cb_box.setText("安全设置已关闭");
                }
            }
        });
    }

}

