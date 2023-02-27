package com.highgo.opendbt.common.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * @Description: 不使用返回包装的注解
 * @Title: NotControllerResponseAnnotation
 * @Package com.highgo.opendbt.common.annotation
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotControllerResponseAnnotation {
}
