package com.yuenidong.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuenidong.activity.R;
import com.yuenidong.adapter.MySignAdapter;
import com.yuenidong.adapter.VenuesAdapter;
import com.yuenidong.adapter.VenuesSignAdapter;
import com.yuenidong.bean.MySignEntity;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.bean.VenuesSignEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩 我的签到
 */
public class MySignFragment extends Fragment {
    List<MySignEntity> list;
    @InjectView(R.id.listView)
    ListView listView;

    public static MySignFragment newInstance() {
        MySignFragment fragment = new MySignFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MySignFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_sign, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<MySignEntity>();
        MySignAdapter adapter = new MySignAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

}
