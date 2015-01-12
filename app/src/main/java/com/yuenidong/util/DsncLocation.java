package com.yuenidong.util;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yuenidong.common.AppData;

/**
 * Created by 石岩 on 2014/12/8.
 */
public class DsncLocation {
    public Context mContext;
    public LocationClient mLocationClient = null;

    public static DsncLocation mDsncLocation = null;

    public static DsncLocation with(Context context) {
        synchronized (DsncLocation.class) {
            if (mDsncLocation == null) {
                mDsncLocation = new DsncLocation(context);
            }
            return mDsncLocation;
        }
    }

    public DsncLocation(Context context) {
        mContext = context;
    }

    public void getLocation(BDLocationListener locationListener) {
        if (mLocationClient == null) {
            //声明LocationClient
            mLocationClient = new LocationClient(AppData.getContext());
        }
        //配置定位SDK
        setOptions();
        //注册监听函数
        mLocationClient.registerLocationListener(locationListener);
        //启动定位SDK
        mLocationClient.start();
        //发起定位，异步获取当前位置
        mLocationClient.requestLocation();
    }

    private void setOptions() {
        LocationClientOption locationClientOption = new LocationClientOption();
        //设置定位模式
        locationClientOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //返回的定位结果是百度经纬度,默认值gcj02
        locationClientOption.setCoorType("bd09ll");
        //设置发起定位请求的间隔时间为5000ms
        locationClientOption.setScanSpan(2000);
        //设置打开GPS
        locationClientOption.setOpenGps(true);
        mLocationClient.setLocOption(locationClientOption);
    }
}
