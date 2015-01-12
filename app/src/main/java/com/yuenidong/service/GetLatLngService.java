package com.yuenidong.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.util.DsncLocation;

/**
 * Created by 石岩 on 2015/1/7.
 */
public class GetLatLngService extends Service {
    private double longtitude;
    private double latitude;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DsncLocation.with(AppData.getContext()).getLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String address = bdLocation.getAddrStr();
//                DsncLog.e("Service当前地址为:", address);
                int code = bdLocation.getLocType();
//                DsncLog.e("Service百度地图定位结果code:", code + "");
                switch (code) {
                    case 161:
//                        DsncLog.e("Service百度地图定位结果:", "成功");
                        break;
                    case 63:
                        DsncLog.e("Service百度地图定位结果:", "网络异常");
                        break;
                    default:
                        DsncLog.e("Service百度地图定位结果", "失败");
                        break;
                }
                //获取经度
                longtitude = bdLocation.getLongitude();
                //获取纬度
                latitude = bdLocation.getLatitude();
//                DsncLog.e("longtitude经度:", longtitude + "");
//                DsncLog.e("latitude纬度:", latitude + "");
                PreferenceUtil.setPreString("longtitude",longtitude+"");
                PreferenceUtil.setPreString("latitude",latitude+"");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
