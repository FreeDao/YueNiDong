package com.yuenidong.util;

import android.os.Bundle;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

/**
 * Created by Dsnc on 14-8-4.
 */
@SuppressWarnings("UnusedDeclaration")
public class UrlUtil {
    //get请求服务器的Url
    public static String encodeUrl(Map<String, String> param) {
        if (param == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        Set<Map.Entry<String, String>> entries = param.entrySet();
        boolean first = true;

        for (Map.Entry<String, String> entry : entries) {
            String value = entry.getValue();
            String key = entry.getKey();
            if (!TextUtils.isEmpty(value)) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }

                try {
                    sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
                            .append(URLEncoder.encode(value, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();
    }

    public static Bundle parseUrl(String url) {
        url = url.replace("weiboconnect", "http");
        try {
            URL u = new URL(url);
            Bundle b = decodeUrl(u.getQuery());
            b.putAll(decodeUrl(u.getRef()));
            return b;
        } catch (MalformedURLException e) {
            return new Bundle();
        }
    }

    public static Bundle decodeUrl(String s) {
        Bundle params = new Bundle();
        if (s != null) {
            String array[] = s.split("&");
            for (String parameter : array) {
                String v[] = parameter.split("=");
                try {
                    params.putString(URLDecoder.decode(v[0], "UTF-8"),
                            URLDecoder.decode(v[1], "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return params;
    }
}
