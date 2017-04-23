package com.example.android.mobilesafe.engine;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
import android.widget.ProgressBar;

import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by liubin on 2017/4/12.
 */

public class SmsBackUp {


    private static int index = 0;

    //备份短信方法

    /**
     * @param context 上下文环境
     * @param path  备份文件路径
     * @param callBack 接口
     */
    public static void backup(Context context, String path, CallBack callBack) {


        FileOutputStream fos = null;
        Cursor cursor = null;

        try {
            //获取文件
            File file = new File(path);
            //获取内容解析器

            ContentResolver contentResolver = context.getContentResolver();

            cursor = contentResolver.query(Uri.parse("content://sms/"),
                    new String[]{"address","date","type","body"},null,null,null);

            //读取数据，写入文件

            fos = new FileOutputStream(file);

            //序列化数据，放入xml
            XmlSerializer newSerializer = Xml.newSerializer();

            newSerializer.setOutput(fos, "utf-8");
            //DTD  xml规范
            newSerializer.startDocument("utf-8", true);

            newSerializer.startTag(null, "smss");

            //备份短信总数指定
            //a对话框
            //b进度条
            if (callBack != null) {
                callBack.setMax(cursor.getCount());
            }


            //读取数据写入xml

            while (cursor.moveToNext()) {
                newSerializer.startTag(null, "sms");

                newSerializer.startTag(null, "address");
                newSerializer.text(cursor.getString(0));
                newSerializer.endTag(null, "address");

                newSerializer.startTag(null, "date");
                newSerializer.text(cursor.getString(1));
                newSerializer.endTag(null, "date");

                newSerializer.startTag(null, "type");
                newSerializer.text(cursor.getString(2));
                newSerializer.endTag(null, "type");

                newSerializer.startTag(null, "body");
                newSerializer.text(cursor.getString(3));
                newSerializer.endTag(null, "body");

                newSerializer.endTag(null, "sms");


                //每循环一个跟新进度条
                index++;
                Thread.sleep(500);
                //可以在子线程中更新
                //pd.setProgress(index);

                //a
                //b
                if( callBack != null) {

                    callBack.setProgress(index);
                }
            }
            newSerializer.endTag(null, "smss");

            newSerializer.endDocument();


        } catch (Exception e) {
            e.printStackTrace();
        }finally {

            try {
                if (fos != null && cursor != null) {

                    cursor.close();
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    //回调

    public interface CallBack {
        //对话框，进度条。
        public void setMax(int max);

        //备份过程中百分比
        public void setProgress(int index);
    }



}
