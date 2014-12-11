package com.yuenidong.app;

import android.text.TextUtils;
import android.util.Log;

/**
 * @Description TODO Log打印管理
 * @author 石岩
 * @date 2014-11-29 下午4:25:55
 */
public class DsncLog {
	public static boolean debug = DEBUG.isDebug();

	// 打印警告信息
	public static void w(String tag, String msg) {
		if (!debug || TextUtils.isEmpty(msg)) {
			return;
		}
		Log.w(tag, msg);
	}

	// 打印提示信息
	public static void i(String tag, String msg) {
		if (!debug || TextUtils.isEmpty(msg)) {
			return;
		}
		Log.i(tag, msg);
	}

	// 打印调试信息
	public static void d(String tag, String msg) {
		if (!debug || TextUtils.isEmpty(msg)) {
			return;
		}
		Log.d(tag, msg);
	}

	// 打印错误信息
	public static void e(String tag, String msg) {
		if (!debug || TextUtils.isEmpty(msg)) {
			return;
		}
		Log.e(tag, msg);
	}
}
