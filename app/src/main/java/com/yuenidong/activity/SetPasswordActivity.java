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

import com.yuenidong.bean.UserEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.fragment.SetPasswordFragment;

import butterknife.InjectView;


public class SetPasswordActivity extends BaseActivity {
    private UserEntity user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBarText(AppData.getString(R.string.setpassword));
        if(getIntent()!=null) {
            user = (UserEntity) getIntent().getExtras().getSerializable("user");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = SetPasswordFragment.newInstance(user);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }

}
