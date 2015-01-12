package com.yuenidong.activity;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.yuenidong.bean.MyUserEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.fragment.ChooseLabelFragment;
import com.yuenidong.fragment.MatchInfoFragment;

import butterknife.InjectView;


public class ChooseLabelActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button btn_right;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showActionBarText(AppData.getString(R.string.chooselabel));
        showActionBarRightButton();
        btn_right.setBackgroundColor(Color.TRANSPARENT);
        btn_right.setText("完成");
        MyUserEntity user= (MyUserEntity) getIntent().getExtras().getSerializable("user");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = ChooseLabelFragment.newInstance(user);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }
}
