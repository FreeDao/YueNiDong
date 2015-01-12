package com.yuenidong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuenidong.activity.R;
import com.yuenidong.adapter.VenuesCommentAdapter;
import com.yuenidong.adapter.VenuesSignAdapter;
import com.yuenidong.bean.VenuesCommentEntity;
import com.yuenidong.bean.VenuesSignEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩  场馆签到
 */
public class VenuesSignFragment extends Fragment {
    private List<VenuesSignEntity> list;
    @InjectView(R.id.listView)
    ListView listView;

    public static VenuesSignFragment newInstance() {
        VenuesSignFragment fragment = new VenuesSignFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public VenuesSignFragment() {
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
        View view = inflater.inflate(R.layout.fragment_venues_sign, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<VenuesSignEntity>();
        VenuesSignAdapter adapter = new VenuesSignAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

}
