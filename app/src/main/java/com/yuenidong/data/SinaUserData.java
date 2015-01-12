
package com.yuenidong.data;

import java.io.Serializable;

/**
 * Created by 石岩 on 14-8-19.
 */
public class SinaUserData implements Serializable{

    private String screen_name;

    private String gender;

    private String profile_image_url;

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

}
