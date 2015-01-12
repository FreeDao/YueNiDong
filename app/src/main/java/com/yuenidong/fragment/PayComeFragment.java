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
import android.widget.ListView;

import com.yuenidong.activity.MyOrderInfoActivity;
import com.yuenidong.activity.R;
import com.yuenidong.adapter.OrderPayAdapter;
import com.yuenidong.bean.OrderEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩  已付款
 */
public class PayComeFragment extends Fragment {
    private List<OrderEntity> list;
    @InjectView(R.id.listView)
    ListView listView;

    public static PayComeFragment newInstance() {
        PayComeFragment fragment = new PayComeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public PayComeFragment() {
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
        View view = inflater.inflate(R.layout.fragment_pay_come, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<OrderEntity>();
        OrderPayAdapter adapter = new OrderPayAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getActivity(), MyOrderInfoActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
