package com.yuenidong.util;

import com.yuenidong.app.DsncLog;

/**
 * Created by 石岩 on 2015/1/8.
 */
public class TimeUtils {
    /**
     * "2015-01-14 15:21"转换成2015年01月14日15：21
     */
    public static String StringToPatternTime(String str) {
        StringBuffer sb = new StringBuffer();
        String year = str.substring(0, 4);
        String month = str.substring(5, 7);
        String day = str.substring(8, 10);
        String hour = str.substring(11, 13);
        String minute = str.substring(14, 16);
        sb.append(year + "年" + month + "月" + day + "日" + hour + ":" + minute);
        return sb.toString();
    }

    /**
     * 2015-01-09 16:41:49.0 转换成16:41
     */
    public static String StringToPatternTimeTwo(String str){
        StringBuffer sb=new StringBuffer();
        String hour=str.substring(11,13);
        String minute=str.substring(14,16);
        sb.append(hour+":"+minute);
        return sb.toString();

    }
}
