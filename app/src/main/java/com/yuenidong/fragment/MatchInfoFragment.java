package com.yuenidong.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.yuenidong.activity.MatchInfoActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.MatchAdapter;
import com.yuenidong.adapter.MatchInfoCommentAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.MatchInfoCommentEntity;
import com.yuenidong.bean.MatchInfoServerEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.util.TimeUtils;
import com.yuenidong.widget.LoadListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * 石岩  活动详情
 */
public class MatchInfoFragment extends Fragment implements LoadListView.ILoadListener {
    private int count = 0;
    private Dialog dialog;
    private int pageNum = 1;
    private int number = 10;
    private List<MatchInfoCommentEntity> list;
    private MatchInfoServerEntity entity;
    @InjectView(R.id.listview)
    LoadListView listView;
    @InjectView(R.id.tv_toast)
    TextView tv_toast;
    @InjectView(R.id.iv_type)
    ImageView iv_type;
    @InjectView(R.id.tv_title)
    TextView tv_title;
    @InjectView(R.id.tv_coin_count)
    TextView tv_coin_count;
    @InjectView(R.id.iv_image)
    ImageView iv_image;
    @InjectView(R.id.tv_name)
    TextView tv_name;
    @InjectView(R.id.iv_sex)
    ImageView iv_sex;
    @InjectView(R.id.tv_place)
    TextView tv_place;
    @InjectView(R.id.tv_distance)
    TextView tv_distance;
    @InjectView(R.id.tv_time)
    TextView tv_time;
    @InjectView(R.id.tv_people_count)
    TextView tv_people_count;
    @InjectView(R.id.tv_heat)
    TextView tv_heat;
    @InjectView(R.id.tv_praise)
    TextView tv_praise;
    @InjectView(R.id.tv_comment)
    TextView tv_comment;


    //参加活动
    @OnClick(R.id.btn_attend)
    void attend() {
        //------------------------------参加活动
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("activityId", entity.getActivityId());
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.ATTENDMATCH, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("参加活动success", response.toString());
                        Commvert commvert = new Commvert(response);
                        if (commvert.getString("status").equals("5")) {
                            Toast.makeText(AppData.getContext(), "已参加过活动!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (commvert.getString("status").equals("6")) {
                            Toast.makeText(AppData.getContext(), "人数已满!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (commvert.getString("status").equals("-1")) {
                            Toast.makeText(AppData.getContext(), "服务器繁忙!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (commvert.getString("status").equals("1")) {
                            tv_toast.setVisibility(View.VISIBLE);
                            final Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    count++;
                                    Message message = new Message();
                                    if (count == 2) {
                                        timer.cancel();
                                        message.what = 1;
                                        handler.sendMessage(message);
                                        count = 0;
                                    }
                                }
                            }, 0, 2000);
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

    //评论活动
    @OnClick(R.id.btn_comment)
    void comment() {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog, null);
        final EditText et_commment = (EditText) view.findViewById(R.id.et_commment);
        dialog = new AlertDialog.Builder(getActivity()).setTitle("请发表你的评论?").setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(et_commment.getText().toString().trim())) {
                    Toast.makeText(AppData.getContext(), "评论内容不能为空!", Toast.LENGTH_SHORT).show();
                } else {
                    //----------------------添加评论-----------------------------
                    final HashMap<String, String> map = new HashMap<String, String>();
                    map.put("userId", PreferenceUtil.getPreString("userId", ""));
                    map.put("activityId", entity.getActivityId());
                    map.put("criticsId", "");
                    map.put("remarkContent", et_commment.getText().toString().trim());
                    JSONObject jsonObject = null;
                    try {
                        String str = CommonUtils.hashMapToJson(map);
                        jsonObject = new JSONObject(str);
                        DsncLog.e("jsonObject", jsonObject.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                            Request.Method.POST, YueNiDongConstants.ADDREMARK, jsonObject,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("添加评论success", response.toString());
                                    Commvert commvert = new Commvert(response);
                                    pageNum = 1;
                                    list.clear();
                                    GetCommentMessage(adapter);
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
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
            }
        }).create();
        dialog.show();
    }

    //分享
    @OnClick(R.id.rl_share)
    void share() {
        showShare();
    }


