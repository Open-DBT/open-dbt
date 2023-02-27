package com.highgo.opendbt.common.utils;

import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 获取用户信息工具类
 * @Title: Authentication
 * @Package com.highgo.opendbt.utils
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 13:44
 */
@Component
public class Authentication {
    @Autowired
    private UserInfoService userInfoService2;
    private static UserInfoService userInfoService;

    @PostConstruct
    public void beforeInit() {
        userInfoService = userInfoService2;
    }

    /**
     * @description: 获取用户
     * @author:
     * @date: 2022/8/25 13:53
     * @param: [request]
     * @return: com.highgo.opendbt.system.domain.entity.UserInfo
     **/
    public static UserInfo getCurrentUser(HttpServletRequest request) {
        return userInfoService.getCurrentUser(request, false);
    }
}
