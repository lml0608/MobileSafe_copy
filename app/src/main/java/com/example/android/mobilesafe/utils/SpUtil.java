package com.example.android.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

/**
 * Created by liubin on 2017/3/20.
 */

public class SpUtil {
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


    public static void putString(Context context,String key, String value) {

        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences("config", 0);
        }

        sPreferences.edit().putString(key, value).commit();

    }
    //写

    public static String getString(Context context, String key, String defvalue) {

        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences("config", 0);
        }
        return sPreferences.getString(key, defvalue);
    }

    /**
     * 写入boolean变量至sp中
     * @param ctx	上下文环境
     * @param key	存储节点名称
     * @param value	存储节点的值string
     */
    public static void putInt(Context ctx,String key,int value){
        //(存储节点文件名称,读写方式)
        if(sPreferences == null){
            sPreferences = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sPreferences.edit().putInt(key, value).commit();
    }
    /**
     * 读取boolean标示从sp中
     * @param ctx	上下文环境
     * @param key	存储节点名称
     * @param defValue	没有此节点默认值
     * @return		默认值或者此节点读取到的结果
     */
    public static int getInt(Context ctx,String key,int defValue){
        //(存储节点文件名称,读写方式)
        if(sPreferences == null){
            sPreferences = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sPreferences.getInt(key, defValue);
    }


    /**
     * 删除储存节点
     * @param context 上下文
     * @param key 要删除的节点
     */
    public static void remove(Context context, String key) {

        if (sPreferences == null) {
            sPreferences = context.getSharedPreferences("config", 0);
        }

        sPreferences.edit().remove(key).commit();
    }
}
