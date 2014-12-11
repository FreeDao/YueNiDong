package com.yuenidong.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.yuenidong.common.AppData;

/**
 * 石岩
 */
public class NetWorkUtil {
    //枚举定义网络状态（有效，无效）
    public static enum NetWorkType {
        NETWORK_VALID, NETWORK_INVALID
    }

    //获取当前网络状态
    public static NetWorkType getNetWorkType() {
        ConnectivityManager cm = (ConnectivityManager) AppData.getContext().getSystemService(
                Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] infos = cm.getAllNetworkInfo();
            for (NetworkInfo ni : infos) {
                if (ni.isConnected()) {
                    return NetWorkType.NETWORK_VALID;
                }
            }
        }
        return NetWorkType.NETWORK_INVALID;
    }

}
