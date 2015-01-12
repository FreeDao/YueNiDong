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
import com.yuenidong.adapter.MessageInfoAdapter;
import com.yuenidong.bean.MessageInfoEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MessageInfoFragment extends Fragment {
    private List<MessageInfoEntity> list;
    @InjectView(R.id.listview)
    ListView listView;


    public static MessageInfoFragment newInstance() {
        MessageInfoFragment fragment = new MessageInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MessageInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_message_info, container, false);
        ButterKnife.inject(this, view);
        list = new ArrayList<MessageInfoEntity>();
        MessageInfoAdapter adapter = new MessageInfoAdapter(getActivity(), list);
        listView.setAdapter(adapter);
        return view;
    }

}
