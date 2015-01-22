package com.yuenidong.constants;

/**
 * Created by 石岩 on 2014/12/22.
 */
public interface SinaConstants {

    public static final String APP_KEY = "3359237026";

    public static final String APP_SECRET = "d6aed3d9da42eb4b771b6d8784611765";

    public static final String SCOPE = "null";

    public static final String BASE_URL = "https://api.weibo.com/";

    //请求用户授权Token(请求授权)
    public static final String AUTHORIZE_URL = BASE_URL + "oauth2/authorize";

    //表示用户身份的Token,用于微博API的调用(获取授权)
    public static final String ACCESSYOKEN_URL = BASE_URL + "oauth2/access_token";

    //第三方应用授权回调页面
    public static final String REDIRECT_URL = "http://www.yuenidong.com";

    public static final String USERS_SHOW_URL = BASE_URL + "2/users/show.json";

}
