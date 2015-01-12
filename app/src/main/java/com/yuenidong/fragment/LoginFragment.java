package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.PhoneValidationActivity;
import com.yuenidong.activity.R;
import com.yuenidong.activity.ResetPasswordActivity;
import com.yuenidong.activity.SinaOauthActivity;
import com.yuenidong.activity.WxOauthActivity;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.util.TimeUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginFragment extends Fragment {
    private String cid;
    boolean isShowPassword = false;
    @InjectView(R.id.tv_forgetpassword)
    TextView tv_forgetpassword;
    @InjectView(R.id.et_phone)
    EditText et_phone;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.iv_phone)
    ImageView iv_phone;
    @InjectView(R.id.iv_password)
    ImageView iv_password;
    @InjectView(R.id.iv_showpassword)
    ImageView iv_showpassword;

    //登录
    @OnClick(R.id.btn_login)
    void login() {
        //向服务器发送请求
        final HashMap<String, String> map = new HashMap<String, String>();
        map.put("phoneNum", et_phone.getText().toString().trim());
        map.put("password", et_password.getText().toString().trim());
        map.put("cid", cid);
        JSONObject jsonObject = null;
        try {
            String str = CommonUtils.hashMapToJson(map);
            jsonObject = new JSONObject(str);
            DsncLog.e("jsonObject", jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                Request.Method.POST, YueNiDongConstants.LOGIN, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("success", response.toString());
                        Commvert commvert = new Commvert(response);
                        if (commvert.getString("status").equals("-1")) {
                            Toast.makeText(getActivity(), "用户名或密码错误!", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (!TextUtils.isEmpty(commvert.getString("userId"))) {
                            PreferenceUtil.setPreString("userId", commvert.getString("userId"));
                            PreferenceUtil.setPreBoolean("isFirstLogin", false);
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
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

    //微信注册
    @OnClick(R.id.btn_weixin)
    void login_weixin() {
        Intent intent = new Intent(getActivity(), WxOauthActivity.class);
        startActivity(intent);
    }

    //微博注册
    @OnClick(R.id.btn_weibo)
    void login_weibo() {
        Intent intent = new Intent(getActivity(), SinaOauthActivity.class);
        startActivity(intent);
    }

    //忘记密码
    @OnClick(R.id.tv_forgetpassword)
    void forgetpassword() {
        Intent intent = new Intent(getActivity(), PhoneValidationActivity.class);
        intent.putExtra("isReset", true);
        startActivity(intent);
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.inject(this, view);
        cid = PushManager.getInstance().getClientid(AppData.getContext());
        PreferenceUtil.setPreString("cid", cid);
        tv_forgetpassword.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // 此处为得到焦点时的处理内容
                if (hasFocus) {
                    et_phone.setBackgroundResource(R.drawable.text_line_selected);
                    iv_phone.setImageResource(R.drawable.ic_telephone_selected);
                } else {
                    et_phone.setBackgroundResource(R.drawable.text_line);
                    iv_phone.setImageResource(R.drawable.ic_telephone);
                }
            }
        });
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

}
