package com.highgo.opendbt.system.domain.model;

import java.util.Arrays;

public class RoleInfo {

	private int roleId = -1; //角色id
	private String roleName; //角色名
	private String roleDesc; //角色描述
	private int isPredefined = 0; //是否是默认角色，1=>默认角色，0=>自定义角色
	private int creator; //创建人id
	private String createTime; //创建时间
	private int operator; //修改人id
	private String updateTime; //修改时间
	private int isDelete = 0; //是否删除

	private int[] resourceIds = new int[0]; //关联模块的id的数组

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public int getIsPredefined() {
		return isPredefined;
	}

	public void setIsPredefined(int isPredefined) {
		this.isPredefined = isPredefined;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getOperator() {
		return operator;
	}

	public void setOperator(int operator) {
		this.operator = operator;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public int[] getResourceIds() {
		return resourceIds;
	}

	public void setResourceIds(int[] resourceIds) {
		this.resourceIds = resourceIds;
	}

	@Override
	public String toString() {
		return "RoleInfo [roleId=" + roleId + ", roleName=" + roleName + ", roleDesc=" + roleDesc + ", isPredefined="
				+ isPredefined + ", creator=" + creator + ", createTime=" + createTime + ", operator=" + operator
				+ ", updateTime=" + updateTime + ", isDelete=" + isDelete + ", resourceIds="
				+ Arrays.toString(resourceIds) + "]";
	}

}
