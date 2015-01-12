package com.yuenidong.activity;


import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeNotify;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeUser;
import com.gotye.api.listener.ChatListener;
import com.gotye.api.listener.DownloadListener;
import com.gotye.api.listener.GroupListener;
import com.gotye.api.listener.NotifyListener;
import com.gotye.api.listener.PlayListener;
import com.gotye.api.listener.RoomListener;
import com.gotye.api.listener.UserListener;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.yuenidong.app.AppManager;
import com.yuenidong.common.AppData;
import com.yuenidong.util.CommonUtils;
import com.yuenidong.util.SmartBarUtils;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class BaseActivity extends FragmentActivity implements ChatListener,
        DownloadListener, GroupListener, UserListener, RoomListener, PlayListener, NotifyListener {
    @InjectView(R.id.actionbar_left)
    ImageView iv_left;
    @InjectView(R.id.actionbar_text)
    TextView tv_title;
    @InjectView(R.id.actionbar_right)
    TextView tv_right;
    @InjectView(R.id.layout_actionbar)
    RelativeLayout rl_actionbar;
    @InjectView(R.id.ll_base)
    LinearLayout ll_base;
    @InjectView(R.id.actionbar_rightbutton)
    Button btn_right;
    @InjectView(R.id.actionbar_rightbutton2)
    Button btn_right2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_base);
        ButterKnife.inject(this);
        AppManager.getAppManager().addActivity(this);
        setStatusBarColor();
//        SmartBarUtils.hide(AppData.getContext(),this.getWindow());
    }

    //隐藏actionbar
    protected void hideActionbar() {
        rl_actionbar.setVisibility(View.GONE);
    }

    //显示标题
    protected void showActionBarText(String str) {
        tv_title.setText(str);
        tv_title.setVisibility(View.VISIBLE);
    }

    //显示acitonbar左侧图标
    protected void showActionBarLeft(int a) {
        iv_left.setImageResource(a);
        iv_left.setVisibility(View.VISIBLE);
    }

    //显示actionbar右侧内容
    protected void showActionBarRight(String str) {
        tv_right.setText(str);
        tv_right.setVisibility(View.VISIBLE);
    }

    //显示actionbar右侧图片按钮
    protected void showActionBarRightButton() {
        btn_right.setVisibility(View.VISIBLE);
    }

    @Override
    public void startActivity(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    //显示actionbar右侧第二个图片按钮
    protected void showActionBarRightButton2(int a) {
        btn_right2.setVisibility(View.VISIBLE);
        btn_right2.setBackgroundResource(a);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity();
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

    public GotyeAPI api = GotyeAPI.getInstance();

    @Override
    public void onPlayStart(int code, GotyeMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlaying(int code, int position) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayStop(int code) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPlayStartReal(int code, long roomId, String who) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEnterRoom(int code, long lastMsgID, GotyeRoom room) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLeaveRoom(int code, GotyeRoom room) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetRoomList(int code, List<GotyeRoom> gotyeroom) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetRoomMemberList(int code, GotyeRoom room,
                                    List<GotyeUser> totalMembers, List<GotyeUser> currentPageMembers,
                                    int pageIndex) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetHistoryMessageList(int code, List<GotyeMessage> list) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestRoomInfo(int code, GotyeRoom room) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestUserInfo(int code, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onModifyUserInfo(int code, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSearchUserList(int code, List<GotyeUser> mList, int pagerIndex) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAddFriend(int code, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetFriendList(int code, List<GotyeUser> mList) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onAddBlocked(int code, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRemoveFriend(int code, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRemoveBlocked(int code, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetBlockedList(int code, List<GotyeUser> mList) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetProfile(int code, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreateGroup(int code, GotyeGroup group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onJoinGroup(int code, GotyeGroup group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onLeaveGroup(int code, GotyeGroup group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDismissGroup(int code, GotyeGroup group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onKickOutUser(int code, GotyeGroup group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetGroupList(int code, List<GotyeGroup> grouplist) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRequestGroupInfo(int code, GotyeGroup group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetGroupMemberList(int code, List<GotyeUser> allList,
                                     List<GotyeUser> curList, GotyeGroup group, int pagerIndex) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveGroupInvite(int code, GotyeGroup group,
                                     GotyeUser sender, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetOfflineMessageList(int code, List<GotyeMessage> messagelist) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSearchGroupList(int code, List<GotyeGroup> mList,
                                  List<GotyeGroup> curList, int pageIndex) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onModifyGroupInfo(int code, GotyeGroup gotyeGroup) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onChangeGroupOwner(int code, GotyeGroup group) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUserJoinGroup(GotyeGroup group, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUserLeaveGroup(GotyeGroup group, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUserDismissGroup(GotyeGroup group, GotyeUser user) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUserKickdFromGroup(GotyeGroup group, GotyeUser kicked,
                                     GotyeUser actor) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDownloadMedia(int code, String path, String url) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendMessage(int code, GotyeMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveMessage(int code, GotyeMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDownloadMessage(int code, GotyeMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReleaseMessage(int code) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReport(int code, GotyeMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStartTalk(int code, boolean isRealTime, int targetType,
                            GotyeChatTarget target) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onStopTalk(int code, GotyeMessage message, boolean isVoiceReal) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveMessage(int code, GotyeMessage message, boolean unRead) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onNotifyStateChanged() {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDecodeMessage(int code, GotyeMessage message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSendNotify(int code, GotyeNotify notify) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveNotify(int code, GotyeNotify notify) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveRequestJoinGroup(int code, GotyeGroup group,
                                          GotyeUser sender, String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReceiveReplayJoinGroup(int code, GotyeGroup group,
                                         GotyeUser sender, String message, boolean isAgree) {
        // TODO Auto-generated method stub

    }





}
