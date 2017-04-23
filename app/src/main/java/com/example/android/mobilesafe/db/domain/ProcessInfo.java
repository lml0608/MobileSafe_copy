package com.example.android.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by liubin on 2017/4/23.
 */

public class ProcessInfo {

    private String mName;
    private Drawable mIcon;
    private long mMemSize;
    private boolean mIsChoiced;
    private boolean mIsSysyem;
    //进程没有名称，就显示包名
    private String mPackageName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }


    public long getMemSize() {
        return mMemSize;
    }

    public void setMemSize(long memSize) {
        mMemSize = memSize;
    }

    public boolean isChoiced() {
        return mIsChoiced;
    }

    public void setChoiced(boolean choiced) {
        mIsChoiced = choiced;
    }

    public boolean isSysyem() {
        return mIsSysyem;
    }

    public void setSysyem(boolean sysyem) {
        mIsSysyem = sysyem;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }
}
