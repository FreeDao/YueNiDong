package com.yuenidong.app;

import android.content.pm.ApplicationInfo;

import com.yuenidong.common.AppData;

/**
 * @Description TODO 调试管理 
 * @author 石岩   
 * @date 2014-11-29 下午4:25:20
 */
public class DEBUG {
	public static boolean isDebug() {
		try {
			ApplicationInfo applicationInfo = AppData.getContext()
					.getApplicationInfo();
			return (applicationInfo.flags & applicationInfo.FLAG_DEBUGGABLE) != 0;
		} catch (Exception e) {

		}
		return true;
	}
}
