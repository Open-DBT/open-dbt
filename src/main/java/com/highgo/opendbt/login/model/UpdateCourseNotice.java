package com.highgo.opendbt.login.model;

public class UpdateCourseNotice {

	private int updateCourseNoticeId;
	private int senderId;
	private int receiverId;
	private String noticeContent;
	private int noticeType;
	private String createTime;
	private int isRead = 0;
	private int courseId = 0;
	private int sceneId = 0;
	private int exerciseId = 0;

	public int getUpdateCourseNoticeId() {
		return updateCourseNoticeId;
	}

	public void setUpdateCourseNoticeId(int updateCourseNoticeId) {
		this.updateCourseNoticeId = updateCourseNoticeId;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public int getNoticeType() {
		return noticeType;
	}

	public void setNoticeType(int noticeType) {
		this.noticeType = noticeType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getIsRead() {
		return isRead;
	}

	public void setIsRead(int isRead) {
		this.isRead = isRead;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}

}
