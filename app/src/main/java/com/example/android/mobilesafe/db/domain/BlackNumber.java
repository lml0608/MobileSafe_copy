package com.example.android.mobilesafe.db.domain;

/**
 * Created by liubin on 2017/4/7.
 */

public class BlackNumber {

    private String phone;

    private String mode;

    public BlackNumber(String phone, String mode) {
        this.phone = phone;
        this.mode = mode;
    }
    public BlackNumber() {

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "BlackNumber [phone=" + phone + ", mode=" + mode + "]";
    }
}
