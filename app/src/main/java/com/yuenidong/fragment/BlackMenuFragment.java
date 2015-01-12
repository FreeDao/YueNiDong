package com.yuenidong.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yuenidong.activity.R;
import com.yuenidong.adapter.BlackMenuAdapter;
import com.yuenidong.bean.FriendEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩  黑名单
 */
public class BlackMenuFragment extends Fragment {
    List<FriendEntity> list;
    @InjectView(R.id.listView)
    ListView listView;

    public static BlackMenuFragment newInstance() {
        BlackMenuFragment fragment = new BlackMenuFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public BlackMenuFragment() {
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
        View view = inflater.inflate(R.layout.fragment_blank_menu, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<FriendEntity>();
        BlackMenuAdapter adapter = new BlackMenuAdapter(getActivity(), list);
        listView.setAdapter(adapter);

        return view;
    }

}
