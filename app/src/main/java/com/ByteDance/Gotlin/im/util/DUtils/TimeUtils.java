package com.ByteDance.Gotlin.im.util.DUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Zhicong Deng
 * on 2021/12/23 17:15
 * https://github.com/LumosDZC
 * <p>
 * 时间类工具函数，用于获取时间
 */
public class TimeUtils {

    //时间戳转字符
    public static String getDateToString(long time) {
        Date date = new Date(time);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sf.format(date);
    }

//    public static String getDateToString(long time) {
//        Date date = new Date(time);
//        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        return sf.format(date);
//    }
    //字符提取日期函数
}
