package com.highgo.opendbt.common.utils;

import lombok.SneakyThrows;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description: 时间处理工具类
 * @Title: TimeUtil
 * @Package com.highgo.opendbt.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 13:44
 */
public class TimeUtil {

    static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat timeStrFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    static SimpleDateFormat timeStrFormat1 = new SimpleDateFormat("MMddHHmmss");

    /**
     * 获取当前日期时间，格式：2020-08-26 09:00:00
     *
     * @return
     */
    public static String getDateTime() {
        return timeFormat.format(new Date());
    }

    /**
     * 获取当前的日期串，格式：20210826090000
     *
     * @return
     */
    public static String getDateTimeStr() {
        return timeStrFormat.format(new Date());
    }

    /**
     * 获取当前的日期串，格式：0826090000
     *
     * @return
     */
    public static String getDateTimeStr1() {
        return timeStrFormat1.format(new Date());
    }

    /**
     * 日期转毫秒数
     *
     * @param date
     * @return
     */
    public static long dateConvertMS(Date date) {
        return date.getTime();
    }

    /**
     * 日期转时间，格式：2020-08-26 09:00:00
     *
     * @param date
     * @return
     */
    public static String convertDateTime(Date date) {
        return timeFormat.format(date);
    }


    @SneakyThrows
    public static Date converTODate(String time) {
        if (time != null && time != "") {
            return timeFormat.parse(time);
        } else {
            return null;
        }

    }

}
