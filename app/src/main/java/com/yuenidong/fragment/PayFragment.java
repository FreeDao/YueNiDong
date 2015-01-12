package com.yuenidong.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yuenidong.activity.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 支付
 */
public class PayFragment extends Fragment {
    private int status_pay = 0;
    @InjectView(R.id.iv_pay_status)
    ImageView iv_pay;

    //支付宝
    @OnClick(R.id.rl_pay_zhifubao)
    void pay() {
        switch (status_pay) {
            case 0:
                iv_pay.setBackgroundResource(R.drawable.ic_selected);
                status_pay = 1;
                break;
            case 1:
                iv_pay.setBackgroundResource(R.drawable.ic_unselected);
                status_pay = 0;
                break;
        }
    }

    public static PayFragment newInstance() {
        PayFragment fragment = new PayFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public PayFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pay, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

}
