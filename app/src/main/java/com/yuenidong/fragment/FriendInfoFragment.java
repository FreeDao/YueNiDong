package com.yuenidong.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.gotye.api.GotyeUser;
import com.qinjia.activity.ChatPage;
import com.yuenidong.activity.MyCollectionActivity;
import com.yuenidong.activity.MyMatchActivity;
import com.yuenidong.activity.MySignActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 好友主页  石岩
 */
public class FriendInfoFragment extends Fragment {
    private FriendEntity entity;

    @InjectView(R.id.iv_image)
    ImageView iv_image;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.iv_sex)
    ImageView iv_sex;
    @InjectView(R.id.tv_id)
    TextView tv_id;
    @InjectView(R.id.iv_label_one)
    ImageView iv_label_one;
    @InjectView(R.id.iv_label_two)
    ImageView iv_label_two;
    @InjectView(R.id.iv_label_three)
    ImageView iv_label_three;
    @InjectView(R.id.tv_care_count)
    TextView tv_care_count;
    @InjectView(R.id.tv_fans_count)
    TextView tv_fans_count;
    @InjectView(R.id.tv_coin_count)
    TextView tv_coin_count;
    @InjectView(R.id.tv_sign)
    TextView tv_sign;

    //我的活动
    @OnClick(R.id.btn_mymatch)
    void match() {
        Intent intent = new Intent(getActivity(), MyMatchActivity.class);
        intent.putExtra("userId", entity.getUserId());
        startActivity(intent);
    }

    //我的收藏
    @OnClick(R.id.btn_mycollection)
    void collect() {
        Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
        startActivity(intent);
    }

    //我的签到
    @OnClick(R.id.btn_mysign)
    void sign() {
        Intent intent = new Intent(getActivity(), MySignActivity.class);
        startActivity(intent);
    }

    //对话
    @OnClick(R.id.rl_talk)
    void talk() {
        Intent intent = new Intent(getActivity(), ChatPage.class);
        GotyeUser user = new GotyeUser(entity.getImId());
        intent.putExtra("user", user);
        intent.putExtra("name", entity.getUserName());
        startActivity(intent);
    }

    //关注
    @OnClick(R.id.rl_care)
    void care() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("careUserId", entity.getUserId());
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.ADDCARE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("关注信息success", response.toString());
                        Commvert commvert = new Commvert(response);
                        Toast.makeText(AppData.getContext(), commvert.getString("msg"), Toast.LENGTH_SHORT).show();
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

    //拉黑
    @OnClick(R.id.rl_lahei)
    void lahei() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("blackId", entity.getUserId());
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.ADDBLACK, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("拉黑信息success", response.toString());
                        Commvert commvert = new Commvert(response);
                        Toast.makeText(AppData.getContext(), commvert.getString("msg"), Toast.LENGTH_SHORT).show();
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

    public static FriendInfoFragment newInstance(FriendEntity entity) {
        FriendInfoFragment fragment = new FriendInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", entity);
        fragment.setArguments(args);
        return fragment;
    }

    public FriendInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (FriendEntity) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_info, container, false);
        ButterKnife.inject(this, view);
        loadImageVolley(entity.getUserImg(), iv_image);
        tv_id.setText("ID:" + entity.getId());
        tv_name.setText(entity.getUserName());
        tv_care_count.setText(entity.getCareNum());
        tv_fans_count.setText(entity.getFansNum());
        tv_coin_count.setText(entity.getCandyNum());
        tv_sign.setText(entity.getSignature());
        if (entity.getGender().equals("m")) {
            iv_sex.setImageResource(R.drawable.ic_man);
        } else {
            iv_sex.setImageResource(R.drawable.ic_woman);
        }
        if (!TextUtils.isEmpty(entity.getLabel1())) {
            iv_label_one.setVisibility(View.VISIBLE);
            iv_label_two.setVisibility(View.GONE);
            iv_label_three.setVisibility(View.GONE);
            showImage(iv_label_one, entity.getLabel1());
        } else {
            iv_label_one.setVisibility(View.GONE);
            iv_label_two.setVisibility(View.GONE);
            iv_label_three.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(entity.getLabel2())) {
            iv_label_one.setVisibility(View.VISIBLE);
            iv_label_two.setVisibility(View.VISIBLE);
            iv_label_three.setVisibility(View.GONE);
            showImage(iv_label_two, entity.getLabel2());
        } else {
            iv_label_two.setVisibility(View.GONE);
            iv_label_three.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(entity.getLabel3())) {
            iv_label_three.setVisibility(View.VISIBLE);
            showImage(iv_label_three, entity.getLabel3());
        } else {
            iv_label_three.setVisibility(View.GONE);
        }
        return view;
    }


    private void showImage(ImageView iv, String label1) {
        if (label1.equals("台球")) {
            iv.setImageResource(R.drawable.label_billiards_filled);
        }
        if (label1.equals("篮球")) {
            iv.setImageResource(R.drawable.label_basketball_filled);
        }
        if (label1.equals("跑步")) {
            iv.setImageResource(R.drawable.label_running_filled);
        }
        if (label1.equals("网球")) {
            iv.setImageResource(R.drawable.label_tennis_filled);
        }
        if (label1.equals("羽毛球")) {
            iv.setImageResource(R.drawable.label_badminton_filled);
        }
        if (label1.equals("足球")) {
            iv.setImageResource(R.drawable.label_football_filled);
        }
        if (label1.equals("骑行")) {
            iv.setImageResource(R.drawable.label_riding_filled);
        }
        if (label1.equals("乒乓球")) {
            iv.setImageResource(R.drawable.label_tabletennis_filled);
        }
        if (label1.equals("游泳")) {
            iv.setImageResource(R.drawable.label_swimming_filled);
        }
        if (label1.equals("健身")) {
            iv.setImageResource(R.drawable.label_bodybuilding_filled);
        }
        if (label1.equals("滑板")) {
            iv.setImageResource(R.drawable.label_slidingplate_filled);
        }
        if (label1.equals("轮滑")) {
            iv.setImageResource(R.drawable.label_skidding_filled);
        }
        if (label1.equals("教练")) {
            iv.setImageResource(R.drawable.ic_coach_filled);
        }
        if (label1.equals("助教")) {
            iv.setImageResource(R.drawable.ic_teacher_filled);
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
