package com.yuenidong.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeStatusCode;
import com.gotye.api.GotyeUser;
import com.gotye.api.PathUtil;
import com.gotye.api.listener.LoginListener;
import com.igexin.sdk.PushManager;
import com.qinjia.util.BitmapUtil;
import com.qinjia.util.FileUtil;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuenidong.app.AppManager;
import com.yuenidong.app.DsncLog;
import com.yuenidong.app.RequestManager;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;
import com.yuenidong.constants.YueNiDongConstants;
import com.yuenidong.fragment.MapFragment;
import com.yuenidong.fragment.MatchFragment;
import com.yuenidong.fragment.MeFragment;
import com.yuenidong.fragment.MessageFragment;
import com.yuenidong.service.GetLatLngService;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.Commvert;
import com.yuenidong.util.NetWorkState;
import com.yuenidong.util.NetWorkUtil;
import com.yuenidong.util.VibratorUtil;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class MainActivity extends FragmentActivity implements View.OnClickListener, Observer, LoginListener {

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


    //亲加通信云参数
    public static final String APPKEY = "606dd011-c427-4654-b685-abaa2576bba7";
    public static final String PACKAGENAME = "com.yuenidong.activity";
    GotyeAPI api;
    String name = "wusong";
    String password = "wusong";
    int code;
    private TextView msgTip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, GetLatLngService.class);
        startService(intent);
        //修改cid lat lng
        if ((!TextUtils.isEmpty(PreferenceUtil.getPreString("userId", "")) && (!TextUtils.isEmpty(PreferenceUtil.getPreString("cid", ""))))) {
            final HashMap<String, String> map = new HashMap<String, String>();
            map.put("lat", PreferenceUtil.getPreString("latitude", ""));
            map.put("lng", PreferenceUtil.getPreString("longtitude", ""));
            map.put("cid", PreferenceUtil.getPreString("cid", ""));
            map.put("userId", PreferenceUtil.getPreString("userId", ""));
            JSONObject jsonObject = null;
            try {
                String str = CommonUtils.hashMapToJson(map);
                jsonObject = new JSONObject(str);
                DsncLog.e("jsonObject", jsonObject.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonRequest<JSONObject> jsonRequest = new JsonObjectRequest(
                    Request.Method.POST, YueNiDongConstants.UPDATE_CLL, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("修改cid success", response.toString());
                            Commvert commvert = new Commvert(response);
                            Toast.makeText(MainActivity.this, commvert.getString("msg"), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("error", error.toString());
                }
            }) {

                @Override
                public Map<String, String> getHeaders() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Accept", "application/json");
                    headers.put("Content-Type", "application/json; charset=UTF-8");
                    return headers;
                }
            };
            RequestManager.addRequest(jsonRequest, this);
        }

        PreferenceUtil.getPreBoolean("isFirstLogin", false);
        //初始化个推推送
        PushManager.getInstance().initialize(this.getApplicationContext());
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
//        setStatusBarColor();
        if (!isGPSOpen()) {
            showOpenGPS();
        }

        //--------亲加-------------
        api = GotyeAPI.getInstance();
        api.init(this, APPKEY, PACKAGENAME);
        // 登录的时候要传入登录监听，当重复登录时会直接返回登录状态
        GotyeAPI.getInstance().addListerer(MainActivity.this);
        boolean isLogin = api.isOnline();
        DsncLog.e("login", isLogin + "");
        code = api.login(name, password);
        DsncLog.e("code", code + "");
//        beep = new BeepManager(MainActivity.this);
//        beep.updatePrefs();
//        // 清理掉通知栏
//        clearNotify();

        msgTip = (TextView) findViewById(R.id.msgTip);
        if (!TextUtils.isEmpty(PreferenceUtil.getPreString("unreadCount", ""))) {
            msgTip.setVisibility(View.VISIBLE);
            DsncLog.e("msgTip", PreferenceUtil.getPreString("unreadCount", ""));
            msgTip.setText(PreferenceUtil.getPreString("unreadCount", ""));
        }

        if (PreferenceUtil.getPreBoolean("notice", true)) {
            refreshTab(MESSAGE_SELECTED);
            changeContentFragment(MESSAGE_TAG);
        }
        setStatusBarColor();
    }

    // 更新提醒
    public void updateUnReadTip() {
        int unreadCount = api.getTotalUnreadMsgCount();
        int unreadNotifyCount = api.getUnreadNotifyCount();
        unreadCount += unreadNotifyCount;
//		msgTip.setVisibility(View.VISIBLE);
//		if (unreadCount > 0 && unreadCount < 100) {
//			msgTip.setText(String.valueOf(unreadCount));
//		} else if (unreadCount >= 100) {
//			msgTip.setText("99");
//		} else {
//			msgTip.setVisibility(View.GONE);
//		}
    }

//    // 页面刷新
//    private void mainRefresh() {
//        updateUnReadTip();
//        messageFragment.refresh();
//        // if (contactsFragment != null) {
//        // contactsFragment.refresh();
//        // }
//
//    }

    // 收到消息（此处只是单纯的更新聊天历史界面，不涉及聊天消息处理，当然你也可以处理，若你非要那样做）
