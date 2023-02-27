package com.highgo.opendbt;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 获取SpringBoot的容器，在不能注入的地方可以通过getBean获取相应的类
 * @Title: ApplicationContextRegister
 * @Package com.highgo.opendbt
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/6 15:37
 */
@Configuration
public class ApplicationContextRegister implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (ApplicationContextRegister.applicationContext == null) {
            ApplicationContextRegister.applicationContext = applicationContext;
        }
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

}
