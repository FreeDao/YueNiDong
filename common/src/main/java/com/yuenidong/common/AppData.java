package com.yuenidong.common;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

/**
 * @Description TODO全局统一资源管理
 * @author 石岩
 * @date 2014-11-29 下午4:19:08
 */
public class AppData {

	private static Context mContext;

	public static void init(Context context) {
		mContext = context;
	}

	// 得到当前程序的上下文
	public static Context getContext() {
		return mContext;
	}

	// 得到当前程序的字符串资源的引用
	public static String getString(int id) {
		return mContext.getResources().getString(id);
	}

	// 得到当前程序的颜色资源的引用
	public static int getColor(int id) {
		return mContext.getResources().getColor(id);
	}

	// 得到当前程序的资源
	public static Resources getResources() {
		return mContext.getResources();
	}

	public static void startActivity(Intent intent) {
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}

}
