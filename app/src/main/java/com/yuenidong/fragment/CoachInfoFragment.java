package com.yuenidong.fragment;

import android.annotation.TargetApi;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.yuenidong.activity.CoachCommentActivity;
import com.yuenidong.activity.ComeRecentActivity;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 教练主页
 */
public class CoachInfoFragment extends Fragment {
    private CoachEntity entity;
    @InjectView(R.id.iv_image)
    ImageView iv_image;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.iv_sex)
    ImageView iv_sex;
    @InjectView(R.id.tv_id)
    TextView tv_id;
    @InjectView(R.id.iv_label_one)
    ImageView iv_lable_one;
    @InjectView(R.id.iv_lable_two)
    ImageView iv_label_two;
    @InjectView(R.id.tv_care_count)
    TextView tv_care_count;
    @InjectView(R.id.tv_fans_count)
    TextView tv_fans_count;
    @InjectView(R.id.tv_coin_count)
    TextView tv_coin_count;
    @InjectView(R.id.tv_course)
    TextView tv_course;
    @InjectView(R.id.tv_honour)
    TextView tv_honour;
    @InjectView(R.id.iv_one)
    ImageView iv_one;
    @InjectView(R.id.iv_two)
    ImageView iv_two;
    @InjectView(R.id.iv_three)
    ImageView iv_three;
    @InjectView(R.id.iv_four)
    ImageView iv_four;
    @InjectView(R.id.iv_five)
    ImageView iv_five;
    @InjectView(R.id.tv_point)
    TextView tv_point;
    @InjectView(R.id.iv_image_one)
    ImageView iv_image_one;
    @InjectView(R.id.iv_image_two)
    ImageView iv_image_two;
    @InjectView(R.id.iv_image_three)
    ImageView iv_image_three;
    @InjectView(R.id.iv_image_four)
    ImageView iv_image_four;
    @InjectView(R.id.iv_image_five)
    ImageView iv_image_five;
    @InjectView(R.id.tv_place)
    TextView tv_place;


    @InjectView(R.id.rl_bottombar)
    RelativeLayout rl_bottombar;
    @InjectView(R.id.iv_phone)
    ImageButton iv_phone;
    //联系他
    @InjectView(R.id.tv_link)
    TextView tv_link;

    //---------------------联系他-----------------------------
    @OnClick(R.id.rl_bottombar)
    void rl_bottombar() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + entity.getPhoneNum()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppData.getContext().startActivity(intent);
    }

    //最近来过
    @OnClick(R.id.btn_come)
    void come() {
        Intent intent = new Intent(getActivity(), ComeRecentActivity.class);
        startActivity(intent);
    }

    //教练评价(总评级)
    @OnClick(R.id.btn_coachcomment)
    void comment() {
        Intent intent = new Intent(getActivity(), CoachCommentActivity.class);
        startActivity(intent);
    }

    public static CoachInfoFragment newInstance(CoachEntity entity) {
        CoachInfoFragment fragment = new CoachInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("coach", entity);
        fragment.setArguments(args);
        return fragment;
    }

    public CoachInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (CoachEntity) getArguments().getSerializable("coach");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_info, container, false);
        ButterKnife.inject(this, view);
        iv_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        DsncLog.e("抬起", "抬起");
                        iv_phone.setBackgroundResource(R.drawable.ic_phone);
                        tv_link.setTextColor(AppData.getColor(R.color.grayblack));
                        break;
                    case MotionEvent.ACTION_DOWN:
                        DsncLog.e("按下", "按下");
                        iv_phone.setBackgroundResource(R.drawable.ic_phone_pressed);
                        tv_link.setTextColor(AppData.getColor(R.color.green));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        DsncLog.e("抬起", "抬起");
                        iv_phone.setBackgroundResource(R.drawable.ic_phone);
                        tv_link.setTextColor(AppData.getColor(R.color.grayblack));
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13850734494"));
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        AppData.getContext().startActivity(intent);
                        break;
                }
                return false;
            }
        });
        //----------------------------教练最近来过-----------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("coachId", entity.getUserId());
        map.put("pageNum", "1");
        map.put("count", "5");
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.COACHRECENTCOME, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DsncLog.e("获取教练最近来过success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取教练最近来过success", "无信息");
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String url = jsonObject.getString("img");
                                    switch (i) {
                                        case 0:
                                            iv_image_one.setVisibility(View.VISIBLE);
                                            loadImageVolley(url,iv_image_one);
                                            break;
                                        case 1:
                                            iv_image_two.setVisibility(View.VISIBLE);
                                            loadImageVolley(url,iv_image_two);
                                            break;
                                        case 2:
                                            iv_image_three.setVisibility(View.VISIBLE);
                                            loadImageVolley(url,iv_image_three);
                                            break;
                                        case 3:
                                            iv_image_four.setVisibility(View.VISIBLE);
                                            loadImageVolley(url,iv_image_four);
                                            break;
                                        case 4:
                                            iv_image_five.setVisibility(View.VISIBLE);
                                            loadImageVolley(url,iv_image_five);
                                            break;
                                    }


                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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


        loadImageVolley(entity.getUserImg(), iv_image);
        tv_name.setText(entity.getTrueName());
        if (entity.getGender().equals("m")) {
            iv_sex.setImageResource(R.drawable.ic_man);
        } else {
            iv_sex.setImageResource(R.drawable.ic_woman);
        }
        tv_id.setText("ID:" + entity.getUserId());
        showImage(iv_lable_one, entity.getLabel0());
        showImage(iv_label_two, entity.getLabel1());
        tv_care_count.setText(entity.getCareNum());
        tv_fans_count.setText(entity.getFansNum());
        tv_coin_count.setText(entity.getCandyNum());
        tv_course.setText(entity.getLabel1());
        tv_point.setText(entity.getAvgScore());
        if (entity.getAvgScore().equals("1.0")) {
            iv_one.setVisibility(View.VISIBLE);
        }
        if (entity.getAvgScore().equals("2.0")) {
            iv_one.setVisibility(View.VISIBLE);
            iv_two.setVisibility(View.VISIBLE);
        }
        if (entity.getAvgScore().equals("3.0")) {
            iv_one.setVisibility(View.VISIBLE);
            iv_two.setVisibility(View.VISIBLE);
            iv_three.setVisibility(View.VISIBLE);
        }
        if (entity.getAvgScore().equals("4.0")) {
            iv_one.setVisibility(View.VISIBLE);
            iv_two.setVisibility(View.VISIBLE);
            iv_three.setVisibility(View.VISIBLE);
            iv_four.setVisibility(View.VISIBLE);
        }
        if (entity.getAvgScore().equals("5.0")) {
            iv_one.setVisibility(View.VISIBLE);
            iv_two.setVisibility(View.VISIBLE);
            iv_three.setVisibility(View.VISIBLE);
            iv_four.setVisibility(View.VISIBLE);
            iv_five.setVisibility(View.VISIBLE);
        }
        tv_place.setText(entity.getLocation());
        tv_honour.setText(entity.getHonor());

        return view;
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
}
