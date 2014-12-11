package com.yuenidong.app;

import com.baidu.mapapi.SDKInitializer;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuenidong.activity.FirstActivity;
import com.yuenidong.activity.LoginActivity;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.PhoneValidationActivity;
import com.yuenidong.activity.R;
import com.yuenidong.activity.ResetPasswordActivity;
import com.yuenidong.activity.ViewPagerActivity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.fragment.ResetPasswordFragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

/**
 * @author 石岩
 * @Description TODO 程序入口
 * @date 2014-11-29 下午4:18:15
 */
public class YueNiDongApplication extends Application {

    @SuppressLint("NewApi")
    @Override
    public void onCreate() {
        super.onCreate();
        AppData.init(getApplicationContext());
        //初始化百度地图SDK
        SDKInitializer.initialize(AppData.getContext());
        PreferenceUtil.setPreBoolean("isDirect", false);
        PreferenceUtil.setPreBoolean("isFirstLogin", true);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

            @Override
            public void onActivityStopped(Activity arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onActivityStarted(Activity arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onActivitySaveInstanceState(Activity arg0, Bundle arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onActivityResumed(Activity arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onActivityPaused(Activity arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onActivityDestroyed(Activity arg0) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onActivityCreated(Activity activity,
                                          Bundle savedInstanceState) {
                if (activity instanceof MainActivity) {
//                    if (PreferenceUtil.getPreBoolean("isFirstLogin", true)) {
//                        Intent intent = new Intent(activity,
//                                ViewPagerActivity.class);
//                        AppData.startActivity(intent);
//                        activity.finish();
//                    }
//                    if (!PreferenceUtil.getPreBoolean("isDirectLogin", false)) {
//                        Intent intent = new Intent(activity, MainActivity.class);
//                        AppData.startActivity(intent);
//                    }
                    //第一次登录判断
                    if (PreferenceUtil.getPreBoolean("isFirstLogin", true) && !PreferenceUtil.getPreBoolean("isDirect", false)) {
                        Intent intent = new Intent(activity, FirstActivity.class);
                        AppData.startActivity(intent);
                        activity.finish();
                    }
//                    Intent intent = new Intent(activity, FirstActivity.class);
//                    AppData.startActivity(intent);
//                    activity.finish();
                }
            }
        });
    }

}
