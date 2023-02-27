package com.highgo.opendbt.login.model;

public class Notice {

	private int id;// 通知id
	private int userId;// 用户id
	private String userName;// 用户名
	private String avatar;// 用户头像
	private String createTime;// 创建时间
	private String noticeContent;// 通知内容
	private int roleType;// 角色类型
	private boolean read;// 是否已读

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public Notice() {
		super();
	}

	public Notice(int userId, String createTime, String noticeContent, int roleType) {
		super();
		this.userId = userId;
		this.createTime = createTime;
		this.noticeContent = noticeContent;
		this.roleType = roleType;
	}

}
