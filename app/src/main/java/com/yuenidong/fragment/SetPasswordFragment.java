package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.UserEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.DsncLocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SetPasswordFragment extends Fragment {
    private String ClientID;
    private UserEntity user;
    private String password;
    double longtitude;
    double latitude;
    boolean isShowPassword = false;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.iv_password)
    ImageView iv_password;
    @InjectView(R.id.iv_showpassword)
    ImageView iv_showpassword;

    @OnClick(R.id.btn_finish)
    void finish() {
        PreferenceUtil.setPreBoolean("isFirstLogin", false);
        ClientID = PushManager.getInstance().getClientid(AppData.getContext());
        password = et_password.getText().toString().trim();
        user.setPassword(password);
        user.setUserRole("1");
        user.setCid(ClientID);
        DsncLog.e("longtitude1", longtitude + "");
        DsncLog.e("latitude", latitude + "");
        user.setLng(longtitude + "");
        user.setLat(latitude + "");
        //提交数据到服务器
        // 使用StringRequest发送一个post请求
        final HashMap<String, String> params = new HashMap<String, String>();
//        params.put("json", new Gson().toJson(user));
//        DsncLog.e("json", user.toString());
        params.put("userName", user.getUserName());
        params.put("gender", user.getGender());
        params.put("phoneNum", user.getPhoneNum());
        params.put("password", user.getPassword());
        params.put("userImg", user.getUserImg());
        params.put("lat", user.getLat());
        params.put("lng", user.getLng());
        params.put("cid", user.getCid());
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(params);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.REGISTER_URL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("success", response.toString());
                        JSONObject jsonObject = response;
                        DsncLog.e("jsonObject", jsonObject.toString());

                        try {
                            if ("9".equals(jsonObject.getString("status"))) {
                                Toast.makeText(getActivity(), "用户名已存在!", Toast.LENGTH_SHORT).show();
                            }
                            if ("0".equals(jsonObject.getString("status"))) {
                                Toast.makeText(getActivity(), "注册失败!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            DsncLog.e("123", "123");
                            e.printStackTrace();
                            String userId = null;
                            try {
                                userId = jsonObject.getString("userId");
                                String imId = jsonObject.getString("imId");
                                PreferenceUtil.setPreString("userId", userId);
                                PreferenceUtil.setPreString("imId", imId);
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error", error.toString());
            }
        }) {
            // 注意此处override的getParams()方法,在此处设置post需要提交的参数根本不起作用
            // 必须象上面那样,构成JSONObject当做实参传入JsonObjectRequest对象里
            // 所以这个方法在此处是不需要的
            // @Override
            // protected Map<String, String> getParams() {
            // Map<String, String> map = new HashMap<String, String>();
            // map.put("name1", "value1");
            // map.put("name2", "value2");
            // return params;
            // }

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

    public static SetPasswordFragment newInstance(UserEntity user) {
        SetPasswordFragment fragment = new SetPasswordFragment();
        Bundle args = new Bundle();
        args.putSerializable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = (UserEntity) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);
        ButterKnife.inject(this, view);
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // 此处为得到焦点时的处理内容
                if (hasFocus) {
                    et_password.setBackgroundResource(R.drawable.text_line_selected);
                    iv_password.setImageResource(R.drawable.ic_password_selected);
                } else {
                    et_password.setBackgroundResource(R.drawable.text_line);
                    iv_password.setImageResource(R.drawable.ic_password);
                }
            }
        });
        iv_showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowPassword) {
                    iv_showpassword.setImageResource(R.drawable.ic_showpassword_select);
                    isShowPassword = true;
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_password.setSelection(et_password.getText().toString().trim().length());
                } else {
                    iv_showpassword.setImageResource(R.drawable.ic_showpassword);
                    isShowPassword = false;
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_password.setSelection(et_password.getText().toString().trim().length());
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DsncLocation.with(getActivity()).getLocation(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                String address = bdLocation.getAddrStr();
                DsncLog.e("当前地址为:", address);
                int code = bdLocation.getLocType();
                DsncLog.e("百度地图定位结果code:", code + "");
                switch (code) {
                    case 161:
                        DsncLog.e("百度地图定位结果:", "成功");
                        break;
                    case 63:
                        DsncLog.e("百度地图定位结果:", "网络异常");
                        break;
                    default:
                        DsncLog.e("百度地图定位结果", "失败");
                        break;
                }
                //获取经度
                longtitude = bdLocation.getLongitude();
                //获取纬度
                latitude = bdLocation.getLatitude();
//                user.setLng(longtitude + "");
//                user.setLat(latitude + "");
                PreferenceUtil.setPreString("longtitude", longtitude + "");
                PreferenceUtil.setPreString("latitude", latitude + "");
                DsncLog.e("longtitude经度:", longtitude + "");
                DsncLog.e("latitude纬度:", latitude + "");
            }
        });
    }
}
