package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.ComeRecentFragment;


public class ComeRecentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String coachId=getIntent().getStringExtra("coachId");
        showActionBarText(AppData.getString(R.string.comerecent));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = ComeRecentFragment.newInstance(coachId);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();


    }
}
