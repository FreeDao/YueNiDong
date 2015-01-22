package com.yuenidong.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yuenidong.activity.BookingQuicklyActivity;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.activity.VenuesAddSignActivity;
import com.yuenidong.activity.VenuesCommentActivity;
import com.yuenidong.activity.VenuesSignActivity;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.widget.ImageCycleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩  场馆信息
 */
public class VenuesInfoFragment extends Fragment {
    private VenuesEntity entity;
    @InjectView(R.id.tv_venues_name)
    TextView tv_venues_name;
    @InjectView(R.id.iv_venues_one)
    ImageView iv_venues_one;
    @InjectView(R.id.iv_venues_two)
    ImageView iv_venues_two;
    @InjectView(R.id.iv_venues_three)
    ImageView iv_venues_three;
    @InjectView(R.id.iv_venues_four)
    ImageView iv_venues_four;
    @InjectView(R.id.iv_venues_five)
    ImageView iv_venues_five;
    @InjectView(R.id.tv_point)
    TextView tv_point;
    @InjectView(R.id.tv_comment)
    TextView tv_comment;
    @InjectView(R.id.tv_phone)
    TextView tv_phone;
    @InjectView(R.id.tv_location)
    TextView tv_location;
    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.tv_signInCount)
    TextView tv_signInCount;
    @InjectView(R.id.tv_service)
    TextView tv_service;
    @InjectView(R.id.tv_remark)
    TextView tv_remark;
    @InjectView(R.id.tv_secondprice)
    TextView tv_secondprice;
    @InjectView(R.id.tv_firstprice)
    TextView tv_firstPrice;
    @InjectView(R.id.ad_view)
    ImageCycleView mAdView;

    @InjectView(R.id.tv_content)
    TextView tv_content;
    @InjectView(R.id.iv_image)
    ImageView iv_image;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.tv_date)
    TextView tv_date;
    @InjectView(R.id.iv_comment_one)
    ImageView iv_comment_one;
    @InjectView(R.id.iv_comment_two)
    ImageView iv_comment_two;
    @InjectView(R.id.iv_comment_three)
    ImageView iv_comment_three;
    @InjectView(R.id.iv_comment_four)
    ImageView iv_comment_four;
    @InjectView(R.id.iv_comment_five)
    ImageView iv_comment_five;


    private ArrayList<String> mImageUrl = null;

    private String imageUrl1 = "http://app.yuenidong.com/rrydBack1.1/img/2.jpg";

    private String imageUrl2 = "http://app.yuenidong.com/rrydBack1.1/img/3.jpg";

    private String imageUrl3 = "http://app.yuenidong.com/rrydBack1.1/img/4.jpg";

    private String imageUrl4 = "http://i1.s1.dpfile.com/pc/mc/3d8f08f18b0ee3dd757e536c84d34cc8(450c280)/aD0yODAmaz0vcGMvbWMvM2Q4ZjA4ZjE4YjBlZTNkZDc1N2U1MzZjODRkMzRjYzgmbG9nbz0wJm09YyZ3PTQ1MA.38cfc7cfb7bb928514511ee191cef45b/thumb.jpg";

    private String imageUrl5 = "http://i2.s1.dpfile.com/pc/mc/71493652f06b9f316e3c2543e1a6b685(450c280)/aD0yODAmaz0vcGMvbWMvNzE0OTM2NTJmMDZiOWYzMTZlM2MyNTQzZTFhNmI2ODUmbG9nbz0wJm09YyZ3PTQ1MA.ace4a1a0bf436fcfce5c9ba250a50475/thumb.jpg";

    //打电话
    @OnClick(R.id.rl_phone)
    void phone() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13850734494"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppData.getContext().startActivity(intent);
    }

    //场馆评价
    @OnClick(R.id.rl_venues_comment)
    void comment() {
        Intent intent = new Intent(getActivity(), VenuesCommentActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", entity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //添加签到
    @OnClick(R.id.rl_sign)
    void sign() {
        Intent intent = new Intent(getActivity(), VenuesSignActivity.class);
        startActivity(intent);
    }

    //立即预定
    @OnClick(R.id.btn_predetermine)
    void predetermine() {
        Intent intent = new Intent(getActivity(), BookingQuicklyActivity.class);
        startActivity(intent);
    }

    public static VenuesInfoFragment newInstance(VenuesEntity entity) {
        VenuesInfoFragment fragment = new VenuesInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("venues", entity);
        fragment.setArguments(args);
        return fragment;
    }

    public VenuesInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (VenuesEntity) getArguments().getSerializable("venues");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_venues_info, container, false);
        ButterKnife.inject(this, view);
        //-----------------获取场馆评论信息----------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("venderId", entity.getVenderId());
        map.put("pageNum", "1");
        map.put("count", "1");
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GETVENUESCOMMENT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取场馆评论1条success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                String content = jsonObject.getString("content");
                                tv_content.setText(content);
                                String img = jsonObject.getString("img");
                                loadImageVolley(img, iv_image);
                                String userName = jsonObject.getString("userName");
                                String remarkPoint = jsonObject.getString("remarkPoint");
                                String time = jsonObject.getString("time");
                                tv_name.setText(userName);
                                tv_time.setText(time);
                                if (remarkPoint.equals("1")) {
                                    iv_comment_one.setVisibility(View.VISIBLE);
                                }
                                if (remarkPoint.equals("2")) {
                                    iv_comment_one.setVisibility(View.VISIBLE);
                                    iv_comment_two.setVisibility(View.VISIBLE);
                                }
                                if (remarkPoint.equals("3")) {
                                    iv_comment_one.setVisibility(View.VISIBLE);
                                    iv_comment_two.setVisibility(View.VISIBLE);
                                    iv_comment_three.setVisibility(View.VISIBLE);
                                }
                                if (remarkPoint.equals("4")) {
                                    iv_comment_one.setVisibility(View.VISIBLE);
                                    iv_comment_two.setVisibility(View.VISIBLE);
                                    iv_comment_three.setVisibility(View.VISIBLE);
                                    iv_comment_four.setVisibility(View.VISIBLE);
                                }
                                if (remarkPoint.equals("5")) {
                                    iv_comment_one.setVisibility(View.VISIBLE);
                                    iv_comment_two.setVisibility(View.VISIBLE);
                                    iv_comment_three.setVisibility(View.VISIBLE);
                                    iv_comment_four.setVisibility(View.VISIBLE);
                                    iv_comment_five.setVisibility(View.VISIBLE);
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

        tv_venues_name.setText(entity.getName());
        tv_point.setText(entity.getPoint() + "总评分");
        if (entity.getPoint().equals("1.0")) {
            iv_venues_one.setVisibility(View.VISIBLE);
        }
        if (entity.getPoint().equals("2.0")) {
            iv_venues_one.setVisibility(View.VISIBLE);
            iv_venues_two.setVisibility(View.VISIBLE);
        }
        if (entity.getPoint().equals("3.0")) {
            iv_venues_one.setVisibility(View.VISIBLE);
            iv_venues_two.setVisibility(View.VISIBLE);
            iv_venues_three.setVisibility(View.VISIBLE);
        }
        if (entity.getPoint().equals("4.0")) {
            iv_venues_one.setVisibility(View.VISIBLE);
            iv_venues_two.setVisibility(View.VISIBLE);
            iv_venues_three.setVisibility(View.VISIBLE);
            iv_venues_four.setVisibility(View.VISIBLE);
        }
        if (entity.getPoint().equals("5.0")) {
            iv_venues_one.setVisibility(View.VISIBLE);
            iv_venues_two.setVisibility(View.VISIBLE);
            iv_venues_three.setVisibility(View.VISIBLE);
            iv_venues_four.setVisibility(View.VISIBLE);
            iv_venues_five.setVisibility(View.VISIBLE);
        }
        tv_comment.setText(entity.getRemarkCount() + "条评论");
        tv_phone.setText(entity.getVenderPhone());
        tv_location.setText(entity.getPlace());
        tv_time.setText(entity.getBusinessHoursStart() + "-" + entity.getBusinessHoursEnd());
        tv_signInCount.setText(entity.getSignInCount() + "人签到");
        tv_service.setText(entity.getService());
        tv_remark.setText(entity.getRemark());
        tv_firstPrice.setText(entity.getStartPrice() + "元/小时");
        tv_firstPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);//中划线
        tv_secondprice.setText(entity.getEndPrice() + "元/小时");

//        //获取手机屏幕宽度和高度
//        DisplayMetrics dm = new DisplayMetrics();
//        //取得窗口属性
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//
//        //窗口的宽度
//        int screenWidth = dm.widthPixels;
//
//        //窗口高度
//        int screenHeight = dm.heightPixels;
//
//        int screenWidthDp = CommonUtils.px2dip(getActivity(), screenWidth);
////        int screenHeightDp = CommonUtils.px2dip(getActivity(), screenHeight);
//        int height = (int) (screenWidthDp);
//        ViewGroup.LayoutParams lp = mAdView.getLayoutParams();
//        lp.height = screenWidthDp * 9 / 16;
////        lp.height = screenHeight;
//        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
//        DsncLog.e("heightDp", height + "");
//        DsncLog.e("widthDp", screenWidthDp + "");
//        DsncLog.e("heightPx", screenHeight + "");
//        DsncLog.e("widthPx", screenWidth + "");
//        DsncLog.e("heightPx/1.4", screenHeight / 1.4 + "");
//        DsncLog.e("widthPx/1.4", screenWidth / 1.4 + "");
//        mAdView.setLayoutParams(lp);
        mImageUrl = new ArrayList<String>();
        mImageUrl.add(entity.getPic0());
        if (!TextUtils.isEmpty(entity.getPic1())) {
            mImageUrl.add(entity.getPic1());
        }
        if (!TextUtils.isEmpty(entity.getPic2())) {
            mImageUrl.add(entity.getPic2());
        }
        if (!TextUtils.isEmpty(entity.getPic3())) {
            mImageUrl.add(entity.getPic3());
        }
        if (!TextUtils.isEmpty(entity.getPic4())) {
            mImageUrl.add(entity.getPic4());
        }
        mAdView.setImageResources(mImageUrl, mAdCycleViewListener);
        return view;
    }

    private ImageCycleView.ImageCycleViewListener mAdCycleViewListener = new ImageCycleView.ImageCycleViewListener() {

        @Override
        public void onImageClick(int position, View imageView) {
            // TODO 单击图片处理事件
            Toast.makeText(getActivity(), "position->" + position, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void displayImage(String imageURL, ImageView imageView) {
            ImageLoader.getInstance().displayImage(imageURL, imageView);// 此处本人使用了ImageLoader对图片进行加装！
        }
    };


    @Override
    public void onResume() {
        super.onResume();
        mAdView.startImageCycle();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdView.pushImageCycle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mAdView.pushImageCycle();
    }

    //加载网络图片
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void loadImageVolley(String imageurl, ImageView imageView) {
//        String imageurl = "http://10.0.0.52/lesson-img.png";
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
        final LruCache<String, Bitmap> lurcache = new LruCache<String, Bitmap>(
                20);
        com.android.volley.toolbox.ImageLoader.ImageCache imageCache = new com.android.volley.toolbox.ImageLoader.ImageCache() {
            @Override
            public void putBitmap(String key, Bitmap value) {
                lurcache.put(key, value);
            }

            @Override
            public Bitmap getBitmap(String key) {
                return lurcache.get(key);
            }
        };
        com.android.volley.toolbox.ImageLoader imageLoader = new com.android.volley.toolbox.ImageLoader(RequestManager.sRequestQueue, imageCache);
        com.android.volley.toolbox.ImageLoader.ImageListener listener = imageLoader.getImageListener(imageView,
                R.drawable.ic_launcher, R.drawable.ic_launcher);
        imageLoader.get(imageurl, listener);
    }

}

