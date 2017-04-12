package com.example.android.mobilesafe.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.ToastUtil;
import com.example.android.mobilesafe.view.SettingItemView;

public class Setup2Activity extends BaseSetupActivity {

    private SettingItemView mSiv_sim_bound;

    private static final int PERMISSION_READ_STATE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup2);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},PERMISSION_READ_STATE);
        } else {
            initUI();
        }
    }

    @Override
    protected void showNextPage() {
        String serialNumber = SpUtil.getString(this, ConstantValue.SIM_NUMBER, "");
        if (!TextUtils.isEmpty(serialNumber)){
            startActivity(new Intent(this, Setup3Activity.class));
            finish();

            overridePendingTransition(R.anim.next_in_anim, R.anim.next_out_anim);
        } else {
            ToastUtil.show(this, "请绑定sim卡");
        }
    }

    @Override
    protected void showPrePage() {

        startActivity(new Intent(this, SetupOneActivity.class));
        finish();
        overridePendingTransition(R.anim.pre_in_anim, R.anim.pre_out_anim);

    }

    /**
     * 初始化控件，绑定sim卡
     */
    private void initUI() {

        mSiv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        //会显
        String sim_number =  SpUtil.getString(getApplicationContext(), ConstantValue.SIM_NUMBER, "");

        if (TextUtils.isEmpty(sim_number)) {
            //序列号为空，则为不选中，切显示desoff的文本
            mSiv_sim_bound.setChecke(false);

        } else {
            //序列号不为空，则为选中，切显示deson的文本
            mSiv_sim_bound.setChecke(true);
        }

        mSiv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取原有的状态
                boolean check = mSiv_sim_bound.isChecked();
                //将原有的状态取方，存储序列号，设置当前条目

                mSiv_sim_bound.setChecke(!check);
                if (!check){
                    //储存
                    //获取sim序列号

                    TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String simSerialNumber = manager.getSimSerialNumber();
                    SpUtil.putString(getApplicationContext(), ConstantValue.SIM_NUMBER, simSerialNumber);



                }else {
                    //将储存序列号的节点，从sp中删除掉

                    SpUtil.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case PERMISSION_READ_STATE:
                if (grantResults.length >= 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initUI();
                } else {

                    ToastUtil.show(this, "You dont have required permission to make the Action");
                }
        }
    }
}
