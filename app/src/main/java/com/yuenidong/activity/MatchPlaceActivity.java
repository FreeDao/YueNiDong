package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.MatchInfoFragment;
import com.yuenidong.fragment.MatchPlaceFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MatchPlaceActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button btn_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        showActionBarText(AppData.getString(R.string.match_place));
        btn_right.setBackgroundResource(R.drawable.button_press);
        btn_right.setVisibility(View.VISIBLE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = MatchPlaceFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container,fragment);
        fragmentTransaction.commit();
    }
}
