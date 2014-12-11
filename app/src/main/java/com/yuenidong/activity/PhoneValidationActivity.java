package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.PhoneValidationFragment;

/**
 * 石岩   手机验证
 */
public class PhoneValidationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean isReset = getIntent().getBooleanExtra("isReset", false);
        showActionBarText(AppData.getString(R.string.phonevalidation));
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = PhoneValidationFragment.newInstance(isReset);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }
}
