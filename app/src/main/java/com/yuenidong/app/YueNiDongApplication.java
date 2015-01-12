package com.yuenidong.app;

import com.baidu.mapapi.SDKInitializer;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeStatusCode;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
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
import android.widget.Toast;

import com.igexin.sdk.PushManager;

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
        // 初始化ImageLoader
        @SuppressWarnings("deprecation")
        DisplayImageOptions options = new DisplayImageOptions.Builder().showStubImage(R.drawable.ic_stub) // 设置图片下载期间显示的图片
                .showImageForEmptyUri(R.drawable.ic_empty) // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.ic_error) // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true) // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true) // 设置下载的图片是否缓存在SD卡中
                        // .displayer(new RoundedBitmapDisplayer(20)) // 设置成圆角图片
                .build(); // 创建配置过得DisplayImageOption对象

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).defaultDisplayImageOptions(options)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).build();
        ImageLoader.getInstance().init(config);
        //初始化个推推送
//        PushManager.getInstance().initialize(this.getApplicationContext());

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
