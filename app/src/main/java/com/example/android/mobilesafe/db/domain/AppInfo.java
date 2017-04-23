package com.example.android.mobilesafe.db.domain;

import android.graphics.drawable.Drawable;

/**
 * Created by liubin on 2017/4/22.
 */

public class AppInfo {

    //名称，包名，图标，（内存，sd卡）（系统，用户））；
    private String mName;
    private String mPackgeName;
    private Drawable mIcon;
    private boolean mIsSdCard;
    private boolean mIsSystem;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPackgeName() {
        return mPackgeName;
    }

    public void setPackgeName(String packgeName) {
        mPackgeName = packgeName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
    }

    public boolean isSdCard() {
        return mIsSdCard;
    }

    public void setSdCard(boolean sdCard) {
        mIsSdCard = sdCard;
    }

    public boolean isSystem() {
        return mIsSystem;
    }

    public void setSystem(boolean system) {
        mIsSystem = system;
    }
}
