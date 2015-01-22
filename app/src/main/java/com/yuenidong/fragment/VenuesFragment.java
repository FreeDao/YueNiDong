package com.yuenidong.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuenidong.activity.R;
import com.yuenidong.activity.VenuesInfoActivity;
import com.yuenidong.adapter.VenuesAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.widget.AutoListView;
import com.yuenidong.widget.LoadListView;

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

public class VenuesFragment extends Fragment implements LoadListView.ILoadListener {
    private List<VenuesEntity> list;
    private VenuesAdapter adapter;
    @InjectView(R.id.listview)
    LoadListView listView;
    private int count = 10;
    private int pageNum = 1;

    public static VenuesFragment newInstance() {
        VenuesFragment fragment = new VenuesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public VenuesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_venues, container, false);
        ButterKnife.inject(this, view);
        this.list = new ArrayList<VenuesEntity>();
        adapter = new VenuesAdapter(getActivity(), list);
        listView.setInterface(this);
        listView.setAdapter(adapter);
        //---------------------------获取场馆信息--------------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("lat", PreferenceUtil.getPreString("latitude", ""));
        map.put("lng", PreferenceUtil.getPreString("longtitude", ""));
        map.put("pageNum", pageNum + "");
        map.put("count", count + "");
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GETVENDERBYPAGE, jsonObject,
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
                                    list.add(entity);
                                }
                                adapter.notifyDataSetChanged();
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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), VenuesInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("venues", list.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                pageNum++;
                final HashMap<String, String> map = new HashMap<String, String>();
                map.put("lat", PreferenceUtil.getPreString("latitude", ""));
                map.put("lng", PreferenceUtil.getPreString("longtitude", ""));
                map.put("pageNum", pageNum + "");
                map.put("count", count + "");
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
                                        DsncLog.e("获取教练信息success", "无活动信息");
                                        Toast.makeText(getActivity(), "已经是最后一条信息!", Toast.LENGTH_SHORT).show();
                                        listView.loadComplete();
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
                                            list.add(entity);
                                        }
                                        adapter.notifyDataSetChanged();
                                        listView.loadComplete();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    pageNum--;
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", error.toString());
                        pageNum--;
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
        }, 2000);
    }
}
