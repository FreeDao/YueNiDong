package com.yuenidong.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerAdapter extends PagerAdapter {
    private Context context;
    private List<View> list;

    public ViewPagerAdapter(Context context, List<View> list) {
        this.context = context;
        this.list = list;
    }

    // 初始化当前的视图
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(list.get(position));
        return list.get(position);
    }

    // 得到视图的总个数
    @Override
    public int getCount() {
        return list.size();
    }

    // 销毁当前的视图
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list.get(position));
    }

    // 判断当前的对象和Object是否有关联
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

}
