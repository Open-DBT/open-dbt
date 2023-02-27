package com.highgo.opendbt.system.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.system.domain.model.RoleInfo;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
//用户信息表
@TableName(value ="t_sys_user")
@Accessors(chain = true)
public class UserInfo {
	@TableId(value = "id")
	private Integer  userId = -1; // 用户id
	@TableField(value = "user_name")
	private String userName; // 用户名
	@TableField(value = "password")
	private String password = "000000"; // 密码
	@TableField(exist = false)
	private String oldPassword = "000000"; // 旧密码
	@TableField(value = "code")
	private String code; // 用户学号
	@TableField(value = "sex")
	private int sex = 1; // 性别
	@TableField(value = "mobile")
	private String mobile; // 电话
	@TableField(value = "avatar")
	private String avatar = "/avatar/default.png"; // 头像
	@TableField(value = "nick_name")
	private String nickName; // 昵称
	@TableField(value = "english_name")
	private String englishName; // 英文名
	@TableField(value = "creator")
	private int creator; // 创建人id
	@TableField(exist = false)
	private String creatorName; // 创建人姓名
	@TableField(value = "create_time")
	private String createTime; // 创建时间
	@TableField(value = "operator")
	private int operator; // 修改人id
	@TableField(value = "update_time")
	private String updateTime; // 修改时间
	@TableField(value = "is_stop")
	private int isStop = 0; // 是否停用
	@TableField(value = "role_type")
	private int roleType = -1; // 用户默认角色
	@TableField(exist = false)
	private int[] roleIds = new int[0]; // 用户关联角色的id的数组
	@TableField(exist = false)
	private List<RoleInfo> roleList = new ArrayList<RoleInfo>(); // 用户关联角色的信息
	@TableField(value = "school")
	private String school; // 学校
	@TableField(value = "major")
	private String major; // 专业
	@TableField(value = "mail")
	private String mail; // 邮件

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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEnglishName() {
		return englishName;
	}

	public void setEnglishName(String englishName) {
		this.englishName = englishName;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
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

	public int getIsStop() {
		return isStop;
	}

	public void setIsStop(int isStop) {
		this.isStop = isStop;
	}

	public int getRoleType() {
		return roleType;
	}

	public void setRoleType(int roleType) {
		this.roleType = roleType;
	}

	public int[] getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(int[] roleIds) {
		this.roleIds = roleIds;
	}

	public List<RoleInfo> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<RoleInfo> roleList) {
		this.roleList = roleList;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	@Override
	public String toString() {
		return "UserInfo [userId=" + userId + ", userName=" + userName + ", password=" + password + ", oldPassword="
				+ oldPassword + ", code=" + code + ", sex=" + sex + ", mobile=" + mobile + ", avatar=" + avatar
				+ ", nickName=" + nickName + ", englishName=" + englishName + ", creator=" + creator + ", creatorName="
				+ creatorName + ", createTime=" + createTime + ", operator=" + operator + ", updateTime=" + updateTime
				+ ", isStop=" + isStop + ", roleType=" + roleType + ", roleIds=" + Arrays.toString(roleIds)
				+ ", roleList=" + roleList + ", school=" + school + ", major=" + major + ", mail=" + mail + "]";
	}

	public UserInfo() {
		super();
	}

	public UserInfo(String userName, String code) {
		super();
		this.userName = userName;
		this.code = code;
	}

}
