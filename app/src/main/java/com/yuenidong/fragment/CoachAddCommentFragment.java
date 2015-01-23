package com.yuenidong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 教练(添加评价)
 */
public class CoachAddCommentFragment extends Fragment {
    @InjectView(R.id.ratingbar)
    RatingBar ratingBar;
    @InjectView(R.id.tv_point)
    TextView tv_point;
    @InjectView(R.id.et_comment)
    EditText et_comment;

    private String coachId;

    private int point = 5;

    public static CoachAddCommentFragment newInstance(String coachId) {
        CoachAddCommentFragment fragment = new CoachAddCommentFragment();
        Bundle args = new Bundle();
        args.putString("coachId", coachId);
        fragment.setArguments(args);
        return fragment;
    }

    public CoachAddCommentFragment() {
        // Required empty public constructor
    }

    @OnClick(R.id.btn_addsure)
    void sure(){
        //----------------------------------添加教练评价------------------------------
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("coachId", coachId);
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        map.put("userPoint", point + "");
        map.put("userRemark", et_comment.getText().toString().trim());
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.ADDCOACHCOMMENT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("提交教练评价success", response.toString());

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
        View view = inflater.inflate(R.layout.fragment_coach_add_comment, container, false);
        ButterKnife.inject(this, view);
        this.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tv_point.setText(v + "分");
                point = (int) v;
            }
        });
        return view;
    }

}
