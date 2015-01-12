package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.bean.MatchEntity;
import com.yuenidong.fragment.PushMapFragment;

/**
 * 推送活动(地图)
 */
public class PushMapActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionbar();
        MatchEntity entity= (MatchEntity) getIntent().getSerializableExtra("match");
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        Fragment fragment= PushMapFragment.newInstance(entity);
        fragmentTransaction.replace(R.id.layout_container,fragment);
        fragmentTransaction.commit();

    }


}
