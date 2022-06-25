package com.ByteDance.Gotlin.im.util.Hutils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: Hx
 * @date: 2022年06月25日 13:58
 */
public class TimeUtils {
    public static String getCurrentTime(){
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = sdf.format(d);
        return date;
    }
}