    private void showShare() {
        ShareSDK.initSDK(AppData.getContext());
        DsncLog.e("true", "已查询!");
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字
        oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//		 oks.setTitle(getString(R.string.share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
//		 oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//		 oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
//		 oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
//		 oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
//		 oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
//		 oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(AppData.getContext());
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    tv_toast.setVisibility(View.GONE);
                    break;
            }
        }
    };

    public static MatchInfoFragment newInstance(MatchInfoServerEntity entity) {
        MatchInfoFragment fragment = new MatchInfoFragment();
        Bundle args = new Bundle();
        args.putSerializable("match", entity);
        fragment.setArguments(args);
        return fragment;
    }

    public MatchInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (MatchInfoServerEntity) getArguments().getSerializable("match");
        }
    }

    private MatchInfoCommentAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match_info, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<MatchInfoCommentEntity>();
        adapter = new MatchInfoCommentAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setInterface(this);
        //---------------------------初始化控件----------------------------------
        if (!TextUtils.isEmpty(entity.toString())) {
            tv_title.setText(entity.getTitle());
            iv_type.setImageResource(R.drawable.label_billiards);
            tv_coin_count.setText("奖励果冻" + entity.getCandyNum());
            loadImageVolley(entity.getUserImg(), iv_image);
            tv_name.setText(entity.getUserName());
            if (entity.getGender().equals("m")) {
                iv_sex.setImageResource(R.drawable.ic_man);
            } else {
                iv_sex.setImageResource(R.drawable.ic_woman);
            }
            tv_place.setText("地点:" + entity.getLocation());
            tv_distance.setText(entity.getDistance() + "km");
            tv_time.setText("时间:" + TimeUtils.StringToPatternTime(entity.getStartTime()));
            tv_people_count.setText("人数:" + entity.getPeopleNum());
            tv_heat.setText(entity.getHeat());
            tv_praise.setText(entity.getGreat());
            tv_comment.setText(entity.getRemarkNum());
        }
        //--------------获取评论信息--------------------------
        GetCommentMessage(adapter);
        //评论别人
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                View view2 = LayoutInflater.from(getActivity()).inflate(R.layout.item_dialog, null);
                final EditText et_commment = (EditText) view2.findViewById(R.id.et_commment);
                dialog = new AlertDialog.Builder(getActivity()).setTitle("请发表你的评论?").setView(view2).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (TextUtils.isEmpty(et_commment.getText().toString().trim())) {
                            Toast.makeText(AppData.getContext(), "评论内容不能为空!", Toast.LENGTH_SHORT).show();
                        } else {
                            //----------------------添加评论-----------------------------
                            final HashMap<String, String> map = new HashMap<String, String>();
                            map.put("userId", PreferenceUtil.getPreString("userId", ""));
                            map.put("activityId", entity.getActivityId());
                            map.put("criticsId", entity.getUserId());
                            map.put("remarkContent", et_commment.getText().toString().trim());
                            JSONObject jsonObject = null;
                            try {
                                String str = CommonUtils.hashMapToJson(map);
                                jsonObject = new JSONObject(str);
                                DsncLog.e("jsonObject", jsonObject.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                                    Request.Method.POST, YueNiDongConstants.ADDREMARK, jsonObject,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("添加评论success", response.toString());
                                            Commvert commvert = new Commvert(response);
                                            pageNum = 1;
                                            list.clear();
                                            GetCommentMessage(adapter);
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
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.cancel();
                    }
                }).create();
                dialog.show();
            }
        });


        return view;
    }

    private void GetCommentMessage(final MatchInfoCommentAdapter adapter) {
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("activityId", entity.getActivityId());
        map.put("pageNum", pageNum + "");
        map.put("count", number + "");
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GETREMARK, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取评论success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() > 0) {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ArrayList<MatchInfoCommentEntity>>() {
                                }.getType();
                                ArrayList<MatchInfoCommentEntity> list2 = gson.fromJson(jsonArray.toString(), type);
                                if (list2.size() > 0) {
                                    for (int i = 0; i < list2.size(); i++) {
                                        list.add(list2.get(i));
                                    }
                                    adapter.notifyDataSetChanged();
                                }
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

    @Override
    public void onLoad() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                pageNum++;
                final HashMap<String, String> map = new HashMap<String, String>();
                map.put("activityId", entity.getActivityId());
                map.put("pageNum", pageNum + "");
                map.put("count", number + "");
                JSONObject jsonObject = null;
                try {
                    String str = CommonUtils.hashMapToJson(map);
                    jsonObject = new JSONObject(str);
                    DsncLog.e("jsonObject", jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                        Request.Method.POST, YueNiDongConstants.GETREMARK, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("获取评论success", response.toString());
                                try {
                                    JSONArray jsonArray = response.getJSONArray("json");
                                    if (jsonArray.length() > 0) {
                                        Gson gson = new Gson();
                                        Type type = new TypeToken<ArrayList<MatchInfoCommentEntity>>() {
                                        }.getType();
                                        ArrayList<MatchInfoCommentEntity> list2 = gson.fromJson(jsonArray.toString(), type);
                                        if (list2.size() > 0) {
                                            for (int i = 0; i < list2.size(); i++) {
                                                list.add(list2.get(i));
                                            }
                                            adapter.notifyDataSetChanged();
                                            listView.loadComplete();
                                        }
                                    } else {
                                        Toast.makeText(getActivity(), "已经是最后一条评论!", Toast.LENGTH_SHORT).show();
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
