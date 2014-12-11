package com.yuenidong.app;

import java.util.Stack;

import android.app.Activity;

/**
 * @Description TODO 程序管理
 * @author 石岩
 * @date 2014-11-29 下午4:23:01
 */
public class AppManager {
	// 保存Acitivity的栈
	private static Stack<Activity> activityStack;
	// 单例
	private static AppManager instance;

	// 构造函数
	public AppManager() {

	}

	// 单一实例
	public static AppManager getAppManager() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}

	// 添加Acitivity到堆栈
	public void addActivity(Activity acitivity) {
		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(acitivity);
	}

	// 获取当前的Acitivity(堆栈中最后一个压入的)
	public Activity currentActivity() {
		Activity acitivity = activityStack.lastElement();
		return acitivity;
	}

	// 结束当前的Activity(堆栈中最后一个压入的)
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		finishActivity(activity);
	}

	// 结束指定的Activity
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	// 结束指定类名的Activity
	public void finishActivity(Class<?> cls) {
		Activity activity = null;
		for (Activity a : activityStack) {
			if (a.getClass().equals(cls)) {
				activity = a;
				break;
			}
			if (activity != null) {
				finishActivity(activity);
			}
		}
	}

	// 获得指定类名的Activity
	public Activity getActivity(Class<?> cls) {
		if (null != activityStack && activityStack.size() != 0) {
			int size = activityStack.size();
			for (int i = size - 1; i >= 0; i--) {
				Activity activity = activityStack.get(i);
				if (activity.getClass().equals(cls)) {
					return activity;
				}
			}
		}
		return null;
	}

	// 结束掉所有Activity
	public void finishAllActivity() {
		for (int i = 0; i < activityStack.size(); i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	// 退出应用程序
	public void appExit() {
		try {
			finishAllActivity();
			System.exit(0);
		} catch (Exception e) {

		}
	}

	// 获取Activity栈
	public Stack<Activity> getStack() {
		return activityStack;
	}
}
