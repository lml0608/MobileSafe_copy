package com.example.android.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.service.LocationService;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //1.判断是否开启防盗保护

        Log.i("SmsReceiver", "收到短信了。短信内容读取");

        boolean open_security = SpUtil.getBoolean(context, ConstantValue.OPEN_SECURITY, false);

        Log.i("SmsReceiver", String.valueOf(open_security));
        if (open_security) {

            //2.获取短信内容
            Object[] objects = (Object[])intent.getExtras().get("pdus");
            //3,x循环便利短信过程

            for (Object object : objects) {

                SmsMessage sms =  SmsMessage.createFromPdu((byte[]) object);

                //获取短信基本信息

                String originatingAddress = sms.getOriginatingAddress();


                String messageBody = sms.getMessageBody();

                Log.i("SmsReceiver", messageBody);

                //判断内容是否包含了播放音乐的关键字

                if (messageBody.contains("#*alarm*#")){
                    //7播放音乐

                    MediaPlayer mediaPlayer = MediaPlayer.create(context, R.raw.ylzs);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                }
                if (messageBody.contains("#*location*#")) {
                    //8开启获取位置服务
                    context.startService(new Intent(context, LocationService.class));
                }
            }
        }
    }
}
