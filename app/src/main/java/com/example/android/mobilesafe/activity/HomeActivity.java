package com.example.android.mobilesafe.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.Md5Util;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubin on 2017/3/14.
 */

public class HomeActivity extends AppCompatActivity {


    private GridView mGridView;
    private String[] mTitleStr;
    private int[] mDrawableIds;
    private Button mBt_submit;
    private Button mBt_cancel;
    private EditText mEt_set_psd;
    private EditText mEt_confirm_psd;
    private Button mConfirmSubmit;
    private Button mConfirmCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        List<String> permissionList = new ArrayList<>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.SEND_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.RECEIVE_SMS);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (!permissionList.isEmpty()) {

            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        initUI();
        //初始化数据
        initData();




    }

    /**
     * 初始化数据
     */
    private void initData() {
        //准备数据 文字9组，图片9组

        mTitleStr = new String[] {
                "手机防盗","通信卫士","软件管家","进程管理","流量统计","手机杀毒","缓存清理","高级工具","设置中心"
        };

        mDrawableIds = new int[] {
                R.drawable.home_safe,R.drawable.home_callmsgsafe,
                R.drawable.home_apps,R.drawable.home_taskmanager,
                R.drawable.home_netmanager,R.drawable.home_trojan,
                R.drawable.home_sysoptimize,R.drawable.home_tools,
                R.drawable.home_settings
        };

        mGridView.setAdapter(new MyAdapter());

        //九宫格单个条目点击事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {

                    case 0:
                        //开启对话框
                        showDialog();

                        break;
                    case 1:
                        //跳转到通信卫士界面
                        startActivity(new Intent(getApplicationContext(), BlackNumberActivity.class));

                        break;
                    case 7:
                        //跳转到高级工具功能列表界面
                        startActivity(new Intent(getApplicationContext(), AToolActivity.class));
                        break;

                    case 8:
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });
    }

    /**
     *
     */
    private void showDialog() {
        //判断本地是否已经储存过米密码
        String psd = SpUtil.getString(this, ConstantValue.MOBILE_SAFE_PSD, "");
        if (TextUtils.isEmpty(psd)) {
            //1.初始设置密码对话框

            showSetPsdDialog();
        } else {
            //2.确定密码对话框
            showConfirmPsdDialog(psd);
        }


    }

    /**
     * 确认对话框
     * @param psd
     */
    private void showConfirmPsdDialog(final String psd) {

        final View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_confirm_psd,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();
        dialog.show();

        mConfirmSubmit = (Button) view.findViewById(R.id.bt_confirm_submit);
        mConfirmCancel = (Button) view.findViewById(R.id.bt_confirm_cancel);


        mConfirmSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText mConfirmPasswd = (EditText) view.findViewById(R.id.confirm_psd_edit);
                String confirmPasswd = Md5Util.encoder(mConfirmPasswd.getText().toString());
                if (TextUtils.isEmpty(confirmPasswd)) {
                    ToastUtil.show(getApplicationContext(),"输入为空");
                } else {

                    if (confirmPasswd.equals(psd)) {
                        startActivity(new Intent(HomeActivity.this, SetupOverActivity.class));
                        dialog.dismiss();
                    }else {
                        ToastUtil.show(getApplicationContext(),"确认密码错误");
                    }
                }

            }
        });

        mConfirmCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    /**
     *设置对话框
     */
    private void showSetPsdDialog() {

        final View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_set_psd,null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);

        final AlertDialog dialog = builder.create();

        dialog.show();
        //点击对话框外部时对话框不消失
        dialog.setCanceledOnTouchOutside(false);

        mBt_submit = (Button) view.findViewById(R.id.bt_submit);
        mBt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        mBt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mEt_set_psd = (EditText) view.findViewById(R.id.et_set_psd);
                mEt_confirm_psd = (EditText) view.findViewById(R.id.et_confirm_psd);

                String psd = mEt_set_psd.getText().toString();
                String confirmPsd = mEt_confirm_psd.getText().toString();

                if (!TextUtils.isEmpty(psd) && !TextUtils.isEmpty(confirmPsd)) {
                    //进入应用手机防盗模块
                    if (confirmPsd.equals(psd)) {
                        SpUtil.putString(getApplicationContext(), ConstantValue.MOBILE_SAFE_PSD, Md5Util.encoder(psd));
                        startActivity(new Intent(HomeActivity.this, SetupOverActivity.class));

                        dialog.dismiss();
                    } else {
                        ToastUtil.show(getApplicationContext(),"确认密码错误");
                    }

                } else {

                    //提示用户输入有误
                    ToastUtil.show(getApplicationContext(),"密码输入为空");
                }
            }
        });

        mBt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initUI() {

        mGridView = (GridView) findViewById(R.id.gv_home);

    }

    /**
     * adapter类
     */
    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            //条目的总数 文字数组 == 图片数组
            return mTitleStr.length;
        }

        @Override
        public Object getItem(int position) {
            //
            return mTitleStr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.gridview_item, parent,false);

            ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_title);

            iv_icon.setBackgroundResource(mDrawableIds[position]);
            tv_title.setText(mTitleStr[position]);
            return view;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {

            case 1:
                if (grantResults.length > 0) {

                    for (int result : grantResults) {

                        if (result != PackageManager.PERMISSION_GRANTED) {

                            ToastUtil.show(this, "必须同一所有权限才可以使用本程序");
                            return;
                        }
                    }
                }else {
                    ToastUtil.show(this, "发生错误");
                }
                break;
            default:

        }
    }
}
