package com.yuenidong.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.yuenidong.activity.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩 添加好友
 */
public class AddFriendFragment extends Fragment {
    @InjectView(R.id.et_number)
    EditText et_number;
    @InjectView(R.id.view_one)
    View view_one;
    @InjectView(R.id.view_two)
    View view_two;


    public static AddFriendFragment newInstance() {
        AddFriendFragment fragment = new AddFriendFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public AddFriendFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_friend, container, false);
        ButterKnife.inject(this, view);


        et_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // 此处为得到焦点时的处理内容
                if (hasFocus) {
                    view_two.setVisibility(View.VISIBLE);
                    view_one.setVisibility(View.GONE);
                } else {
                    view_two.setVisibility(View.GONE);
                    view_one.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }

}
