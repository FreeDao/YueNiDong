package com.yuenidong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;

import com.yuenidong.common.AppData;
import com.yuenidong.fragment.CoachCommentFragment;
import com.yuenidong.fragment.CoachInfoFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩 教练评价
 */
public class CoachCommentActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;
    String coachId;

    //教练评价
    @OnClick(R.id.actionbar_rightbutton)
    void comment() {
        Intent intent = new Intent(this, CoachAddCommentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("coachId",coachId);
        AppData.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         coachId=getIntent().getStringExtra("coachId");
        showActionBarText(AppData.getString(R.string.coachcomment));
        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_add_venues);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = CoachCommentFragment.newInstance(coachId);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();


    }
}
