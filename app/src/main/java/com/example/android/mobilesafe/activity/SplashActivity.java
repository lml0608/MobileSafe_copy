package com.example.android.mobilesafe.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.mobilesafe.R;
import com.example.android.mobilesafe.utils.ConstantValue;
import com.example.android.mobilesafe.utils.SpUtil;
import com.example.android.mobilesafe.utils.StreamUtil;
import com.example.android.mobilesafe.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    //需要更新
    private static final int UPDATE_VERSION = 100;
    //不需要更新
    private static final int ENTER_HOME = 101;
    //url出错
    private static final int URL_ERROR = 102;
    private static final int JSON_ERROR = 103;
    private static final int IO_ERROR = 104;

    private String mVersionDes;

    private String mDownloadUrl;
    private TextView tv_version_name;

    private ProgressDialog progressDialog;

    private int mLocalVersionCode;
    private Context mContext;

    private Handler mHandler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                case UPDATE_VERSION:
                    ToastUtil.show(getApplicationContext(),"有更新");
                    //弹出对话框，提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入主界面
                    enterHome();
                    break;
                case URL_ERROR:
                    //进入主界面
                    enterHome();
                    ToastUtil.show(getApplicationContext(),"url错误");
                    break;
                case JSON_ERROR:
                    //进入主界面
                    enterHome();
                    ToastUtil.show(getApplicationContext(),"json错误");
                    break;
                case IO_ERROR:
                    //进入主界面
                    enterHome();
                    ToastUtil.show(getApplicationContext(),"io错误");
                    break;
                default:
                    break;
            }
        }


    };
    private RelativeLayout rl_root;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        mContext = this;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //初始化ui
        initUI();
        //初始化数据
        initData();

        //初始化动画
        initAnimation();

        //初始化数据库
        initDB();



    }

    private void initDB() {

        //1.归属地数据拷贝
        initAddressDB("address.db");
    }

    /**
     * 拷贝数据库值files文件夹下
     * @param dbName
     */
    private void initAddressDB(String dbName) {

        File files = getFilesDir();
        Log.i(TAG, String.valueOf(files));///data/user/0/com.example.android.mobilesafe/files

        File file = new File(files, dbName);

        Log.i(TAG, String.valueOf(file));///data/user/0/com.example.android.mobilesafe/files/address.db

        if (file.exists()) {
            return;
        }
        //2.读取第三方资产目录下的文件
        InputStream stream = null;
        FileOutputStream fos = null;
        try {
            stream = getAssets().open(dbName);

            fos = new FileOutputStream(file);

            byte[] bs = new byte[1024];

            int temp = -1;

            while ((temp = stream.read(bs)) != -1) {
                fos.write(bs, 0, temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (stream != null && fos != null) {
                try {
                    stream.close();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 添加淡入动画效果
     */
    private void initAnimation() {

        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(3000);

        rl_root.startAnimation(alphaAnimation);
    }

    /**
     * 跳转到主界面
     */
    private void enterHome() {

        startActivity(new Intent(this, HomeActivity.class));

        //关闭导航
        finish();
    }

    /**
     * 升级对话框，弹出升级提示  http://192.168.1.101/MobileSafe.apk
     */
    private void showUpdateDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //下载apk
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //取消对话框，进入主界面
                enterHome();

            }
        });
        //点击取消事件监听
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                //取消对话框，进入对话框
                enterHome();
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        //点击对话框外部时对话框不消失
        alertDialog.setCanceledOnTouchOutside(false);
        //builder.//setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void downloadApk() {
        progressDialog = new ProgressDialog(this);

        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            //2获取sd路径
            final String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "app-release.apk";

            Log.i(TAG, path);

            RequestParams requestParams = new RequestParams(mDownloadUrl);
            //储存路径
            requestParams.setSaveFilePath(path);
            x.http().get(requestParams, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {

                    ToastUtil.show(getApplicationContext(),"下载成功");
                    progressDialog.dismiss();
                    Log.i(TAG, "文件名是什么"+String.valueOf(result));
                    installApk(result);

                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {

                    ex.printStackTrace();
                    ToastUtil.show(getApplicationContext(),"下载失败");
                    progressDialog.dismiss();

                }

                @Override
                public void onCancelled(CancelledException cex) {

                }

                @Override
                public void onFinished() {

                }

                @Override
                public void onWaiting() {

                }

                @Override
                public void onStarted() {
                    ToastUtil.show(getApplicationContext(),"开始下载");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {

                    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    progressDialog.setMessage("亲，努力下载中。。。");
                    progressDialog.show();
                    progressDialog.setMax((int) total);
                    progressDialog.setProgress((int) current);

                }
            });


        }
    }



    /**
     * 安装对应apk
     * @param file	安装文件
     */
    protected void installApk(File file) {
        //系统应用界面,源码,安装apk入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
		/*//文件作为数据源
		intent.setData(Uri.fromFile(file));
		//设置安装的类型
		intent.setType("application/vnd.android.package-archive");*/
        intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
//		startActivity(intent);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 获取数据方法
     */
    private void initData() {
        //1.应用版本名称
        String mVersionName = getVersionName();
        tv_version_name.setText("版本名称:" + mVersionName);
        //获取本地版本号和服务器版本对比，检测是否有更新，如果有更新，提示下载
        mLocalVersionCode = getVersionCode();

        //获取服务器的版本号(客服端发请求) json,xml

        if (SpUtil.getBoolean(this, ConstantValue.OPEN_UPDATE, false)) {
            checkVersion();
        } else {

            //直接进入主界面,当时进入主界面太快了，看不到第一个界面，需要修改，考虑消息机制
            //enterHome();
            //在发送消息完成后4秒后取处理当前的状态执行的消息
            mHandler.sendEmptyMessageDelayed( ENTER_HOME,4000);
        }

    }

    /**
     * 检测版本号
     */
    private void checkVersion() {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = Message.obtain();
                //记录运行时间 ,开始请求时间
                long startTime = System.currentTimeMillis();
                //发送请求
                try {
                    URL url = new URL("http://192.168.1.101/update.json");

                    Log.i(TAG, String.valueOf(url));

                    //1.开启链接
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();

                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    connection.setRequestMethod("GET");

                    if (connection.getResponseCode() == 200) {

                        //获取数据流并处理
                        InputStream is = connection.getInputStream();


                        String json = StreamUtil.streamToString(is);

                        Log.i(TAG, json);


                        JSONObject jsonObject = new JSONObject(json);

                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");

//                            Log.i(TAG, versionName);
//                            Log.i(TAG, versionDes);
//                            Log.i(TAG, versionCode);
//                            Log.i(TAG, downloadUrl);

                        Log.i(TAG, String.valueOf(Integer.parseInt(versionCode)));


                        if (mLocalVersionCode < Integer.parseInt(versionCode)) {

                            msg.what = UPDATE_VERSION;

                        } else {
                            msg.what = ENTER_HOME;
                        }

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                }catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                }finally {
                    //指定睡眠时间，请求网络超过4秒则不处理。小于4秒，强制睡眠满4秒
                   long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }

            }
        }).start();
    }

    /**
     * 返回版本号
     * @return
     * 非0代表获取成功
     */
    private int getVersionCode() {

        //1.包管理对象PackageManager
        PackageManager pm = getPackageManager();
        //2.从包管理者对象中，获取指定包名的基本信息（版本名称，版本号）,传0代编获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取版本号
            return packageInfo.versionCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;


    }

    /**
     * 获取版本名称
     */
    private String getVersionName() {
        //1.包管理对象PackageManager
        PackageManager pm = getPackageManager();
        //2.从包管理者对象中，获取指定包名的基本信息（版本名称，版本号）,传0代编获取基本信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //获取版本名称
            return packageInfo.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI方法
     */
    private void initUI() {

        tv_version_name = (TextView) findViewById(R.id.tv_version_name);

        rl_root = (RelativeLayout) findViewById(R.id.rl_root);

    }
}
