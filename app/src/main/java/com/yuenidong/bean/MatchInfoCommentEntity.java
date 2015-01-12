package com.yuenidong.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2014/12/15.
 */
public class MatchInfoCommentEntity implements Serializable {
    private String img;
    private String name;
    private String time;
    private String content;

    public MatchInfoCommentEntity(String img, String name, String time, String content) {
        this.img = img;
        this.name = name;
        this.time = time;
        this.content = content;
    }

    public MatchInfoCommentEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
