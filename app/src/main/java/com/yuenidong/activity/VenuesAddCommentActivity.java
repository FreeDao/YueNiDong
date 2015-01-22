package com.yuenidong.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.yuenidong.bean.VenuesCommentEntity;
import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.fragment.SetPasswordFragment;
import com.yuenidong.fragment.VenuesAddCommentFragment;
import com.yuenidong.fragment.VenuesCommentFragment;

import butterknife.InjectView;

/**
 * 石岩 添加评价
 */
public class VenuesAddCommentActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VenuesEntity entity= (VenuesEntity) getIntent().getSerializableExtra("venues");

        showActionBarText(AppData.getString(R.string.addcomment));
        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_share_text);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = VenuesAddCommentFragment.newInstance(entity);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }

}
