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
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.BlackMenuActivity;
import com.yuenidong.activity.ChooseLabelActivity;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.MyCollectionActivity;
import com.yuenidong.activity.MyInfoUpdateActivity;
import com.yuenidong.activity.MyMatchActivity;
import com.yuenidong.activity.MyOrderActivity;
import com.yuenidong.activity.MySignActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.MyUserEntity;
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
 * 石岩  我的
 */
public class MeFragment extends Fragment {
    @InjectView(R.id.iv_image)
    ImageView iv_image;
    @InjectView(R.id.tv_name)
    TextView tv_userName;
    @InjectView(R.id.iv_gender)
    ImageView iv_gender;
    @InjectView(R.id.tv_id)
    TextView tv_id;
    @InjectView(R.id.iv_label_one)
    ImageView iv_label_one;
    @InjectView(R.id.iv_lable_two)
    ImageView iv_label_two;
    @InjectView(R.id.iv_label_three)
    ImageView iv_label_three;
    @InjectView(R.id.iv_mylabel_one)
    ImageView iv_mylabel_one;
    @InjectView(R.id.iv_mylabel_two)
    ImageView iv_mylabel_two;
    @InjectView(R.id.iv_mylabel_three)
    ImageView iv_mylabel_three;
    @InjectView(R.id.tv_care_count)
    TextView tv_care;
    @InjectView(R.id.tv_fans_count)
    TextView tv_fans;
    @InjectView(R.id.tv_candy_count)
    TextView tv_candy;
    @InjectView(R.id.iv_pointme)
    ImageView iv_pointme;
    @InjectView(R.id.tv_sign)
    TextView tv_sign;


    private String Image_path;
    private String userName;
    private String gender;
    private String ID;
    private String label1;
    private String label2;
    private String label3;
    private String care;
    private String fans;
    private String candy;
    private String sign;
    private String phoneNumber;
    MyUserEntity user=new MyUserEntity();

    boolean isBack=false;


    //我的订单
    @OnClick(R.id.btn_myorder)
    void order() {
        Intent intent = new Intent(getActivity(), MyOrderActivity.class);
        startActivity(intent);
    }

    //我的活动
    @OnClick(R.id.btn_mymatch)
    void match() {
        Intent intent = new Intent(getActivity(), MyMatchActivity.class);
        startActivity(intent);
    }

    //我的签到
    @OnClick(R.id.btn_mysign)
    void mysign() {
        Intent intent = new Intent(getActivity(), MySignActivity.class);
        startActivity(intent);
    }

    //黑名单
    @OnClick(R.id.btn_blackmenu)
    void blackmenu() {
        Intent intent = new Intent(getActivity(), BlackMenuActivity.class);
        startActivity(intent);
    }

    //我的收藏
    @OnClick(R.id.btn_mycollection)
    void collection() {
        Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
        startActivity(intent);
    }

    //我的标签
    @OnClick(R.id.rl_four)
    void label() {
        Intent intent = new Intent(getActivity(), ChooseLabelActivity.class);
        Bundle bundle=new Bundle();
        DsncLog.e("我的标签","我的标签");
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //设置
    @OnClick(R.id.btn_setting)
    void setting() {
        Intent intent = new Intent(getActivity(), MyInfoUpdateActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("user",user);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        ButterKnife.inject(this, view);
        getMyInfo();
        return view;
    }

    private void getMyInfo() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GET_INFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("用户信息", response.toString());
                        Commvert commvert = new Commvert(response);
                        try {
                            Image_path = response.getString("userImg");
                            userName = response.getString("userName");
                            gender = response.getString("gender");
                            ID = response.getString("id");
                            label1 = commvert.getString("label0");
                            label2 = commvert.getString("label1");
                            label3 = commvert.getString("label2");
                            care = response.getString("careNum");
                            fans = response.getString("fansNum");
                            candy = response.getString("candyNum");
                            sign = response.getString("signature");
                            phoneNumber=response.getString("phoneNum");

                            user.setImage_path(Image_path);
                            user.setUserName(userName);
                            user.setGender(gender);
                            user.setID(ID);
                            user.setLabel1(label1);
                            user.setLabel2(label2);
                            user.setLabel3(label3);
                            user.setCare(care);
                            user.setFans(fans);
                            user.setCandy(candy);
                            user.setSign(sign);


                            loadImageVolley(Image_path, iv_image);
                            tv_userName.setText(userName);
                            if (gender.equals("m")) {
                                iv_gender.setImageResource(R.drawable.ic_man);
                            } else {
                                iv_gender.setImageResource(R.drawable.ic_woman);
                            }
                            tv_id.setText(ID);
                            if (!TextUtils.isEmpty(label1)) {
                                iv_label_one.setVisibility(View.VISIBLE);
                                iv_mylabel_one.setVisibility(View.VISIBLE);
                                showImage(iv_label_one, label1);
                                showImage(iv_mylabel_one, label1);
                                iv_pointme.setVisibility(View.GONE);
                            } else {
                                iv_label_one.setVisibility(View.GONE);
                                iv_mylabel_one.setVisibility(View.GONE);
                                iv_pointme.setVisibility(View.VISIBLE);
                            }
                            if (!TextUtils.isEmpty(label2)) {
                                iv_label_two.setVisibility(View.VISIBLE);
                                iv_mylabel_two.setVisibility(View.VISIBLE);
                                showImage(iv_label_two, label2);
                                showImage(iv_mylabel_two, label2);
                            } else {
                                iv_label_two.setVisibility(View.GONE);
                                iv_mylabel_two.setVisibility(View.GONE);
                            }
                            if (!TextUtils.isEmpty(label3)) {
                                iv_label_three.setVisibility(View.VISIBLE);
                                iv_mylabel_three.setVisibility(View.VISIBLE);
                                showImage(iv_label_three, label3);
                                showImage(iv_mylabel_three, label3);
                            } else {
                                iv_label_three.setVisibility(View.GONE);
                                iv_mylabel_three.setVisibility(View.GONE);
                            }
                            tv_care.setText(care);
                            tv_fans.setText(fans);
                            tv_candy.setText(candy);
                            if (!sign.equals("null")) {
                                tv_sign.setText(sign);
                            }

                        } catch (JSONException e) {
                            DsncLog.e("-------", "数据解析错误");
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

    @Override
    public void onResume() {
        super.onResume();
        DsncLog.e("onResume","OnResume");
        if(isBack){
            getMyInfo();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        isBack=true;
    }
}
