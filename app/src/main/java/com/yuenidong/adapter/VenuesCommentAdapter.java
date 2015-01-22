package com.yuenidong.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
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
import com.yuenidong.bean.VenuesCommentEntity;

import java.util.List;

/**
 * Created by 石岩 on 2014/12/4.
 * 场馆评论
 */
public class VenuesCommentAdapter extends BaseAdapter {
    private Context context;
    private List<VenuesCommentEntity> list;
    private LayoutInflater inflater;

    public VenuesCommentAdapter(Context context, List<VenuesCommentEntity> list) {
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
        VenuesCommentViewHolder holder = null;
        if (convertView == null) {
            holder = new VenuesCommentViewHolder();
            convertView = inflater.inflate(R.layout.venues_comment, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.iv_comment_one = (ImageView) convertView.findViewById(R.id.iv_comment_one);
            holder.iv_comment_two = (ImageView) convertView.findViewById(R.id.iv_comment_two);
            holder.iv_comment_three = (ImageView) convertView.findViewById(R.id.iv_comment_three);
            holder.iv_comment_four = (ImageView) convertView.findViewById(R.id.iv_comment_four);
            holder.iv_comment_five = (ImageView) convertView.findViewById(R.id.iv_comment_five);
            holder.tv_sign = (TextView) convertView.findViewById(R.id.tv_sign);


            convertView.setTag(holder);
        } else {
            holder = (VenuesCommentViewHolder) convertView.getTag();
        }
        loadImageVolley(list.get(i).getImg(), holder.iv_image);
        holder.tv_name.setText(list.get(i).getUserName());
        holder.tv_time.setText(list.get(i).getTime());
        holder.tv_sign.setText(list.get(i).getContent());
        if (list.get(i).getRemarkPoint().equals("1")) {
            holder.iv_comment_one.setVisibility(View.VISIBLE);
        }

        if (list.get(i).getRemarkPoint().equals("2")) {
            holder.iv_comment_one.setVisibility(View.VISIBLE);
            holder.iv_comment_two.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getRemarkPoint().equals("3")) {
            holder.iv_comment_one.setVisibility(View.VISIBLE);
            holder.iv_comment_two.setVisibility(View.VISIBLE);
            holder.iv_comment_three.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getRemarkPoint().equals("4")) {
            holder.iv_comment_one.setVisibility(View.VISIBLE);
            holder.iv_comment_two.setVisibility(View.VISIBLE);
            holder.iv_comment_three.setVisibility(View.VISIBLE);
            holder.iv_comment_four.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getRemarkPoint().equals("5")) {
            holder.iv_comment_one.setVisibility(View.VISIBLE);
            holder.iv_comment_two.setVisibility(View.VISIBLE);
            holder.iv_comment_three.setVisibility(View.VISIBLE);
            holder.iv_comment_four.setVisibility(View.VISIBLE);
            holder.iv_comment_five.setVisibility(View.VISIBLE);
        }


        return convertView;
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

class VenuesCommentViewHolder {
    ImageView iv_image;
    TextView tv_name;
    TextView tv_time;
    ImageView iv_comment_one;
    ImageView iv_comment_two;
    ImageView iv_comment_three;
    ImageView iv_comment_four;
    ImageView iv_comment_five;
    TextView tv_sign;
}
