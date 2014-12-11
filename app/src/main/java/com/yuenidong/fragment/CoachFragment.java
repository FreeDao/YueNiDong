package com.yuenidong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yuenidong.activity.R;
import com.yuenidong.adapter.CoachAdapter;
import com.yuenidong.bean.CoachEntity;
import com.yuenidong.widget.AutoListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class CoachFragment extends Fragment {
    private List<CoachEntity> list;
    @InjectView(R.id.listview)
    AutoListView listView;

    public static CoachFragment newInstance() {
        CoachFragment fragment = new CoachFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public CoachFragment() {
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
        View view = inflater.inflate(R.layout.fragment_coach, container, false);
        ButterKnife.inject(this, view);
        this.list = new ArrayList<CoachEntity>();
        CoachAdapter adapter = new CoachAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

}
