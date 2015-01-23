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
import com.yuenidong.adapter.CoachCommentAdapter;
import com.yuenidong.adapter.VenuesCommentAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.CoachCommentEntity;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.bean.VenuesCommentEntity;
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
 * 石岩 教练评价
 */
public class CoachCommentFragment extends Fragment {
    private List<VenuesCommentEntity> list;
    @InjectView(R.id.listView)
    LoadListView listView;
    private String coachId;

    public static CoachCommentFragment newInstance(String coachId) {
        CoachCommentFragment fragment = new CoachCommentFragment();
        Bundle args = new Bundle();
        args.putString("coachId", coachId);
        fragment.setArguments(args);
        return fragment;
    }

    public CoachCommentFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            coachId = getArguments().getString("coachId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_coach_comment, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<VenuesCommentEntity>();
        final VenuesCommentAdapter adapter = new VenuesCommentAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        //--------------------------获取教练评价---------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("coachId", coachId);
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GETCOACHCOMMENT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取教练评论success", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("json");
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    VenuesCommentEntity entity = new VenuesCommentEntity();
                                    JSONObject jsonObject0 = jsonArray.getJSONArray(i).getJSONObject(0);
                                    String remarkPoint = jsonObject0.getString("userPoint");
                                    String content = jsonObject0.getString("userRemark");
                                    String remarkTime = jsonObject0.getString("remarkTime");
                                    JSONObject jsonObject1 = jsonArray.getJSONArray(i).getJSONObject(1);
                                    String img = jsonObject1.getString("userImg");
                                    String userName = jsonObject1.getString("userName");
                                    entity.setContent(content);
                                    entity.setRemarkPoint(remarkPoint);
                                    entity.setTime(remarkTime);
                                    entity.setImg(img);
                                    entity.setUserName(userName);
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

}
