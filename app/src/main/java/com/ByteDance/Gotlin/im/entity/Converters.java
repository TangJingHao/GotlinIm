package com.ByteDance.Gotlin.im.entity;

import androidx.room.TypeConverter;

import java.sql.Date;

/**
 * @Author Zhicong Deng
 * @Date 2022/6/21 0:47
 * @Email 1520483847@qq.com
 * @Description 用于数据库类型转换
 */
public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}