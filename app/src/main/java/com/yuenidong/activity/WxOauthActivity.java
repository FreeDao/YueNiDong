package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.SinaOauthFragment;
import com.yuenidong.fragment.WxOauthFragment;


public class WxOauthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBarText(AppData.getString(R.string.wxlogin));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = WxOauthFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }
}
