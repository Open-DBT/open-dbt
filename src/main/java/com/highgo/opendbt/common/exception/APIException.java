package com.highgo.opendbt.common.exception;

import com.highgo.opendbt.common.exception.enums.AppCodeEnum;
import com.highgo.opendbt.common.exception.enums.StatusCode;

import java.text.MessageFormat;

public class APIException extends RuntimeException {
    private int code;
    private String msg;

    // 手动设置异常
    public APIException(StatusCode statusCode, String message, Object... args) {
        // message用于用户设置抛出错误详情，例如：当前价格-5，小于0
        super(args.length>0?MessageFormat.format(message, args):message);
        // 状态码
        this.code = statusCode.getCode();
        // 状态码配套的msg
        this.msg = statusCode.getMsg();
    }

    // 默认异常使用APP_ERROR状态码
    public APIException(String message, Object... args) {
        super(args.length>0?MessageFormat.format(message, args):message);
        this.code = AppCodeEnum.APP_ERROR.getCode();
        this.msg = AppCodeEnum.APP_ERROR.getMsg();
    }

}
