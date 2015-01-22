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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.yuenidong.activity.R;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.FriendEntity;

import java.util.List;

/**
 * Created by 石岩 on 2014/12/4.
 * 球友
 */
public class FriendAdapter extends BaseAdapter {
    private Context context;
    private List<FriendEntity> list;
    private LayoutInflater inflater;

    public FriendAdapter(Context context, List<FriendEntity> list) {
        this.context = context;
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
        ViewHolderFriend holder = null;
        if (convertView == null) {
            holder = new ViewHolderFriend();
            convertView = inflater.inflate(R.layout.adapter_friend, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.iv_label_one = (ImageView) convertView.findViewById(R.id.iv_label_one);
            holder.iv_label_two = (ImageView) convertView.findViewById(R.id.iv_label_two);
            holder.iv_label_three = (ImageView) convertView.findViewById(R.id.iv_label_three);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderFriend) convertView.getTag();
        }
        loadImageVolley(list.get(i).getUserImg(),holder.iv_image);
        holder.tv_name.setText(list.get(i).getUserName());
        if (list.get(i).getGender().equals("m")) {
            holder.iv_sex.setImageResource(R.drawable.ic_man);
        } else {
            holder.iv_sex.setImageResource(R.drawable.ic_woman);
        }
        if (!TextUtils.isEmpty(list.get(i).getLabel1())) {
            holder.iv_label_one.setVisibility(View.VISIBLE);
            holder.iv_label_two.setVisibility(View.GONE);
            holder.iv_label_three.setVisibility(View.GONE);
            showImage(holder.iv_label_one, list.get(i).getLabel1());
        } else {
            holder.iv_label_one.setVisibility(View.GONE);
            holder.iv_label_two.setVisibility(View.GONE);
            holder.iv_label_three.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(list.get(i).getLabel2())) {
            holder.iv_label_one.setVisibility(View.VISIBLE);
            holder.iv_label_two.setVisibility(View.VISIBLE);
            holder.iv_label_three.setVisibility(View.GONE);
            showImage(holder.iv_label_two, list.get(i).getLabel2());
        } else {
            holder.iv_label_two.setVisibility(View.GONE);
            holder.iv_label_three.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(list.get(i).getLabel3())) {
            holder.iv_label_three.setVisibility(View.VISIBLE);
            showImage(holder.iv_label_three, list.get(i).getLabel3());
        } else {
            holder.iv_label_three.setVisibility(View.GONE);
        }
        holder.tv_distance.setText(list.get(i).getDistance()+"km");
        holder.tv_sign.setText(list.get(i).getSignature());
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
            iv.setImageResource(R.drawable.ic_teacher);
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
class ViewHolderFriend {
    //球友头像
    ImageView iv_image;
    //球友姓名
    TextView tv_name;
    //球友性别
    ImageView iv_sex;
    //距离球友距离
    TextView tv_distance;
    //标签一
    ImageView iv_label_one;
    //标签二
    ImageView iv_label_two;
    //标签三
    ImageView iv_label_three;
    //球友宣言
    TextView tv_sign;
}
