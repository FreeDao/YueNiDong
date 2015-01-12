package com.yuenidong.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.CoachAddCommentFragment;
import com.yuenidong.fragment.VenuesAddCommentFragment;

import butterknife.InjectView;

/**
 * 石岩 教练添加评论
 */
public class CoachAddCommentActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showActionBarText(AppData.getString(R.string.addcomment));
        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_share_text);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = CoachAddCommentFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();
    }

}
