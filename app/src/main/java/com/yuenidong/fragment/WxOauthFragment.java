package com.yuenidong.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.common.AppData;
import com.yuenidong.constants.SinaConstants;
import com.yuenidong.constants.WeiXinConstants;
import com.yuenidong.data.SinaToken;
import com.yuenidong.util.UrlUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.sharesdk.demo.wxapi.WXEntryActivity;

public class WxOauthFragment extends Fragment implements IWXAPIEventHandler {
    //    @InjectView(R.id.webview)
//    WebView mWebView;
//    @InjectView(R.id.progressbar)
//    ProgressBar mProgressBar;
    private String access_token;
    private final static String APP_ID = "wxa6e5ec6e5da42692";
    private final static String AppSecret = "938c90598eab5c46983d5cb5bbb39855";
    private String openid;
    private String code;
    private IWXAPI api;

    public static WxOauthFragment newInstance() {
        WxOauthFragment fragment = new WxOauthFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public WxOauthFragment() {
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
        View view = inflater.inflate(R.layout.fragment_wx_oauth, container, false);
        ButterKnife.inject(this, view);
        api = WXAPIFactory.createWXAPI(AppData.getContext(), APP_ID, false);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(getActivity(), "微信没有安装!", Toast.LENGTH_SHORT).show();
        } else {
            boolean isRegister = api.registerApp(APP_ID);
            DsncLog.e("微信注册:", isRegister + "");
        }

//        initWebView();
//        mWebView.setWebViewClient(new WeiboWebViewClient());
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        boolean b = api.sendReq(req);
        DsncLog.e("b", b + "");
        api.handleIntent(getActivity().getIntent(), WXEntryActivity.getInstance());

//        Oauth();

        return view;
    }

//    public void initWebView() {
//        WebSettings settings = mWebView.getSettings();
//        settings.setJavaScriptEnabled(true);
//        settings.setSaveFormData(false);
//        settings.setSavePassword(false);
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
//        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
//    }


//    private class WeiboWebViewClient extends WebViewClient {
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            return false;
//        }
//
//        @Override
//        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            mProgressBar.setVisibility(View.VISIBLE);
//            view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//            if (url.startsWith(SinaConstants.REDIRECT_URL)) {
//                mProgressBar.setVisibility(View.GONE);
////                getAccessToken(url);
//                view.stopLoading();
//                view.loadUrl("about:blank");
//                return;
//            }
//            super.onPageStarted(view, url, favicon);
//
//        }
//
//        @Override
//        public void onPageFinished(WebView view, String url) {
//            super.onPageFinished(view, url);
//            if (!url.equals("about:blank"))
//                mProgressBar.setVisibility(View.GONE);
//        }
//    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        DsncLog.e("result", result + "");

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                code = ((SendAuth.Resp) resp).code;
                DsncLog.e("code", code);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                result = R.string.errcode_cancel;
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                result = R.string.errcode_deny;
                break;
            default:
                result = R.string.errcode_unknown;
                break;
        }
        Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
    }

    //通过code获取access_token
    private void getAccess_token() {
        final Map<String, String> params = new HashMap<String, String>();
        //应用的AppKey
        params.put("appid", APP_ID);
        //密钥
        params.put("secret", AppSecret);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        //应用的回调页
        params.put("redirect_uri", SinaConstants.REDIRECT_URL);
        String requestUrl = WeiXinConstants.GETACCESSTOKEN + "?" + UrlUtil.encodeUrl(params);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DsncLog.e("success", response);
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

    //获取用户个人信息
    private void getPersonInfo() {
        final Map<String, String> params = new HashMap<String, String>();
        //密钥
        params.put("access_token", access_token);
        //openid
        params.put("openid", openid);
        String requestUrl = WeiXinConstants.GETPERSONINFO + "?" + UrlUtil.encodeUrl(params);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DsncLog.e("success", response);
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

}


