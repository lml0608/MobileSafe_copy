package com.example.android.mobilesafe.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.db.domain.AppInfo;
import com.example.android.mobilesafe.db.domain.BlackNumber;
import com.example.android.mobilesafe.engine.AppInfoProvider;
import com.example.android.mobilesafe.engine.SdTest;
import com.example.android.mobilesafe.utils.ToastUtil;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AppManagerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "AppManagerActivity";
    private List<AppInfo> mAppInfoList;

    private AppAdapter mAdapter;
    private ListView mList;
    private List<AppInfo> mSystemList;
    private List<AppInfo> mCustomerList;
    private TextView mLv_des;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            mAdapter = new AppAdapter();

            mList.setAdapter(mAdapter);
            if (mLv_des != null && mCustomerList != null) {

                mLv_des.setText("用户应用（" + mCustomerList.size() + "）");
            }

        }
    };
    private AppInfo mAppInfo;
    private PopupWindow mPopupWindow;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_manager);

        initTitle();

        initAppList();
    }

    private void initAppList() {
        mList = (ListView) findViewById(R.id.lv_app_list);
        mLv_des = (TextView) findViewById(R.id.tv_des);

        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            //滚动过程中，发生改变调用的方法
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滚动过程中

                if (mCustomerList != null && mSystemList != null) {
                    if (firstVisibleItem >= mCustomerList.size() + 1) {
                        //滚动到系统应用
                        mLv_des.setText("系统应用（" + mSystemList.size() + "）");

                    } else {
                        mLv_des.setText("用户应用（" + mCustomerList.size() + "）");
                    }
                }

            }
        });
        //点击事件
        //描述条目不需要点击事件
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == mCustomerList.size() + 1) {

                    return;
                } else {

                    if (position < mCustomerList.size() + 1) {
                        mAppInfo = mCustomerList.get(position - 1);
                        //ToastUtil.show(getApplicationContext(), "你好啊！");
                    } else {
                        mAppInfo = mSystemList.get(position - mCustomerList.size() - 2);
                    }
                    //弹出窗体
                    showPopupWindow(view);

//                    View popupView = View.inflate(getApplicationContext(), R.layout.popupwindow_layout, null);
//
//                    TextView uninstallTextView = (TextView) popupView.findViewById(R.id.uninstall_text_view);
//                    TextView openAppTextView = (TextView) popupView.findViewById(R.id.open_text_view);
//                    TextView shareTextView = (TextView) popupView.findViewById(R.id.share_text_view);
////        /*卸载*/
////                    uninstallTextView.setOnClickListener();
////        /*开启*/
////                    openAppTextView.setOnClickListener(this);
////        /*分享*/
////                    shareTextView.setOnClickListener(this);
//
//                    //创建窗体对象，指定宽高
//                    PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
//                            LinearLayout.LayoutParams.WRAP_CONTENT, true);
//                    //设置一个透明背景
//                    popupWindow.setBackgroundDrawable(new ColorDrawable());
//
//                    //指定窗口位置
//                    popupWindow.showAsDropDown(view, 50, -view.getHeight());

                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    private void getData() {
        new Thread() {

            @Override
            public void run() {
                mAppInfoList = AppInfoProvider.getAppInfoList(getApplicationContext());
                //系统应用列表
                mSystemList = new ArrayList<AppInfo>();
                //用户应用列表
                mCustomerList = new ArrayList<AppInfo>();

                for (AppInfo appInfo : mAppInfoList) {

                    if (appInfo.isSystem()) {
                        //系统应用
                        mSystemList.add(appInfo);
                    } else {
                        //用户应用
                        mCustomerList.add(appInfo);
                    }
                }
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    /**
     * 点击list条目。弹出窗体，卸载，打开，分享
     * @param view
     */
    private void showPopupWindow(View view) {

//        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
//        View popupView = inflater.inflate(R.layout.popupwindow_layout, null);
        View popupView = View.inflate(getApplicationContext(), R.layout.popupwindow_layout, null);

        TextView uninstallTextView = (TextView) popupView.findViewById(R.id.uninstall_text_view);
        TextView openAppTextView = (TextView) popupView.findViewById(R.id.open_text_view);
        TextView shareTextView = (TextView) popupView.findViewById(R.id.share_text_view);
        /*卸载*/
        uninstallTextView.setOnClickListener(this);
        /*开启*/
        openAppTextView.setOnClickListener(this);
        /*分享*/
        shareTextView.setOnClickListener(this);

        //窗体动画 淡入淡出，由小变大两种
        //透明变不透明
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);

        //由小变大

        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0, 1,
                0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        scaleAnimation.setDuration(500);
        scaleAnimation.setFillAfter(true);

        //动画集合

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);


        //创建窗体对象，指定宽高
        mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置一个透明背景
        mPopupWindow.setBackgroundDrawable(new ColorDrawable());

        Log.i(TAG, String.valueOf(view.findViewById(R.id.iv_icon).getWidth()));
        //指定窗口位置
        mPopupWindow.showAsDropDown(view, view.findViewById(R.id.iv_icon).getWidth(), -view.getHeight());
        //popupWindow.showAsDropDown(view, 0, );
        //开启动画
        popupView.startAnimation(animationSet);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void initTitle() {
        //1获取／data可用大小，磁盘路径

        String path = Environment.getDataDirectory().getAbsolutePath();
        File file = Environment.getDataDirectory();
//        file.getTotalSpace();
//        file.getFreeSpace();
        Log.i(TAG, "磁盘路径：" + path);// "/data"
        Log.i(TAG, "总空间：" + file.getTotalSpace());// "/data"
        Log.i(TAG, "可用空间：" + file.getFreeSpace());// "/data"

        //2获取SD卡可用大小，sd卡路径  /storage/emulated/0

        String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        Log.i(TAG, "SD卡路径：" + sdPath);// "/storage/emulated/0"

        //sd卡  sdmulu/storage/0403-0201
        String sdmulu = SdTest.getStoragePath(this, true);
        Log.i(TAG, "sdmulu" + sdmulu);

        String formatFileSize = Formatter.formatFileSize(this, getAvailSpace(sdPath));
        Log.i(TAG, "内部储存：" + formatFileSize);
        //1.14G
        String formatSdFileSize = Formatter.formatFileSize(this, getAvailSpace(sdmulu));
        Log.i(TAG, "外部sd：" + formatSdFileSize);

        //显示内部储存
        TextView tv_memory = (TextView) findViewById(R.id.tv_memory);
        //显示外部储存
        TextView tv_sd_memory = (TextView) findViewById(R.id.tv_sd_memory);

        tv_memory.setText("磁盘可用:" + formatFileSize);
        tv_sd_memory.setText("sd卡可用:" + formatSdFileSize);


    }

    //int能保存2G
    /**
     * 返回byte = 8bit
     * @param path
     * @return 返回类型为long,因为int不够用
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private long getAvailSpace(String path) {
        //获取可用磁盘大小类
        StatFs statfs = new StatFs(path);
        //获取可用区块的个数
        //long count = statfs.getAvailableBlocks();
        long count = statfs.getAvailableBlocksLong();
        //long count = statfs.getFreeBlocksLong();
        //long count = statfs.getBlockCountLong();
        //区块大小
        //long size = statfs.getBlockSize();
        long size = statfs.getBlockSizeLong();
        //可用空间大小
        return count * size;
    }

    //实现接口的点击事件
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.uninstall_text_view:

                //卸载应用
                //ToastUtil.show(getApplicationContext(), "下载");

                if (mAppInfo.isSystem()) {
                    //系统应用不可卸载
                    ToastUtil.show(getApplicationContext(), "系统应用不可卸载");

                } else {
                    //卸载用户应用
                    Intent intent = new Intent("android.intent.action.DELETE");
                    intent.addCategory("android.intent.category.DEFAULT");
                    intent.setData(Uri.parse("package:" + mAppInfo.getPackgeName()));
                    startActivity(intent);

                }
                break;
            case R.id.open_text_view:

                //开启应用
                //ToastUtil.show(getApplicationContext(), "下载");
                PackageManager pm = getPackageManager();

                //ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
                //通过Launch开启指定报名的意图，去开启应用
                Intent launchIntentForPackage = pm.getLaunchIntentForPackage(mAppInfo.getPackgeName());

                if (launchIntentForPackage != null) {
                    startActivity(launchIntentForPackage);
                } else {

                    ToastUtil.show(getApplicationContext(), "此应用不能被开启");
                }
                break;
            case R.id.share_text_view:

                //分享应用
                //ToastUtil.show(getApplicationContext(), "下载");
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "分享一个应用，应用名称为" + mAppInfo.getName());
                shareIntent.setType("text/plain");
                startActivity(Intent.createChooser(shareIntent, getResources().getText(R.string.send_to)));

                break;
            default:
                break;
        }
        if (mPopupWindow != null) {
            //关闭窗口
            mPopupWindow.dismiss();
        }

    }

    private class AppAdapter extends BaseAdapter {


//        public int getViewTypeCount() {
//            return 1;
//        }
        //获取数据适配器中条目类型的总数,修改成2种
        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;
        }

//        public int getItemViewType(int position) {
//            return 0;
//        }
        //指定索引指向的条目类型,条目类型状态码指定，0（系统复用），1

        @Override
        public int getItemViewType(int position) {

            if (position == 0 || position  == mCustomerList.size() + 1) {
                return 0;//纯文本的状态码
            } else {
                //代表纯文本+图片
                return 1;
            }
        }



        //增加2个描述条目
        @Override
        public int getCount() {
            return mSystemList.size() + mCustomerList.size() + 2;
        }

        @Override
        //public Object getItem(int position) {
        public AppInfo getItem(int position) {

            if (position == 0 || position == mCustomerList.size() + 1) {

                return null;
            } else {

                if (position < mCustomerList.size() + 1) {
                    return mCustomerList.get(position - 1);
                } else {
                    return mSystemList.get(position - mCustomerList.size() - 2);
                }

            }



        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            //Log.i(TAG, "position= " + String.valueOf(position) + "type= " + String.valueOf(getItemViewType(position)));
            //Log.i(TAG, "position= " + String.valueOf(position) + getItem(position).getName());
            int type = getItemViewType(position);
            if (type == 0) {
                //展示描述

                ViewTitleHolder holder = null;
                if (convertView == null) {

                    convertView = View.inflate(getApplicationContext(), R.layout.app_list_item_title, null);
                    //2。减少findViewById()次数
                    //复用ViewHolder步骤三
                    holder = new ViewTitleHolder();
                    holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);


                    convertView.setTag(holder);
                } else {

                    holder = (AppManagerActivity.ViewTitleHolder)convertView.getTag();
                }

                if (position == 0) {



                    //Log.i(TAG, "position = " + position);
                    holder.tv_title.setText("用户应用（" + mCustomerList.size() + "）");

                } else {
                    holder.tv_title.setText("系统应用（" + mSystemList.size() + "）");
                }
                return convertView;


            } else {
                //展示应用条目

                //1˙种方式优化listvie,fu复用view

                //复用ViewHolder
                ViewHolder holder = null;
                if (convertView == null) {

                    convertView = View.inflate(getApplicationContext(), R.layout.app_list_item, null);
                    //2。减少findViewById()次数
                    //复用ViewHolder步骤三
                    holder = new ViewHolder();
                    holder.iv_icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    holder.tv_path = (TextView) convertView.findViewById(R.id.tv_path);
                    holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);


                    convertView.setTag(holder);
                } else {

                    holder = (AppManagerActivity.ViewHolder)convertView.getTag();
                }




                //AppInfo appInfo = mAppInfoList.get(position);
                //AppInfo appInfo = (AppInfo) getItem(position);
                AppInfo appInfo = getItem(position);
                holder.iv_icon.setImageDrawable(appInfo.getIcon());
                holder.tv_path.setText(appInfo.getPackgeName());
                holder.tv_name.setText(appInfo.getName());


                return convertView;
            }

        }
    }

    //复用ViewHolder步骤2
    class ViewHolder {

        TextView tv_path;
        TextView tv_name;
        ImageView iv_icon;

    }


    class ViewTitleHolder {
        TextView tv_title;
    }

//    // 获取SD卡的完整空间大小，返回MB
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    public static long getSDCardSize() {
//        if (isSDCardMounted()) {
//            StatFs fs = new StatFs(getSDCardBaseDir());
//            long count = fs.getBlockCountLong();
//            long size = fs.getBlockSizeLong();
//            return count * size / 1024 / 1024;
//        }
//        return 0;
//    }
//
//    // 获取SD卡的剩余空间大小
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    public static long getSDCardFreeSize() {
//        if (isSDCardMounted()) {
//            StatFs fs = new StatFs(getSDCardBaseDir());
//            long count = fs.getFreeBlocksLong();
//            long size = fs.getBlockSizeLong();
//            return count * size / 1024 / 1024;
//        }
//        return 0;
//    }
//
//    // 获取SD卡的可用空间大小
//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    public static long getSDCardAvailableSize() {
//        if (isSDCardMounted()) {
//            StatFs fs = new StatFs(getSDCardBaseDir());
//            long count = fs.getAvailableBlocksLong();
//            long size = fs.getBlockSizeLong();
//            return count * size / 1024 / 1024;
//        }
//        return 0;
//    }


}
