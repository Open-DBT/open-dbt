package com.highgo.opendbt.api;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.login.model.NoticesListTO;
import com.highgo.opendbt.login.service.LoginService;
import com.highgo.opendbt.login.service.NoticesService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.domain.model.ResourceMenuTO;
import com.highgo.opendbt.system.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 登录相关接口类
 * @Title: LoginApi
 * @Package com.highgo.opendbt.api
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@RestController
@CrossOrigin
@RequestMapping("/")
@RequiredArgsConstructor
public class LoginApi {

    Logger logger = LoggerFactory.getLogger(getClass());

    final LoginService loginService;
    final UserInfoService userInfoService;
    final NoticesService noticesService;


    /**
     * 登录
     *
     * @param  userInfo 用户信息
     * @return void
     */
    @RequestMapping("/login")
    public ResultTO<Void> login(@RequestBody UserInfo userInfo) {
        logger.info("Enter, code = " + userInfo.getCode() + ", userName = " + userInfo.getUserName());
        return loginService.login(userInfo);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param request request
     * @return UserInfo
     */
    @RequestMapping("/getCurrentUser")
    public UserInfo getCurrentUser(HttpServletRequest request) {
        logger.info("Enter, ");
        return userInfoService.getCurrentUser(request, true);
    }

    /**
     * 获取当前用户有权限的菜单模块
     *
     * @param request request
     * @return List<ResourceMenuTO>
     */
    @RequestMapping("/getMenu")
    public List<ResourceMenuTO> getMenu(HttpServletRequest request) {
        logger.info("Enter, ");
        return userInfoService.getMenu(request);
    }

    /**
     * 获取所有通知消息  暂时不用
     *
     * @return NoticesListTO
     */
    @RequestMapping("/getNotices")
    public NoticesListTO getNotices(HttpServletRequest request) {
        logger.info("Enter, ");
        return noticesService.getNotices(request);
    }

    /**
     * 获取所有未读通知消息，并判断token是否快过期，如果快过期就刷新
     * 暂时不用
     *
     * @param request request
     * @return NoticesListTO
     */
    @RequestMapping("/getNoticesNotRead")
    public ResultTO<NoticesListTO> getNoticesNotRead(HttpServletRequest request) {
        logger.info("Enter, ");
        return noticesService.getNoticesNotRead(request);
    }

    /**
     * @description: 修改通知为已读  暂时不用
     * @param request request
     * @param type     通知类型
     * @param noticeId 通知id
     * @return integer
     */
    @RequestMapping("/changeRead/{type}/{noticeId}")
    public Integer changeRead(HttpServletRequest request, @PathVariable("type") int type, @PathVariable("noticeId") int noticeId) {
        logger.info("Enter, type = " + type + ", noticeId = " + noticeId);
        return noticesService.changeRead(request, type, noticeId);
    }

    /**
     * 清空未读通知  暂时不用
     *
     * @param request request
     * @param type    通知类型
     * @return Integer
     */
    @RequestMapping("/clearNotRead/{type}")
    public Integer clearNotRead(HttpServletRequest request, @PathVariable("type") int type) {
        logger.info("Enter, type = " + type);
        return noticesService.clearNotRead(request, type);
    }

}
