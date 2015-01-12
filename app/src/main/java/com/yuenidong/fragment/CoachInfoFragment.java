package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yuenidong.activity.CoachCommentActivity;
import com.yuenidong.activity.ComeRecentActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 教练主页
 */
public class CoachInfoFragment extends Fragment {
    @InjectView(R.id.rl_bottombar)
    RelativeLayout rl_bottombar;
    @InjectView(R.id.iv_phone)
    ImageButton iv_phone;
    //联系他
    @InjectView(R.id.tv_link)
    TextView tv_link;

    @OnClick(R.id.rl_bottombar)
    void rl_bottombar() {
        DsncLog.e("123", "123");
//        Intent intent = new Intent();
//        intent.setAction("android.intent.action.CALL");
//        intent.setData(Uri.parse("tel:" + "13628688550"));//mobile为你要拨打的电话号码
//        getActivity().startActivity(intent);
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:13850734494"));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppData.getContext().startActivity(intent);
    }

    //最近来过
    @OnClick(R.id.btn_come)
    void come() {
        Intent intent = new Intent(getActivity(), ComeRecentActivity.class);
        startActivity(intent);
    }

    //教练评价(总评级)
    @OnClick(R.id.btn_coachcomment)
    void comment() {
        Intent intent = new Intent(getActivity(), CoachCommentActivity.class);
        startActivity(intent);
    }

    public static CoachInfoFragment newInstance() {
        CoachInfoFragment fragment = new CoachInfoFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public CoachInfoFragment() {
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
        View view = inflater.inflate(R.layout.fragment_coach_info, container, false);
        ButterKnife.inject(this, view);
        iv_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        DsncLog.e("抬起", "抬起");
                        iv_phone.setBackgroundResource(R.drawable.ic_phone);
                        tv_link.setTextColor(AppData.getColor(R.color.grayblack));
                        break;
                    case MotionEvent.ACTION_DOWN:
                        DsncLog.e("按下", "按下");
                        iv_phone.setBackgroundResource(R.drawable.ic_phone_pressed);
                        tv_link.setTextColor(AppData.getColor(R.color.green));
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        DsncLog.e("抬起", "抬起");
                        iv_phone.setBackgroundResource(R.drawable.ic_phone);
                        tv_link.setTextColor(AppData.getColor(R.color.grayblack));
                        break;
                }
                return false;
            }
        });
        return view;
    }

}
