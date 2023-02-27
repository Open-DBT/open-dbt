package com.highgo.opendbt.system.mapper;

import com.highgo.opendbt.system.domain.model.ResourceInfo;
import com.highgo.opendbt.system.domain.model.ResourceInfoPage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceInfoMapper {

	/**
	 * 获取模块
	 *
	 * @param resourceInfoPage
	 * @return
	 * @
	 */
	public List<ResourceInfo> getResource(ResourceInfoPage resourceInfoPage) ;

	/**
	 * 通过角色id获取模块
	 *
	 * @param roleId
	 * @return
	 * @
	 */
	public List<ResourceInfo> getResourceByRoleId(@Param("roleId") int roleId) ;

}
