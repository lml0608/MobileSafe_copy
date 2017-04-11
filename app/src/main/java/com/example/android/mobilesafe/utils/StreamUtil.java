package com.example.android.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * Created by liubin on 2017/3/14.
 */

public class StreamUtil {

    public static String streamToString(InputStream is) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];

        int temp = -1;
        try {
            while((temp = is.read(buffer)) != -1){
                bos.write(buffer, 0, temp);
            }

            return bos.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();

            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
