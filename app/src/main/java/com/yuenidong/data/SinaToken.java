
package com.yuenidong.data;

/**
 * Created by 石岩 on 14-8-4.
 */
public class SinaToken {
    //访问令牌
    private String access_token;

    private long expires_in;

    private long remind_in;

    private long uid;
    @SuppressWarnings("UnusedDeclaration")
    public String getAccess_token() {
        return access_token;
    }
    @SuppressWarnings("UnusedDeclaration")
    public long getExpires_in() {
        return expires_in;
    }
    @SuppressWarnings("UnusedDeclaration")
    public long getUid() {
        return uid;
    }
    @SuppressWarnings("UnusedDeclaration")
    public long getRemind_in() {
        return remind_in;
    }
}
