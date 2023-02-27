package com.highgo.opendbt.feedback.model;

import com.highgo.opendbt.system.domain.entity.UserInfo;

public class Feedback {

	private int id;
	private String content;
	private String mobile;
	private int creator; // 创建人id
	private String createTime;

	private UserInfo user;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	@Override
	public String toString() {
		return "Feedback [id=" + id + ", content=" + content + ", mobile=" + mobile + ", creator=" + creator
				+ ", createTime=" + createTime + "]";
	}
	public UserInfo getUser() {
		return user;
	}
	public void setUser(UserInfo user) {
		this.user = user;
	}

}
