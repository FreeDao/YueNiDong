package com.yuenidong.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuenidong.activity.CoachInfoActivity;
import com.yuenidong.activity.R;
import com.yuenidong.activity.VenuesInfoActivity;
import com.yuenidong.adapter.ViewPagerAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.DsncLocation;
import com.yuenidong.widget.ZoomControlView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CoachMapFragment extends Fragment {
    private List<View> list_view;
    private View view;
    private ZoomControlView mZoomControlView;
    private double latitude;
    private double longtitude;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //软件园中路测试纬度，经度
    private double latitude_test = 30.437986;
    private double longtitude_test = 114.448277;
    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();
    @InjectView(R.id.container)
    FrameLayout mapFrameLayout;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;
    private Marker maker;
    ArrayList<CoachEntity> list;

    public static CoachMapFragment newInstance() {
        CoachMapFragment fragment = new CoachMapFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public CoachMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_map, container, false);
        ButterKnife.inject(this, view);
        //放入百度地图的FrameLayout
        mapFrameLayout = (FrameLayout) view.findViewById(R.id.container);
        LatLng point = new LatLng(latitude, longtitude);
        mMapView = new MapView(getActivity(),
                new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(point).zoom(12.0f).build()));
        mMapView.getChildAt(1).setAlpha(0f);
        mMapView.getChildAt(2).setAlpha(0f);
        mapFrameLayout.addView(mMapView);
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);

        //缩放控制
        mZoomControlView = (ZoomControlView) view.findViewById(R.id.ZoomControlView);
        mZoomControlView.setMapView(mMapView);

        latitude = Double.parseDouble(PreferenceUtil.getPreString("latitude", ""));
        longtitude = Double.parseDouble(PreferenceUtil.getPreString("longtitude", ""));
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .latitude(latitude)
                .longitude(longtitude).build();
        // 设置定位数据
        mBaiduMap.setMyLocationData(locData);
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mMakers = BitmapDescriptorFactory
                .fromResource(R.drawable.pic_position);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING, true, mMakers);
        mBaiduMap.setMyLocationConfigeration(config);
        getFriendsData();

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Bundle extraInfo = marker.getExtraInfo();
                CoachEntity entity = (CoachEntity) extraInfo.getSerializable("coach");
                Intent intent = new Intent(getActivity(), CoachInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("coach", entity);
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    //得到球友数据
    private void getFriendsData() {
//        //---------------------获取教练信息-------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userRole", "2");
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GETALLFRIEND, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取教练信息success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取教练信息success", "无教练信息");
                                return;
                            } else {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<CoachEntity>>() {
                                }.getType();
                                list = gson.fromJson(jsonArray.toString(), type);
                                //-----------------------------------------------------------------
                                list_view = new ArrayList<View>();
                                if (list.size() > 0) {
                                    for (int i = 0; i < list.size(); i++) {
                                        view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_coach_map, null);
                                        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                                        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                                        ImageView iv_sex = (ImageView) view.findViewById(R.id.iv_sex);
                                        TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                                        ImageView iv_label_one = (ImageView) view.findViewById(R.id.iv_hobby_one);
                                        ImageView iv_label_two = (ImageView) view.findViewById(R.id.iv_hobby_two);
                                        ImageView iv_one = (ImageView) view.findViewById(R.id.iv_one);
                                        ImageView iv_two = (ImageView) view.findViewById(R.id.iv_two);
                                        ImageView iv_three = (ImageView) view.findViewById(R.id.iv_three);
                                        ImageView iv_four = (ImageView) view.findViewById(R.id.iv_four);
                                        ImageView iv_five = (ImageView) view.findViewById(R.id.iv_five);
                                        TextView tv_sign = (TextView) view.findViewById(R.id.tv_sign);

                                        loadImageVolley(list.get(i).getUserImg(), iv_image);
                                        tv_name.setText(list.get(i).getTrueName());
                                        if (list.get(i).getGender().equals("f")) {
                                            iv_sex.setImageResource(R.drawable.ic_woman);
                                        } else {
                                            iv_sex.setImageResource(R.drawable.ic_man);
                                        }
                                        tv_distance.setText(list.get(i).getDistance());
                                        showImage(iv_label_one, list.get(i).getLabel0());
                                        showImage(iv_label_two, list.get(i).getLabel1());
                                        if (list.get(i).getAvgScore().equals("1.0")) {
                                            iv_one.setVisibility(View.VISIBLE);
                                        }
                                        if (list.get(i).getAvgScore().equals("2.0")) {
                                            iv_one.setVisibility(View.VISIBLE);
                                            iv_two.setVisibility(View.VISIBLE);
                                        }
                                        if (list.get(i).getAvgScore().equals("3.0")) {
                                            iv_one.setVisibility(View.VISIBLE);
                                            iv_two.setVisibility(View.VISIBLE);
                                            iv_three.setVisibility(View.VISIBLE);
                                        }
                                        if (list.get(i).getAvgScore().equals("4.0")) {
                                            iv_one.setVisibility(View.VISIBLE);
                                            iv_two.setVisibility(View.VISIBLE);
                                            iv_three.setVisibility(View.VISIBLE);
                                            iv_four.setVisibility(View.VISIBLE);
                                        }
                                        if (list.get(i).getAvgScore().equals("5.0")) {
                                            iv_one.setVisibility(View.VISIBLE);
                                            iv_two.setVisibility(View.VISIBLE);
                                            iv_three.setVisibility(View.VISIBLE);
                                            iv_four.setVisibility(View.VISIBLE);
                                            iv_five.setVisibility(View.VISIBLE);
                                        }
                                        tv_sign.setText(list.get(i).getSignature());
                                        list_view.add(view);
                                        final int finalI = i;
                                        list_view.get(i).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                Toast.makeText(getActivity(), finalI + "", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getActivity(), CoachInfoActivity.class);
                                                Bundle bundle = new Bundle();
                                                bundle.putSerializable("coach", list.get(finalI));
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                    ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), list_view);
                                    viewPager.setAdapter(adapter);
                                    viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                        @Override
                                        public void onPageScrolled(int i, float v, int i2) {

                                        }

                                        @Override
                                        public void onPageSelected(int i) {
                                            if (maker != null) {
                                                maker.remove();
                                                mMapView.invalidate();
                                            }
                                            LatLng point = null;
                                            OverlayOptions options = null;
                                            BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_teacher_checked);
                                            Toast.makeText(getActivity(), i + "滑动", Toast.LENGTH_SHORT).show();
                                            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng())), 12.0f));
                                            point = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
                                            options = new MarkerOptions().position(point).icon(bitmap2);
                                            maker = (Marker) mBaiduMap.addOverlay(options);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("coach", list.get(i));
                                            maker.setExtraInfo(bundle);
                                        }

                                        @Override
                                        public void onPageScrollStateChanged(int i) {

                                        }
                                    });
                                    setMarkerInMap();
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

    }

    //添加标注图标
    private void setMarkerInMap() {
        LatLng point = null;
        OverlayOptions options = null;
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_teacher_checked);
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_teacher);
        for (int i = 0; i < list.size(); i++) {
            point = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
            options = new MarkerOptions().position(point).icon(bitmap2);
            Marker marker = (Marker) mBaiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putSerializable("coach", list.get(i));
            marker.setExtraInfo(bundle);
            mMarkers.add(marker);
        }
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(list.get(0).getLat()), Double.parseDouble(list.get(0).getLng())), 12.0f));
        point = new LatLng(Double.parseDouble(list.get(0).getLat()), Double.parseDouble(list.get(0).getLng()));
        options = new MarkerOptions().position(point).icon(bitmap);
        maker = (Marker) mBaiduMap.addOverlay(options);
        Bundle bundle = new Bundle();
        bundle.putSerializable("coach", list.get(0));
        maker.setExtraInfo(bundle);
    }

    @Override
    public void onDestroy() {
        if (mMapView != null) {
            mMapView.onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        if (mMapView != null) {
            mMapView.setVisibility(View.INVISIBLE);
            mMapView.onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mMapView != null) {
            mMapView.setVisibility(View.VISIBLE);
            mMapView.onResume();
        }
        super.onResume();
    }

    private void refreshScaleAndZoomControl() {
        //更新缩放按钮的状态
        mZoomControlView.refreshZoomButtonStatus(Math.round(mMapView.getMap().getMapStatus().zoom));
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

}