//    @Override
//    public void onReceiveMessage(int code, GotyeMessage message, boolean unRead) {
//        if (returnNotify) {
//            return;
//        }
//        messageFragment.refresh();
//        if (unRead) {
//            updateUnReadTip();
//
//            if (!api.isNewMsgNotify()) {
//                return;
//            }
//            if (message.getReceiverType() == 2) {
//                if (api.isNotReceiveGroupMsg()) {
//                    return;
//                }
//                if (api.isGroupDontdisturb(((GotyeGroup) message.getReceiver())
//                        .getGroupID())) {
//                    return;
//                }
//            }
//            beep.playBeepSoundAndVibrate();
//        }
//    }
//
//    // 自己发送的信息统一在此处理
//    @Override
//    public void onSendMessage(int code, GotyeMessage message) {
//        if (returnNotify) {
//            return;
//        }
//        messageFragment.refresh();
//    }
//
//    // 收到群邀请信息
//    @Override
//    public void onReceiveNotify(int code, GotyeNotify notify) {
//        if (returnNotify) {
//            return;
//        }
//        messageFragment.refresh();
//        updateUnReadTip();
//        if (!api.isNotReceiveGroupMsg()) {
//            beep.playBeepSoundAndVibrate();
//        }
//    }
//
//    @Override
//    public void onRemoveFriend(int code, GotyeUser user) {
//        if (returnNotify) {
//            return;
//        }
//        api.deleteSession(user);
//        messageFragment.refresh();
//        // contactsFragment.refresh();
//    }
//
//    @Override
//    public void onAddFriend(int code, GotyeUser user) {
//        // TODO Auto-generated method stub
//        if (returnNotify) {
//            return;
//        }
//        if (currentPosition == 1) {
//            // contactsFragment.refresh();
//        }
//    }
//
//    @Override
//    public void onNotifyStateChanged() {
//        // TODO Auto-generated method stub
//        mainRefresh();
//    }

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
                mTabMap.setEnabled(false);
                mTabMatch.setEnabled(true);
                mTabMessage.setEnabled(true);
                mTabMe.setEnabled(true);
                break;
            case MATCH_SELECTED:
                mMatch.setBackgroundResource(R.drawable.main_match_select);
                mMatchText.setTextColor(AppData.getColor(R.color.tab_text_color_select));
                mTabMap.setEnabled(true);
                mTabMatch.setEnabled(false);
                mTabMessage.setEnabled(true);
                mTabMe.setEnabled(true);
                break;
            case MESSAGE_SELECTED:
                mMessage.setBackgroundResource(R.drawable.main_message_select);
                mMessageText.setTextColor(AppData.getColor(R.color.tab_text_color_select));
                mTabMap.setEnabled(true);
                mTabMatch.setEnabled(true);
                mTabMessage.setEnabled(false);
                mTabMe.setEnabled(true);
                break;
            case ME_SELECTED:
                mMe.setBackgroundResource(R.drawable.main_me_select);
                mMeText.setTextColor(AppData.getColor(R.color.tab_text_color_select));
                mTabMap.setEnabled(true);
                mTabMatch.setEnabled(true);
                mTabMessage.setEnabled(true);
                mTabMe.setEnabled(false);
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
        if(Build.MANUFACTURER.equalsIgnoreCase("meizu")){
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window=getWindow();
            WindowManager.LayoutParams params=window.getAttributes();
            params.flags|= WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            window.setAttributes(params);
            // create our manager instance after the content view is set
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            // enable status bar tint
            tintManager.setStatusBarTintEnabled(true);
            // enable navigation bar tint
//            tintManager.setNavigationBarTintEnabled(true);
            // set a custom tint color for all system bars
            tintManager.setTintColor(Color.parseColor("#0ac896"));
// set a custom navigation bar resource
//            tintManager.setNavigationBarTintResource(R.drawable.ic_launcher);
            findViewById(android.R.id.content).setPadding(0, CommonUtils.getStatusBarHeight(AppData.getContext()), 0, 0);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出登录
        if (!TextUtils.isEmpty(code + "")) {
            onLogout(code);
        }
        api.removeListener(this);

    }


    @Override
    public void onLogin(int i, GotyeUser gotyeUser) {
        // 判断登陆是否成功
        if (code == GotyeStatusCode.CODE_OK) {
            DsncLog.e("login", api.isOnline() + "");
//			GotyeUser user2 = new GotyeUser("shiyan");
//			Intent intent = new Intent(this, ChatPage.class);
//			intent.putExtra("user", user2);
//			startActivity(intent);
//            Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        } else {
            // 失败,可根据code定位失败原因
//            Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
        }
    }


    // 此处处理账号在另外设备登陆造成的被动下线
    @Override
    public void onLogout(int code) {
        if (code == GotyeStatusCode.CODE_FORCELOGOUT) {
//            Toast.makeText(this, "您的账号在另外一台设备上登录了！", Toast.LENGTH_SHORT).show();
        } else if (code == GotyeStatusCode.CODE_NETWORD_DISCONNECTED) {
//            Toast.makeText(this, "您的账号掉线了！", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "退出登陆！", Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            int tab = intent.getIntExtra("tab", -1);
            if (tab == 1) {
                // contactsFragment.refresh();
            }
            int notify = intent.getIntExtra("notify", 0);
            if (notify == 1) {
                clearNotify();
            }

            int selection_index = intent.getIntExtra("selection_index", -1);
            if (selection_index == 1) {
                // setTabSelection(1);
            }
        }

    }

    private void clearNotify() {
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 选取图片的返回值
        if (resultCode == RESULT_OK) {
            if (data != null) {
                Uri selectedImage = data.getData();
                if (selectedImage != null) {
                    String path = FileUtil.uriToPath(this, selectedImage);
                    setPicture(path);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicture(String path) {
        File f = new File(PathUtil.getAppFIlePath());
        if (!f.isDirectory()) {
            f.mkdirs();
        }
        File file = new File(PathUtil.getAppFIlePath()
                + System.currentTimeMillis() + "jpg");
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Bitmap smaillBit = BitmapUtil.getSmallBitmap(path, 50, 50);
        String smallPath = BitmapUtil.saveBitmapFile(smaillBit);
        // settingFragment.modifyUserIcon(smallPath);
    }

}
