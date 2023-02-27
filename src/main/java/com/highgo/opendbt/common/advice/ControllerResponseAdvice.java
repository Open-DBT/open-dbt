package com.highgo.opendbt.common.advice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.highgo.opendbt.common.annotation.NotControllerResponseAnnotation;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.AppCodeEnum;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @Description: 对返回进行封装（response是ResultTO类型，或者注释了NotControllerResponseAdvice都不进行包装） 使用 @ControllerAdvice 和 实现ResponseBodyAdvice接口， 拦截Controller方法默认返回参数，统一处理返回值/响应体
 * @Title: ControllerResponseAdvice
 * @Package com.highgo.opendbt.common.advice
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
@RestControllerAdvice(basePackages = {"com.highgo.opendbt"})
public class ControllerResponseAdvice implements ResponseBodyAdvice<Object> {
    //supports对你需要进行拦截的response进行判断筛选，返回true则进行拦截反之放行
    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        // response是ResultTO类型，或者注释了NotControllerResponseAdvice都不进行包装
        return !(methodParameter.getParameterType().isAssignableFrom(ResultTO.class) || methodParameter.hasMethodAnnotation(NotControllerResponseAnnotation.class));
    }

    //beforeBodyWrite对supports进行拦截的response进行处理，封装你需要的类型参数，加密等等
    @Override
    public Object beforeBodyWrite(Object data, MethodParameter returnType, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest request, ServerHttpResponse response) {
        // String类型不能直接包装
        if (returnType.getGenericParameterType().equals(String.class)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 将数据包装在ResultTO里后转换为json串进行返回
                return objectMapper.writeValueAsString(ResultTO.OK(data));
            } catch (JsonProcessingException e) {
                throw new APIException(AppCodeEnum.VALIDATE_ERROR, e.getMessage());
            }
        }
        // 否则直接包装成ResultVo返回
        return ResultTO.OK(data);
    }
}
