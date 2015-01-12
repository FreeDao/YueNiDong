package com.yuenidong.constants;

import android.widget.ImageView;

import com.yuenidong.widget.SelectedImageView;

import java.util.ArrayList;

/**
 * Created by 石岩 on 2014/12/22.
 */
public class YueNiDongConstants {
    public static int count = 0;
    //    public static final String BASE_URL = "http://app.yuenidong.com/rrydBack1.1";
    public static final String BASE_URL = "http://192.168.20.107:8080/rrydBack1.1";
    //注册
    public static final String REGISTER_URL = BASE_URL + "/user/registerUser";
    //登录
    public static final String LOGIN = BASE_URL + "/user/loginUser";
    //头像图片上传
    public static final String IMAGE_UPLOAD = BASE_URL + "/user/upload";
    //获取个人信息
    public static final String GET_INFO = BASE_URL + "/user/findUserByUserId";
    //修改个人信息
    public static final String UPDATE_INFO = BASE_URL + "/user/updateUser";
    //版本更新
    public static final String UPDATE_VERSIONCODE = BASE_URL + "/apk/YueNiDong.apk";
    //选择标签
    public static final String CHOOSE_LABEL = BASE_URL + "/user/addLabel";
    //发起活动信息
    public static final String LANUCHER_ACTIVITY = BASE_URL + "/activity/publish";
    //修改cid lat lng
    public static final String UPDATE_CLL = BASE_URL + "/user/loginDay";
    //获取推送活动的用户信息
    public static final String PUSH_GETINFO = BASE_URL + "/user/getAllUserByScope";
    //获取活动信息
    public static final String GETMATCHINFO = BASE_URL + "/activity/findAllActivity";
    //活动点赞
    public static final String MATCHPRAISE = BASE_URL + "/user/like";
    //参加活动
    public static final String ATTENDMATCH = BASE_URL + "/activity/joinAC";
    //添加活动评论
    public static final String ADDREMARK = BASE_URL + "/remark/addAcRemark";
    //获取活动评论
    public static final String GETREMARK = BASE_URL + "/remark/checkAcRemark";
    //获取我发起的活动
    public static final String GETMYLAUNCHERMATCH = BASE_URL + "/activity/getMyStartAc";
}
