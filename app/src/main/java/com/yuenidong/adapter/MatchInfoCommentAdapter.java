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
import com.yuenidong.bean.MatchInfoCommentEntity;
import com.yuenidong.util.TimeUtils;

import java.util.List;

/**
 * Created by 石岩 on 2014/12/15.
 * 活动详情评论
 */
public class MatchInfoCommentAdapter extends BaseAdapter {
    private List<MatchInfoCommentEntity> list;
    private Context context;
    private LayoutInflater inflater;

    public MatchInfoCommentAdapter(Context context, List<MatchInfoCommentEntity> list) {
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
        ViewAolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_match_comment, null);
            holder = new ViewAolder();
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewAolder) convertView.getTag();
        }
        loadImageVolley(list.get(i).getImg(), holder.iv_image);
        holder.tv_name.setText(list.get(i).getName());
        holder.tv_time.setText(list.get(i).getTime());
        holder.tv_content.setText(list.get(i).getContent());
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

class ViewAolder {
    ImageView iv_image;
    TextView tv_name;
    TextView tv_time;
    TextView tv_content;
}
