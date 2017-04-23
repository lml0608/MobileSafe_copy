package com.example.android.mobilesafe.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.example.android.mobilesafe.db.domain.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubin on 2017/4/22.
 */

public class AppInfoProvider {

    private static final String TAG = "AppInfoProvider";


    /**
     * 返回目前手机所有应用的相关信息（名称，包名，图标，（内存，sd卡）（系统，用户））；
     */
    public static List<AppInfo> getAppInfoList(Context mContext) {

        //1。包的管理对象

        PackageManager pm = mContext.getPackageManager();

        List<PackageInfo> packageInfoList = pm.getInstalledPackages(0);

        List<AppInfo> appInfos = new ArrayList<>();

        for (PackageInfo packageInfo : packageInfoList) {

            AppInfo appInfo = new AppInfo();
            //应用包名
            String packageName = packageInfo.packageName;
            appInfo.setPackgeName(packageName);
            //应用名称
            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
            //名称
            appInfo.setName(applicationInfo.loadLabel(pm).toString());
            //Log.i(TAG, packageName);
            //图标
            appInfo.setIcon(applicationInfo.loadIcon(pm));

            //Log.i(TAG, "flag" + String.valueOf(applicationInfo.flags));

            //Log.i(TAG, String.valueOf(applicationInfo.loadIcon(pm)));
            //是否系统应用
            //ApplicationInfo.FLAG_SYSTEM=1
            boolean isSystemApp = (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM;

            appInfo.setSystem(isSystemApp);

//            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
//                //系统应用
//                appInfo.setSystem((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM);
//                Log.i(TAG, "系统应用");
//            } else {
//
//                //非系统应用
//                Log.i(TAG, "非系统应用");
//            }

            //是否安装在内存或sd

            boolean isExteranalStorage = (applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE;

            appInfo.setSdCard(isExteranalStorage);

            appInfos.add(appInfo);

        }


        return appInfos;
    }
}
