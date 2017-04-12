package com.example.android.mobilesafe.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.StreamUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

public class Setup3Activity extends BaseSetupActivity {

    public static final int CONTACT_QUESTCODE = 123;
    private static final int READ_CONTACT = 100;
    private EditText mEt_phone_number;
    private Button mEt_select_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup3);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS},READ_CONTACT);
        } else {
            initUI();
        }

    }

    @Override
    protected void showNextPage() {
        //获取输入框的联系人，在做下一步处理

        String phone = mEt_phone_number.getText().toString();
        if (!TextUtils.isEmpty(phone)) {
            startActivity(new Intent(this, Setup4Activity.class));
            finish();

            //如果是输入号码。则需要取保存
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);

            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);

        } else {
            ToastUtil.show(this, "请输入电话号码");
        }

    }

    @Override
    protected void showPrePage() {

        startActivity(new Intent(this, Setup2Activity.class));
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }

    private void initUI() {

        mEt_phone_number = (EditText) findViewById(R.id.et_phone_number);

        //回显电话号码
        String phone = SpUtil.getString(this, ConstantValue.CONTACT_PHONE,"");

        mEt_phone_number.setText(phone);

        mEt_select_number = (Button)findViewById(R.id.bt_select_number);

        mEt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactListActivity.class);

                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            //1,返回到当前界面的时候,接受结果的方法
            String phone = data.getStringExtra("phone");
            //2,将特殊字符过滤(中划线转换成空字符串)
            phone = phone.replace("-", "").replace(" ", "").trim();
            mEt_phone_number.setText(phone);

            //3,存储联系人至sp中
            SpUtil.putString(getApplicationContext(), ConstantValue.CONTACT_PHONE, phone);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case READ_CONTACT:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initUI();
                } else {

                    ToastUtil.show(this, "You dont have required permission to make the Action");
                }
        }
    }
}
