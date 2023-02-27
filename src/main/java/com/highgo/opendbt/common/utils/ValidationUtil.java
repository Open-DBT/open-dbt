package com.highgo.opendbt.common.utils;

import com.highgo.opendbt.common.exception.APIException;
import org.springframework.validation.BindingResult;

/**
 * @Description: 校验异常抛出封装
 * @Title: ValidationUtil
 * @Package com.highgo.opendbt.utils.test
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/26 16:54
 */
public class ValidationUtil {
    public static void Validation(BindingResult result) {
        if (result.hasErrors()) {
            throw new APIException(result.getFieldError().getDefaultMessage());
        }
    }
}
