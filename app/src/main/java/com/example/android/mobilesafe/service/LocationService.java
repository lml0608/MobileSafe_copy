package com.example.android.mobilesafe.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IntDef;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;

public class LocationService extends Service {

    public LocationService() {
    }


    @Override
    public void onCreate() {

        Log.i("LocationService", "服务启动了");
        super.onCreate();


        //获取手机的经纬度坐标
        //1，获取位置管理者对象
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //以最优的方式获取经纬度坐标
        Criteria criteria = new Criteria();
        //允许花费
        criteria.setCostAllowed(true);
        //指定准确性
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        locationManager.getBestProvider(criteria, true);

        //一定时间间隔，一定距离后获取坐标


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new MyLocationListener());
    }

    class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            //经度

            double longitude = location.getLongitude();


            //纬度
            double latitude = location.getLatitude();

            //发送短信给联系人
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("15112568068", null, "longitude = " + longitude + "，" + "latitude = " + latitude, null, null);

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LocationService", "服务销毁了");
    }
}
