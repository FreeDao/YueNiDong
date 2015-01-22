package com.yuenidong.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
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
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.VenuesEntity;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 石岩 on 2014/12/4.
 * 场馆
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
        VenuesViewHolder holder = null;
        if (convertView == null) {
            holder = new VenuesViewHolder();
            convertView = inflater.inflate(R.layout.adapter_venues, null);
            holder.tv_startprice = (TextView) convertView.findViewById(R.id.tv_startprice);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.iv_one = (ImageView) convertView.findViewById(R.id.iv_one);
            holder.iv_two = (ImageView) convertView.findViewById(R.id.iv_two);
            holder.iv_three = (ImageView) convertView.findViewById(R.id.iv_three);
            holder.iv_four = (ImageView) convertView.findViewById(R.id.iv_four);
            holder.iv_five = (ImageView) convertView.findViewById(R.id.iv_five);
            holder.tv_endprice = (TextView) convertView.findViewById(R.id.tv_endprice);
            holder.tv_location = (TextView) convertView.findViewById(R.id.tv_location);
            convertView.setTag(holder);
        } else {
            holder = (VenuesViewHolder) convertView.getTag();
        }
        loadImageVolley(list.get(i).getImg(), holder.iv_image);
        holder.tv_name.setText(list.get(i).getName());
        holder.tv_distance.setText(list.get(i).getDistance()+"km");
        holder.tv_location.setText(list.get(i).getPlace());
        if (!TextUtils.isEmpty(list.get(i).getStartPrice())) {
            holder.tv_startprice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tv_startprice.setText(list.get(i).getStartPrice()+"元/小时");
        }
        if(!TextUtils.isEmpty(list.get(i).getEndPrice())) {
            holder.tv_endprice.setText(list.get(i).getEndPrice()+"元/小时");
        }
        if (list.get(i).getPoint().equals("1.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getPoint().equals("2.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getPoint().equals("3.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
            holder.iv_three.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getPoint().equals("4.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
            holder.iv_three.setVisibility(View.VISIBLE);
            holder.iv_four.setVisibility(View.VISIBLE);
        }
        if (list.get(i).getPoint().equals("5.0")) {
            holder.iv_one.setVisibility(View.VISIBLE);
            holder.iv_two.setVisibility(View.VISIBLE);
            holder.iv_three.setVisibility(View.VISIBLE);
            holder.iv_four.setVisibility(View.VISIBLE);
            holder.iv_five.setVisibility(View.VISIBLE);
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

class VenuesViewHolder {
    ImageView iv_image;
    TextView tv_name;
    TextView tv_distance;
    ImageView iv_one;
    ImageView iv_two;
    ImageView iv_three;
    ImageView iv_four;
    ImageView iv_five;
    TextView tv_startprice;
    TextView tv_endprice;
    TextView tv_location;
}
