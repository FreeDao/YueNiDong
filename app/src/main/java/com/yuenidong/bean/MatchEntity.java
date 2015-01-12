package com.yuenidong.bean;

import java.io.Serializable;

/**
 * 活动信息
 * Created by 石岩 on 2014/12/9.
 */
public class MatchEntity implements Serializable {
    private String title;
    private String time;
    private String place;
    private String peopleCount;
    private String pushDistance;
    private String candyCount;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPeopleCount() {
        return peopleCount;
    }

    public void setPeopleCount(String peopleCount) {
        this.peopleCount = peopleCount;
    }

    public String getPushDistance() {
        return pushDistance;
    }

    public void setPushDistance(String pushDistance) {
        this.pushDistance = pushDistance;
    }

    public String getCandyCount() {
        return candyCount;
    }

    public void setCandyCount(String candyCount) {
        this.candyCount = candyCount;
    }

    public MatchEntity() {
    }
}
