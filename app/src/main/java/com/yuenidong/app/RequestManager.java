
package com.yuenidong.app;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.yuenidong.common.AppData;

/**
 * Created by Dsnc on 14-8-4.
 */
public class RequestManager {
    public static RequestQueue sRequestQueue = Volley.newRequestQueue(AppData.getContext());

    public static void addRequest(Request request, Object tag) {
        if (tag != null) {
            request.setTag(tag);
        }
        sRequestQueue.add(request);
    }
    public static void cancelRequest(Object tag) {
        sRequestQueue.cancelAll(tag);
    }
}
