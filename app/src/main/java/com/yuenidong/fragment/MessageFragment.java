package com.yuenidong.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeMessage;
import com.gotye.api.GotyeNotify;
import com.gotye.api.GotyeUser;
import com.gotye.api.listener.NotifyListener;
import com.qinjia.fragment.MessageQinFragment;
import com.qinjia.util.BeepManager;
import com.yuenidong.activity.AddFriendActivity;
import com.yuenidong.activity.R;
import com.yuenidong.common.AppData;
import com.yuenidong.common.PreferenceUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MessageFragment extends Fragment implements NotifyListener {
    GotyeAPI api;
    @InjectView(R.id.actionbar_rightbutton)
    Button actionbar_rightbutton;

    @InjectView(R.id.actionbar_text)
    TextView tv_title;
    @InjectView(R.id.tv_message)
    TextView tv_message;
    @InjectView(R.id.tv_friends)
    TextView tv_friends;
    @InjectView(R.id.view_message)
    View view_message;
    @InjectView(R.id.view_message_select)
    View view_message_select;
    @InjectView(R.id.view_friends)
    View view_friends;
    @InjectView(R.id.view_friends_select)
    View view_friends_select;
    @InjectView(R.id.tv_notice)
    TextView tv_notice;
    @InjectView(R.id.view_notice)
    View view_notice;
    @InjectView(R.id.view_notice_select)
    View view_notice_select;

    private TextView msgTip;
    private int currentPosition = 0;
    private BeepManager beep;
    private boolean returnNotify = false;
    MessageQinFragment messageFragment;

    //消息
    @OnClick(R.id.rl_message)
    void message() {
        tv_message.setTextColor(AppData.getColor(R.color.green));
        tv_friends.setTextColor(AppData.getColor(R.color.grayblack));
        tv_notice.setTextColor(AppData.getColor(R.color.grayblack));
        view_message_select.setVisibility(View.VISIBLE);
        view_message.setVisibility(View.GONE);
        view_friends.setVisibility(View.VISIBLE);
        view_friends_select.setVisibility(View.GONE);
        view_notice.setVisibility(View.VISIBLE);
        view_notice_select.setVisibility(View.GONE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        messageFragment = MessageQinFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, messageFragment);
        fragmentTransaction.commit();
    }

    //好友
    @OnClick(R.id.rl_friends)
    void friends() {
        tv_friends.setTextColor(AppData.getColor(R.color.green));
        tv_message.setTextColor(AppData.getColor(R.color.grayblack));
        tv_notice.setTextColor(AppData.getColor(R.color.grayblack));
        view_friends_select.setVisibility(View.VISIBLE);
        view_message_select.setVisibility(View.GONE);
        view_message.setVisibility(View.VISIBLE);
        view_friends.setVisibility(View.GONE);
        view_notice.setVisibility(View.VISIBLE);
        view_notice_select.setVisibility(View.GONE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = MessageFriendFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    //通知
    @OnClick(R.id.rl_notice)
    void notice() {
        tv_notice.setTextColor(AppData.getColor(R.color.green));
        tv_message.setTextColor(AppData.getColor(R.color.grayblack));
        tv_friends.setTextColor(AppData.getColor(R.color.grayblack));
        view_friends.setVisibility(View.VISIBLE);
        view_friends_select.setVisibility(View.GONE);
        view_message_select.setVisibility(View.GONE);
        view_message.setVisibility(View.VISIBLE);
        view_notice.setVisibility(View.GONE);
        view_notice_select.setVisibility(View.VISIBLE);

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = MessageNoticeFragment.newInstance();
        fragmentTransaction.replace(R.id.layout_fragment_container, fragment);
        fragmentTransaction.commit();
    }

    //添加
    @OnClick(R.id.actionbar_rightbutton)
    void add() {
        Intent intent = new Intent(getActivity(), AddFriendActivity.class);
        startActivity(intent);
    }

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MessageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.inject(this, view);
        msgTip = (TextView) getActivity().findViewById(R.id.msgTip);
        tv_title.setText(AppData.getString(R.string.main_message));
        tv_title.setVisibility(View.VISIBLE);
        message();
        actionbar_rightbutton.setVisibility(View.VISIBLE);
        actionbar_rightbutton.setBackgroundResource(R.drawable.button_add_venues);
        api = GotyeAPI.getInstance();
        api.addListerer(MessageFragment.this);
//        GotyeAPI.getInstance().addListerer((com.gotye.api.listener.GotyeListener) getActivity());
        beep = new BeepManager(getActivity());
        beep.updatePrefs();
        // 清理掉通知栏
        clearNotify();
        if (PreferenceUtil.getPreBoolean("notice", true)) {
            notice();
            PreferenceUtil.setPreBoolean("notice", false);
        }
        return view;
    }

    private void clearNotify() {
        NotificationManager notificationManager = (NotificationManager) getActivity()
                .getSystemService(getActivity().NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    // 页面刷新
    private void mainRefresh() {
        updateUnReadTip();
        messageFragment.refresh();
        // if (contactsFragment != null) {
        // contactsFragment.refresh();
        // }

    }

    // 更新提醒
    public void updateUnReadTip() {
        int unreadCount = api.getTotalUnreadMsgCount();
        int unreadNotifyCount = api.getUnreadNotifyCount();
        unreadCount += unreadNotifyCount;
        msgTip.setVisibility(View.VISIBLE);
        if (unreadCount > 0 && unreadCount < 100) {
            msgTip.setText(String.valueOf(unreadCount));
            //自己添加功能
            PreferenceUtil.setPreString("unreadCount", unreadCount + "");
        } else if (unreadCount >= 100) {
            msgTip.setText("99");
            PreferenceUtil.setPreString("unreadCount", "99");
        } else {
            msgTip.setVisibility(View.GONE);
            PreferenceUtil.setPreString("unreadCount", "");
        }
    }

    @Override
    public void onReceiveMessage(int code, GotyeMessage message, boolean unRead) {
        if (returnNotify) {
            return;
        }
        messageFragment.refresh();
        if (unRead) {
            updateUnReadTip();

            if (!api.isNewMsgNotify()) {
                return;
            }
            if (message.getReceiverType() == 2) {
                if (api.isNotReceiveGroupMsg()) {
                    return;
                }
                if (api.isGroupDontdisturb(((GotyeGroup) message.getReceiver())
                        .getGroupID())) {
                    return;
                }
            }
            beep.playBeepSoundAndVibrate();
        }
    }

    // 自己发送的信息统一在此处理
    @Override
    public void onSendMessage(int code, GotyeMessage message) {
        if (returnNotify) {
            return;
        }
        messageFragment.refresh();
    }

    // 收到群邀请信息
    @Override
    public void onReceiveNotify(int code, GotyeNotify notify) {
        if (returnNotify) {
            return;
        }
        messageFragment.refresh();
        updateUnReadTip();
        if (!api.isNotReceiveGroupMsg()) {
            beep.playBeepSoundAndVibrate();
        }
    }

    @Override
    public void onRemoveFriend(int code, GotyeUser user) {
        if (returnNotify) {
            return;
        }
        api.deleteSession(user);
        messageFragment.refresh();
        // contactsFragment.refresh();
    }

    @Override
    public void onAddFriend(int code, GotyeUser user) {
        // TODO Auto-generated method stub
        if (returnNotify) {
            return;
        }
        if (currentPosition == 1) {
            // contactsFragment.refresh();
        }
    }

    @Override
    public void onNotifyStateChanged() {
        // TODO Auto-generated method stub
        mainRefresh();
    }
}
