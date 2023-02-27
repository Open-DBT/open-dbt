package com.highgo.opendbt.common.exception;

import com.highgo.opendbt.common.exception.enums.IResponseEnum;

/**
 * @Description: 业务异常，业务处理时，出现异常，可以抛出该异常
 * @Title: BusinessException
 * @Package com.highgo.opendbt.common.exception
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/1 13:32
 */
public class BusinessException extends  BaseException {

    private static final long serialVersionUID = 1L;

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message) {
        super(responseEnum, args, message);
    }

    public BusinessException(IResponseEnum responseEnum, Object[] args, String message, Throwable cause) {
        super(responseEnum, args, message, cause);
    }

}
