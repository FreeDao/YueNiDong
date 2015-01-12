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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩  我的订单
 */
public class MyOrderFragment extends Fragment {
    @InjectView(R.id.tv_paycome)
    TextView tv_paycome;
    @InjectView(R.id.tv_paynotcome)
    TextView tv_paynotcome;
    @InjectView(R.id.view_paycome)
    View view_paycome;
    @InjectView(R.id.view_paycome_select)
    View view_paycome_select;
    @InjectView(R.id.view_paynotcome)
    View view_paynotcome;
    @InjectView(R.id.view_paynotcome_select)
    View view_paynotcome_select;

    //已付款
    @OnClick(R.id.rl_paycome)
    void paycome() {
        tv_paycome.setTextColor(AppData.getColor(R.color.green));
        tv_paynotcome.setTextColor(AppData.getColor(R.color.grayblack));
        view_paycome_select.setVisibility(View.VISIBLE);
        view_paycome.setVisibility(View.GONE);
        view_paynotcome.setVisibility(View.VISIBLE);
        view_paynotcome_select.setVisibility(View.GONE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = PayComeFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    //未付款
    @OnClick(R.id.rl_paynotcome)
    void paynotcome() {
        tv_paynotcome.setTextColor(AppData.getColor(R.color.green));
        tv_paycome.setTextColor(AppData.getColor(R.color.grayblack));
        view_paycome_select.setVisibility(View.GONE);
        view_paynotcome_select.setVisibility(View.VISIBLE);
        view_paynotcome.setVisibility(View.GONE);
        view_paycome.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = PayNotComeFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
        fragmentTransaction.commit();
    }


    public static MyOrderFragment newInstance() {
        MyOrderFragment fragment = new MyOrderFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MyOrderFragment() {
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
        View view = inflater.inflate(R.layout.fragment_my_order, container, false);
        ButterKnife.inject(this, view);
        paycome();
        return view;
    }
}
