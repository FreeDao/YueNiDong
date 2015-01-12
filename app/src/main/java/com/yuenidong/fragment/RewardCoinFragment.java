package com.yuenidong.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.yuenidong.activity.PushMapActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.MatchEntity;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 石岩
 * 奖励果冻
 */
public class RewardCoinFragment extends Fragment {
    private MatchEntity entity;
    private int count_coin_me;
    //发起活动的人数
    private int count_people;
    //果冻数量
    private int count_coin;
    @InjectView(R.id.tv_coin_count_me)
    TextView tv_coin_count_me;
    @InjectView(R.id.tv_coininfo)
    TextView tv_coininfo;
    @InjectView(R.id.tv_coin_count)
    TextView tv_coin_count;
    @InjectView(R.id.iv_reduce)
    ImageView iv_reduce;

    //增加果冻
    @OnClick(R.id.iv_add)
    void add() {
        if (count_people > count_coin_me) {
            return;
        }
        count_coin = count_coin + count_people;
        tv_coin_count.setText(count_coin + "");
        count_coin_me = count_coin_me - count_people;
        tv_coin_count_me.setText(count_coin_me + "");
        iv_reduce.setImageResource(R.drawable.ic_reduce_ll);
        if (count_coin > count_people) {
            iv_reduce.setEnabled(true);
        }
    }

    //减少果冻
    @OnClick(R.id.iv_reduce)
    void delete() {
        if (count_coin == count_people) {
            iv_reduce.setImageResource(R.drawable.ic_reduce);
            iv_reduce.setEnabled(false);
        } else {
            count_coin = count_coin - count_people;
            tv_coin_count.setText(count_coin + "");
            count_coin_me = count_coin_me + count_people;
            tv_coin_count_me.setText(count_coin_me + "");
            if (count_coin == count_people) {
                iv_reduce.setImageResource(R.drawable.ic_reduce);
                iv_reduce.setEnabled(false);
            }
        }
    }

    //立即发起
    @OnClick(R.id.btn_launcher)
    void launcher() {
        entity.setCandyCount(count_coin + "");
        Intent intent = new Intent(getActivity(), PushMapActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("match", entity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static RewardCoinFragment newInstance(MatchEntity entity) {
        RewardCoinFragment fragment = new RewardCoinFragment();
        Bundle args = new Bundle();
        args.putSerializable("match", entity);
        fragment.setArguments(args);
        return fragment;
    }

    public RewardCoinFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (MatchEntity) getArguments().getSerializable("match");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward_coin, container, false);
        ButterKnife.inject(this, view);
        count_people = Integer.parseInt(entity.getPeopleCount());
        count_coin = count_people;
        tv_coin_count.setText(count_coin + "");
        //获取果冻数量
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("userId", PreferenceUtil.getPreString("userId", ""));
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.GET_INFO, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("用户信息", response.toString());
                        Commvert commvert = new Commvert(response);
                        count_coin_me = Integer.parseInt(commvert.getString("candyNum"));
                        tv_coin_count_me.setText(count_coin_me + "");
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

        //--------------------果冻加减处理
        iv_reduce.setEnabled(false);
        //果冻信息下划线
        tv_coininfo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
        return view;
    }

}
