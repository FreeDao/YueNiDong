
package com.yuenidong.util;

import java.util.Observable;

/**
 * Created by 石岩 on 14-9-10.
 */
public class NetWorkState extends Observable {
    private static NetWorkState sSingleton = null;

    private static NetWorkUtil.NetWorkType sNetWorkType = NetWorkUtil.NetWorkType.NETWORK_INVALID;

    public static NetWorkState with() {
        synchronized (NetWorkState.class) {
            if (sSingleton == null) {
                sSingleton = new NetWorkState();
            }
        }
        return sSingleton;
    }

    //检查网络状态
    public void checkoutNetWork() {
        sNetWorkType = NetWorkUtil.getNetWorkType();
        setChanged();
        notifyObservers(sNetWorkType);
    }
}
