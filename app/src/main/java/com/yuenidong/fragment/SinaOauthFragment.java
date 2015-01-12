package com.yuenidong.fragment;

import android.app.Activity;
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
import com.igexin.sdk.PushManager;
import com.yuenidong.activity.PhoneValidationActivity;
import com.yuenidong.activity.R;
import com.yuenidong.activity.SinaOauthActivity;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.common.AppData;
import com.yuenidong.constants.SinaConstants;
import com.yuenidong.data.SinaToken;
import com.yuenidong.data.SinaUserData;
import com.yuenidong.util.UrlUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SinaOauthFragment extends Fragment {
//    private String ClientID;
    //新浪用户数据
    private SinaUserData mSinaUserData;
    private SinaToken mSinaToken;
    @InjectView(R.id.webview)
    WebView mWebView;
    @InjectView(R.id.progressbar)
    ProgressBar mProgressBar;

    public static SinaOauthFragment newInstance() {
        SinaOauthFragment fragment = new SinaOauthFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public SinaOauthFragment() {
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
        View view = inflater.inflate(R.layout.fragment_sina_oauth, container, false);
        ButterKnife.inject(this, view);
//        ClientID = PushManager.getInstance().getClientid(AppData.getContext());
        initWebView();
        mWebView.setWebViewClient(new WeiboWebViewClient());
        Oauth();
        return view;
    }

    public void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSaveFormData(false);
        settings.setSavePassword(false);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
    }


    private class WeiboWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            mProgressBar.setVisibility(View.VISIBLE);
            view.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            if (url.startsWith(SinaConstants.REDIRECT_URL)) {
                mProgressBar.setVisibility(View.GONE);
                getAccessToken(url);

                view.stopLoading();
                view.loadUrl("about:blank");
                return;
            }
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (!url.equals("about:blank"))
                mProgressBar.setVisibility(View.GONE);
        }
    }


    //得到访问令牌
    private void getAccessToken(String url) {
        Bundle values = UrlUtil.parseUrl(url);
        String code = values.getString("code");
        DsncLog.e("访问令牌code", code);
        if (code == null) {
            loginFail();
            return;
        }

        final Map<String, String> params = new HashMap<String, String>();
        //应用的AppKey
        params.put("client_id", SinaConstants.APP_KEY);
        //密钥
        params.put("client_secret", SinaConstants.APP_SECRET);
        params.put("grant_type", "authorization_code");
        params.put("code", code);
        //应用的回调页
        params.put("redirect_uri", SinaConstants.REDIRECT_URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SinaConstants.ACCESSYOKEN_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DsncLog.e("success", response);
                        mSinaToken = new Gson().fromJson(response.toString(), SinaToken.class);
                        getSinaUserData();
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

    private void loginFail() {
        getActivity().setProgressBarIndeterminateVisibility(false);
        Toast.makeText(getActivity(), R.string.error_loading_login, Toast.LENGTH_SHORT)
                .show();
        getActivity().finish();
    }


    //获得新浪用户数据
    private void getSinaUserData() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", mSinaToken.getAccess_token());
        params.put("uid", String.valueOf(mSinaToken.getUid()));
        DsncLog.e("uid", "" + mSinaToken.getUid());

        String requestUrl = SinaConstants.USERS_SHOW_URL + "?" + UrlUtil.encodeUrl(params);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DsncLog.e("用户在新浪的数据:", response.toString());
                        //获得新浪用户数据
                        mSinaUserData = new Gson().fromJson(response.toString(), SinaUserData.class);
                        DsncLog.e("mSinaUserData", new Gson().toJson(mSinaUserData));
                        Intent intent = new Intent(getActivity(), PhoneValidationActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", mSinaUserData);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        //上传到服务器
//                        insertToServer();

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


    private void Oauth() {
        Map<String, String> params = new HashMap<String, String>();
        //应用的AppKey
        params.put("client_id", SinaConstants.APP_KEY);
        params.put("response_type", "code");
        //应用的回调页
        params.put("redirect_uri", SinaConstants.REDIRECT_URL);
        //应用申请的高级权限
        params.put("scope", SinaConstants.SCOPE);
        params.put("display", "mobile");
        mWebView.loadUrl(SinaConstants.AUTHORIZE_URL + "?" + UrlUtil.encodeUrl(params));
    }


    private void insertToServer() {
//        Intent intent = new Intent(getActivity(), PhoneValidationActivity.class);
//        startActivity(intent);
    }
}
