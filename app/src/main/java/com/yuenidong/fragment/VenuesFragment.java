package com.yuenidong.fragment;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.yuenidong.activity.R;
import com.yuenidong.activity.VenuesInfoActivity;
import com.yuenidong.adapter.VenuesAdapter;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.widget.AutoListView;
import com.yuenidong.widget.LoadListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class VenuesFragment extends Fragment {
    private List<VenuesEntity> list;

    @InjectView(R.id.listview)
    LoadListView listView;

    public static VenuesFragment newInstance() {
        VenuesFragment fragment = new VenuesFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public VenuesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_venues, container, false);
        ButterKnife.inject(this, view);
        this.list = new ArrayList<VenuesEntity>();
        VenuesAdapter adapter = new VenuesAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), VenuesInfoActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
