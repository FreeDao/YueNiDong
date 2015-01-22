package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuenidong.activity.FriendInfoActivity;
import com.yuenidong.activity.MatchInfoActivity;
import com.yuenidong.activity.PushMapActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.FriendAdapter;
import com.yuenidong.adapter.MatchAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.MatchInfoServerEntity;
import com.yuenidong.common.AppData;
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

public class FriendFragment extends Fragment implements AdapterView.OnItemClickListener, LoadListView.ILoadListener {
    private List<FriendEntity> list;
    private FriendAdapter adapter;
    @InjectView(R.id.listview)
    LoadListView listView;

    private String lng;
    private String userRole;
    private String cid;
    private String password;
    private String registerTime;
    private String id;
    private String imId;
    private String distance;
    private String phoneNum;
    private String labelNum;
    private String userId;
    private String gender;
    private String userName;
    private String lat;
    private String userImg;
    private String signature;
    private String label1;
    private String label2;
    private String label3;
    private String careNum;
    private String fansNum;
    private String candyNum;

    private int count = 10;
    private int pageNum = 1;


    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public FriendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.inject(this, view);
        this.list = new ArrayList<FriendEntity>();
        adapter = new FriendAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setInterface(this);
        listView.setOnItemClickListener(this);

        //--------------------获取球友信息------------------------
        //获取活动信息
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("lat", PreferenceUtil.getPreString("latitude", ""));
        map.put("lng", PreferenceUtil.getPreString("longtitude", ""));
        map.put("pushScope", "5");
        map.put("pageNum", pageNum + "");
        map.put("count", count + "");
//        map.put("userRole", PreferenceUtil.getPreString("userRole", "1"));
        map.put("userRole", "1");
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GETFRIENDINFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取球友信息success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取球友信息success", "无球友信息");
                                return;
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Commvert commvert = new Commvert(jsonObject);
                                    FriendEntity entity = new FriendEntity();
                                    lng = commvert.getString("lng");
                                    entity.setLng(lng);
                                    userRole = commvert.getString("userRole");
                                    entity.setUserRole(userRole);
                                    cid = commvert.getString("cid");
                                    entity.setCid(cid);
                                    distance = commvert.getString("distance");
                                    entity.setDistance(distance);
                                    password = commvert.getString("password");
                                    entity.setPassword(password);
                                    registerTime = commvert.getString("registerTime");
                                    entity.setRegisterTime(registerTime);
                                    id = commvert.getString("id");
                                    entity.setId(id);
                                    imId = commvert.getString("imId");
                                    entity.setImId(imId);
                                    phoneNum = commvert.getString("phoneNum");
                                    entity.setPhoneNum(phoneNum);
                                    labelNum = commvert.getString("labelNum");
                                    entity.setLabelNum(labelNum);
                                    userId = commvert.getString("userId");
                                    entity.setUserId(userId);
                                    gender = commvert.getString("gender");
                                    entity.setGender(gender);
                                    userName = commvert.getString("userName");
                                    entity.setUserName(userName);
                                    lat = commvert.getString("lat");
                                    entity.setLat(lat);
                                    userImg = commvert.getString("userImg");
                                    entity.setUserImg(userImg);
                                    signature = commvert.getString("signature");
                                    entity.setSignature(signature);
                                    label1 = commvert.getString("label0");
                                    label2 = commvert.getString("label1");
                                    label3 = commvert.getString("label2");
                                    entity.setLabel1(label1);
                                    entity.setLabel2(label2);
                                    entity.setLabel3(label3);
                                    careNum = commvert.getString("careNum");
                                    fansNum = commvert.getString("fansNum");
                                    candyNum = commvert.getString("candyNum");
                                    entity.setCareNum(careNum);
                                    entity.setFansNum(fansNum);
                                    entity.setCandyNum(candyNum);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DsncLog.e("listview点击事件", i + "");
        Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", list.get(i));
        intent.putExtras(bundle);
        startActivity(intent);
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
                map.put("userId", PreferenceUtil.getPreString("userId", ""));
                map.put("pushScope", "5");
                map.put("pageNum", pageNum + "");
                map.put("count", count + "");
                map.put("userRole", "1");
                JSONObject jsonObject = null;
                try {
                    String str = CommonUtils.hashMapToJson(map);
                    jsonObject = new JSONObject(str);
                    DsncLog.e("jsonObject", jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                        Request.Method.POST, YueNiDongConstants.GETFRIENDINFO, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("获取球友信息success", response.toString());
                                try {
                                    JSONArray jsonArray = response.getJSONArray("json");
                                    if (jsonArray.length() == 0) {
                                        DsncLog.e("获取球友信息success", "无活动信息");
                                        Toast.makeText(AppData.getContext(), "已经是最后一条信息!", Toast.LENGTH_SHORT).show();
                                        listView.loadComplete();
                                        return;
                                    } else {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<FriendEntity>>() {
                                        }.getType();
                                        List<FriendEntity> list2 = gson.fromJson(jsonArray.toString(), type);
                                        for (int i = 0; i < list2.size(); i++) {
                                            list.add(list2.get(i));
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
