package com.yuenidong.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yuenidong.activity.R;
import com.yuenidong.activity.ResetPasswordActivity;
import com.yuenidong.activity.SetPasswordActivity;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.bean.UserEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.data.SinaUserData;
import com.yuenidong.util.RandomDataUtil;
import com.yuenidong.util.UrlUtil;
import com.yuenidong.util.VibratorUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PhoneValidationFragment extends Fragment {
    private boolean isReset;
    private String phoneNumber;
    private SinaUserData mSinaUserData;

    private final static String APP_ID = "wxa6e5ec6e5da42692";
    private final static String APP_SECRET = "938c90598eab5c46983d5cb5bbb39855";
    private String access_token;
    private String openid;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    /**
     * 服务http地址
     */
    private static String BASE_URI = "http://yunpian.com";
    /**
     * 服务版本号
     */
    private static String VERSION = "v1";

    /**
     * 模板发送接口的http地址
     */
    private static String URI_TPL_SEND_SMS = BASE_URI + "/" + VERSION + "/sms/tpl_send.json";

    private final static int REQUEST_SUCCESS = 7;

    private final static int REQUEST_FAIL = 77;

    private final static int CODE_LENGTH = 6;

    private String code = RandomDataUtil.with(CODE_LENGTH);

    private boolean isSendSMS = false;
    private int time_residual = 60;
    @InjectView(R.id.et_phone)
    EditText et_phone;
    @InjectView(R.id.et_validation)
    EditText et_validation;
    @InjectView(R.id.iv_phone)
    ImageView iv_phone;
    @InjectView(R.id.iv_validationCode)
    ImageView iv_validationCode;
    @InjectView(R.id.btn_validation)
    Button btn_validation;

    //获取验证码
    @OnClick(R.id.btn_validation)
    void getValidation() {
        updateTextTimer();
        if (!checkPhoneNumber()) {
            Toast.makeText(getActivity(), AppData.getString(R.string.error_phone_number), Toast.LENGTH_SHORT)
                    .show();
            VibratorUtil.vibrate(VibratorUtil.SHORT_VIBRATE_DURATION);
            return;
        }

        if (isSendSMS) {
            return;
        }
        isSendSMS = true;

        //给云片服务器发送请求
        final Map<String, String> params = new HashMap<String, String>();
        params.put("apikey", "c69ac6f4a6191d68a1a570816f14a713");
        params.put("tpl_id", "582497");
        params.put("tpl_value", "#code#=" + code);
        DsncLog.e("code", code);
        params.put("mobile", et_phone.getText().toString());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, this.URI_TPL_SEND_SMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DsncLog.e("发送验证码成功!", response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };
        RequestManager.addRequest(stringRequest, this);
    }

    //下一步
    @OnClick(R.id.btn_nextStep)
    void nextStep() {
        if (et_validation.getText().toString().trim().equals(code)) {
            Toast.makeText(getActivity(), "验证码正确", Toast.LENGTH_SHORT).show();
        } else {
            return;
        }

        if (isReset) {
            Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SetPasswordActivity.class);
            phoneNumber = et_phone.getText().toString().trim();
            DsncLog.e("123", mSinaUserData.toString());
            UserEntity user = new UserEntity();
            user.setUserName(mSinaUserData.getScreen_name());
            user.setGender(mSinaUserData.getGender());
            user.setUserImg(mSinaUserData.getProfile_image_url());
            user.setPhoneNum(phoneNumber);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    btn_validation.setText(time_residual + "s");
                    break;

                case 2:
                    time_residual = 60;
                    btn_validation.setEnabled(true);
                    btn_validation.setText(AppData.getString(R.string.getvalidation));
                    break;
            }
        }
    };

    public static PhoneValidationFragment newInstance(boolean isReset, SinaUserData mSinaUserData) {
        PhoneValidationFragment fragment = new PhoneValidationFragment();
        Bundle args = new Bundle();
        args.putBoolean("isReset", isReset);
        args.putSerializable("user", mSinaUserData);
        fragment.setArguments(args);
        return fragment;
    }

    public PhoneValidationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isReset = getArguments().getBoolean("isReset");
            mSinaUserData = (SinaUserData) getArguments().getSerializable("user");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_validation, container, false);
        ButterKnife.inject(this, view);
//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
//        api = WXAPIFactory.createWXAPI(getActivity(), APP_ID, false);
//        api.handleIntent(getActivity().getIntent(), this);

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
        et_validation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // 此处为得到焦点时的处理内容
                if (hasFocus) {
                    et_validation.setBackgroundResource(R.drawable.text_line_selected);
                    iv_validationCode.setImageResource(R.drawable.ic_validation_selected);
                } else {
                    et_validation.setBackgroundResource(R.drawable.text_line);
                    iv_validationCode.setImageResource(R.drawable.ic_validation);
                }
            }
        });

        return view;
    }


    private void updateTextTimer() {
        btn_validation.setEnabled(false);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                time_residual--;
                if (time_residual >= 0) {
                    message.what = 1;
                    handler.sendMessage(message);
                } else {
                    timer.cancel();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        }, 0, 1000);
    }

    //检查手机号码
    private boolean checkPhoneNumber() {
        if (et_phone.getText().length() == 11) {
            return true;
        }
        return false;
    }


//    // 微信发送请求到第三方应用时，会回调到该方法
//    @Override
//    public void onReq(BaseReq req) {
//        DsncLog.e("result", "req");
//        switch (req.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
////                goToGetMsg();
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
////                goToShowMsg((ShowMessageFromWX.Req) req);
//                break;
//            default:
//                break;
//        }
//    }


}
