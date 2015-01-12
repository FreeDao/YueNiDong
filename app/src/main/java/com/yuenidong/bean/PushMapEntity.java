package com.yuenidong.bean;

import java.io.Serializable;

/**
 * 推送活动返回的用户信息
 * Created by 石岩 on 2015/1/8.
 */
public class PushMapEntity implements Serializable{
    private String lat;
    private String lng;
    private String userId;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PushMapEntity() {
    }
}
