package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.yuenidong.activity.LanucherMatchActivity;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.MatchInfoActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.MatchAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.MatchEntity;
import com.yuenidong.bean.MatchInfoServerEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.widget.AutoListView;

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
import butterknife.OnClick;

/**
 * 石岩
 * 活动列表
 */
public class MatchFragment extends Fragment implements AutoListView.IReflashListener {
    private List<MatchInfoServerEntity> list;
    private MatchAdapter adapter;
    @InjectView(R.id.listview)
    AutoListView listView;
    @InjectView(R.id.actionbar_text)
    TextView tv_title;
    @InjectView(R.id.actionbar_rightbutton)
    Button btn_lanucher;

    private int pageNum = 1;
    private int count = 10;

    //发起活动
    @OnClick(R.id.actionbar_rightbutton)
    void lanucher() {
        Intent intent = new Intent(getActivity(), LanucherMatchActivity.class);
        startActivity(intent);
    }

    public static MatchFragment newInstance() {
        MatchFragment fragment = new MatchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);
        ButterKnife.inject(this, view);
        tv_title.setText(AppData.getString(R.string.main_match));
        tv_title.setVisibility(View.VISIBLE);
//        btn_lanucher.setText(AppData.getString(R.string.launcher));
        btn_lanucher.setVisibility(View.VISIBLE);

        //获取活动信息
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
                Request.Method.POST, YueNiDongConstants.GETMATCHINFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取活动信息success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取活动信息success", "无活动信息");
                                return;
                            } else {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<MatchInfoServerEntity>>() {
                                }.getType();
                                list = gson.fromJson(jsonArray.toString(), type);
                                adapter = new MatchAdapter(getActivity(), list);
                                listView.setAdapter(adapter);
                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        DsncLog.e("12121212",i+"");
                                        Intent intent = new Intent(getActivity(), MatchInfoActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("match", list.get(i-1));
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
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
        listView.setInterface(this);
        return view;
    }

    @Override
    public void onReflash() {
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                listView.reflashComplete();
//            }
//        }, 2000);
        final int currentNumber = pageNum;
        pageNum = 1;
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
                Request.Method.POST, YueNiDongConstants.GETMATCHINFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取活动信息success", response.toString());
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
                                list.clear();
                                for (int i = 0; i < list2.size(); i++) {
                                    list.add(list2.get(i));
                                }
                                adapter.OnDateChange(list);
                                listView.reflashComplete();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            pageNum = currentNumber;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
                pageNum = currentNumber;
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
                        Request.Method.POST, YueNiDongConstants.GETMATCHINFO, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("获取活动信息success", response.toString());
                                try {
                                    JSONArray jsonArray = response.getJSONArray("json");
                                    if (jsonArray.length() == 0) {
                                        DsncLog.e("获取活动信息success", "无活动信息");
                                        Toast.makeText(getActivity(), "已经是最后一条信息!", Toast.LENGTH_SHORT).show();
                                        pageNum--;
                                        listView.loadComplete();
                                        return;
                                    } else {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<MatchInfoServerEntity>>() {
                                        }.getType();
                                        List<MatchInfoServerEntity> list2 = gson.fromJson(jsonArray.toString(), type);
                                        for (int i = 0; i < list2.size(); i++) {
                                            list.add(list2.get(i));
                                        }

                                        adapter.OnDateChange(list);
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
//        pageNum++;
//        final HashMap<String, String> map = new HashMap<String, String>();
//        map.put("lat", PreferenceUtil.getPreString("latitude", ""));
//        map.put("lng", PreferenceUtil.getPreString("longtitude", ""));
//        map.put("pageNum", pageNum + "");
//        map.put("count", count + "");
//        JSONObject jsonObject = null;
//        try {
//            String str = CommonUtils.hashMapToJson(map);
//            jsonObject = new JSONObject(str);
//            DsncLog.e("jsonObject", jsonObject.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
//                Request.Method.POST, YueNiDongConstants.GETMATCHINFO, jsonObject,
//                new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("获取活动信息success", response.toString());
//                        try {
//                            JSONArray jsonArray = response.getJSONArray("json");
//                            if (jsonArray.length() == 0) {
//                                DsncLog.e("获取活动信息success", "无活动信息");
//                                Toast.makeText(getActivity(), "已经是最后一条信息!", Toast.LENGTH_SHORT).show();
//                                pageNum--;
//                                listView.loadComplete();
//                                return;
//                            } else {
//                                Gson gson = new Gson();
//                                Type type = new TypeToken<ArrayList<MatchInfoServerEntity>>() {
//                                }.getType();
//                                List<MatchInfoServerEntity> list2 = gson.fromJson(jsonArray.toString(), type);
//                                for (int i = 0; i < list2.size(); i++) {
//                                    list.add(list2.get(i));
//                                }
//
//                                adapter.OnDateChange(list);
//                                listView.loadComplete();
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            pageNum--;
//                        }
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("error", error.toString());
//                pageNum--;
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() {
//                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Accept", "application/json");
//                headers.put("Content-Type", "application/json; charset=UTF-8");
//                return headers;
//            }
//        };
//        RequestManager.addRequest(jsonRequest, this);
    }
}
