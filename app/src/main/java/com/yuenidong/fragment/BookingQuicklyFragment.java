package com.yuenidong.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yuenidong.activity.PayActivity;
import com.yuenidong.activity.R;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 立即预定
 */
public class BookingQuicklyFragment extends Fragment {
    //订单时段
    private String time;
    private int status_type = 0;
    private int status_level = 0;
    private int count = 1;
    @InjectView(R.id.iv_add)
    ImageView iv_add;
    @InjectView(R.id.iv_reduce)
    ImageView iv_reduce;
    @InjectView(R.id.tv_coin_count)
    TextView tv_coin_count;

    @InjectView(R.id.iv_type)
    ImageView iv_type;
    @InjectView(R.id.iv_level)
    ImageView iv_level;
    @InjectView(R.id.ll_one)
    LinearLayout ll_type;
    @InjectView(R.id.ll_two)
    LinearLayout ll_level;

    @InjectView(R.id.tv_type)
    TextView tv_type;
    @InjectView(R.id.tv_level)
    TextView tv_level;

    @InjectView(R.id.tv_type_chinesetableball)
    TextView tv_type_chinesetableball;
    @InjectView(R.id.tv_type_americantableball)
    TextView tv_type_americantableball;
    @InjectView(R.id.tv_type_snooker)
    TextView tv_type_snooker;

    @InjectView(R.id.view_type_chinesetableball)
    View view_type_chinesetableball;
    @InjectView(R.id.view_type_americantableball)
    View view_type_americantableball;
    @InjectView(R.id.view_type_snooker)
    View view_type_snooker;

    @InjectView(R.id.tv_level_normal)
    TextView tv_level_normal;
    @InjectView(R.id.tv_level_higher)
    TextView tv_level_higher;
    @InjectView(R.id.tv_level_best)
    TextView tv_level_best;

    @InjectView(R.id.view_level_normal)
    View view_level_normal;
    @InjectView(R.id.view_level_higher)
    View view_level_higher;
    @InjectView(R.id.view_level_best)
    View view_level_best;

    @InjectView(R.id.tv_starttime)
    TextView tv_starttime;
    @InjectView(R.id.tv_endtime)
    TextView tv_endtime;

    //项目类型
    @OnClick(R.id.rl_type)
    void type() {
        switch (status_type) {
            case 0:
                iv_type.setBackgroundResource(R.drawable.ic_underslider_up);
                ll_type.setVisibility(View.VISIBLE);
                status_type = 1;
                break;
            case 1:
                iv_type.setBackgroundResource(R.drawable.ic_underslider);
                ll_type.setVisibility(View.GONE);
                status_type = 0;
                break;
        }
    }

    //桌球等级
    @OnClick(R.id.rl_level)
    void level() {
        switch (status_level) {
            case 0:
                iv_level.setBackgroundResource(R.drawable.ic_underslider_up);
                ll_level.setVisibility(View.VISIBLE);
                status_level = 1;
                break;
            case 1:
                iv_level.setBackgroundResource(R.drawable.ic_underslider);
                ll_level.setVisibility(View.GONE);
                status_level = 0;
                break;
        }
    }

    //中式八球
    @OnClick(R.id.rl_type_chinesetableball)
    void chiesetableball() {
        hideTypeView();
        tv_type_chinesetableball.setTextColor(AppData.getColor(R.color.green));
        view_type_chinesetableball.setVisibility(View.VISIBLE);
        tv_type.setText(tv_type_chinesetableball.getText().toString().trim());
    }

    //美式九球
    @OnClick(R.id.rl_type_americantableball)
    void americantaleball() {
        hideTypeView();
        tv_type_americantableball.setTextColor(AppData.getColor(R.color.green));
        view_type_americantableball.setVisibility(View.VISIBLE);
        tv_type.setText(tv_type_americantableball.getText().toString().trim());
    }

