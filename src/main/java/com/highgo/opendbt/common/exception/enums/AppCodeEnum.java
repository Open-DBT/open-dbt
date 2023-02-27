package com.highgo.opendbt.common.exception.enums;


import lombok.Getter;

@Getter
public enum AppCodeEnum implements StatusCode{

    APP_ERROR(2000, "业务异常"),
    VALIDATE_ERROR(1002, "参数校验失败"),
    INVALIDATE_CODE(1003,"登录超时，请重新登录！");
    private int code;
    private String msg;

    AppCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
