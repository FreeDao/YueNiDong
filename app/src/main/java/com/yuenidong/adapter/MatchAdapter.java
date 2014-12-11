package com.yuenidong.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.MatchEntity;

import java.util.List;

/**
 * Created by Administrator on 2014/12/4.
 */
public class MatchAdapter extends BaseAdapter {
    private Context context;
    private List<MatchEntity> list;
    private LayoutInflater inflater;
    private ViewHolder holder = null;

    public MatchAdapter(Context context, List<MatchEntity> list) {
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
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_match, null);
            holder = new ViewHolder();
            holder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_praise);
            convertView.setTag(holder);
//            holder = new ViewHolder();
//            holder.rl_praise = (RelativeLayout) convertView.findViewById(R.id.rl_praise);
//            holder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_praise);
//            holder.imageView= (CircleImageView) convertView.findViewById(R.id.iv_touxiang);
//            holder.tv_name= (TextView) convertView.findViewById(R.id.tv_name);
//            holder.iv_sex= (ImageView) convertView.findViewById(R.id.iv_sex);
//            holder.iv_level= (ImageView) convertView.findViewById(R.id.iv_level);
//            if(sex.equals("female")){
//                holder.iv_sex.setImageResource(R.drawable.ic_female);
//            }
//            convertView.setTag(holder);
//            holder.rl_praise.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    DsncLog.e("点暂","");
//                    AnimationSet set = new AnimationSet(true);
//                    ScaleAnimation animation = new ScaleAnimation(0, 1.5f, 0, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//                    holder.iv_praise.setAnimation(animation);
//                    animation.start();
//                }
//            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DsncLog.e("点暂","");
            }
        });
        holder.iv_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DsncLog.e("点暂","");
            }
        });
        return convertView;
    }
}

class ViewHolder {
    RelativeLayout rl_praise;
    ImageView iv_praise;
}
