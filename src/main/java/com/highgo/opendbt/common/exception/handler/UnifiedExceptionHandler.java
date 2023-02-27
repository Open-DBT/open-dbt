package com.highgo.opendbt.common.exception.handler;

/**
 * @Description: 定义统一异常处理器类
 * @Title: UnifiedExceptionHandler
 * @Package com.highgo.opendbt.common.exception
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/1 14:02
 */

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.exception.BaseException;
import com.highgo.opendbt.common.exception.BusinessException;
import com.highgo.opendbt.common.exception.enums.CommonResponseEnum;
import com.highgo.opendbt.common.exception.enums.ServletResponseEnum;
import com.highgo.opendbt.common.exception.i18n.UnifiedMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;


/**
 * <p>全局异常处理器</p>
 */
@RestControllerAdvice
public class UnifiedExceptionHandler {
    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 生产环境BusinessException
     */
    private final static String ENV_PROD = "prod";

    @Autowired
    private UnifiedMessageSource unifiedMessageSource;
    /**
     * 当前环境
     */
    @Value("${spring.profiles.active}")
    private String profile;

    /**
     * 自定义异常
     * <p>
     * BaseException extends RuntimeException
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BaseException.class)
    public ResultTO handleBaseException(BaseException e) {
        logger.error(e.getMessage(), e);
        // return ResultTO.FAILURE(getMessage(e), e.getResponseEnum().getCode());
        return ResultTO.FAILURE(getMessage(e));

    }

    /**
     * 未定义异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = Exception.class)
    public ResultTO handleException(Exception e) {
        logger.error(e.getMessage(), e);

        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如数据库异常信息.
            int code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getMessage(baseException);
            return ResultTO.FAILURE(message, code);
        }
        //return ResultTO.FAILURE(e.getMessage(), CommonResponseEnum.SERVER_ERROR.getCode());
        return ResultTO.FAILURE(e.getMessage());

    }

    /**
     * 业务异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BusinessException.class)
    public ResultTO handleBusinessException(BaseException e) {
        logger.error(e.getMessage(), e);

        //return  ResultTO.FAILURE(getMessage(e),e.getResponseEnum().getCode());
        return ResultTO.FAILURE(getMessage(e));
    }

    /**
     * Controller上一层相关异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            HttpMediaTypeNotSupportedException.class,
            HttpMediaTypeNotAcceptableException.class,
            MissingPathVariableException.class,
            MissingServletRequestParameterException.class,
            TypeMismatchException.class,
            HttpMessageNotReadableException.class,
            HttpMessageNotWritableException.class,
            // BindException.class,
            // MethodArgumentNotValidException.class
            ServletRequestBindingException.class,
            ConversionNotSupportedException.class,
            MissingServletRequestPartException.class,
            AsyncRequestTimeoutException.class
    })

    public ResultTO handleServletException(Exception e) {
        logger.error(e.getMessage(), e);
        int code = CommonResponseEnum.SERVER_ERROR.getCode();
        try {
            ServletResponseEnum servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
            code = servletExceptionEnum.getCode();
        } catch (IllegalArgumentException e1) {
            logger.error("class [{}] not defined in enum {}", e.getClass().getName(), ServletResponseEnum.class.getName());
        }

        if (ENV_PROD.equals(profile)) {
            // 当为生产环境, 不适合把具体的异常信息展示给用户, 比如404.
            code = CommonResponseEnum.SERVER_ERROR.getCode();
            BaseException baseException = new BaseException(CommonResponseEnum.SERVER_ERROR);
            String message = getMessage(baseException);
            return ResultTO.FAILURE(message, code);
        }

        //return ResultTO.FAILURE(e.getMessage(), code);
        return ResultTO.FAILURE(e.getMessage());

    }


    /**
     * 参数绑定异常
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = BindException.class)
    public ResultTO handleBindException(BindException e) {
        logger.error("参数绑定校验异常", e);

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 参数校验(Valid)异常，将校验失败的所有异常组合成一条错误信息
     *
     * @param e 异常
     * @return 异常结果
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResultTO handleValidException(MethodArgumentNotValidException e) {
        logger.error("参数绑定校验异常", e);

        return wrapperBindingResult(e.getBindingResult());
    }

    /**
     * 包装绑定异常结果
     *
     * @param bindingResult 绑定结果
     * @return 异常结果
     */
    private ResultTO wrapperBindingResult(BindingResult bindingResult) {
        StringBuilder msg = new StringBuilder();

        for (ObjectError error : bindingResult.getAllErrors()) {
            msg.append(", ");
            if (error instanceof FieldError) {
                msg.append(((FieldError) error).getField()).append(": ");
            }
            msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());

        }

        //return ResultTO.FAILURE(msg.substring(2), ArgumentResponseEnum.VALID_ERROR.getCode());
        return ResultTO.FAILURE(msg.substring(2));
    }


    /**
     * 获取国际化消息
     *
     * @param e 异常
     * @return
     */
    public String getMessage(BaseException e) {
        String code = "response." + e.getResponseEnum().toString();
        String message = unifiedMessageSource.getMessage(code, e.getArgs());

        if (message == null || message.isEmpty()) {
            return e.getMessage();
        }
        return message;
    }

}
