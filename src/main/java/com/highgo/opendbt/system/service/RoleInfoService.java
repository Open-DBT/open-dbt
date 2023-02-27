package com.highgo.opendbt.system.service;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.system.domain.model.RoleInfo;

import javax.servlet.http.HttpServletRequest;

public interface RoleInfoService {
    //获取角色
    public PageInfo<RoleInfo> getRole(PageTO pageTO);

    //更新角色
    public ResultTO<Integer> updateRole(HttpServletRequest request, RoleInfo roleInfo);

    //删除角色
    public ResultTO<Integer> deleteRole(int roleId);

    //修改角色和模块的关联关系
    public ResultTO<Integer> updateRoleResource(RoleInfo roleInfo);

}
