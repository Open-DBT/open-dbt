package com.highgo.opendbt.sclass.domain.model;

public class SclassStu {

	private int sclassId; //班级id
	private int userId; //用户id
	private String code; //学号
	private String userName; //用户名

	public int getSclassId() {
		return sclassId;
	}

	public void setSclassId(int sclassId) {
		this.sclassId = sclassId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public SclassStu() {
		super();
	}

	public SclassStu(int sclassId, int userId) {
		super();
		this.sclassId = sclassId;
		this.userId = userId;
	}

	public SclassStu(int sclassId, String code, String userName) {
		super();
		this.sclassId = sclassId;
		this.code = code;
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "SclassStu [sclassId=" + sclassId + ", userId=" + userId + ", code=" + code + ", userName=" + userName
				+ "]";
	}

}
