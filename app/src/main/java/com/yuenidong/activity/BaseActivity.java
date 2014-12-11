package com.yuenidong.activity;


import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuenidong.app.AppManager;
import com.yuenidong.common.AppData;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BaseActivity extends FragmentActivity {
    @InjectView(R.id.actionbar_left)
    ImageView iv_left;
    @InjectView(R.id.actionbar_text)
    TextView tv_title;
    @InjectView(R.id.actionbar_right)
    TextView tv_right;
    @InjectView(R.id.layout_actionbar)
    RelativeLayout rl_actionbar;
    @InjectView(R.id.ll_base)
    LinearLayout ll_base;
    @InjectView(R.id.actionbar_rightbutton)
    Button btn_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_base);
        ButterKnife.inject(this);
        AppManager.getAppManager().addActivity(this);
    }

    //隐藏actionbar
    protected void hideActionbar() {
        rl_actionbar.setVisibility(View.GONE);
    }

    //显示标题
    protected void showActionBarText(String str) {
        tv_title.setText(str);
        tv_title.setVisibility(View.VISIBLE);
    }

    //显示acitonbar左侧图标
    protected void showActionBarLeft(int a) {
        iv_left.setImageResource(a);
        iv_left.setVisibility(View.VISIBLE);
    }

    //显示actionbar右侧内容
    protected void showActionBarRight(String str) {
        tv_right.setText(str);
        tv_right.setVisibility(View.VISIBLE);
    }

    //显示actionbar右侧图片按钮
    protected void showActionBarRightButton() {
        btn_right.setVisibility(View.VISIBLE);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity();
    }

    @TargetApi(19)
    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // create our manager instance after the content view is set
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
//            tintManager.setNavigationBarTintEnabled(true);
            // set a custom tint color for all system bars
            tintManager.setTintColor(Color.parseColor("#7CFC00"));
// set a custom navigation bar resource
//            tintManager.setNavigationBarTintResource(R.drawable.ic_launcher);
        }
    }

}
