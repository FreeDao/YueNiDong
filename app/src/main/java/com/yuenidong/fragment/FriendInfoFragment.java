package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gotye.api.GotyeUser;
import com.qinjia.activity.ChatPage;
import com.yuenidong.activity.MyCollectionActivity;
import com.yuenidong.activity.MyMatchActivity;
import com.yuenidong.activity.MySignActivity;
import com.yuenidong.activity.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 好友主页  石岩
 */
public class FriendInfoFragment extends Fragment {
    //我的活动
    @OnClick(R.id.btn_mymatch)
    void match() {
        Intent intent = new Intent(getActivity(), MyMatchActivity.class);
        startActivity(intent);
    }

    //我的收藏
    @OnClick(R.id.btn_mycollection)
    void collect() {
        Intent intent = new Intent(getActivity(), MyCollectionActivity.class);
        startActivity(intent);
    }

    //我的签到
    @OnClick(R.id.btn_mysign)
    void sign() {
        Intent intent = new Intent(getActivity(), MySignActivity.class);
        startActivity(intent);
    }

    //对话
    @OnClick(R.id.rl_talk)
    void talk() {
        Intent intent = new Intent(getActivity(), ChatPage.class);
        GotyeUser user = new GotyeUser("chuanzhang");
        intent.putExtra("user",user);
        startActivity(intent);
    }

    public static FriendInfoFragment newInstance() {
        FriendInfoFragment fragment = new FriendInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public FriendInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_friend_info, container, false);
        ButterKnife.inject(this, view);
        return view;
    }


}
