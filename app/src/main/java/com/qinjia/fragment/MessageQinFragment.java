package com.qinjia.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.gotye.api.GotyeAPI;
import com.gotye.api.GotyeChatTarget;
import com.gotye.api.GotyeChatTargetType;
import com.gotye.api.GotyeGroup;
import com.gotye.api.GotyeRoom;
import com.gotye.api.GotyeUser;
import com.gotye.api.listener.DownloadListener;
import com.qinjia.activity.ChatPage;
import com.qinjia.adapter.MessageListAdapter;
import com.qinjia.view.SwipeMenu;
import com.qinjia.view.SwipeMenuCreator;
import com.qinjia.view.SwipeMenuItem;
import com.qinjia.view.SwipeMenuListView;
import com.yuenidong.activity.R;

//此页面为回话历史页面，由客户端自己实现
@SuppressLint("NewApi")
public class MessageQinFragment extends Fragment implements DownloadListener {
	private SwipeMenuListView listView;
	private MessageListAdapter adapter;

	public static final String fixName = "通知列表";
	private GotyeAPI api = GotyeAPI.getInstance();

    public static MessageQinFragment newInstance() {
        MessageQinFragment fragment = new MessageQinFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
        return fragment;
    }

    public MessageQinFragment() {
        // Required empty public constructor
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.layout_message_page, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		GotyeAPI.getInstance().addListerer(this);
		initView();
	}

	private void initView() {
		listView = (SwipeMenuListView) getView().findViewById(R.id.listview);
		SwipeMenuCreator creator = new SwipeMenuCreator() {

			@Override
			public void create(SwipeMenu menu) {
				// Create different menus depending on the view type
				switch (menu.getViewType()) {
				case 0:
					createMenu1(menu);
					break;
				case 1:
					createMenu2(menu);
					break;
				}
			}
		};
		listView.setMenuCreator(creator);
		listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
			public boolean onMenuItemClick(int position, SwipeMenu menu,
					int index) {
				GotyeChatTarget target = adapter.getItem(position);
				api.deleteSession(target);
				updateList();
				return false;
			}
		});
		updateList();
		setListener();
	}

	private void createMenu1(SwipeMenu menu) {
		 
	}

	private void createMenu2(SwipeMenu menu) {
		// SwipeMenuItem item1 = new SwipeMenuItem(
		// getActivity());
		// item1.setBackground(new ColorDrawable(Color.rgb(0xE5, 0xE0,
		// 0x3F)));
		// item1.setWidth(dp2px(70));
		// item1.setIcon(R.drawable.ic_action_about);
		// menu.addMenuItem(item1);
		SwipeMenuItem item2 = new SwipeMenuItem(getActivity());
		item2.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
		item2.setWidth(dp2px(70));
		item2.setIcon(R.drawable.ic_action_discard);
		menu.addMenuItem(item2);
	}

	private void setListener() {
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				GotyeChatTarget target = (GotyeChatTarget) adapter
						.getItem(arg2);
				if (target.name.equals(fixName)) {
//					Intent i = new Intent(getActivity(), NotifyListPage.class);
//					startActivity(i);
				} else {
					GotyeAPI.getInstance().markMeeagesAsread(target);
					if (target.type == GotyeChatTargetType.GotyeChatTargetTypeUser) {
						Intent toChat = new Intent(getActivity(),
								ChatPage.class);
						toChat.putExtra("user", (GotyeUser) target);
						startActivity(toChat);
						// updateList();
					} else if (target.type == GotyeChatTargetType.GotyeChatTargetTypeRoom) {
						Intent toChat = new Intent(getActivity(),
								ChatPage.class);
						toChat.putExtra("room", (GotyeRoom) target);
						startActivity(toChat);

					} else if (target.type == GotyeChatTargetType.GotyeChatTargetTypeGroup) {
						Intent toChat = new Intent(getActivity(),
								ChatPage.class);
						toChat.putExtra("group", (GotyeGroup) target);
						startActivity(toChat);
					}
					refresh();
				}
			}
		});
	}

	private void updateList() {
		List<GotyeChatTarget> sessions = api.getSessionList();
		Log.d("offLine", "List--sessions" + sessions);

		GotyeChatTarget target = new GotyeChatTarget();
		target.name = fixName;

		if (sessions == null) {
			sessions = new ArrayList<GotyeChatTarget>();
			sessions.add(target);
		} else {
			sessions.add(0, target);
		}
		if (adapter == null) {
			adapter = new MessageListAdapter(MessageQinFragment.this, sessions);
			listView.setAdapter(adapter);
		} else {
			adapter.setData(sessions);
		}
	}

	public void refresh() {
		updateList();
	}

	@Override
	public void onDestroy() {
		GotyeAPI.getInstance().removeListener(this);
		super.onDestroy();

	}

	@Override
	public void onDownloadMedia(int code, String path, String url) {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}

	private int dp2px(int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				getResources().getDisplayMetrics());
	}
}
