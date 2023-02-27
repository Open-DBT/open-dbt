package com.highgo.opendbt.system.domain.model;

import com.highgo.opendbt.common.bean.PageTO;

public class UserInfoPage extends PageTO {

	private String userName; //用户名
	private String code; //学号
	private int roleId = -1; //角色id

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	@Override
	public String toString() {
		return "UserInfoPage [userName=" + userName + ", code=" + code + ", roleId=" + roleId + "]";
	}

}
