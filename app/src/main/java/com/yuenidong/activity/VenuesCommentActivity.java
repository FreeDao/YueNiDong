package com.yuenidong.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.yuenidong.bean.VenuesEntity;
import com.yuenidong.common.AppData;
import com.yuenidong.fragment.VenuesCommentFragment;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 石岩  场馆评论
 */
public class VenuesCommentActivity extends BaseActivity {
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;
    private VenuesEntity entity;

    //添加场馆评论
    @OnClick(R.id.actionbar_rightbutton)
    void comment() {
        Intent intent = new Intent(this, VenuesAddCommentActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle=new Bundle();
        bundle.putSerializable("venues",entity);
        intent.putExtras(bundle);
        AppData.getContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        entity = (VenuesEntity) getIntent().getSerializableExtra("user");
        showActionBarText(AppData.getString(R.string.venuescomment));
        showActionBarRightButton();
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_add_venues);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = VenuesCommentFragment.newInstance(entity);
        fragmentTransaction.replace(R.id.layout_container, fragment);
        fragmentTransaction.commit();

    }
}
