package com.highgo.opendbt.common.exception.enums;

import com.highgo.opendbt.common.exception.assertion.CommonExceptionAssert;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>通用返回结果</p>
 *
 */
@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements CommonExceptionAssert {
    /**
     * 成功
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 服务器繁忙，请稍后重试
     */
    SERVER_BUSY(9998, "服务器繁忙"),
    /**
     * 服务器异常，无法识别的异常，尽可能对通过判断减少未定义异常抛出
     */
    SERVER_ERROR(9999, "网络异常");

    /**
     * 返回码
     */
    private int code;
    /**
     * 返回消息
     */
    private String message;


}
