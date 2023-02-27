package com.highgo.opendbt.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.system.domain.model.ResourceMenuTO;
import com.highgo.opendbt.system.domain.model.StudentStatQuery;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.domain.model.UserInfoPage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

 public interface UserInfoService extends IService<UserInfo> {

     PageInfo<UserInfo> getUser(UserInfoPage userInfoPage);

     Integer updateUser(HttpServletRequest request, UserInfo userInfo, boolean isDeleteRoleIds);

     Integer resetUserPassword(UserInfo userInfo);

     Integer userUpdatePassword(HttpServletRequest request, UserInfo userInfo);

     Integer updateUserIsStop(UserInfo userInfo);

     Integer updateUserDefRole(UserInfo userInfo);

    //获取当前登录用户信息
     UserInfo getCurrentUser(HttpServletRequest request, boolean isRole);

    //获取当前用户有权限的菜单模块
     List<ResourceMenuTO> getMenu(HttpServletRequest request);

     List<UserInfo> getStuBySclassAndCode(StudentStatQuery studentStat);

     UserInfo uploadAvatar(HttpServletRequest request, MultipartFile file);

     Integer updateAccountInfo(HttpServletRequest request, UserInfo userInfo);

     List<UserInfo> getTeachers(HttpServletRequest request);

}
