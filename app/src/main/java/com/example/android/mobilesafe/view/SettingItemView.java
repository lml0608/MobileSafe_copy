package com.example.android.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.mobilesafe.R;

/**
 * Created by liubin on 2017/3/17.
 */

public class SettingItemView extends RelativeLayout {


    private static final String TAG = SettingItemView.class.getSimpleName();
    private static final String NAMESPACE = "http://schemas.android.com/apk/res/com.example.android.mobilesafe";
    private TextView mTv_title;
    private TextView mTv_desf;
    private CheckBox mCb_box;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.setting_item_view, this);

//        View view = View.inflate(context, R.layout.setting_item_view, null);
//        this.addView(view);

        mTv_title = (TextView) findViewById(R.id.tv_title);
        mTv_desf = (TextView) findViewById(R.id.tv_des);
        mCb_box = (CheckBox) findViewById(R.id.cb_box);

        //获取自定义属性以及原声属性的操作
        initAttrs(attrs);
        mTv_title.setText(mDestitle);

    }

    /**
     * @param attrs 构造方法中维护好的属性集合
     *              返回属性集合中自定义属性值
     */
    private void initAttrs(AttributeSet attrs) {

//        int attrs1 = attrs.getAttributeCount();
//        Log.i(TAG, String.valueOf(attrs1));
//
//        //获取属性名称以及属性值
//        for (int i = 0; i < attrs.getAttributeCount(); i++) {
//            Log.i(TAG, "name = " + attrs.getAttributeName(i));
//            Log.i(TAG, "value = " + attrs.getAttributeValue(i));
//            Log.i(TAG, "分割线==============================");
//        }

        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");
        Log.i(TAG, mDestitle);
        Log.i(TAG, mDesoff);
        Log.i(TAG, mDeson);
    }


    //返回SettingItemView是否被选中，
    public boolean isChecked() {
        return mCb_box.isChecked();
    }

    //是否作为开启的变量，有点急过程中取做传递
    public void setChecke(boolean isChecked) {
        //在条目选中的过程中，mCb_box选中的状态也跟随对应传递的值变化
        mCb_box.setChecked(isChecked);
        if (isChecked) {
            //开启
            mTv_desf.setText(mDeson);
        } else {
            //关闭
            mTv_desf.setText(mDesoff);
        }
    }



}
