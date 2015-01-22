package com.yuenidong.adapter;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.MatchEntity;
import com.yuenidong.bean.MatchInfoServerEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.util.TimeUtils;
import com.yuenidong.widget.CircleImageView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 石岩 on 2014/12/4.
 * 活动
 */
public class MatchAdapter extends BaseAdapter {
    private int count;
    private Context context;
    private List<MatchInfoServerEntity> list;
    private LayoutInflater inflater;

    public MatchAdapter(Context context, List<MatchInfoServerEntity> list) {
        this.context = context;
        this.list = list;
        this.inflater = LayoutInflater.from(context);
    }

    public void OnDateChange(List<MatchInfoServerEntity> list) {
        this.list = list;
        this.notifyDataSetChanged();
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

    //加载网络图片
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void loadImageVolley(String imageurl, ImageView imageView) {
//        String imageurl = "http://10.0.0.52/lesson-img.png";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        int maxSize = 20 * 1024 * 1024;
        final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
        ImageLoader.ImageCache imageCache = new ImageLoader.ImageCache() {

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);
            }
        };
        ImageLoader imageLoader = new ImageLoader(RequestManager.sRequestQueue, imageCache);
        ImageLoader.ImageListener listener = imageLoader.getImageListener(imageView,
                R.drawable.ic_launcher, R.drawable.ic_launcher);
        imageLoader.get(imageurl, listener);
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        DsncLog.e("i", i + "");
        MatchInfoServerEntity entity = list.get(i);
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_match, null);
            holder.iv_type = (ImageView) convertView.findViewById(R.id.iv_type);
            holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tv_coin_count = (TextView) convertView.findViewById(R.id.tv_coin_count);
            holder.iv_image = (CircleImageView) convertView.findViewById(R.id.iv_image);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.iv_sex = (ImageView) convertView.findViewById(R.id.iv_sex);
            holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
            holder.tv_distance = (TextView) convertView.findViewById(R.id.tv_distance);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_people_count = (TextView) convertView.findViewById(R.id.tv_people_count);
            holder.tv_heat = (TextView) convertView.findViewById(R.id.tv_heat);
            holder.tv_praise = (TextView) convertView.findViewById(R.id.tv_praise);
            holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            holder.rl_praise = (RelativeLayout) convertView.findViewById(R.id.rl_praise);
            holder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_praise);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_type.setImageResource(R.drawable.label_billiards);
        holder.tv_title.setText(entity.getTitle());
        holder.tv_coin_count.setText("奖励果冻" + entity.getCandyNum());
        loadImageVolley(entity.getUserImg(), holder.iv_image);
        holder.tv_name.setText(entity.getUserName());
        if (entity.getGender().equals("m")) {
            holder.iv_sex.setImageResource(R.drawable.ic_man);
        } else {
            holder.iv_sex.setImageResource(R.drawable.ic_woman);
        }
        holder.tv_place.setText(entity.getLocation());
        holder.tv_distance.setText(entity.getDistance() + "km");
        holder.tv_time.setText("时间:" + TimeUtils.StringToPatternTime(entity.getStartTime()));
        holder.tv_people_count.setText("人数:" + entity.getPeopleNum());
        holder.tv_heat.setText(entity.getHeat());
        holder.tv_praise.setText(entity.getGreat());
        holder.tv_comment.setText(entity.getRemarkNum());
        holder.rl_praise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DsncLog.e("i点击", i + "");
                ScaleAnimation animation = new ScaleAnimation(1.0f, 2.0f, 1.0f, 2.0f,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(300);//设置动画持续时间
                holder.iv_praise.startAnimation(animation);
                //--------------------点赞----------------------
                final HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", list.get(i).getUserId());
                map.put("activityId", list.get(i).getActivityId());
                map.put("parisedId", PreferenceUtil.getPreString("userId", ""));
                JSONObject jsonObject = null;
                try {
                    String str = CommonUtils.hashMapToJson(map);
                    jsonObject = new JSONObject(str);
                    DsncLog.e("jsonObject", jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                        Request.Method.POST, YueNiDongConstants.MATCHPRAISE, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("活动点赞success", response.toString());
                                Commvert commvert = new Commvert(response);
                                if (commvert.getString("status").equals("8")) {
                                    Toast.makeText(AppData.getContext(), "已点赞过!", Toast.LENGTH_SHORT).show();
                                    return;
                                } else if (commvert.getString("status").equals("1")) {
                                    String count = commvert.getString("greatNum");
                                    holder.tv_praise.setText(count);
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                    }
                }) {

                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Accept", "application/json");
                        headers.put("Content-Type", "application/json; charset=UTF-8");
                        return headers;
                    }
                };
                RequestManager.addRequest(jsonRequest, this);
            }
        });
        return convertView;
    }


}


class ViewHolder {
    //活动类型
    ImageView iv_type;
    //活动标题
    TextView tv_title;
    //果冻数量
    TextView tv_coin_count;
    //活动发起人的头像
    CircleImageView iv_image;
    //活动发起人的姓名
    TextView tv_name;
    //活动发起人的性别
    ImageView iv_sex;
    //活动的地点
    TextView tv_place;
    //活动距离用户的距离
    TextView tv_distance;
    //活动的时间
    TextView tv_time;
    //活动的人数
    TextView tv_people_count;
    //热度
    TextView tv_heat;
    //点赞
    TextView tv_praise;
    //评论
    TextView tv_comment;
    //点赞
    RelativeLayout rl_praise;
    //点赞
    ImageView iv_praise;
}
