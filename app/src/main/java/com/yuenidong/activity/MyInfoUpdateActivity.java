package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.bean.MyUserEntity;
import com.yuenidong.fragment.MyInfoUpdateFragment;


public class MyInfoUpdateActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionbar();
        MyUserEntity user= (MyUserEntity) getIntent().getExtras().getSerializable("user");
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = MyInfoUpdateFragment.newInstance(user);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }

}
