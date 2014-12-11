package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yuenidong.activity.FirstActivity;
import com.yuenidong.activity.LoginActivity;
import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.common.PreferenceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class FirstFragment extends Fragment {
    //随便看看
    @OnClick(R.id.btn_loginDirect)
    void logindirect() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        PreferenceUtil.setPreBoolean("isDirect",true);
        intent.putExtra("isDirect", true);
        startActivity(intent);
    }

    //立即登录
    @OnClick(R.id.btn_login)
    void login() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    public static FirstFragment newInstance() {
        FirstFragment fragment = new FirstFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public FirstFragment() {
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
        View view = inflater.inflate(R.layout.fragment_first, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

}
