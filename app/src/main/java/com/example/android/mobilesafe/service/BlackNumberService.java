package com.example.android.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.db.dao.BlackNumberDao;
import com.example.android.mobilesafe.db.domain.BlackNumber;

import java.util.List;

public class BlackNumberService extends Service {

    private static final String TAG = "BlackNumberService";


    private InnerSmsReceiver mInnerSmsReceiver;
    private BlackNumberDao mBlackNumberDao;

    public BlackNumberService() {


    }

    @Override
    public void onCreate() {
        super.onCreate();

        //拦截短信

        IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(1000);
        mInnerSmsReceiver = new InnerSmsReceiver();
        //注册广播

        registerReceiver(mInnerSmsReceiver, intentFilter);
    }

    private class InnerSmsReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //获取短信内容，获取发送短信的电话号码。如果此电话号码在黑名单中
            //并且mode 为1或3，就拦截

            //2.获取短信内容
            Object[] objects = (Object[])intent.getExtras().get("pdus");
            //3,x循环便利短信过程

            for (Object object : objects) {

                SmsMessage sms =  SmsMessage.createFromPdu((byte[]) object);

                //获取短信基本信息

                String originatingAddress = sms.getOriginatingAddress();


                Log.i("SmsReceiver", originatingAddress);
                String messageBody = sms.getMessageBody();

                Log.i("SmsReceiver", messageBody);

                mBlackNumberDao = BlackNumberDao.getInstance(context);

                //String phone =  originatingAddress.substring(3,14);
                //Log.i(TAG, "phone= " + phone);
                int mode = mBlackNumberDao.getMode(originatingAddress);

                Log.i(TAG, "mode= " + String.valueOf(mode));

                if (mode == 1 || mode == 3) {

                    //拦截短信
                    abortBroadcast();
                } else {

                    //不拦截
                }
            }


        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {

        if (mInnerSmsReceiver != null) {

            unregisterReceiver(mInnerSmsReceiver);
        }
        super.onDestroy();
    }
}
