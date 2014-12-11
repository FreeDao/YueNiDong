package com.yuenidong.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;

import butterknife.ButterKnife;

public class WxOauthFragment extends Fragment {
    private final static String APP_ID = "wxa6e5ec6e5da42692";
    private final static String AppSecret = "938c90598eab5c46983d5cb5bbb39855";
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
        api = WXAPIFactory.createWXAPI(getActivity(), APP_ID, false);
        if (!api.isWXAppInstalled()) {
            Toast.makeText(getActivity(), "微信没有安装!", Toast.LENGTH_SHORT).show();
        } else {
            boolean isRegister = api.registerApp(APP_ID);
            DsncLog.e("微信注册:", isRegister + "");
        }
        return view;
    }

}
