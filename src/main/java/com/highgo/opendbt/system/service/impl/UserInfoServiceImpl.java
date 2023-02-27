package com.highgo.opendbt.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.ExcelUtil;
import com.highgo.opendbt.common.utils.JwtUtil;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.domain.model.*;
import com.highgo.opendbt.system.mapper.ResourceInfoMapper;
import com.highgo.opendbt.system.mapper.RoleInfoMapper;
import com.highgo.opendbt.system.mapper.UserInfoMapper;
import com.highgo.opendbt.system.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final UserInfoMapper userInfoMapper;

    private final RoleInfoMapper roleInfoMapper;

    private final ResourceInfoMapper resourceInfoMapper;


    @Override
    public PageInfo<UserInfo> getUser(UserInfoPage userInfoPage) {
            // 分页查询配置
            PageMethod.startPage(userInfoPage.getCurrent(), userInfoPage.getPageSize());

            // 涉及到模糊查询用户
            List<UserInfo> userList;

            // 角色id不等于-1，以角色为主体查询用户；等于-1则以用户为主体查询用户
            if (userInfoPage.getRoleId() != -1) {
                userList = userInfoMapper.getUserByRolrId(userInfoPage);
            } else {
                userList = userInfoMapper.getUser(userInfoPage);
            }
            return new PageInfo<UserInfo>(userList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUser(HttpServletRequest request, UserInfo userInfo, boolean isDeleteRoleIds) {
        AtomicReference<Integer> res = new AtomicReference<>(0);
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 通过学号或工号查询已有用户
        List<UserInfo> oldUserList = userInfoMapper.getUserByCode(userInfo.getCode());
        // 用户id为-1，是新增用户，否则是修改
        if (userInfo.getUserId() == -1) {
            //不为空抛出异常
            BusinessResponseEnum.USERALREADYEXIST.assertIsEmpty(oldUserList, userInfo.getCode());
            // 如果通学号没有查询到用户，则新增，否则就是添加重复
            userInfo.setCreator(loginUser.getUserId());
            userInfo.setCreateTime(TimeUtil.getDateTime());

            // 把角色id从小到大排序，去第一个角色id作为默认角色
            int[] roleIds = userInfo.getRoleIds();
            if (roleIds.length > 0) {
                Arrays.sort(roleIds);
                userInfo.setRoleType(roleIds[0]);
            }
             res.set(userInfoMapper.addUser(userInfo));
            if (roleIds.length > 0) {
                userInfoMapper.addUserRole(userInfo.getUserId(), roleIds);
            }
        } else {
            // 如果通过学号没有查询到用户，或者查询到的用户与修改的id相同，则修改，否则修改用户重复
            BusinessResponseEnum.USERALREADYEXIST.assertIsTrue((oldUserList.isEmpty()
                            || isHaveSameId(oldUserList, userInfo.getUserId()))
                    , userInfo.getCode());
            userInfo.setOperator(loginUser.getUserId());
            userInfo.setUpdateTime(TimeUtil.getDateTime());
            res.set(userInfoMapper.updateUser(userInfo));
            // 是否删除角色和用户关联关系
            if (isDeleteRoleIds) {
                // 把角色id从小到大排序，去第一个角色id作为默认角色
                int[] roleIds = userInfo.getRoleIds();
                if (roleIds.length > 0) {
                    Arrays.sort(roleIds);
                    userInfo.setRoleType(roleIds[0]);
                    res.set(userInfoMapper.updateUserDefRole(userInfo));
                }
                // 删除原有的角色和用户关联关系
                userInfoMapper.deleteUserRoleByUserId(userInfo.getUserId());
                // 添加新的角色和用户的关联关系
                if (roleIds.length > 0) {
                    userInfoMapper.addUserRole(userInfo.getUserId(), roleIds);
                }
            }
        }
        return res.get();

    }

    private boolean isHaveSameId(List<UserInfo> oldUserList, int userId) {
        for (UserInfo userInfo : oldUserList) {
            if (userId == userInfo.getUserId()) {
                return true;// 有相同id返回true
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer resetUserPassword(UserInfo userInfo) {
            return userInfoMapper.resetUserPassword(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer userUpdatePassword(HttpServletRequest request, UserInfo userInfo) {
            UserInfo loginUser = Authentication.getCurrentUser(request);
            List<UserInfo> savedUserList = userInfoMapper.getUserByCode(loginUser.getCode().trim());
            // 账号不存在
            BusinessResponseEnum.USERNOTEXIST.assertIsNotEmpty(savedUserList);
            // 登录密码错误
            BusinessResponseEnum.ERRORPASSWORD.assertIsTrue(userInfo.getOldPassword().trim().equals(savedUserList.get(0).getPassword()));
            userInfo.setUserId(loginUser.getUserId());
            return userInfoMapper.resetUserPassword(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserIsStop(UserInfo userInfo) {
        return userInfoMapper.updateUserIsStop(userInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateUserDefRole(UserInfo userInfo) {
       return userInfoMapper.updateUserDefRole(userInfo);
    }

    @Override
    public UserInfo getCurrentUser(HttpServletRequest request, boolean isRole) {
        // 获取token
        String token = request.getHeader("authorization").replace(JwtUtil.TOKEN_PREFIX, "");
        // 通过token获取用户学号
        UserInfo user = JwtUtil.getUser(token);
        // 通过学号查询用户信息
        List<UserInfo> userInfoList = userInfoMapper.getUserByCode(user.getCode());
        UserInfo userInfo = userInfoList.get(0);
        // 是否查询权限
        if (isRole) {
            List<RoleInfo> roleList = roleInfoMapper.getRoleByUserId(userInfo.getUserId());
            userInfo.setRoleList(roleList);
        }
        return userInfo;

    }

    @Override
    public List<ResourceMenuTO> getMenu(HttpServletRequest request) {
        List<ResourceMenuTO> resourceTreeDataList = new ArrayList<>();
            // 获取token
            String token = request.getHeader("authorization").replace(JwtUtil.TOKEN_PREFIX, "");
            // 通过token获取用户学号
            UserInfo user = JwtUtil.getUser(token);
            // 通过学号查询用户信息
            List<UserInfo> userInfoList = userInfoMapper.getUserByCode(user.getCode());
            UserInfo userInfo = userInfoList.get(0);
            // 通过用户id查询用户拥有的角色信息
            List<RoleInfo> roleList = roleInfoMapper.getRoleByUserId(userInfo.getUserId());
            if (!roleList.isEmpty()) {
                // 判断用户第一角色是否是课程专家
                boolean isCourseExpert = false;
                if (roleList.get(0).getRoleId() == 2) {
                    isCourseExpert = true;
                }
                // 获取所有功能模块
                List<ResourceInfo> resourceInfoList = resourceInfoMapper.getResource(new ResourceInfoPage());
                // 把功能模块利用递归封装成树形数据
                for (int i = 0; i < resourceInfoList.size(); i++) {
                    ResourceInfo resourceInfo = resourceInfoList.get(i);

                    if (isCourseExpert && resourceInfo.getResourceId() == 4) {
                        resourceInfoList.remove(i);
                        i--;
                        continue;
                    }
                    if (resourceInfo.getParentId() == 0) {
                        ResourceMenuTO resourceTreeData = new ResourceMenuTO();
                        resourceTreeData.setId(resourceInfo.getResourceId());
                        resourceTreeData.setName(resourceInfo.getResourceName());
                        resourceTreeData.setPath(resourceInfo.getResourcePath());
                        resourceTreeData.setIcon(resourceInfo.getIcon());
                        resourceTreeDataList.add(resourceTreeData);

                        // 已经记录的数据移除，防止重复遍历
                        resourceInfoList.remove(i);
                        i--;
                    }
                }
                convertToTreeData(resourceInfoList, resourceTreeDataList);
                // 获取用户默认角色的模块权限
                List<ResourceInfo> selectedResourceList = resourceInfoMapper.getResourceByRoleId(userInfo.getRoleType());
                // 用递归移除掉所有模块树形数据中没有权限的模块
                removeNotAuthorityResource(selectedResourceList, resourceTreeDataList);
            }
            return resourceTreeDataList;

    }

    private void convertToTreeData(List<ResourceInfo> resourceInfoList, List<ResourceMenuTO> resourceTreeDataList) {
        for (int i = 0; i < resourceTreeDataList.size(); i++) {
            ResourceMenuTO resourceTreeData = resourceTreeDataList.get(i);
            for (int j = 0; j < resourceInfoList.size(); j++) {
                ResourceInfo resourceInfoChildren = resourceInfoList.get(j);

                // 树对象数据的id是模块的id，如果有模块的父id等于id，说明它是该树对象的子模块
                if (resourceTreeData.getId() == resourceInfoChildren.getParentId()) {
                    ResourceMenuTO resourceTreeDataChildren = new ResourceMenuTO();
                    resourceTreeDataChildren.setId(resourceInfoChildren.getResourceId());
                    resourceTreeDataChildren.setName(resourceInfoChildren.getResourceName());
                    resourceTreeDataChildren.setPath(resourceInfoChildren.getResourcePath());
                    resourceTreeDataChildren.setIcon(resourceInfoChildren.getIcon());
                    resourceTreeData.getRoutes().add(resourceTreeDataChildren);

                    // 已经记录的数据移除，防止重复遍历
                    resourceInfoList.remove(j);
                    j--;
                }
            }

            // 树对象的子模块集合不为空就继续寻找子模块的子模块
            if (resourceTreeData.getRoutes() != null && !resourceTreeData.getRoutes().isEmpty()) {
                convertToTreeData(resourceInfoList, resourceTreeData.getRoutes());
            }
        }
    }

    private void removeNotAuthorityResource(List<ResourceInfo> selectedResourceList, List<ResourceMenuTO> resourceTreeDataList) {
        for (int i = 0; i < resourceTreeDataList.size(); i++) {
            ResourceMenuTO resourceTreeData = resourceTreeDataList.get(i);
            // 获取树节点是否有子节点，从叶子节点开始删除没权限的模块
            if (resourceTreeData.getRoutes() != null && !resourceTreeData.getRoutes().isEmpty()) {
                removeNotAuthorityResource(selectedResourceList, resourceTreeData.getRoutes());
            }
            boolean isRemove = true;
            // 所有的子节点都判断完，在判断该节点是否还有子节点，如果没有就判断是否有权限，如果没有就移除
            if (resourceTreeData.getRoutes() == null || resourceTreeData.getRoutes().isEmpty()) {
                for (int j = 0; j < selectedResourceList.size(); j++) {
                    if (resourceTreeData.getId() == selectedResourceList.get(j).getResourceId()) {
                        isRemove = false;
                        // 移除已经对比过得数据，避免重复遍历
                        selectedResourceList.remove(j);
                        j--;
                        break;
                    }
                }
                if (isRemove) {
                    resourceTreeDataList.remove(i);
                    i--;
                }
            }
        }
    }

    @Override
    public List<UserInfo> getStuBySclassAndCode(StudentStatQuery studentStat) {
        return userInfoMapper.getSclassStu(studentStat.getSclassId(), null, studentStat.getCode());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserInfo uploadAvatar(HttpServletRequest request, MultipartFile file) {
        try {
            UserInfo loginUser = Authentication.getCurrentUser(request);
            String fileName = file.getOriginalFilename();
            String[] fileNameArray = fileName.split("\\.");
            String newFileName = loginUser.getUserId() + "_" + loginUser.getCode() + "." + fileNameArray[fileNameArray.length - 1];
            String folderPath = ExcelUtil.getProjectPath() + File.separator + "avatar";
            File folderPathFile = new File(folderPath);
            if (!folderPathFile.exists()) {
                folderPathFile.mkdir();
            }
            String filePath = folderPath + File.separator + newFileName;
            File filePathFile = new File(filePath);
            if (filePathFile.exists()) {
                filePathFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(file.getBytes());
            fos.close();
            UserInfo newUserInfo = getCurrentUser(request, true);
            newUserInfo.setAvatar("/avatar/" + newFileName);
            return newUserInfo;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateAccountInfo(HttpServletRequest request, UserInfo userInfo) {
            UserInfo loginUser = Authentication.getCurrentUser(request);
            userInfo.setOperator(loginUser.getUserId());
            userInfo.setUpdateTime(TimeUtil.getDateTime());
            return userInfoMapper.updateAccountInfo(userInfo);

    }

    @Override
    public List<UserInfo> getTeachers(HttpServletRequest request) {
        return userInfoMapper.getTeachers(getCurrentUser(request, false).getUserId());
    }

}
