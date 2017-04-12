package com.example.android.mobilesafe.activity;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.engine.AddressDao;
import com.example.android.mobilesafe.utils.ToastUtil;

public class QueryAddressActivity extends AppCompatActivity {

    private EditText mEt_phone;
    private Button mBt_query;
    private TextView mTv_query_result;
    private String mAddress;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            mTv_query_result.setText(mAddress);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_address);

        //测试代码

        //AddressDao.getAddress("15245896325");

        initUI();

    }

    private void initUI() {

        mEt_phone = (EditText) findViewById(R.id.et_phone);
        mBt_query = (Button) findViewById(R.id.bt_query);
        mTv_query_result = (TextView) findViewById(R.id.tv_query_result);



        mBt_query.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String phone = mEt_phone.getText().toString();

                //查询是耗时操作，开启子线程
                if (!TextUtils.isEmpty(phone)) {
                    query(phone);
                } else {

                    //抖动

                    Animation mSnake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                    mEt_phone.startAnimation(mSnake);
                    ToastUtil.show(getApplicationContext(), "请输入要查询的号码");

                    //手机震动效果
                    Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //震动毫秒值
                    vibrator.vibrate(2000);
                    //规律震动 （不震动时间，震动时间，不震动时间，震动时间。。。。），重复次数 -1表示不重复
                    vibrator.vibrate(new long[]{2000,5000,2000,5000}, -1);

                }

            }
        });

        //5。实时查询，（监听输入框文本变化）
        mEt_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone = mEt_phone.getText().toString();
                query(phone);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 获取电话号码归属地
     * @param phone 查询的电话号码
     */
    private void query(final String phone) {

        new Thread(){
            @Override
            public void run() {

                mAddress = AddressDao.getAddress(phone);
                //消息机制，告知主线程查询结束，可以取更新查询结果
                mHandler.sendEmptyMessage(0);
            }
        }.start();


    }
}
