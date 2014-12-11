package com.yuenidong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.yuenidong.activity.R;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.VenuesEntity;

import java.util.List;

/**
 * Created by Administrator on 2014/12/4.
 */
public class VenuesAdapter extends BaseAdapter {
    private Context context;
    private List<VenuesEntity> list;
    private LayoutInflater inflater;

    public VenuesAdapter(Context context, List<VenuesEntity> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 50;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        ViewBolder holder=null;
        if (convertView == null) {
//            holder=new ViewBolder();
            convertView = inflater.inflate(R.layout.adapter_venues, null);
//            holder.imageView= (CircleImageView) convertView.findViewById(R.id.iv_touxiang);
//            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
//            holder.iv_sex= (ImageView) convertView.findViewById(R.id.iv_sex);
//            holder.iv_level= (ImageView) convertView.findViewById(R.id.iv_level);
//            if(sex.equals("female")){
//                holder.iv_sex.setImageResource(R.drawable.ic_female);
//            }
//            convertView.setTag(holder);
        } else {
//            holder = (ViewBolder)convertView.getTag();
        }
        return convertView;
    }
}
