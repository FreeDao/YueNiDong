/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.yuenidong.activity.wxapi;

import android.content.Intent;
import android.os.Bundle;
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
import com.yuenidong.activity.PhoneValidationActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.constants.SinaConstants;
import com.yuenidong.data.SinaUserData;
import com.yuenidong.util.UrlUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler {
    private final static String APP_ID = "wxa6e5ec6e5da42692";
    private final static String APP_SECRET = "938c90598eab5c46983d5cb5bbb39855";
    private String code;
    private String access_token;
    private String openid;

    // IWXAPI 是第三方app和微信通信的openapi接口
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, APP_ID, false);
        // 将该app注册到微信
        api.registerApp(APP_ID);
        // send oauth request
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        api.sendReq(req);
        api.handleIntent(getIntent(), this);
    }

    /**
     * 处理微信发出的向第三方应用请求app message
     * <p/>
     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
     * 做点其他的事情，包括根本不打开任何页面
     */
    public void onGetMessageFromWXReq(WXMediaMessage msg) {
        Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
        startActivity(iLaunchMyself);
    }

    /**
     * 处理微信向第三方应用发起的消息
     * <p/>
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     * <p/>
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
    public void onShowMessageFromWXReq(WXMediaMessage msg) {
        if (msg != null && msg.mediaObject != null
                && (msg.mediaObject instanceof WXAppExtendObject)) {
            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
        DsncLog.e("result", "req");
        switch (req.getType()) {
            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                goToGetMsg();
                break;
            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                goToShowMsg((ShowMessageFromWX.Req) req);
                break;
            default:
                break;
        }
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    @Override
    public void onResp(BaseResp resp) {
        int result = 0;
        DsncLog.e("result", resp.errCode + "");

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                result = R.string.errcode_success;
                code = ((SendAuth.Resp) resp).code;
                DsncLog.e("code值是Activity", code);
                finish();
                //-------------
                final Map<String, String> params = new HashMap<String, String>();
                params.put("appid", APP_ID);
                params.put("secret", APP_SECRET);
                params.put("code", code);
                params.put("grant_type", "authorization_code");

                String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token" + "?" + UrlUtil.encodeUrl(params);
                StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                DsncLog.e("获取access_token成功", response.toString());
                                Toast.makeText(WXEntryActivity.this, "获取access_token成功", Toast.LENGTH_SHORT).show();
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    access_token = jsonObject.getString("access_token");
                                    openid = jsonObject.getString("openid");
                                    getUserInfo();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    DsncLog.e("获取token失败", "获取token失败");
                                }

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

        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
    }

    //
    public void getUserInfo() {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("access_token", access_token);
        params.put("openid", openid);
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo" + "?" + UrlUtil.encodeUrl(params);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        DsncLog.e("获取微信用户信息成功", response.toString());
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String userName = jsonObject.getString("nickname");
                            String gender = jsonObject.getString("sex");
                            String image_url = jsonObject.getString("headimgurl");
                            DsncLog.e("userName", userName);
                            DsncLog.e("gender", gender);
                            DsncLog.e("image_url", image_url);
                            SinaUserData user = new SinaUserData();
                            user.setProfile_image_url(image_url);
                            user.setScreen_name(userName);
                            if (gender.equals("1")) {
                                user.setGender("m");
                            }
                            if (gender.equals("2")) {
                                user.setGender("f");
                            }
                            Intent intent = new Intent(WXEntryActivity.this, PhoneValidationActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("user", user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