    //斯诺克
    @OnClick(R.id.rl_type_snooker)
    void snooker() {
        hideTypeView();
        tv_type_snooker.setTextColor(AppData.getColor(R.color.green));
        view_type_snooker.setVisibility(View.VISIBLE);
        tv_type.setText(tv_type_snooker.getText().toString().trim());
    }

    //普通
    @OnClick(R.id.rl_level_normal)
    void normal() {
        hideLevelView();
        tv_level_normal.setTextColor(AppData.getColor(R.color.green));
        view_level_normal.setVisibility(View.VISIBLE);
        tv_level.setText(tv_level_normal.getText().toString().trim());
    }

    //高级
    @OnClick(R.id.rl_level_higher)
    void higher() {
        hideLevelView();
        tv_level_higher.setTextColor(AppData.getColor(R.color.green));
        view_level_higher.setVisibility(View.VISIBLE);
        tv_level.setText(tv_level_higher.getText().toString().trim());
    }

    //总统
    @OnClick(R.id.rl_level_best)
    void best() {
        hideLevelView();
        tv_level_best.setTextColor(AppData.getColor(R.color.green));
        view_level_best.setVisibility(View.VISIBLE);
        tv_level.setText(tv_level_best.getText().toString().trim());
    }

    //加数量
    @OnClick(R.id.iv_add)
    void add() {
        iv_reduce.setEnabled(true);
        count++;
        iv_reduce.setImageResource(R.drawable.ic_reduce_ll);
        tv_coin_count.setText(count + "");
    }

    //减数量
    @OnClick(R.id.iv_reduce)
    void reduce() {
        count--;
        if (count <= 1) {
            iv_reduce.setImageResource(R.drawable.ic_reduce);
            iv_reduce.setEnabled(false);
            count = 1;
            tv_coin_count.setText(count + "");
        } else {
            iv_reduce.setImageResource(R.drawable.ic_reduce_ll);
            tv_coin_count.setText(count + "");
        }
    }

    //开始时间
    @OnClick(R.id.rl_starttime)
    void starttime() {
        Dialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                time = "";
                time = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                final Dialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = time + hourOfDay + ":" + minute;
                        DsncLog.e("time", time);
                        tv_starttime.setText(time);
                    }
                }, 19, 20, true);
                dialog.show(); // 显示对话框
            }
        }, 2014, 0, 1);
        dialog.show(); // 显示对话框
    }

    //结束时间
    @OnClick(R.id.rl_endtime)
    void endtime() {
        Dialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                time = "";
                time = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
                final Dialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time = time + hourOfDay + ":" + minute;
                        DsncLog.e("time", time);
                        tv_endtime.setText(time);
                    }
                }, 19, 20, true);
                dialog.show(); // 显示对话框
            }
        }, 2014, 0, 1);
        dialog.show(); // 显示对话框
    }

    //提交订单
    @OnClick(R.id.btn_commitorder)
    void commitorder() {
        Intent intent = new Intent(getActivity(), PayActivity.class);
        startActivity(intent);
    }

    private void hideTypeView() {
        tv_type_chinesetableball.setTextColor(AppData.getColor(R.color.black));
        tv_type_americantableball.setTextColor(AppData.getColor(R.color.black));
        tv_type_snooker.setTextColor(AppData.getColor(R.color.black));
        view_type_chinesetableball.setVisibility(View.GONE);
        view_type_americantableball.setVisibility(View.GONE);
        view_type_snooker.setVisibility(View.GONE);
    }

    private void hideLevelView() {
        tv_level_normal.setTextColor(AppData.getColor(R.color.black));
        tv_level_higher.setTextColor(AppData.getColor(R.color.black));
        tv_level_best.setTextColor(AppData.getColor(R.color.black));
        view_level_normal.setVisibility(View.GONE);
        view_level_higher.setVisibility(View.GONE);
        view_level_best.setVisibility(View.GONE);
    }


    public static BookingQuicklyFragment newInstance() {
        BookingQuicklyFragment fragment = new BookingQuicklyFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public BookingQuicklyFragment() {
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
        View view = inflater.inflate(R.layout.fragment_booking_quickly, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

}
