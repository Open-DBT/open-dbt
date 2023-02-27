package com.highgo.opendbt.common.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

/**
 * @Description: 信息获取
 * @Title: Message
 * @Package com.highgo.opendbt.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 13:44
 */
public class Message {

    private static ResourceBundle messageBundle = ResourceBundle.getBundle("message");

    public static String get(String pattern, Object... args) {
        if (args.length > 0) {
            return MessageFormat.format(messageBundle.getString(pattern), args);
        } else {
            return messageBundle.getString(pattern);
        }
    }

}
