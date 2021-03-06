package com.yuenidong.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.bean.CoachEntity;
import com.yuenidong.fragment.CoachFragment;
import com.yuenidong.fragment.CoachInfoFragment;


public class CoachInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CoachEntity entity= (CoachEntity) getIntent().getSerializableExtra("coach");
        hideActionbar();

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = CoachInfoFragment.newInstance(entity);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }


}
