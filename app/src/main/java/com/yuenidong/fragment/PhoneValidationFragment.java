package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.yuenidong.activity.R;
import com.yuenidong.activity.ResetPasswordActivity;
import com.yuenidong.activity.SetPasswordActivity;
import com.yuenidong.common.AppData;

import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class PhoneValidationFragment extends Fragment {
    private boolean isReset;

    private int time_residual = 60;
    @InjectView(R.id.et_phone)
    EditText et_phone;
    @InjectView(R.id.et_validation)
    EditText et_validation;
    @InjectView(R.id.iv_phone)
    ImageView iv_phone;
    @InjectView(R.id.iv_validationCode)
    ImageView iv_validationCode;
    @InjectView(R.id.btn_validation)
    Button btn_validation;

    //获取验证码
    @OnClick(R.id.btn_validation)
    void getValidation() {
        updateTextTimer();
    }

    //下一步
    @OnClick(R.id.btn_nextStep)
    void nextStep() {
        if (isReset) {
            Intent intent = new Intent(getActivity(), ResetPasswordActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), SetPasswordActivity.class);
            startActivity(intent);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    btn_validation.setText(time_residual + "s");
                    break;

                case 2:
                    time_residual = 60;
                    btn_validation.setEnabled(true);
                    btn_validation.setText(AppData.getString(R.string.getvalidation));
                    break;
            }
        }
    };

    public static PhoneValidationFragment newInstance(boolean isReset) {
        PhoneValidationFragment fragment = new PhoneValidationFragment();
        Bundle args = new Bundle();
        args.putBoolean("isReset", isReset);
        fragment.setArguments(args);
        return fragment;
    }

    public PhoneValidationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isReset = getArguments().getBoolean("isReset");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_validation, container, false);
        ButterKnife.inject(this, view);
        et_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // 此处为得到焦点时的处理内容
                if (hasFocus) {
                    et_phone.setBackgroundResource(R.drawable.text_line_selected);
                    iv_phone.setImageResource(R.drawable.ic_telephone_selected);
                } else {
                    et_phone.setBackgroundResource(R.drawable.text_line);
                    iv_phone.setImageResource(R.drawable.ic_telephone);
                }
            }
        });
        et_validation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // 此处为得到焦点时的处理内容
                if (hasFocus) {
                    et_validation.setBackgroundResource(R.drawable.text_line_selected);
                    iv_validationCode.setImageResource(R.drawable.ic_validation_selected);
                } else {
                    et_validation.setBackgroundResource(R.drawable.text_line);
                    iv_validationCode.setImageResource(R.drawable.ic_validation);
                }
            }
        });

        return view;
    }

    private void updateTextTimer() {
        btn_validation.setEnabled(false);
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                time_residual--;
                if (time_residual >= 0) {
                    message.what = 1;
                    handler.sendMessage(message);
                } else {
                    timer.cancel();
                    message.what = 2;
                    handler.sendMessage(message);
                }
            }
        }, 0, 1000);
    }
}
