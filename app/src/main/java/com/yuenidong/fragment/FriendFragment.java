package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yuenidong.activity.FriendInfoActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.FriendAdapter;
import com.yuenidong.app.DsncLog;
import com.yuenidong.bean.FriendEntity;
import com.yuenidong.widget.AutoListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FriendFragment extends Fragment implements AdapterView.OnItemClickListener {
    private List<FriendEntity> list;
    @InjectView(R.id.listview)
    AutoListView listView;


    public static FriendFragment newInstance() {
        FriendFragment fragment = new FriendFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public FriendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_friend, container, false);
        ButterKnife.inject(this, view);
        this.list = new ArrayList<FriendEntity>();
        FriendAdapter adapter = new FriendAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        DsncLog.e("listview点击事件", i + "");
        Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
        startActivity(intent);
    }
}
