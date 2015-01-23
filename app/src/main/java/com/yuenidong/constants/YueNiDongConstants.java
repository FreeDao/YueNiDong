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
    public static final String BASE_URL = "http://192.168.20.105:8080/rrydBack1.1";
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
    public static final String ATTENDMATCH = BASE_URL + "/activity/joinCamp";
    //添加活动评论
    public static final String ADDREMARK = BASE_URL + "/remark/addAcRemark";
    //获取活动评论
    public static final String GETREMARK = BASE_URL + "/remark/checkAcRemark";
    //获取我参与的活动
    public static final String GETMYATTENDMATCH = BASE_URL + "/activity/getMyJoinAc";
    //获取我发起的活动
    public static final String GETMYLAUNCHERMATCH = BASE_URL + "/activity/getMyStartAc";
    //获取球友信息
    public static final String GETFRIENDINFO = BASE_URL + "/user/getAllUserByScope";
    //添加关注
    public static final String ADDCARE = BASE_URL + "/user/addCare";
    //拉黑
    public static final String ADDBLACK = BASE_URL + "/user/addBlack";
    //活动详情(最近来过)
    public static final String RECENTCOME = BASE_URL + "/activity/getAcVisitor";
    //我的黑名单
    public static final String BLACKMENU = BASE_URL + "/user/getBlackList";
    //移除黑名单
    public static final String REMOVEBLACKMENU = BASE_URL + "/user/delBlack";
    //查找所有场馆
    public static final String GETVENDER = BASE_URL + "/vender/getAllVender";
    //查找所有场馆(分页)
    public static final String GETVENDERBYPAGE = BASE_URL + "/vender/getVenderByPage";
    //获取场馆评论信息
    public static final String GETVENUESCOMMENT = BASE_URL + "/remark/checkVeRemark";
    //场馆收藏
    public static final String VENUESCOLLECTION = BASE_URL + "/vender/collectVender";
    //教练最近来过
    public static final String COACHRECENTCOME = BASE_URL + "/vender/getCoachVisitor";
    //添加场馆评价
    public static final String ADDVENUESCOMMENT = BASE_URL + "/remark/addVeRemark";
    //获取所有伙伴
    public static final String GETALLFRIEND = BASE_URL + "/user/getAllUser";
    //添加教练评价
    public static final String ADDCOACHCOMMENT = BASE_URL + "/remark/addCoachRemark";
    //获取场馆最近来过的人
    public static final String GETVENUESRECENTCOME = BASE_URL + "/vender/getVenderVisitor";
    //获取收藏场馆
    public static final String GETVENUESCOLLECTION = BASE_URL + "/vender/getCollectVender";
    //获取教练评价
    public static final String GETCOACHCOMMENT = BASE_URL + "/remark/checkCoachRemark";

}
