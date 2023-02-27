package com.highgo.opendbt.system.mapper;

import com.highgo.opendbt.system.domain.model.RoleInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleInfoMapper {

	/**
	 * 获取角色
	 *
	 * @return
	 * @
	 */
	public List<RoleInfo> getRole() ;

	/**
	 * 通过角色名获取角色
	 *
	 * @param roleName
	 * @return
	 * @
	 */
	public List<RoleInfo> getRoleByRoleName(@Param("roleName") String roleName) ;

	/**
	 * 新增角色
	 *
	 * @param roleInfo
	 * @return
	 * @
	 */
	public Integer addRole(RoleInfo roleInfo) ;

	/**
	 * 修改角色
	 *
	 * @param roleInfo
	 * @return
	 * @
	 */
	public Integer updateRole(RoleInfo roleInfo) ;

	/**
	 * 删除角色
	 *
	 * @param roleId
	 * @return
	 * @
	 */
	public Integer deleteRole(@Param("roleId") int roleId) ;

	/**
	 * 删除角色和模块关联关系
	 *
	 * @param roleId
	 * @
	 */
	public void deleteRoleResource(@Param("roleId") int roleId) ;

	/**
	 * 添加角色和模块关联关系
	 *
	 * @param roleId
	 * @param resourceIds
	 * @
	 */
	public void addRoleResource(@Param("roleId") int roleId, @Param("resourceIds") int[] resourceIds) ;

	/**
	 * 通过用户id获取角色
	 *
	 * @param userId
	 * @return
	 * @
	 */
	public List<RoleInfo> getRoleByUserId(@Param("userId") int userId) ;

}
