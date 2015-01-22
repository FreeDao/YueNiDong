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
import com.yuenidong.activity.R;
import com.yuenidong.activity.VenuesInfoActivity;
import com.yuenidong.adapter.ViewPagerAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
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

public class VenuesMapFragment extends Fragment {
    private List<View> list_view;
    private View view = null;
    private ZoomControlView mZoomControlView;
    private double latitude;
    private double longtitude;
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    //软件园中路测试纬度，经度
    private double latitude_test = 30.437986;
    private double longtitude_test = 114.448277;
    private ArrayList<Marker> mMarkers = new ArrayList<Marker>();
    private List<VenuesEntity> list = new ArrayList<VenuesEntity>();
    @InjectView(R.id.container)
    FrameLayout mapFrameLayout;
    @InjectView(R.id.viewpager)
    ViewPager viewPager;

    private Marker maker;

    boolean isShow = false;

    public static VenuesMapFragment newInstance() {
        VenuesMapFragment fragment = new VenuesMapFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public VenuesMapFragment() {
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
        View view = inflater.inflate(R.layout.fragment_venues_map, container, false);
        ButterKnife.inject(this, view);

        DsncLog.e("onCreateView", "onCreateView");
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
                VenuesEntity entity = (VenuesEntity) extraInfo.getSerializable("venues");
                Intent intent = new Intent(getActivity(), VenuesInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("venues", entity);
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
        DsncLog.e("onActivityCreated", "onActivityCreated");
    }

    //得到球友数据
    private void getFriendsData() {
        //-----------------------获取场馆信息-------------------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
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
                Request.Method.POST, YueNiDongConstants.GETVENDER, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取场馆信息success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取场馆信息success", "无场馆信息");
                                return;
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Commvert commvert = new Commvert(jsonObject);
                                    String img = commvert.getString("pic");
                                    String name = commvert.getString("name");
                                    String distance = commvert.getString("distance");
                                    String point = commvert.getString("point");
                                    String place = commvert.getString("location");
                                    String startPrice = commvert.getString("price");
                                    String endPrice = commvert.getString("couponPrice");
                                    String venderId = commvert.getString("venderId");

                                    String pic0 = commvert.getString("pic0");
                                    String pic1 = commvert.getString("pic1");
                                    String pic2 = commvert.getString("pic2");
                                    String pic3 = commvert.getString("pic3");
                                    String pic4 = commvert.getString("pic4");

                                    String remarkCount = commvert.getString("remarkCount");
                                    String venderPhone = commvert.getString("venderPhone");
                                    String businessHoursStart = commvert.getString("businessHoursStart");
                                    String businessHoursEnd = commvert.getString("businessHoursEnd");
                                    String signInCount = commvert.getString("signInCount");
                                    String service = commvert.getString("service");
                                    String remark = commvert.getString("remark");
                                    String lng = commvert.getString("lng");
                                    String lat = commvert.getString("lat");


                                    VenuesEntity entity = new VenuesEntity();
                                    entity.setImg(img);
                                    entity.setName(name);
                                    entity.setDistance(distance);
                                    entity.setPoint(point);
                                    entity.setPlace(place);
                                    entity.setStartPrice(startPrice);
                                    entity.setEndPrice(endPrice);
                                    entity.setVenderId(venderId);
                                    entity.setPic0(pic0);
                                    entity.setPic1(pic1);
                                    entity.setPic2(pic2);
                                    entity.setPic3(pic3);
                                    entity.setPic4(pic4);
                                    entity.setRemark(remark);
                                    entity.setRemarkCount(remarkCount);
                                    entity.setVenderPhone(venderPhone);
                                    entity.setBusinessHoursStart(businessHoursStart);
                                    entity.setBusinessHoursEnd(businessHoursEnd);
                                    entity.setSignInCount(signInCount);
                                    entity.setService(service);
                                    entity.setLat(lat);
                                    entity.setLng(lng);
                                    list.add(entity);
                                }
                                list_view = new ArrayList<View>();
                                for (int i = 0; i < list.size(); i++) {
                                    view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_venues_map, null);
                                    ImageView iv_image = (ImageView) view.findViewById(R.id.iv_image);
                                    TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
                                    TextView tv_distance = (TextView) view.findViewById(R.id.tv_distance);
                                    ImageView iv_one = (ImageView) view.findViewById(R.id.iv_one);
                                    ImageView iv_two = (ImageView) view.findViewById(R.id.iv_two);
                                    ImageView iv_three = (ImageView) view.findViewById(R.id.iv_three);
                                    ImageView iv_four = (ImageView) view.findViewById(R.id.iv_four);
                                    ImageView iv_five = (ImageView) view.findViewById(R.id.iv_five);
                                    TextView tv_location = (TextView) view.findViewById(R.id.tv_location);
                                    TextView tv_price = (TextView) view.findViewById(R.id.tv_price);
                                    if (list.get(i).equals("1.0")) {
                                        iv_one.setVisibility(View.VISIBLE);
                                    }
                                    if (list.get(i).equals("2.0")) {
                                        iv_one.setVisibility(View.VISIBLE);
                                        iv_two.setVisibility(View.VISIBLE);
                                    }
                                    if (list.get(i).equals("3.0")) {
                                        iv_one.setVisibility(View.VISIBLE);
                                        iv_two.setVisibility(View.VISIBLE);
                                        iv_three.setVisibility(View.VISIBLE);
                                    }
                                    if (list.get(i).equals("4.0")) {
                                        iv_one.setVisibility(View.VISIBLE);
                                        iv_two.setVisibility(View.VISIBLE);
                                        iv_three.setVisibility(View.VISIBLE);
                                        iv_four.setVisibility(View.VISIBLE);
                                    }
                                    if (list.get(i).equals("5.0")) {
                                        iv_one.setVisibility(View.VISIBLE);
                                        iv_two.setVisibility(View.VISIBLE);
                                        iv_three.setVisibility(View.VISIBLE);
                                        iv_four.setVisibility(View.VISIBLE);
                                        iv_five.setVisibility(View.VISIBLE);
                                    }

                                    loadImageVolley(list.get(i).getImg(), iv_image);
                                    tv_name.setText(list.get(i).getName());
                                    tv_distance.setText(list.get(i).getDistance());
                                    tv_location.setText(list.get(i).getPlace());
                                    tv_price.setText(list.get(i).getEndPrice());


                                    list_view.add(view);
                                    final int finalI = i;
                                    list_view.get(i).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Toast.makeText(getActivity(), finalI + "", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getActivity(), VenuesInfoActivity.class);
                                            Bundle bundle = new Bundle();
                                            bundle.putSerializable("venues", list.get(finalI));
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
//                                            mMarkers.get(mMarkers.size() - 1).remove();
//                                            mMarkers.remove(mMarkers.size() - 1);
                                            maker.remove();
                                            mMapView.invalidate();
                                        }
                                        LatLng point = null;
                                        OverlayOptions options = null;
                                        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_venues);
                                        Toast.makeText(getActivity(), i + "滑动", Toast.LENGTH_SHORT).show();
                                        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng())), 12.0f));
                                        point = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
                                        options = new MarkerOptions().position(point).icon(bitmap2);
                                        maker = (Marker) mBaiduMap.addOverlay(options);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("venues", list.get(i));
                                        maker.setExtraInfo(bundle);
                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int i) {

                                    }
                                });

                            }
                            setMarkerInMap();
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
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ic_venues);
        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.ic_venues_nochecked);
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                point = new LatLng(Double.parseDouble(list.get(i).getLat()), Double.parseDouble(list.get(i).getLng()));
                options = new MarkerOptions().position(point).icon(bitmap2);
                Marker maker = (Marker) mBaiduMap.addOverlay(options);
                Bundle bundle = new Bundle();
                bundle.putSerializable("venues", list.get(i));
                maker.setExtraInfo(bundle);
                mMarkers.add(maker);
            }
            mBaiduMap.setMapStatus(MapStatusUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(list.get(0).getLat()), Double.parseDouble(list.get(0).getLng())), 12.0f));
            point = new LatLng(Double.parseDouble(list.get(0).getLat()), Double.parseDouble(list.get(0).getLng()));
            options = new MarkerOptions().position(point).icon(bitmap);
            maker = (Marker) mBaiduMap.addOverlay(options);
            Bundle bundle = new Bundle();
            bundle.putSerializable("venues", list.get(0));
            maker.setExtraInfo(bundle);

        }
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

}

class FriendMapViewHolder {
    ImageView iv_image;
    TextView tv_name;
    TextView tv_distance;
    ImageView iv_one;
    ImageView iv_two;
    ImageView iv_three;
    ImageView iv_four;
    ImageView iv_five;
    TextView tv_location;
    TextView tv_price;
}
