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
import com.yuenidong.adapter.VenuesCommentAdapter;
import com.yuenidong.bean.VenuesCommentEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩 场馆评价
 */
public class VenuesCommentFragment extends Fragment {
    private List<VenuesCommentEntity> list;
    @InjectView(R.id.listView)
    ListView listView;


    public static VenuesCommentFragment newInstance() {
        VenuesCommentFragment fragment = new VenuesCommentFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public VenuesCommentFragment() {
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
        View view = inflater.inflate(R.layout.fragment_venues_comment, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<VenuesCommentEntity>();
        VenuesCommentAdapter adapter = new VenuesCommentAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

}
