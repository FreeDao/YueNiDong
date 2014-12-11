package com.yuenidong.common;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Map;

/**
 * @Description TODO 偏好设置
 * @author 石岩
 * @date 2014-11-29 下午4:31:27
 */
public class PreferenceUtil {
	// 设置偏好
	public static void setPreString(String key, String value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(AppData.getContext());
		sharedPreferences.edit().putString(key, value).commit();
	}

	// 设置偏好
	public static void setPreBoolean(String key, Boolean value) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(AppData.getContext());
		sharedPreferences.edit().putBoolean(key, value).commit();
	}

	// 获得偏好
	public static String getPreString(String key, String defaultValue) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(AppData.getContext());
		return sharedPreferences.getString(key, defaultValue);
	}

	// 获得偏好
	public static Boolean getPreBoolean(String key, Boolean defaultValue) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(AppData.getContext());
		return sharedPreferences.getBoolean(key, defaultValue);
	}

	// 移除偏好某个key值已经对应的值
	public static void remove(String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(AppData.getContext());
		sharedPreferences.edit().remove(key);
	}

	// 查询偏好某个key是否已经存在
	public static Boolean contain(String key) {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(AppData.getContext());
		return sharedPreferences.contains(key);
	}

	// 返回偏好所有的键值对
	public static Map<String, ?> getAll() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(AppData.getContext());
		return sharedPreferences.getAll();
	}

	// 清除偏好
	public static void clear(SharedPreferences p) {
		SharedPreferences.Editor editor = p.edit();
		editor.clear();
		editor.commit();
	}

}
