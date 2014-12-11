package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.fragment.FirstFragment;
import com.yuenidong.fragment.ResetPasswordFragment;


public class FirstActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_first);

        hideActionbar();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = FirstFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }

}
