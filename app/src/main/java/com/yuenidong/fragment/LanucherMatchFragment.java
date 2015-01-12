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
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.MatchPlaceActivity;
import com.yuenidong.activity.R;
import com.yuenidong.activity.RewardCoinActivity;
import com.yuenidong.app.DsncLog;
import com.yuenidong.bean.MatchEntity;
import com.yuenidong.widget.MyDateTimePickerDialog;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩
 * 发起活动
 */
public class LanucherMatchFragment extends Fragment {
    private int count = 0;
    //活动主题
    private String title;
    //活动地点
    private String place;
    //活动时间
    private String time;
    //活动人数
    private String peopleCount;
    //活动距离
    private int distance = 5;
    @InjectView(R.id.seekbar)
    SeekBar seekBar;
    @InjectView(R.id.et_match_title)
    EditText et_match_title;
    @InjectView(R.id.tv_match_time)
    TextView tv_match_time;
    @InjectView(R.id.tv_match_place)
    TextView tv_match_place;
    @InjectView(R.id.et_match_peopleCount)
    EditText et_match_peopleCount;
    @InjectView(R.id.tv_distance)
    TextView tv_distance;

    Boolean isShow = false;
    public String monthOfYear1;
    String dayOfMonth1;
    String hour1;
    String minute1;

    //活动时间
    @OnClick(R.id.tv_match_time)
    void matchTime() {
//        Dialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker view, int year, int monthOfYear,
//                                  int dayOfMonth) {
//                isShow=true;
////                tv_date.setText(year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日");
//                time = "";
//                time = year + "年" + (monthOfYear + 1) + "月" + dayOfMonth + "日";
//                final Dialog dialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
////                        tv_time.setText(hourOfDay + ":" + minute);
//                        if(isShow) {
//                            time = time + hourOfDay + ":" + minute;
//                            DsncLog.e("time", time);
//                            tv_match_time.setText(time);
//                            isShow=false;
//                        }
//                    }
//                }, 19, 20, true);
//                dialog.show(); // 显示对话框
//            }
//        }, 2014, 0, 1);
//        dialog.show(); // 显示对话框

        new MyDateTimePickerDialog(getActivity(), new MyDateTimePickerDialog.OnDateTimeSetListener() {

            @Override
            public void onDateTimeSet(int year, int monthOfYear, int dayOfMonth,
                                      int hour, int minute) {
//                tv_match_time.setText(year+"年"+monthOfYear+"月"+dayOfMonth+"日"+hour+":"+minute);
                monthOfYear1 = monthOfYear + "";
                dayOfMonth1 = dayOfMonth + "";
                hour1 = hour + "";
                minute1 = minute + "";
                if (monthOfYear < 10) {
                    monthOfYear1 = "0" + monthOfYear;
                }
                if (dayOfMonth < 10) {
                    dayOfMonth1 = "0" + dayOfMonth;
                }
                if (hour < 10) {
                    hour1 = "0" + hour;
                }
                if (minute < 10) {
                    minute1 = "0" + minute;
                }

                tv_match_time.setText(year + "-" + monthOfYear1 + "-" + dayOfMonth1 + " " + hour1 + ":" + minute1);
            }
        }).show();
    }

    //活动地点
    @OnClick(R.id.tv_match_place)
    void matchPlace() {
        Intent intent = new Intent(getActivity(), MatchPlaceActivity.class);
        startActivityForResult(intent, 0);
    }

    public static LanucherMatchFragment newInstance() {
        LanucherMatchFragment fragment = new LanucherMatchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    //下一步
    @OnClick(R.id.btn_nextStep)
    void nextStep() {
        title = et_match_title.getText().toString().trim();
        time = tv_match_time.getText().toString().trim();
        place = tv_match_place.getText().toString().trim();
        peopleCount = et_match_peopleCount.getText().toString().trim();
        MatchEntity entity = new MatchEntity();
        entity.setTitle(title);
        entity.setTime(time);
        entity.setPlace(place);
        entity.setPeopleCount(peopleCount);
        entity.setPushDistance(distance + "");
        Intent intent = new Intent(getActivity(), RewardCoinActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("match", entity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public LanucherMatchFragment() {
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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_lanucher_match, container, false);
        ButterKnife.inject(this, view);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                count = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (count >= 0 && count < 2) {
                    count = 0;
                    distance = 3;
                    DsncLog.e("distance", distance + "");
                    seekBar.setProgress(count);
                    tv_distance.setText(distance + "km");
                }
                if (count >= 2 && count < 4) {
                    count = 3;
                    DsncLog.e("distance", distance + "");
                    distance = 5;
                    seekBar.setProgress(count);
                    tv_distance.setText(distance + "km");
                }
                if (count >= 4) {
                    count = 6;
                    distance = 7;
                    DsncLog.e("distance", distance + "");
                    seekBar.setProgress(count);
                    tv_distance.setText(distance + "km");
                }
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == 1) {
            place = data.getStringExtra("place");
            DsncLog.e("place", place);
            tv_match_place.setText(place);
        }

    }
}
