package com.yuenidong.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.VenuesSignFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩  场馆签到
 */
public class VenuesSignActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;

    //添加签到
    @OnClick(R.id.actionbar_rightbutton)
    void sign() {
        Intent intent = new Intent(this, VenuesAddSignActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppData.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBarText(AppData.getString(R.string.venuessign));
        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_add_venues);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = VenuesSignFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }

}
