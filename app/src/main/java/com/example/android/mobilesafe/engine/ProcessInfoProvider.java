package com.example.android.mobilesafe.engine;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Debug;

import com.example.android.mobilesafe.db.domain.ProcessInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liubin on 2017/4/23.
 */

public class ProcessInfoProvider {

    //内存  读取文件／proc/meminfo 第一行是total内存

    //获取进程总数

    public static int getProcessCount(Context mContext) {
        //1.获取activityManager

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        //2。获取正在运行的进程集合
        List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();

        return runningAppProcesses.size();

    }


    //获取剩余内存大小
    public static long getAvailSpace(Context mContext) {
        //1.获取activityManager

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        //2。获取储存可用对象的对象

        MemoryInfo memoryInfo = new MemoryInfo();

        //给memoryInfo对象赋值，可用对象
        am.getMemoryInfo(memoryInfo);
        //获取memoryInfo中响应的可用内存
        return memoryInfo.availMem;

    }


    //获取内容总量

    public static long getTotalSpace(Context mContext) {
        //1.获取activityManager

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        //2。获取储存可用对象的对象

        MemoryInfo memoryInfo = new MemoryInfo();

        //给memoryInfo对象赋值，可用对象
        am.getMemoryInfo(memoryInfo);
        //获取memoryInfo中响应的可用内存
        return memoryInfo.totalMem;

    }


    public static List<ProcessInfo> getProcessInfo(Context mContext) {

        List<ProcessInfo> processInfoList = new ArrayList<>();

        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);

        //获取正在运行的进程集合

        List<RunningAppProcessInfo> runningAppProcesses = am.getRunningAppProcesses();
        //获取进程信息，名称。图标，包名。是否系统应用，使用内存大小

        for (RunningAppProcessInfo info : runningAppProcesses) {
            ProcessInfo processInfo = new ProcessInfo();
            //包名
            String processName = info.processName;
            //进程id
            int pid = info.pid;
            //获取进程占用的内存大小
            Debug.MemoryInfo[] processMemoryInfo = am.getProcessMemoryInfo(new int[]{pid});
            //返回数组中索引为0的对象，为当前进程的内存信息的对象
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            //获取已使用内存大小
            //int totalPrivateDirty = memoryInfo.getTotalPrivateDirty();
            long totalPrivateDirty = memoryInfo.getTotalPrivateDirty() * 1024;

        }

        return processInfoList;

    }

}
