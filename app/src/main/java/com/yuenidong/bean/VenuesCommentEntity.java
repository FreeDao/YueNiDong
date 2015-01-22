package com.yuenidong.bean;

import java.io.Serializable;

/**
 * Created by 石岩 on 2014/12/17.
 * 场馆评论
 */
public class VenuesCommentEntity implements Serializable {
    private String remarkPoint;
    private String content;
    private String img;
    private String time;
    private String userName;

    public String getRemarkPoint() {
        return remarkPoint;
    }

    public void setRemarkPoint(String remarkPoint) {
        this.remarkPoint = remarkPoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public VenuesCommentEntity() {

    }
}
