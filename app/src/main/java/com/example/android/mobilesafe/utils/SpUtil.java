package com.example.android.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liubin on 2017/3/20.
 */

public class SpUtils {
    private static SharedPreferences sPreferences;

    //读

    /**
     * @param context 上下文环境
     * @param key 存储的节点名称
     * @param value 值
     */
    public static void putBoolean(Context context,String key, boolean value) {

        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences("config", 0);
        }

        sPreferences.edit().putBoolean(key, value).commit();

    }
    //写

    public static boolean getBoolean(Context context, String key, boolean defvalue) {

        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences("config", 0);
        }
        return sPreferences.getBoolean(key, defvalue);
    }


}
