package com.yuenidong.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yuenidong.activity.R;
import com.yuenidong.common.AppData;
import com.yuenidong.widget.LoadListView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩  我的活动
 */
public class MyMatchFragment extends Fragment {
    @InjectView(R.id.tv_mylanucher)
    TextView tv_mylanucher;
    @InjectView(R.id.tv_myattend)
    TextView tv_myattend;
    @InjectView(R.id.view_mylanucher)
    View view_mylanucher;
    @InjectView(R.id.view_mylanucher_select)
    View view_mylanucher_select;
    @InjectView(R.id.view_myattend)
    View view_myattend;
    @InjectView(R.id.view_myattend_select)
    View view_myattend_select;

    //我发起
    @OnClick(R.id.rl_mylanucher)
    void mylanucher() {
        tv_mylanucher.setTextColor(AppData.getColor(R.color.green));
        tv_myattend.setTextColor(AppData.getColor(R.color.grayblack));
        view_mylanucher_select.setVisibility(View.VISIBLE);
        view_mylanucher.setVisibility(View.GONE);
        view_myattend.setVisibility(View.VISIBLE);
        view_myattend_select.setVisibility(View.GONE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = MylanucherMatchFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    //我参与
    @OnClick(R.id.rl_myattend)
    void myattend() {
        tv_myattend.setTextColor(AppData.getColor(R.color.green));
        tv_mylanucher.setTextColor(AppData.getColor(R.color.grayblack));
        view_mylanucher_select.setVisibility(View.GONE);
        view_myattend_select.setVisibility(View.VISIBLE);
        view_myattend.setVisibility(View.GONE);
        view_mylanucher.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = MyattendMatchFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
        fragmentTransaction.commit();

    }

    public static MyMatchFragment newInstance() {
        MyMatchFragment fragment = new MyMatchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MyMatchFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_match, container, false);
        ButterKnife.inject(this, view);
        mylanucher();

        return view;
    }

}
