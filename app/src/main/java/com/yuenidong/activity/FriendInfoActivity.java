package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.bean.FriendEntity;
import com.yuenidong.fragment.FriendInfoFragment;


public class FriendInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideActionbar();
        FriendEntity entity= (FriendEntity) getIntent().getSerializableExtra("user");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = FriendInfoFragment.newInstance(entity);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }


}
