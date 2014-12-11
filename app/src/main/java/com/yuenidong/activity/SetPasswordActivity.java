package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.SetPasswordFragment;

import butterknife.InjectView;


public class SetPasswordActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBarText(AppData.getString(R.string.setpassword));

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = SetPasswordFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }

}
