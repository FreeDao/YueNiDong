package com.yuenidong.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.ViewPagerAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.MatchEntity;
import com.yuenidong.bean.PushMapEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.util.DsncLocation;
import com.yuenidong.widget.ZoomControlView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static android.view.Gravity.BOTTOM;

/**
 * 石岩  活动推送(地图)
 */
public class PushMapFragment extends Fragment {
    private MatchEntity entity;

    private Toast toast;
    private List<View> list_view;
    private View view;
    boolean isGetLocation = true;
    private double latitude;
    private double longtitude;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //软件园中路测试纬度，经度
    private double latitude_test = 30.437986;
    private double longtitude_test = 114.448277;
    @InjectView(R.id.container)
    FrameLayout mapFrameLayout;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;
    @InjectView(R.id.iv_map)
    ImageView iv_map;

    private LatLng point;
    private OverlayOptions options;

    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();

    private List<PushMapEntity> list = new ArrayList<PushMapEntity>();

    public static PushMapFragment newInstance(MatchEntity entity) {
        PushMapFragment fragment = new PushMapFragment();
        Bundle args = new Bundle();
        args.putSerializable("match", entity);
        fragment.setArguments(args);
        return fragment;
    }

    public PushMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (MatchEntity) getArguments().getSerializable("match");
            DsncLog.e("CandyCount", entity.getCandyCount());
            DsncLog.e("PeopleCount()", entity.getPeopleCount());
            DsncLog.e("Place", entity.getPlace());
            DsncLog.e("Distance", entity.getPushDistance());
            DsncLog.e("title", entity.getTitle());
            DsncLog.e("time", entity.getTime());
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_push_map, container, false);
        ButterKnife.inject(this, view);
//        this.btn_big.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DsncLog.e("我被点击了", "");
//                if (!TextUtils.isEmpty(latitude + "")) {
//                    DsncLog.e("执行了", "");
//                    LatLng point = new LatLng(latitude, longtitude);
//                    mMapView = new MapView(getActivity(),
//                            new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(point).zoom(13.0f).build()));
//                }
//            }
//        });
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
        //获取地图ui控制器
        UiSettings settings = mBaiduMap.getUiSettings();
        settings.setAllGesturesEnabled(false);
        MapStatusUpdate m = MapStatusUpdateFactory.zoomTo(13);
        mBaiduMap.setMaxAndMinZoomLevel(13, 13);
        mBaiduMap.animateMapStatus(m);
        latitude = Double.parseDouble(PreferenceUtil.getPreString("latitude", ""));
        longtitude = Double.parseDouble(PreferenceUtil.getPreString("longtitude", ""));
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .latitude(latitude)
                .longitude(longtitude).build();
        // 设置定位数据
        if (locData != null && !locData.equals("")) {
            mBaiduMap.setMyLocationData(locData);
        }
        // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
        BitmapDescriptor mMakers = BitmapDescriptorFactory
                .fromResource(R.drawable.pic_position);
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mMakers);
        mBaiduMap.setMyLocationConfigeration(config);

        //添加覆盖物
        getMakerInfo();

        //请求数据到服务器(提交发起活动信息)
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("title", entity.getTitle());
        map.put("location", entity.getPlace());
        map.put("candyNum", entity.getCandyCount());
        map.put("peopleNum", entity.getPeopleCount());
        map.put("pushScope", entity.getPushDistance());
        map.put("startTime", entity.getTime());

        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.LANUCHER_ACTIVITY, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("success", response.toString());
                        Commvert commvert = new Commvert(response);
                        if (commvert.getString("status").equals("2")) {
                            Toast.makeText(getActivity(), "地图解析错误", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppData.getContext().startActivity(intent);
                        }
                        if (commvert.getString("status").equals("1")) {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppData.getContext().startActivity(intent);
                        }
                        if (commvert.getString("status").equals("3")) {
                            Toast.makeText(getActivity(), "果冻不够!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppData.getContext().startActivity(intent);
                        }
                        if (commvert.getString("status").equals("-1")) {
                            Toast.makeText(getActivity(), "推送活动失败!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            AppData.getContext().startActivity(intent);
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
        return view;
    }

    private void getMakerInfo() {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("pushScope", entity.getPushDistance());
        map.put("lat", PreferenceUtil.getPreString("latitude", ""));
        map.put("lng", PreferenceUtil.getPreString("longtitude", ""));

        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.PUSH_GETINFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        DsncLog.e("推动活动获取信息success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                DsncLog.e("jsonObject", jsonObject.toString());
                                String lat = jsonObject.getString("lat");
                                String lng = jsonObject.getString("lng");
                                String userId = jsonObject.getString("userId");
                                PushMapEntity entity = new PushMapEntity();
                                entity.setLat(lat);
                                entity.setLng(lng);
                                entity.setUserId(userId);
                                list.add(entity);
                            }
                            if (list.size() > 0) {
                                for (int i = 0; i < list.size(); i++) {
                                    BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_user);
                                    point = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
                                    options = new MarkerOptions().position(point).icon(bitmap2);
                                    mMarkers.add((Marker) mBaiduMap.addOverlay(options));
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                DsncLog.e("error", error.toString());
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

    //显示Toast
    private void showToast() {
        toast = Toast.makeText(getActivity(), null, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        View view_toast = LayoutInflater.from(getActivity()).inflate(R.layout.toast_pushmap, null, false);
        toast.setView(view_toast);
        toast.show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list_view = new ArrayList<View>();
        for (int i = 0; i < 5; i++) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_venues_map, null);
            list_view.add(view);
            final int finalI = i;
            list_view.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), finalI + "", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getActivity(), i + "滑动", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        RotateAnimation upAnim = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        upAnim.setDuration(2000);
        upAnim.setRepeatCount(-1);
        LinearInterpolator lin = new LinearInterpolator();
        upAnim.setInterpolator(lin);
        upAnim.setFillAfter(true);
        iv_map.startAnimation(upAnim);
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
}
