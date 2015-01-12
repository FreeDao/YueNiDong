package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;
import com.yuenidong.fragment.SinaOauthFragment;
//import com.yuenidong.fragment.WxOauthFragment;


public class WxOauthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBarText(AppData.getString(R.string.wxlogin));
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        Fragment fragment = WxOauthFragment.newInstance();
//        fragmentTransaction.replace(R.id.layout_container, fragment);
//        fragmentTransaction.commit();

    }

//    @Override
//    public void onReq(BaseReq baseReq) {
//
//    }

//    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
//    @Override
//    public void onResp(BaseResp resp) {
//        int result = 0;
//        DsncLog.e("result", result + "");
//
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                result = R.string.errcode_success;
//                String code = ((SendAuth.Resp) resp).code;
//                DsncLog.e("code", code);
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = R.string.errcode_cancel;
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = R.string.errcode_deny;
//                break;
//            default:
//                result = R.string.errcode_unknown;
//                break;
//        }
//        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
//    }
}
