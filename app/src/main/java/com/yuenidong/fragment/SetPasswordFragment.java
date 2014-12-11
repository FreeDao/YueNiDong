package com.yuenidong.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.yuenidong.activity.MainActivity;
import com.yuenidong.activity.R;
import com.yuenidong.common.PreferenceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SetPasswordFragment extends Fragment {
    boolean isShowPassword = false;
    @InjectView(R.id.et_password)
    EditText et_password;
    @InjectView(R.id.iv_password)
    ImageView iv_password;
    @InjectView(R.id.iv_showpassword)
    ImageView iv_showpassword;

    @OnClick(R.id.btn_finish)
    void finish() {
        PreferenceUtil.setPreBoolean("isFirstLogin", false);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    public static SetPasswordFragment newInstance() {
        SetPasswordFragment fragment = new SetPasswordFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_set_password, container, false);
        ButterKnife.inject(this, view);
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                // 此处为得到焦点时的处理内容
                if (hasFocus) {
                    et_password.setBackgroundResource(R.drawable.text_line_selected);
                    iv_password.setImageResource(R.drawable.ic_password_selected);
                } else {
                    et_password.setBackgroundResource(R.drawable.text_line);
                    iv_password.setImageResource(R.drawable.ic_password);
                }
            }
        });
        iv_showpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShowPassword) {
                    iv_showpassword.setImageResource(R.drawable.ic_showpassword_select);
                    isShowPassword = true;
                    et_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    et_password.setSelection(et_password.getText().toString().trim().length());
                } else {
                    iv_showpassword.setImageResource(R.drawable.ic_showpassword);
                    isShowPassword = false;
                    et_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    et_password.setSelection(et_password.getText().toString().trim().length());
                }
            }
        });
        return view;
    }


}
