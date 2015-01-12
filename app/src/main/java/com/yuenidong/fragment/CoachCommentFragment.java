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
import com.yuenidong.adapter.CoachCommentAdapter;
import com.yuenidong.bean.CoachCommentEntity;
import com.yuenidong.widget.LoadListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩 教练评价
 */
public class CoachCommentFragment extends Fragment {
    private List<CoachCommentEntity> list;
    @InjectView(R.id.listView)
    LoadListView listView;

    public static CoachCommentFragment newInstance() {
        CoachCommentFragment fragment = new CoachCommentFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public CoachCommentFragment() {
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
        View view = inflater.inflate(R.layout.fragment_coach_comment, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<CoachCommentEntity>();
        CoachCommentAdapter adapter = new CoachCommentAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

}
