package com.example.android.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeechService;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.i(TAG, "重启成功");
        //获取手机的序列号

        TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //重启后新的sim序列号
        String simSerialNumber = manager.getSimSerialNumber() + "1123";
        //取出原来储存的序列号
        String sim_number = SpUtil.getString(context, ConstantValue.SIM_NUMBER, "");

        Log.i(TAG, sim_number);

        String phone = SpUtil.getString(context, ConstantValue.CONTACT_PHONE, "");

        Log.i(TAG, phone);
        //如果sim序列号不一致，就发送报警
        if (!simSerialNumber.equals(sim_number)) {
            //发送短信给联系人
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("15217963186", null, "longitude", null, null);
            Log.i(TAG, "发送短信");


        }
    }
}
