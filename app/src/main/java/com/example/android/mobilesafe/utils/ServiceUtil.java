package com.example.android.mobilesafe.utils;


import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.util.List;

/**
 * Created by liubin on 2017/4/6.
 */

public class ServiceUtil {


    public static boolean isRunning(Context context, String serviceName) {

        //获取activityManager管理者对象，可以获取手机正在运行的服务

        ActivityManager mAM = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        //获取手机中正在运行的服务集合
        List<RunningServiceInfo> runningServiceInfos = mAM.getRunningServices(100);

        //遍历获取的所有服务集合，拿到每一个服务的类的名称和传递进来的serviceName 进行对比，如果一致，说明服务在运行

        for (RunningServiceInfo runningServiceInfo : runningServiceInfos) {

            if (serviceName.equals(runningServiceInfo.service.getClassName())){
                return true;
            }
        }
        //

        return false;
    }
}
