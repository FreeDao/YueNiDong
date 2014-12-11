package com.yuenidong.activity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuenidong.adapter.ViewPagerAdapter;
import com.yuenidong.common.PreferenceUtil;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author 石岩
 * @Description TODO 程序引导页
 * @date 2014-11-29 下午4:40:43
 */
public class ViewPagerActivity extends Activity implements OnClickListener {
    private List<View> list;
    private LayoutInflater inflater;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        ButterKnife.inject(this);
        setStatusBarColor();
        // this.viewPager=(ViewPager) this.findViewById(R.id.viewpager);
        inflater = LayoutInflater.from(this);
        list = new ArrayList<View>();
        View view_one = inflater.inflate(R.layout.item_pagerone, null);
        View view_two = inflater.inflate(R.layout.item_pagertwo, null);
        View view_three = inflater.inflate(R.layout.item_pagerthree, null);
        View view_four = inflater.inflate(R.layout.item_pagerfour, null);
        view_four.setOnClickListener(this);
        list.add(view_one);
        list.add(view_two);
        list.add(view_three);
        list.add(view_four);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, list);
        viewPager.setAdapter(adapter);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.rl_four) {
            PreferenceUtil.setPreBoolean("isFirstLogin", false);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            this.finish();
        }
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
        }
    }

}
