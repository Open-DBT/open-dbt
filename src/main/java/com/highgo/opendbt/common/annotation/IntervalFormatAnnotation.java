package com.highgo.opendbt.common.annotation;


import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.highgo.opendbt.common.Interval.IntervalSerializer;

import java.lang.annotation.*;
/**
 * @Description: 自定义时间间隔格式化注解
 * @Title: IntervalFormatAnnotation
 * @Package com.highgo.opendbt.common.annotation
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@JacksonAnnotationsInside
@JsonSerialize(using = IntervalSerializer.class)
public @interface IntervalFormatAnnotation {
    //时间差显示类型
    IntervalTypeEnum type();
}
