package com.yuenidong.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuenidong.app.AppManager;
import com.yuenidong.app.DsncLog;
import com.yuenidong.common.AppData;
import com.yuenidong.fragment.MapFragment;
import com.yuenidong.fragment.MatchFragment;
import com.yuenidong.fragment.MeFragment;
import com.yuenidong.fragment.MessageFragment;
import com.yuenidong.util.NetWorkState;
import com.yuenidong.util.NetWorkUtil;
import com.yuenidong.util.VibratorUtil;

import java.util.Observable;
import java.util.Observer;

public class MainActivity extends FragmentActivity implements View.OnClickListener, Observer {

    private String currentFragmentTag;

    private static final int MAP_SELECTED = 1 << 0;

    private static final int MATCH_SELECTED = 1 << 1;

    private static final int MESSAGE_SELECTED = 1 << 2;

    private static final int ME_SELECTED = 1 << 3;

    private static final String MAP_TAG = MapFragment.class.getName();

    private static final String MATCH_TAG = MatchFragment.class.getName();

    private static final String MESSAGE_TAG = MessageFragment.class.getName();

    private static final String ME_TAG = MeFragment.class.getName();

    private LinearLayout mTabMap;

    private LinearLayout mTabMatch;

    private LinearLayout mTabMessage;

    private LinearLayout mTabMe;

    private ImageView mMap;

    private ImageView mMatch;

    private ImageView mMessage;

    private ImageView mMe;

    private TextView mMapText;

    private TextView mMatchText;

    private TextView mMessageText;

    private TextView mMeText;

