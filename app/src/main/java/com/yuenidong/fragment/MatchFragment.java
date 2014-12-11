package com.yuenidong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yuenidong.activity.R;
import com.yuenidong.adapter.MatchAdapter;
import com.yuenidong.bean.MatchEntity;
import com.yuenidong.common.AppData;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MatchFragment extends Fragment {
    private List<MatchEntity> list;
    @InjectView(R.id.listview)
    ListView listView;
    @InjectView(R.id.actionbar_text)
    TextView tv_title;
    @InjectView(R.id.actionbar_rightbutton)
    Button btn_lanucher;

    public static MatchFragment newInstance() {
        MatchFragment fragment = new MatchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MatchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_match, container, false);
        ButterKnife.inject(this, view);
        tv_title.setText(AppData.getString(R.string.main_match));
        tv_title.setVisibility(View.VISIBLE);
        btn_lanucher.setText(AppData.getString(R.string.launcher));
        btn_lanucher.setVisibility(View.VISIBLE);
        list = new ArrayList<MatchEntity>();
        MatchAdapter adapter = new MatchAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

}
