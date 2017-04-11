package com.example.android.mobilesafe.activity;

import android.app.Application;

import org.xutils.x;

/**
 * Created by liubin on 2017/3/16.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
