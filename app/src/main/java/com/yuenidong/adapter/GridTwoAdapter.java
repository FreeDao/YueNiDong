package com.yuenidong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.widget.SelectedImageView;

import java.util.List;

/**
 * Created by 石岩 on 2014/12/20.
 */
public class GridTwoAdapter extends BaseAdapter {
    private String[] sports = {"台球", "篮球", "跑步", "网球", "羽毛球", "足球", "骑行",
            "乒乓球", "游泳", "健身", "滑板", "轮滑"};
    private Context context;
    private List<Integer> list;
    private int screenWidthDp;
    private int screenHeightDp;

    public GridTwoAdapter(Context context, List<Integer> list, int screenWidthDp, int screenHeightDp) {
        this.context = context;
        this.list = list;
        this.screenWidthDp = screenWidthDp;
        this.screenHeightDp = screenHeightDp;
    }

    @Override
    public int getCount() {
        return 12;
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

        SelectedImageView imageView;
        if (convertView == null) {
            imageView = new SelectedImageView(context, true, sports[position]);
            DsncLog.e("screenWidthDp-250/3", (screenWidthDp - 250) / 3 + "");
            DsncLog.e("(screenHeightDp - 250) / 3)", (screenHeightDp - 250) / 3 + "");
            imageView.setLayoutParams(new GridView.LayoutParams((screenWidthDp - 250) / 3, (screenWidthDp - 250) / 3));//设置ImageView对象布局
//            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));//设置ImageView对象布局
            imageView.setAdjustViewBounds(false);//设置边界对齐
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
            imageView.setPadding(8, 8, 8, 8);//设置间距
        } else {
            imageView = (SelectedImageView) convertView;
        }
        DsncLog.e("position", position + "");
        DsncLog.e("sports[position]", sports[position]);
        DsncLog.e("one", PreferenceUtil.getPreString("label_one", ""));
        DsncLog.e("two", PreferenceUtil.getPreString("label_two", ""));
        DsncLog.e("three", PreferenceUtil.getPreString("label_three", ""));
        imageView.unselected(sports[position]);//为ImageView设置图片资源
//        if (sports[position].equals(PreferenceUtil.getPreString("label_one", "")) ||
//                sports[position].equals(PreferenceUtil.getPreString("label_two", "")) ||
//                sports[position].equals(PreferenceUtil.getPreString("label_three", ""))) {
//            imageView.selected(sports[position]);//为ImageView设置图片资源
//        }
        return imageView;
    }
}
