package com.yuenidong.fragment;

import android.content.Intent;
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
import com.yuenidong.activity.CoachInfoActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.CoachAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.MatchInfoServerEntity;
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

public class CoachFragment extends Fragment implements AdapterView.OnItemClickListener, LoadListView.ILoadListener {
    private List<CoachEntity> list;
    private CoachAdapter adapter;
    @InjectView(R.id.listview)
    LoadListView listView;

    private int count = 10;
    private int pageNum = 1;

    public static CoachFragment newInstance() {
        CoachFragment fragment = new CoachFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public CoachFragment() {
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
        View view = inflater.inflate(R.layout.fragment_coach, container, false);
        ButterKnife.inject(this, view);
        this.list = new ArrayList<CoachEntity>();
        adapter = new CoachAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        //---------------------获取教练信息-------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("lat", PreferenceUtil.getPreString("latitude", ""));
        map.put("lng", PreferenceUtil.getPreString("longtitude", ""));
        map.put("pushScope", "5");
        map.put("pageNum", pageNum + "");
        map.put("count", count + "");
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
                Request.Method.POST, YueNiDongConstants.GETFRIENDINFO, jsonObject,
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
                                ArrayList<CoachEntity> list2 = gson.fromJson(jsonArray.toString(), type);
                                for (int i = 0; i < list2.size(); i++) {
                                    list.add(list2.get(i));
                                }
                                adapter.notifyDataSetChanged();



                                DsncLog.e("list2", list.size() + "");
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
        //----------------------------------------------------------------

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(getActivity(), CoachInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("coach", list.get(i));
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
                        Request.Method.POST, YueNiDongConstants.GETFRIENDINFO, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("获取教练信息success", response.toString());
                                try {
                                    JSONArray jsonArray = response.getJSONArray("json");
                                    if (jsonArray.length() == 0) {
                                        DsncLog.e("获取教练信息success", "无活动信息");
                                        Toast.makeText(getActivity(), "已经是最后一条信息!", Toast.LENGTH_SHORT).show();
                                        listView.loadComplete();
                                        return;
                                    } else {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<CoachEntity>>() {
                                        }.getType();
                                        List<CoachEntity> list2 = gson.fromJson(jsonArray.toString(), type);
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
