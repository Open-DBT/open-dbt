package com.highgo.opendbt.common.advice;

import com.highgo.opendbt.common.bean.ResultTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 捕获所有异常进行封装  暂时不用
 * @Title: ControllerExceptionAdvice
 * @Package com.highgo.opendbt.common.advice
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
//@RestControllerAdvice
public class ControllerExceptionAdvice {
    Logger logger = LoggerFactory.getLogger(getClass());
    //@ExceptionHandler({Exception.class})
    public ResultTO MethodArgumentNotValidExceptionHandler(Exception e) {
        // 从异常对象中拿到ObjectError对象
       // ObjectError objectError = e.getBindingResult().getAllErrors().get(0);
        logger.error("出错啦！快去检查~",e);
        return ResultTO.FAILURE(e.getMessage());
    }
}
