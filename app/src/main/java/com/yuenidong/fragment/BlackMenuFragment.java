package com.yuenidong.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.yuenidong.activity.R;
import com.yuenidong.adapter.BlackMenuAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.MatchInfoServerEntity;
import com.yuenidong.common.AppData;
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
 * 石岩  黑名单
 */
public class BlackMenuFragment extends Fragment implements LoadListView.ILoadListener {
    List<FriendEntity> list;
    @InjectView(R.id.listView)
    LoadListView listView;

    private int count = 10;
    private int pageNum = 1;
    BlackMenuAdapter adapter;
    private int position;

    public static BlackMenuFragment newInstance() {
        BlackMenuFragment fragment = new BlackMenuFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public BlackMenuFragment() {
        
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
        View view = inflater.inflate(R.layout.fragment_blank_menu, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<FriendEntity>();
        adapter = new BlackMenuAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setInterface(this);

        //获取我的黑名单
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
                Request.Method.POST, YueNiDongConstants.BLACKMENU, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取黑名单信息success", response.toString());
                        Commvert commvert = new Commvert(response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() == 0) {
                                DsncLog.e("获取黑名单信息success", "无活动信息");
                                return;
                            } else {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String img = jsonObject.getString("userImg");
                                    String userName = jsonObject.getString("userName");
                                    String sex = jsonObject.getString("gender");
                                    String label = jsonObject.getString("label");
                                    String sign = jsonObject.getString("signature");
                                    String userId = jsonObject.getString("userId");
                                    FriendEntity entity = new FriendEntity();
                                    entity.setUserImg(img);
                                    entity.setUserName(userName);
                                    entity.setGender(sex);
                                    entity.setSignature(sign);
                                    entity.setUserId(userId);
                                    //---------------------设置标签--------------------------
                                    if (label.equals("")) {
                                        entity.setLabel1("");
                                        entity.setLabel2("");
                                        entity.setLabel3("");
                                    } else {
                                        if (label.contains(",")) {
                                            String[] str = label.split(",");
                                            if (str.length == 2) {
                                                entity.setLabel1(str[0]);
                                                entity.setLabel2(str[1]);
                                                entity.setLabel3("");
                                            }
                                            if (str.length == 3) {
                                                entity.setLabel1(str[0]);
                                                entity.setLabel2(str[1]);
                                                entity.setLabel3(str[2]);
                                            }
                                        } else {
                                            entity.setLabel1(label);
                                            entity.setLabel2("");
                                            entity.setLabel3("");
                                        }
                                    }
                                    //----------------------------------------------------------------------
                                    list.add(entity);
                                }
                            }
                            adapter.notifyDataSetChanged();
                            listView.loadComplete();
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    position = i;
                                    Dialog dialog = new AlertDialog.Builder(getActivity()).setTitle("你确定要将此人移除黑名单吗?")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    //获取黑名单
                                                    final HashMap<String, String> map = new HashMap<String, String>();
                                                    map.put("userId", PreferenceUtil.getPreString("userId", ""));
                                                    map.put("blackId", list.get(position).getUserId());
                                                    JSONObject jsonObject = null;
                                                    try {
                                                        String str = CommonUtils.hashMapToJson(map);
                                                        jsonObject = new JSONObject(str);
                                                        DsncLog.e("jsonObject", jsonObject.toString());
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                                                            Request.Method.POST, YueNiDongConstants.REMOVEBLACKMENU, jsonObject,
                                                            new Response.Listener<JSONObject>() {
                                                                @Override
                                                                public void onResponse(JSONObject response) {
                                                                    Log.e("删除黑名单信息success", response.toString());
                                                                    Commvert commvert = new Commvert(response);
                                                                    list.remove(position);
                                                                    adapter.notifyDataSetChanged();

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
                                            })
                                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            }).create();
                                    dialog.show();
                                }
                            });
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
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                pageNum++;
                //获取黑名单
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
                        Request.Method.POST, YueNiDongConstants.GETMYLAUNCHERMATCH, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("获取黑名单信息success", response.toString());
                                Commvert commvert = new Commvert(response);
                                try {
                                    JSONArray jsonArray = response.getJSONArray("json");
                                    if (jsonArray.length() == 0) {
                                        DsncLog.e("获取黑名单信息success", "无活动信息");
                                        Toast.makeText(AppData.getContext(), "已经是最后一条信息!", Toast.LENGTH_SHORT).show();
                                        listView.loadComplete();
                                        return;
                                    } else {
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            String img = jsonObject.getString("userImg");
                                            String userName = jsonObject.getString("userName");
                                            String sex = jsonObject.getString("gender");
                                            String label = jsonObject.getString("label");
                                            String sign = jsonObject.getString("signature");
                                            FriendEntity entity = new FriendEntity();
                                            entity.setUserImg(img);
                                            entity.setUserName(userName);
                                            entity.setGender(sex);
                                            entity.setSignature(sign);
                                            //---------------------设置标签--------------------------
                                            if (label.equals("")) {
                                                entity.setLabel1("");
                                                entity.setLabel2("");
                                                entity.setLabel3("");
                                            } else {
                                                if (label.contains(",")) {
                                                    String[] str = label.split(",");
                                                    if (str.length == 2) {
                                                        entity.setLabel1(str[0]);
                                                        entity.setLabel2(str[1]);
                                                        entity.setLabel3("");
                                                    }
                                                    if (str.length == 3) {
                                                        entity.setLabel1(str[0]);
                                                        entity.setLabel2(str[1]);
                                                        entity.setLabel3(str[2]);
                                                    }
                                                } else {
                                                    entity.setLabel1(label);
                                                    entity.setLabel2("");
                                                    entity.setLabel3("");
                                                }
                                            }
                                            //----------------------------------------------------------------------
                                            list.add(entity);
                                        }
                                    }
                                    adapter.notifyDataSetChanged();
                                    listView.loadComplete();
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
