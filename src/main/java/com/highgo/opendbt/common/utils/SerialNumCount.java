package com.highgo.opendbt.common.utils;

import lombok.Data;

/**
 * @Description: 用于解决lambda内部无法修改外部变量问题的类
 * @Title: SerialNumCount
 * @Package com.highgo.opendbt.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 13:44
 */
@Data
public class SerialNumCount {
    private String num = null;

    public String countlevel(String serialNum) {
        num = serialNum + ".1";
        return num;
    }

    public String addSerralNum(String serial_num) {
        String lastString = serial_num.substring(serial_num.lastIndexOf(".") + 1);
        num = serial_num.substring(0, serial_num.lastIndexOf(".") + 1) + (Integer.parseInt(lastString) + 1);
        return num;
    }


}
