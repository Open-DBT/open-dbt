package com.highgo.opendbt.system.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.utils.Message;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.system.mapper.ResourceInfoMapper;
import com.highgo.opendbt.system.mapper.RoleInfoMapper;
import com.highgo.opendbt.system.domain.model.ResourceInfo;
import com.highgo.opendbt.system.domain.model.RoleInfo;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.service.RoleInfoService;
import com.highgo.opendbt.system.service.UserInfoService;
import com.highgo.opendbt.common.utils.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class RoleInfoServiceImpl implements RoleInfoService {

    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleInfoMapper roleInfoMapper;

    @Autowired
    private ResourceInfoMapper resourceInfoMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Override
    public PageInfo<RoleInfo> getRole(PageTO pageTO) {
        // 分页查询配置
        PageHelper.startPage(pageTO.getCurrent(), pageTO.getPageSize());
        List<RoleInfo> roleInfoList = roleInfoMapper.getRole();
        PageInfo<RoleInfo> rolePageInfo = new PageInfo<RoleInfo>(roleInfoList);
        // 遍历分页后角色结果集查询角色拥有的模块功能
        List<RoleInfo> roleListPageInfo = rolePageInfo.getList();
        for (RoleInfo roleInfo : roleListPageInfo) {
            // 角色拥有模块的list
            List<ResourceInfo> resourceInfoList = resourceInfoMapper.getResourceByRoleId(roleInfo.getRoleId());
            // 把模块的id放到数组中，用于前端显示选中的模块
            int[] resourceIds = new int[resourceInfoList.size()];
            if (resourceInfoList != null && !resourceInfoList.isEmpty()) {
                for (int i = 0; i < resourceInfoList.size(); i++) {
                    resourceIds[i] = resourceInfoList.get(i).getResourceId();
                }
            }
            roleInfo.setResourceIds(resourceIds);
        }
        return rolePageInfo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultTO<Integer> updateRole(HttpServletRequest request, RoleInfo roleInfo) {
        try {
            // 获取用户信息 证
            UserInfo loginUser = Authentication.getCurrentUser(request);
            // 根据角色名称查询是否存在
            List<RoleInfo> oldRoleList = roleInfoMapper.getRoleByRoleName(roleInfo.getRoleName());
            // 角色id为-1，是新增角色，否则是修改
            if (roleInfo.getRoleId() == -1) {
                // 角色名称未被使用，则新增，否则就是添加重复
                if (oldRoleList.size() == 0) {
                    roleInfo.setCreator(loginUser.getUserId());
                    roleInfo.setCreateTime(TimeUtil.getDateTime());
                    roleInfoMapper.addRole(roleInfo);
                } else {
                    throw new Exception(Message.get("RoleAlreadyExist", roleInfo.getRoleName()));
                }
            } else {
                // 角色名称未被使用，或者角色名被使用但是与修改的id相同，则修改，否则就是角色名修改重复
                if (oldRoleList.size() == 0 || isHaveSameId(oldRoleList, roleInfo.getRoleId())) {
                    roleInfo.setOperator(loginUser.getUserId());
                    roleInfo.setUpdateTime(TimeUtil.getDateTime());
                    roleInfoMapper.updateRole(roleInfo);
                } else {
                    throw new Exception(Message.get("RoleAlreadyExist", roleInfo.getRoleName()));
                }
            }
            return ResultTO.OK();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    private boolean isHaveSameId(List<RoleInfo> oldRoleList, int roleId) throws Exception {
        for (RoleInfo roleInfo : oldRoleList) {
            if (roleId == roleInfo.getRoleId()) {
                return true;// 有相同的id则返回true
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultTO<Integer> deleteRole(int roleId) {
        try {
            // 删除角色和模块的关联关系
            roleInfoMapper.deleteRoleResource(roleId);
            // 删除角色
            roleInfoMapper.deleteRole(roleId);
            return ResultTO.OK();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultTO<Integer> updateRoleResource(RoleInfo roleInfo) {
        try {
            // 删除原来的角色和模块的关联关系
            roleInfoMapper.deleteRoleResource(roleInfo.getRoleId());
            // 数组不为空，添加新的角色和模块的关联关系
            if (roleInfo.getResourceIds().length > 0) {
                roleInfoMapper.addRoleResource(roleInfo.getRoleId(), roleInfo.getResourceIds());
            }
            return ResultTO.OK();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

}
