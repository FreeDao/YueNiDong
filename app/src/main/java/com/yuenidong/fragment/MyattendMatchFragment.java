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
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yuenidong.activity.MatchInfoActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.MatchAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.MatchEntity;
import com.yuenidong.bean.MatchInfoServerEntity;
import com.yuenidong.common.AppData;
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
 * 石岩 我参与的活动
 */
public class MyattendMatchFragment extends Fragment implements LoadListView.ILoadListener {
    private int count = 10;
    private int pageNum = 1;
    MatchAdapter adapter;

    private List<MatchInfoServerEntity> list;
    @InjectView(R.id.listView)
    LoadListView listView;

    private String userId;

    public static MyattendMatchFragment newInstance(String userId) {
        MyattendMatchFragment fragment = new MyattendMatchFragment();
        Bundle args = new Bundle();
        args.putString("userId", userId);
        fragment.setArguments(args);
        return fragment;
    }

    public MyattendMatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myattend_match, container, false);
        ButterKnife.inject(this, view);
        listView.setInterface(this);
        list = new ArrayList<MatchInfoServerEntity>();
        adapter = new MatchAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        //获取我发起的活动
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", userId);
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
                Request.Method.POST, YueNiDongConstants.GETMYATTENDMATCH, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("success", response.toString());
                        Commvert commvert = new Commvert(response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取活动信息success", "无活动信息");
                                return;
                            } else {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<MatchInfoServerEntity>>() {
                                }.getType();
                                List<MatchInfoServerEntity> list2 = gson.fromJson(jsonArray.toString(), type);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    list.add(list2.get(i));
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
//        list = new ArrayList<MatchInfoServerEntity>();
//        MatchAdapter adapter = new MatchAdapter(getActivity(), list);
//        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DsncLog.e("12121212", i + "");
                Intent intent = new Intent(getActivity(), MatchInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("match", list.get(i - 1));
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
                //获取我发起的活动
                final HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", userId);
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
                        Request.Method.POST, YueNiDongConstants.GETMYATTENDMATCH, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Commvert commvert = new Commvert(response);
                                try {
                                    JSONArray jsonArray = response.getJSONArray("json");
                                    if (jsonArray.length() == 0) {
                                        Toast.makeText(AppData.getContext(), "已经是最后一条活动信息!", Toast.LENGTH_SHORT).show();
                                        listView.loadComplete();
                                        return;
                                    } else {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<MatchInfoServerEntity>>() {
                                        }.getType();
                                        List<MatchInfoServerEntity> list2 = gson.fromJson(jsonArray.toString(), type);
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            list.add(list2.get(i));
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
        }, 2000);
    }
}
