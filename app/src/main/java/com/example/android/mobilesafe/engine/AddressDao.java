package com.example.android.mobilesafe.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by liubin on 2017/4/3.
 */

public class AddressDao {

    private static final String TAG = "AddressDao";
    //1。指定访问数据库的路径

    public static String path = "/data/user/0/com.example.android.mobilesafe/files/address.db";
    public static String mAddress = "未知";


    //2。传递一个电话号码，来气数据库链接，进行访问，返回一个归属地

    public static String getAddress(String phone) {
        mAddress = "未知号码";

        //开启数据库链接,只读
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);

        //1。正则表达式，匹配手机号
        String regularExpression = "^1[3-8]\\d{9}";
        if (phone.matches(regularExpression)) {
            phone =  phone.substring(0,7);

            Log.i(TAG,  phone);



            Cursor cursor = db.query("data1", new String[]{"outkey"}, "id = ?", new String[]{phone}, null, null,null) ;

            if (cursor.moveToNext()) {

                String outkey = cursor.getString(0);
                Cursor indexCursor = db.query("data2", new String[]{"location"}, "id = ?", new String[]{outkey}, null, null,null) ;

                if (indexCursor.moveToNext()) {

                    mAddress = indexCursor.getString(0);
                    Log.i(TAG, mAddress);
                }

            }
            else {
                mAddress = "未知号码";
            }
        } else {

            int length = phone.length();
            switch (length) {

                case 3: //110 //114 //112...
                    mAddress = "报警号码";
                    break;
                case 4: //110 //114 //112...
                    mAddress = "模拟器";
                    break;
                case 5: //10086.
                    mAddress = "服务电话";
                    break;
                case 7: //110 //114 //112...
                    mAddress = "本地电话";
                    break;
                case 8: //110 //114 //112...
                    mAddress = "本地电话";
                    break;
                case 11: //区号+座机号码
                    //查询 010-12365478
                    String area = phone.substring(1,3);
                    Log.i(TAG, "3+8" + area);
                    Cursor cursor = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area}, null, null, null);
                    if (cursor.moveToNext()) {
                        mAddress = cursor.getString(0);
                    }else {
                        mAddress = "未知号码";
                    }

                    break;
                case 12: //区号(0791)+座机号码
                    String area1 = phone.substring(1,4);

                    Log.i(TAG, "4+8" + area1);
                    Cursor cursor1 = db.query("data2", new String[]{"location"}, "area = ?", new String[]{area1}, null, null, null);
                    if (cursor1.moveToNext()) {
                        mAddress = cursor1.getString(0);
                    }else {
                        mAddress = "未知号码";
                    }
                    break;
                default:

            }
        }
        return mAddress;
    }
}
