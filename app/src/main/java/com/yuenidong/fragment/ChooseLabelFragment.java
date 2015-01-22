package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.GridOneAdapter;
import com.yuenidong.adapter.GridTwoAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.MyUserEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩  选择标签
 */
public class ChooseLabelFragment extends Fragment {
    private MyUserEntity user;
    private List<Integer> list_one;
    private List<Integer> list_two;
    @InjectView(R.id.gridview_one)
    GridView gridView_one;
    @InjectView(R.id.gridview_two)
    GridView gridView_two;


    //图片数组
    private Integer[] imgs = {
            R.drawable.label_billiards_filled_delete, R.drawable.label_basketball_filled_delete, R.drawable.label_running_filled_delete,
            R.drawable.label_tennis_filled_delete, R.drawable.label_badminton_filled_delete, R.drawable.label_football_filled_delete,
            R.drawable.label_riding_filled_delete, R.drawable.label_tabletennis_filled_delete, R.drawable.label_swimming_filled_delete,
            R.drawable.label_bodybuilding_filled_delete, R.drawable.label_slidingplate_filled_delete, R.drawable.label_skidding_filled_delete

    };

    //图片数组
    private Integer[] imgs2 = {
            R.drawable.label_billiards_grey, R.drawable.label_basketball_grey, R.drawable.label_running_grey,
            R.drawable.label_tennis_grey, R.drawable.label_badminton_grey, R.drawable.label_football_grey,
            R.drawable.label_riding_grey, R.drawable.label_tabletennis_grey, R.drawable.label_swimming_grey,
            R.drawable.label_bodybuilding_grey, R.drawable.label_slidingplate_grey, R.drawable.label_skidding_grey
    };

    public static ChooseLabelFragment newInstance(MyUserEntity user) {
        ChooseLabelFragment fragment = new ChooseLabelFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    public ChooseLabelFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (MyUserEntity) getArguments().getSerializable("user");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button btn_finish = (Button) getActivity().findViewById(R.id.actionbar_rightbutton);
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //提交标签内容到服务器
                StringBuffer sb = new StringBuffer();
                if (!TextUtils.isEmpty(PreferenceUtil.getPreString("label_one", ""))) {
                    sb.append(PreferenceUtil.getPreString("label_one", ""));
                }
                if (!TextUtils.isEmpty(PreferenceUtil.getPreString("label_two", ""))) {
                    sb.append("," + PreferenceUtil.getPreString("label_two", ""));
                }
                if (!TextUtils.isEmpty(PreferenceUtil.getPreString("label_three", ""))) {
                    sb.append("," + PreferenceUtil.getPreString("label_three", ""));
                }
                DsncLog.e("sb", sb.toString());
                //向服务器发送请求
                final HashMap<String, String> map = new HashMap<String, String>();
                map.put("userId", PreferenceUtil.getPreString("userId", ""));
                map.put("label1", PreferenceUtil.getPreString("label_one", ""));
                map.put("label2", PreferenceUtil.getPreString("label_two", ""));
                map.put("label3", PreferenceUtil.getPreString("label_three", ""));
                JSONObject jsonObject = null;
                try {
                    String str = CommonUtils.hashMapToJson(map);

                    jsonObject = new JSONObject(str);
                    DsncLog.e("jsonObject", jsonObject.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                        Request.Method.POST, YueNiDongConstants.CHOOSE_LABEL, jsonObject,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.e("success:标签", response.toString());
                                getActivity().finish();

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
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_label, container, false);
        ButterKnife.inject(this, view);
        PreferenceUtil.setPreString("label_one", "");
        PreferenceUtil.setPreString("label_two", "");
        PreferenceUtil.setPreString("label_three", "");
        PreferenceUtil.setPreString("label_one", user.getLabel1());
        PreferenceUtil.setPreString("label_two", user.getLabel2());
        PreferenceUtil.setPreString("label_three", user.getLabel3());
        DsncLog.e("llllll", user.getLabel1());
        DsncLog.e("llllll", PreferenceUtil.getPreString("label_one", ""));
        DsncLog.e("222222", user.getLabel2());
        DsncLog.e("222222", PreferenceUtil.getPreString("label_two", ""));
        DsncLog.e("333333", user.getLabel3());
        DsncLog.e("333333", PreferenceUtil.getPreString("label_three", ""));

        YueNiDongConstants.count = 0;
        //获取手机屏幕宽度和高度
        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);

        //窗口的宽度
        int screenWidth = dm.widthPixels;

        //窗口高度
        int screenHeight = dm.heightPixels;

        int screenWidthDp = CommonUtils.px2dip(getActivity(), screenWidth);
        int screenHeightDp = CommonUtils.px2dip(getActivity(), screenHeight);


        list_one = new ArrayList<Integer>();
        list_two = new ArrayList<Integer>();
        list_one.add(imgs[0]);
        list_one.add(imgs[1]);
        list_one.add(imgs[2]);
        for (int i = 0; i < imgs2.length; i++) {
            list_two.add(imgs2[i]);
        }
        GridOneAdapter adapter = new GridOneAdapter(getActivity(), list_one);
        gridView_one.setAdapter(adapter);
        GridTwoAdapter adaptertwo = new GridTwoAdapter(getActivity(), list_two, screenWidth, screenHeight,user);
        gridView_two.setAdapter(adaptertwo);
        return view;
    }

}
