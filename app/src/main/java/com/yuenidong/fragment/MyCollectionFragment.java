package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.VenuesAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩  我的收藏
 */
public class MyCollectionFragment extends Fragment implements LoadListView.ILoadListener {
    private List<VenuesEntity> list;
    @InjectView(R.id.listView)
    LoadListView listView;

    private int count = 10;
    private int pageNum = 1;
    VenuesAdapter adapter;


    public static MyCollectionFragment newInstance() {
        MyCollectionFragment fragment = new MyCollectionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MyCollectionFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_my_collection, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<VenuesEntity>();
        adapter = new VenuesAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        //-------------------获取我收藏的场馆---------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
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
                Request.Method.POST, YueNiDongConstants.GETVENUESCOLLECTION, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取收藏场馆success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取收藏场馆success", "无场馆信息");
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


        return view;
    }

    @Override
    public void onLoad() {
        pageNum++;
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
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
                Request.Method.POST, YueNiDongConstants.GETVENUESCOLLECTION, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取收藏场馆success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取收藏场馆success", "无场馆信息");
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
}
