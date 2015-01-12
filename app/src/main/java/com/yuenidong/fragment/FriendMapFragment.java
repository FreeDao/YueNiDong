package com.yuenidong.fragment;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.qinjia.util.PreferenceUitl;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.FriendAdapter;
import com.yuenidong.adapter.ViewPagerAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.util.DsncLocation;
import com.yuenidong.widget.HorizontialListView;
import com.yuenidong.widget.ZoomControlView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FriendMapFragment extends Fragment {
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
    private List<FriendEntity> list = new ArrayList<FriendEntity>();
    @InjectView(R.id.container)
    FrameLayout mapFrameLayout;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;


    public static FriendMapFragment newInstance() {
        FriendMapFragment fragment = new FriendMapFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public FriendMapFragment() {
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
        View view = inflater.inflate(R.layout.fragment_friend_map, container, false);
        ButterKnife.inject(this, view);
//        this.btn_big.setOnClickListener(new View.OnClickListener() {
//
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
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mMakers);
        mBaiduMap.setMyLocationConfigeration(config);
        getFriendsData();
//        list_view = new ArrayList<View>();
//        for (int i = 0; i <5; i++) {
//                view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_friend_map, null);
//            list_view.add(view);
//        }
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity(), list_view);
//        viewPager.setAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        list_view = new ArrayList<View>();
        for (int i = 0; i < 5; i++) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_friend_map, null);
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
    }

    //得到球友数据
    private void getFriendsData() {
//        for (int i = 0; i < mMarkers.size(); i++) {
//            mMarkers.get(i).remove();
//        }
//        mMarkers.clear();
        setMarkerInMap();

    }

    //添加标注图标
    private void setMarkerInMap() {
        LatLng point = null;
        OverlayOptions options = null;
//        mMarkers.clear();
        //我的地址
//        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.pic_position);
//        point = new LatLng(latitude, longtitude);
//        DsncLog.e("我的latitude", latitude + "-----");
//        DsncLog.e("我的longtitude", longtitude + "-----");
//        options = new MarkerOptions().position(point).icon(bitmap);
//        mMarkers.add((Marker) mBaiduMap.addOverlay(options));
        //测试地址
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_user);
        point = new LatLng(latitude_test, longtitude_test);
        DsncLog.e("测试的latitude", latitude_test + "-----");
        DsncLog.e("测试的longtitude", longtitude_test + "-----");
        options = new MarkerOptions().position(point).icon(bitmap2);
        mMarkers.add((Marker) mBaiduMap.addOverlay(options));
        //添加阴影
//        BitmapDescriptor bitmap3 = BitmapDescriptorFactory.fromResource(R.drawable.pic_shadow);
//        point = new LatLng(latitude_test, longtitude_test);
//        options = new MarkerOptions().position(point).icon(bitmap3);
//        mMarkers.add((Marker) mBaiduMap.addOverlay(options));
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


}
