package com.example.android.mobilesafe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

public abstract class BaseSetupActivity extends AppCompatActivity {

    private GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //2。创建手势管理的对象，用作管理在onTouchEvent(event)传递过来的手势动作
        //监听手势的移动
        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //监听手势的移动

                if (e1.getX() - e2.getX() > 0) {
                    //调用子类的下一页方法

                    //在第一个界面的时候，跳转到第2个界面
                    showNextPage();
                }
                if (e1.getX() - e2.getX() < 0) {
                    //调用子类的上一页方法
                    //在第1个界面的时候，无响应
                    //在第2个界面的时候，跳转到第1个界面
                    showPrePage();
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }




//    public void nextPage(View view) {
//
//    }



    //1。监听屏幕上响应的事件类型，
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //2。通过手势处理类，接收各种类型的事件，用做处理的方法
        mGestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    //下一页抽象方法，由子类决定具体跳转到那个界面
    protected abstract void showNextPage();

    //上一页抽象方法，由子类决定具体跳转到那个界面
    protected abstract void showPrePage();


    public void nextPage(View view) {
        showNextPage();
    }
    public void prePage(View view) {
        showPrePage();
    }
}
