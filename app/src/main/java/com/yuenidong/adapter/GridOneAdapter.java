package com.yuenidong.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yuenidong.activity.R;
import com.yuenidong.common.AppData;

import java.util.List;

/**
 * Created by 石岩 on 2014/12/20.
 */
public class GridOneAdapter extends BaseAdapter {
    private Context context;
    private List<Integer> list;

    public GridOneAdapter(Context context, List<Integer> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(176, 42));//设置ImageView对象布局
//            imageView.setLayoutParams(new GridView.LayoutParams(88, 21));//设置ImageView对象布局
            imageView.setAdjustViewBounds(false);//设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(8, 8, 8, 8);//设置间距
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(list.get(position));//为ImageView设置图片资源
        return imageView;
    }
}
