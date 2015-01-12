package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.Button;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.VenuesAddSignFragment;

import butterknife.InjectView;

/**
 * 石岩  添加签到
 */
public class VenuesAddSignActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showActionBarText(AppData.getString(R.string.addsign));

        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_share_text);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = VenuesAddSignFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }
}
