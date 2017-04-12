package com.example.android.mobilesafe.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.db.BlackNumberOpenHelper;
import com.example.android.mobilesafe.db.dao.BlackNumberDao;
import com.example.android.mobilesafe.db.domain.BlackNumber;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.Md5Util;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlackNumberActivity extends AppCompatActivity {

    private static final String TAG = "BlackNumberActivity";
    private List<BlackNumber> mNumberList;
    private BlackNumberDao mBlackNumberDao;
    //添加按钮
    private Button bt_add;
    //listview
    private ListView lv_blacknumber;
    private MyAdapter mAdapter;
    private int mode = 1;

    private boolean mIsLoad = false;




    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            //4。listview取设置数据适配器
            if (mAdapter == null) {
                mAdapter = new MyAdapter();
                lv_blacknumber.setAdapter(mAdapter);
            } else {
                mAdapter.notifyDataSetChanged();
            }

        }
    };
    private int mCount;

    //适配器adapter
    class MyAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return mNumberList.size();
        }

        @Override
        public Object getItem(int position) {
            return mNumberList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            //1˙种方式优化listvie,fu复用view

            //复用ViewHolder
            ViewHolder holder = null;
            if (convertView == null) {

                convertView = View.inflate(getApplicationContext(), R.layout.listview_blacknumber_item, null);
                //2。减少findViewById()次数
                //复用ViewHolder步骤三
                holder = new ViewHolder();
                holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
                holder.tv_mode = (TextView) convertView.findViewById(R.id.tv_mode);
                holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);

                convertView.setTag(holder);
            } else {

                holder = (ViewHolder)convertView.getTag();
            }




            final BlackNumber blackNumber = mNumberList.get(position);
            holder.tv_phone.setText(blackNumber.getPhone());

            int mode = Integer.parseInt(blackNumber.getMode());
            switch (mode) {

                case 1:
                    holder.tv_mode.setText("拦截短信");
                    break;
                case 2:
                    holder.tv_mode.setText("拦截电话");
                    break;
                case 3:
                    holder.tv_mode.setText("拦截所有");
                    break;
                default:
                    break;
            }

            holder.iv_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                    mBlackNumberDao.deleteBlackNumber(blackNumber);
                    if (mAdapter != null) {
                        mAdapter.notifyDataSetChanged();
                        mNumberList.remove(position);
                        if (mAdapter != null) {

                            mAdapter.notifyDataSetChanged();
                        }
                    }

                }
            });


            return convertView;
        }
    }

    //复用ViewHolder步骤2
    class ViewHolder {

        TextView tv_phone;
        TextView tv_mode;
        ImageView iv_delete;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        initUI();
        initData();

    }

    /**
     * 获取数据库里所有的电话号码
     */
    private void initData() {
        new Thread(){
            @Override
            public void run() {
                //1.获取操作数据库黑名单的对象
                mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
                //2.数据库查询所有数据，保存在列表List<BlackNumber> mNumberList
                //mNumberList = mBlackNumberDao.getBlackNumber();
                mNumberList = mBlackNumberDao.getNumbers(0);

                //数据总条数
                mCount = mBlackNumberDao.getCounts();
                //通过消息机制告知主线程
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

//    private void loadData(int i) {
//
//        //1.获取操作数据库黑名单的对象
//        mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
//        //2.数据库查询所有数据，保存在列表List<BlackNumber> mNumberList
//        //mNumberList = mBlackNumberDao.getBlackNumber();
//        mNumberList = mBlackNumberDao.getNumbers(i);
//        //通过消息机制告知主线程
//        mHandler.sendEmptyMessage(0);
//    }

    private void initUI() {
        //添加按钮
        bt_add = (Button) findViewById(R.id.bt_add);
        //listview
        lv_blacknumber = (ListView) findViewById(R.id.lv_blacknumber);

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //添加弹框
                showDialog();
            }
        });

        //监听其滚动状态

        lv_blacknumber.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滚动过程中，发生改变调用的方法
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

               // AbsListView.OnScrollListener.SCROLL_STATE_FLING;飞速滚动
                // AbsListView.OnScrollListener.SCROLL_STATE_IDLE;空闲
                //AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL;拿手触摸着取滚动
                if (mNumberList != null) {

                    //1。滚动到停止状态
                    //2滚动到最后一个条目可见
                    if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE
                            && lv_blacknumber.getLastVisiblePosition() >= mNumberList.size() - 1
                            && !mIsLoad) {
                        //如果总条目大于集合大小
                        if (mCount > mNumberList.size()) {
                            //加载下一页数据
                            //如果正在加载mIsLoad改成true,本次加载完改为false
                            //下一次取执行加载的时候会判断mIsLoad的值，为false就加载，如果为true,就等待上一次加载完
                            new Thread(){
                                @Override
                                public void run() {
                                    //1.获取操作数据库黑名单的对象
                                    mBlackNumberDao = BlackNumberDao.getInstance(getApplicationContext());
                                    //2.数据库查询所有数据，保存在列表List<BlackNumber> mNumberList
                                    //mNumberList = mBlackNumberDao.getBlackNumber();
                                    List<BlackNumber> moreData = mBlackNumberDao.getNumbers(mNumberList.size());

                                    mNumberList.addAll(moreData);
                                    //通知适配器刷新
                                    //通过消息机制告知主线程
                                    mHandler.sendEmptyMessage(0);
                                }
                            }.start();
                        }


                    }

                }


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    /**
     * 添加电话号码和拦截模式弹框
     */
    private void showDialog() {

        final View view = LayoutInflater.from(this)
                .inflate(R.layout.dialog_add_blacknumber, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        //builder.setView(view,0,0,0,0);
        final AlertDialog dialog = builder.create();
        //点击对话框外部时对话框不消失
        dialog.setCanceledOnTouchOutside(false);

        final EditText et_phone = (EditText) view.findViewById(R.id.et_phone);
        RadioGroup rg_group = (RadioGroup) view.findViewById(R.id.rg_group);
        Button bt_submit = (Button) view.findViewById(R.id.bt_submit);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_cancel);

        rg_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                switch (checkedId) {

                    case R.id.rb_sms:
                        //拦截短信
                        mode = 1;
                        break;
                    case R.id.rb_phone:
                        //拦截电话
                        mode = 2;
                        break;
                    case R.id.rb_all:
                        //拦截所有
                        mode = 3;
                        break;
                }

            }
        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //获取输入框输入的电话号码
                String phone = et_phone.getText().toString();
                if (!TextUtils.isEmpty(phone)) {
                    //数据库插入
                    BlackNumber blackNumber = new BlackNumber();
                    blackNumber.setPhone("+86" + phone);
                    blackNumber.setMode(mode+"");
                    //插入数据
                    mBlackNumberDao.addBlackNumber(blackNumber);
                    //将对象插入到集合的最顶部
                    mNumberList.add(0, blackNumber);
                    //通知数据适配器刷新
                    if (mAdapter != null) {

                        mAdapter.notifyDataSetChanged();
                    }
                    //关闭弹框
                    dialog.dismiss();
                } else {

                    ToastUtil.show(getApplicationContext(), "请输入拦截号码");
                }

            }
        });

        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //关闭弹框
                dialog.dismiss();
            }
        });

        dialog.show();

    }


}
