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
import android.widget.Toast;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.VenuesInfoFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 场馆信息
 */
public class VenuesInfoActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;


    //收藏
    @OnClick(R.id.actionbar_rightbutton)
    void collent() {
        Toast.makeText(this, "收藏成功!", Toast.LENGTH_SHORT).show();
    }

    //分享
    @OnClick(R.id.actionbar_rightbutton2)
    void share() {
        Toast.makeText(this, "分享成功!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBarText(AppData.getString(R.string.venues));
        showActionBarRightButton2(R.drawable.button_share_venues);
        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_collect_venues);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = VenuesInfoFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }

}