    private boolean isBackPressed = false;

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(MainActivity.this);
        NetWorkState.with().addObserver(this);
        findViews();
        if (getIntent().getBooleanExtra("isDirect", false)) {
            btn_login.setVisibility(View.VISIBLE);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            });
        }
        refreshTab(MAP_SELECTED);
        changeContentFragment(MAP_TAG);
        setStatusBarColor();
        if (!isGPSOpen()) {
            showOpenGPS();
        }
    }

    private void findViews() {
        btn_login = (Button) findViewById(R.id.btn_login);
        mTabMap = (LinearLayout) findViewById(R.id.tab_map);
        mTabMatch = (LinearLayout) findViewById(R.id.tab_match);
        mTabMessage = (LinearLayout) findViewById(R.id.tab_message);
        mTabMe = (LinearLayout) findViewById(R.id.tab_me);

        mTabMap.setOnClickListener(this);
        mTabMatch.setOnClickListener(this);
        mTabMessage.setOnClickListener(this);
        mTabMe.setOnClickListener(this);

        mMap = (ImageView) findViewById(R.id.map);
        mMatch = (ImageView) findViewById(R.id.match);
        mMessage = (ImageView) findViewById(R.id.message);
        mMe = (ImageView) findViewById(R.id.me);
        mMapText = (TextView) findViewById(R.id.map_text);
        mMatchText = (TextView) findViewById(R.id.match_text);
        mMessageText = (TextView) findViewById(R.id.message_text);
        mMeText = (TextView) findViewById(R.id.me_text);
    }

    private void refreshTab(final int whichSelected) {
        mMap.setBackgroundResource(R.drawable.main_map);
        mMatch.setBackgroundResource(R.drawable.main_match);
        mMessage.setBackgroundResource(R.drawable.main_message);
        mMe.setBackgroundResource(R.drawable.main_me);

        mMapText.setTextColor(AppData.getColor(R.color.tab_text_color));
        mMatchText.setTextColor(AppData.getColor(R.color.tab_text_color));
        mMessageText.setTextColor(AppData.getColor(R.color.tab_text_color));
        mMeText.setTextColor(AppData.getColor(R.color.tab_text_color));

        switch (whichSelected) {
            case MAP_SELECTED:
                mMap.setBackgroundResource(R.drawable.main_map_select);
                mMapText.setTextColor(AppData.getColor(R.color.tab_text_color_select));
                break;
            case MATCH_SELECTED:
                mMatch.setBackgroundResource(R.drawable.main_match_select);
                mMatchText.setTextColor(AppData.getColor(R.color.tab_text_color_select));
                break;
            case MESSAGE_SELECTED:
                mMessage.setBackgroundResource(R.drawable.main_message_select);
                mMessageText.setTextColor(AppData.getColor(R.color.tab_text_color_select));
                break;
            case ME_SELECTED:
                mMe.setBackgroundResource(R.drawable.main_me_select);
                mMeText.setTextColor(AppData.getColor(R.color.tab_text_color_select));
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            doublePressBackToast();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void doublePressBackToast() {
        if (!isBackPressed) {
            isBackPressed = true;
            Toast.makeText(this, "再次点击返回退出程序", Toast.LENGTH_SHORT).show();
        } else {
            AppManager.getAppManager().appExit();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isBackPressed = false;
            }
        }, 3000);
    }

    @Override
    public void update(Observable observable, Object data) {
        if (observable instanceof NetWorkState) {
            NetWorkUtil.NetWorkType netWorkType = (NetWorkUtil.NetWorkType) data;
            if (netWorkType == NetWorkUtil.NetWorkType.NETWORK_INVALID) {
                Toast.makeText(this, AppData.getString(R.string.error_conection), Toast.LENGTH_LONG)
                        .show();
                VibratorUtil.vibrate(VibratorUtil.SHORT_VIBRATE_DURATION);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_map:
                refreshTab(MAP_SELECTED);
                changeContentFragment(MAP_TAG);
                break;
            case R.id.tab_match:
                refreshTab(MATCH_SELECTED);
                changeContentFragment(MATCH_TAG);
                break;
            case R.id.tab_message:
                refreshTab(MESSAGE_SELECTED);
                changeContentFragment(MESSAGE_TAG);
                break;
            case R.id.tab_me:
                refreshTab(ME_SELECTED);
                changeContentFragment(ME_TAG);
                break;
        }
    }

    private void changeContentFragment(String tag) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (tag.equals(MAP_TAG)) {
            currentFragmentTag = MAP_TAG;
            fragmentTransaction.replace(R.id.layout_container, getFragment(MAP_TAG));
        }
        if (tag.equals(MATCH_TAG)) {
            currentFragmentTag = MATCH_TAG;
            fragmentTransaction.replace(R.id.layout_container, getFragment(MATCH_TAG));
        }
        if (tag.equals(MESSAGE_TAG)) {
            currentFragmentTag = MESSAGE_TAG;
            fragmentTransaction.replace(R.id.layout_container, getFragment(MESSAGE_TAG));
        }
        if (tag.equals(ME_TAG)) {
            currentFragmentTag = ME_TAG;
            fragmentTransaction.replace(R.id.layout_container, getFragment(ME_TAG));
        }

        fragmentTransaction.commit();
    }

    private Fragment getFragment(String tag) {
        Fragment fragment = findFragment(tag);
        if (fragment == null) {
            if (MAP_TAG.equals(tag)) {
                fragment = MapFragment.newInstance();
            }
            if (MATCH_TAG.equals(tag)) {
                fragment = MatchFragment.newInstance();
            }
            if (MESSAGE_TAG.equals(tag)) {
                fragment = MessageFragment.newInstance();
            }
            if (ME_TAG.equals(tag)) {
                fragment = MeFragment.newInstance();
            }
        }
        return fragment;
    }

    private Fragment findFragment(String tag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
        return fragment;
    }

    private boolean isGPSOpen() {
        LocationManager locationManager = (LocationManager) AppData.getContext().getSystemService(
                Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps) {
            return true;
        }
        return false;
    }

    private void openGPS() {
        Intent GPSIntent = new Intent(Settings.ACTION_SETTINGS);
        startActivity(GPSIntent);
    }

    private void showOpenGPS() {
        new AlertDialog.Builder(this).setTitle("打开GPS").setMessage("为了提升体验，建议您打开GPS")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openGPS();
                    }
                }).setNegativeButton("否", null).show();
    }

    @TargetApi(19)
    protected void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // create our manager instance after the content view is set
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
//            tintManager.setNavigationBarTintEnabled(true);
            // set a custom tint color for all system bars
            tintManager.setTintColor(Color.parseColor("#7CFC00"));
// set a custom navigation bar resource
//            tintManager.setNavigationBarTintResource(R.drawable.ic_launcher);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
