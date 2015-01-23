package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.VenuesCommentAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.bean.VenuesCommentEntity;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
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

/**
 * 石岩 场馆评价
 */
public class VenuesCommentFragment extends Fragment implements LoadListView.ILoadListener {
    private List<VenuesCommentEntity> list;
    VenuesCommentAdapter adapter;
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
    TextView tv_commnet;
    private int count = 10;
    private int pageNum = 1;

    @InjectView(R.id.listView)
    LoadListView listView;
    private VenuesEntity entity;

    public static VenuesCommentFragment newInstance(VenuesEntity entity) {
        VenuesCommentFragment fragment = new VenuesCommentFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", entity);
        fragment.setArguments(args);
        return fragment;
    }

    public VenuesCommentFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DsncLog.e("onActivityCreated", "onActivityCreated");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DsncLog.e("onCreate", "onCreate");
        if (getArguments() != null) {
            entity = (VenuesEntity) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DsncLog.e("onCreateView", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_venues_comment, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<VenuesCommentEntity>();
        adapter = new VenuesCommentAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        tv_venues_name.setText(entity.getName());
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
        tv_point.setText(entity.getPoint() + "总评分");
        tv_commnet.setText(entity.getRemarkCount() + "条评价");
        //-----------------------获取场馆最近来过的人-----------------------------------
        final HashMap<String, String> map2 = new HashMap<String, String>();
        map2.put("venderId", entity.getVenderId());
        map2.put("pageNum", "1");
        map2.put("count", "5");
        map2.put("userId", PreferenceUtil.getPreString("userId", ""));
        JSONObject jsonObject2 = null;
        try {
            String str = CommonUtils.hashMapToJson(map2);
            jsonObject2 = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject2.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest2 = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GETVENUESRECENTCOME, jsonObject2,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取场馆最近来过的人success", response.toString());
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
        RequestManager.addRequest(jsonRequest2, this);

        //-----------------------获取场馆评论----------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("venderId", entity.getVenderId());
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
                Request.Method.POST, YueNiDongConstants.GETVENUESCOMMENT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取场馆评论success", response.toString());
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() > 0) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<VenuesCommentEntity>>() {
                                }.getType();
                                ArrayList<VenuesCommentEntity> list2=new ArrayList<VenuesCommentEntity>();
                                list2 = gson.fromJson(jsonArray.toString(), type);
                                for(int i=0;i<list2.size();i++){
                                    list.add(list2.get(i));
                                }
                                DsncLog.e("123",list.get(0).getContent());
                                DsncLog.e("123",list.get(0).getUserName());
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
                pageNum = 1;
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
        map.put("venderId", entity.getVenderId());
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
                Request.Method.POST, YueNiDongConstants.GETVENUESCOMMENT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取场馆评论更多success", response.toString());
                        Commvert commvert = new Commvert(response);
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() > 0) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<VenuesCommentEntity>>() {
                                }.getType();
                                list = gson.fromJson(jsonArray.toString(), type);
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


    @Override
    public void onStart() {
        DsncLog.e("onStart", "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        DsncLog.e("onResume", "onResume");
        super.onResume();
    }
}
