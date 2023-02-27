package com.highgo.opendbt.api;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.bean.ResourceTreeTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.domain.model.*;
import com.highgo.opendbt.system.service.ResourceInfoService;
import com.highgo.opendbt.system.service.RoleInfoService;
import com.highgo.opendbt.system.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 系统设置模块相关的接口
 * @Title: SystemApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/sys")
public class SystemApi {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final RoleInfoService roleInfoService;
    private final ResourceInfoService resourceInfoService;
    private final UserInfoService userInfoService;


    /**
     * 分页获取所有角色
     *
     * @param pageTO 分页信息
     * @return PageInfo<RoleInfo>
     */
    @RequestMapping("/getRole")
    public PageInfo<RoleInfo> getRole(@RequestBody PageTO pageTO) {
        logger.debug("Enter, pageTO = " + pageTO.toString());
        return roleInfoService.getRole(pageTO);
    }

    /**
     * 添加和修改角色
     *
     * @param roleInfo 角色信息
     * @return Integer
     */
    @RequestMapping("/updateRole")
    public ResultTO<Integer> updateRole(HttpServletRequest request, @RequestBody RoleInfo roleInfo) {
        logger.info("Enter, roleInfo = " + roleInfo.toString());
        return roleInfoService.updateRole(request, roleInfo);
    }

    /**
     * 删除角色
     *
     * @param roleId 角色id
     * @return Integer
     */
    @RequestMapping("/deleteRole/{roleId}")
    public ResultTO<Integer> deleteRole(@PathVariable("roleId") int roleId) {
        logger.debug("Enter, roleId = " + roleId);
        return roleInfoService.deleteRole(roleId);
    }

    /**
     * 分页获取模块
     *
     * @param resourceInfoPage 分页和模糊查询参数
     * @return PageInfo<ResourceInfo>
     */
    @RequestMapping("/getResource")
    public ResultTO<PageInfo<ResourceInfo>> getResource(@RequestBody ResourceInfoPage resourceInfoPage) {
        logger.debug("Enter, resourceInfoPage = " + resourceInfoPage);
        return resourceInfoService.getResource(resourceInfoPage);
    }

    /**
     * 获取模块树形数据
     *
     * @return List<ResourceTreeTO>
     */
    @RequestMapping("/getResourceTree")
    public ResultTO<List<ResourceTreeTO>> getResourceTree() {
        logger.debug("Enter, ");
        return resourceInfoService.getResourceTree();
    }

    /**
     * 修改角色和模块的关联关系
     *
     * @param roleInfo 角色信息
     * @return Integer
     */
    @RequestMapping("/updateRoleResource")
    public ResultTO<Integer> updateRoleResource(@RequestBody RoleInfo roleInfo) {
        logger.debug("Enter, roleInfo = " + roleInfo.toString());
        return roleInfoService.updateRoleResource(roleInfo);
    }

    /**
     * 分页获取用户
     *
     * @param userInfoPage 分页和模糊查询参数
     * @return PageInfo<UserInfo>
     */
    @RequestMapping("/getUser")
    public PageInfo<UserInfo> getUser(@RequestBody UserInfoPage userInfoPage) {
        logger.debug("Enter, userInfoPage = " + userInfoPage);
        return userInfoService.getUser(userInfoPage);
    }

    /**
     * 新增和修改用户
     *
     * @param userInfo 用户信息
     * @return Integer
     */
    @RequestMapping("/updateUser")
    public Integer updateUser(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        logger.info("Enter, userInfo = " + userInfo.toString());
        return userInfoService.updateUser(request, userInfo, true);
    }

    /**
     * 修改用户但不操作用户和角色的关联关系
     *
     * @param userInfo 用户信息
     * @return Integer
     */
    @RequestMapping("/updateUserCenter")
    public Integer updateUserCenter(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        logger.info("Enter, userInfo = " + userInfo.toString());
        return userInfoService.updateUser(request, userInfo, false);
    }

    /**
     * 重置和修改用户的密码
     *
     * @param userInfo 用户信息
     * @return Integer
     */
    @RequestMapping("/resetUserPassword")
    public Integer resetUserPassword(@RequestBody UserInfo userInfo) {
        logger.debug("Enter,");
        return userInfoService.resetUserPassword(userInfo);
    }

    /**
     * 用户修改密码
     *
     * @param userInfo 用户信息
     * @return Integer
     */
    @RequestMapping("/userUpdatePassword")
    public Integer userUpdatePassword(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        logger.debug("Enter,");
        return userInfoService.userUpdatePassword(request, userInfo);
    }

    /**
     * 修改用户是否停用
     *
     * @param userInfo 用户信息
     * @return Integer
     */
    @RequestMapping("/updateUserIsStop")
    public Integer updateUserIsStop(@RequestBody UserInfo userInfo) {
        logger.debug("Enter, userInfo = " + userInfo.toString());
        return userInfoService.updateUserIsStop(userInfo);
    }

    /**
     * 修改用户的默认角色，用于角色切换
     *
     * @param userInfo 用户信息
     * @return Integer
     */
    @RequestMapping("/updateUserDefRole")
    public Integer updateUserDefRole(@RequestBody UserInfo userInfo) {
        logger.debug("Enter, userInfo = " + userInfo.toString());
        return userInfoService.updateUserDefRole(userInfo);
    }

    /**
     * 上传头像
     *
     * @param file 头像文件
     * @return UserInfo
     */
    @RequestMapping("/uploadAvatar")
    public UserInfo uploadAvatar(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        logger.info("Enter, uploadAvatar file = " + file);
        return userInfoService.uploadAvatar(request, file);
    }

    /**
     * 根据<班级id>和<学生学号>查询学生列表 教师--学生统计
     *
     * @param studentStat 学生信息
     * @return List<UserInfo>
     */
    @RequestMapping("/getStuBySclassAndCode")
    public List<UserInfo> getStuBySclassAndCode(@RequestBody StudentStatQuery studentStat) {
        return userInfoService.getStuBySclassAndCode(studentStat);
    }

    /**
     * 页面个人设置账号信息修改
     *
     * @param userInfo 个人信息
     * @return Integer
     */
    @RequestMapping("/updateAccountInfo")
    public Integer updateAccountInfo(HttpServletRequest request, @RequestBody UserInfo userInfo) {
        logger.info("Enter, userInfo = " + userInfo.toString());
        return userInfoService.updateAccountInfo(request, userInfo);
    }

    /**
     * 获取第一角色为教师的用户
     *
     * @return List<UserInfo>
     */
    @RequestMapping("/getTeachers")
    public List<UserInfo> getTeachers(HttpServletRequest request) {
        logger.info("Enter, ");
        return userInfoService.getTeachers(request);
    }

}
