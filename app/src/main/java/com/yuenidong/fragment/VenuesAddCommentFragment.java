package com.yuenidong.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.yuenidong.activity.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 石岩 场馆(添加评价)
 */
public class VenuesAddCommentFragment extends Fragment {
    @InjectView(R.id.ratingbar)
    RatingBar ratingBar;
    @InjectView(R.id.tv_point)
    TextView tv_point;

    public static VenuesAddCommentFragment newInstance() {
        VenuesAddCommentFragment fragment = new VenuesAddCommentFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public VenuesAddCommentFragment() {
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
        View view = inflater.inflate(R.layout.fragment_venues_add_comment, container, false);
        ButterKnife.inject(this, view);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                tv_point.setText(v + "分");
            }
        });
        return view;
    }

}
