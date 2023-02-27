package com.highgo.opendbt.common.exception;

import com.highgo.opendbt.common.exception.enums.IResponseEnum;
import lombok.Getter;

/**
 * @Description: 异常基类
 * @Title: BaseException
 * @Package com.highgo.opendbt.common.exception
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/1 13:43
 */

@Getter
public class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    /**
     * 返回码
     */
    protected IResponseEnum responseEnum;
    /**
     * 异常消息参数
     */
    protected Object[] args;

    public BaseException(IResponseEnum responseEnum) {
        super(responseEnum.getMessage());
        this.responseEnum = responseEnum;
    }

    public BaseException(int code, String msg) {
        super(msg);
        this.responseEnum = new IResponseEnum() {
            @Override
            public int getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return msg;
            }
        };
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message) {
        super(message);
        this.responseEnum = responseEnum;
        this.args = args;
    }

    public BaseException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(message, cause);
        this.responseEnum = responseEnum;
        this.args = args;
    }
}
