package com.yuenidong.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.yuenidong.activity.R;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.bean.FriendEntity;

import java.util.List;

/**
 * Created by 石岩 on 2014/12/4.
 * 教练
 */
public class CoachAdapter extends BaseAdapter {
    private List<CoachEntity> list;
    private LayoutInflater inflater;

    public CoachAdapter(Context context, List<CoachEntity> list) {
        this.list = list;
        this.inflater = LayoutInflater.from(context);
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        CoachViewHolder holder = null;
        if (convertView == null) {
            holder = new CoachViewHolder();
            convertView = inflater.inflate(R.layout.adapter_coach, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            holder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.iv_hobby_one = (ImageView) convertView.findViewById(R.id.iv_hobby_one);
            holder.iv_hobby_two = (ImageView) convertView.findViewById(R.id.iv_hobby_two);
            holder.iv_one = (ImageView) convertView.findViewById(R.id.iv_one);
            holder.iv_two = (ImageView) convertView.findViewById(R.id.iv_two);
            holder.iv_three = (ImageView) convertView.findViewById(R.id.iv_three);
            holder.iv_four = (ImageView) convertView.findViewById(R.id.iv_four);
            holder.iv_five = (ImageView) convertView.findViewById(R.id.iv_five);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            convertView.setTag(holder);
        } else {
            holder = (CoachViewHolder) convertView.getTag();
        }
        loadImageVolley(list.get(i).getUserImg(), holder.iv_image);
        holder.tv_name.setText(list.get(i).getTrueName());
        if (list.get(i).getGender().equals("m")) {
            holder.iv_sex.setImageResource(R.drawable.ic_man);
        } else {
            holder.iv_sex.setImageResource(R.drawable.ic_woman);
        }
        if (!TextUtils.isEmpty(list.get(i).getLabel0())) {
            holder.iv_hobby_one.setVisibility(View.VISIBLE);
            holder.iv_hobby_two.setVisibility(View.GONE);
            showImage(holder.iv_hobby_one, list.get(i).getLabel0());
        } else {
            holder.iv_hobby_one.setVisibility(View.GONE);
            holder.iv_hobby_two.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(list.get(i).getLabel1())) {
            holder.iv_hobby_one.setVisibility(View.VISIBLE);
            holder.iv_hobby_two.setVisibility(View.VISIBLE);
            showImage(holder.iv_hobby_two, list.get(i).getLabel1());
        } else {
            holder.iv_hobby_one.setVisibility(View.VISIBLE);
            holder.iv_hobby_two.setVisibility(View.GONE);
        }
        holder.tv_distance.setText(list.get(i).getDistance() + "km");
        holder.tv_sign.setText(list.get(i).getSignature());
        if (list.get(i).getAvgScore().equals("1.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getAvgScore().equals("2.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getAvgScore().equals("3.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
            holder.iv_three.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getAvgScore().equals("4.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
            holder.iv_three.setVisibility(View.VISIBLE);
            holder.iv_four.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getAvgScore().equals("5.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
            holder.iv_three.setVisibility(View.VISIBLE);
            holder.iv_four.setVisibility(View.VISIBLE);
            holder.iv_five.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    private void showImage(ImageView iv, String label1) {
        if (label1.equals("台球")) {
            iv.setImageResource(R.drawable.ic_billiards);
        }
        if (label1.equals("篮球")) {
            iv.setImageResource(R.drawable.ic_basketball);
        }
        if (label1.equals("跑步")) {
            iv.setImageResource(R.drawable.ic_running);
        }
        if (label1.equals("网球")) {
            iv.setImageResource(R.drawable.ic_tennis);
        }
        if (label1.equals("羽毛球")) {
            iv.setImageResource(R.drawable.ic_badminton);
        }
        if (label1.equals("足球")) {
            iv.setImageResource(R.drawable.ic_football);
        }
        if (label1.equals("骑行")) {
            iv.setImageResource(R.drawable.ic_riding);
        }
        if (label1.equals("乒乓球")) {
            iv.setImageResource(R.drawable.ic_tabletennis);
        }
        if (label1.equals("游泳")) {
            iv.setImageResource(R.drawable.ic_swimming);
        }
        if (label1.equals("健身")) {
            iv.setImageResource(R.drawable.ic_bodybuilding);
        }
        if (label1.equals("滑板")) {
            iv.setImageResource(R.drawable.ic_slidingplate);
        }
        if (label1.equals("轮滑")) {
            iv.setImageResource(R.drawable.icl_skidding);
        }
        if (label1.equals("教练")) {
            iv.setImageResource(R.drawable.ic_coach);
        }
        if (label1.equals("助教")) {
            iv.setImageResource(R.drawable.ic_teacher_unfilled);
        }
    }


    //加载网络图片
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void loadImageVolley(String imageurl, ImageView imageView) {
//        String imageurl = "http://10.0.0.52/lesson-img.png";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lurcache = new LruCache<String, Bitmap>(
                20);
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lurcache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lurcache.get(key);
            }
        };
        ImageLoader imageLoader = new ImageLoader(RequestManager.sRequestQueue, imageCache);
        ImageLoader.ImageListener listener = imageLoader.getImageListener(imageView,
                R.drawable.ic_launcher, R.drawable.ic_launcher);
        imageLoader.get(imageurl, listener);
    }
}

class CoachViewHolder {
    ImageView iv_image;
    TextView tv_name;
    ImageView iv_sex;
    ImageView iv_hobby_one;
    ImageView iv_hobby_two;
    ImageView iv_one;
    ImageView iv_two;
    ImageView iv_three;
    ImageView iv_four;
    ImageView iv_five;
    TextView tv_distance;
    TextView tv_sign;
}

