package com.highgo.opendbt.login.model;

public class CourseNotice {

	private int courseNoticeId;
	private int senderId;
	private int senderType;
	private int receiverId;
	private int receiverType;
	private String noticeContent;
	private int noticeType;
	private String createTime;
	private int courseId;
	private int sceneId;
	private int exerciseId;

	private boolean read;// 是否已读

	public int getCourseNoticeId() {
		return courseNoticeId;
	}

	public void setCourseNoticeId(int courseNoticeId) {
		this.courseNoticeId = courseNoticeId;
	}

	public int getSenderId() {
		return senderId;
	}

	public void setSenderId(int senderId) {
		this.senderId = senderId;
	}

	public int getSenderType() {
		return senderType;
	}

	public void setSenderType(int senderType) {
		this.senderType = senderType;
	}

	public int getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(int receiverId) {
		this.receiverId = receiverId;
	}

	public int getReceiverType() {
		return receiverType;
	}

	public void setReceiverType(int receiverType) {
		this.receiverType = receiverType;
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

	public boolean getRead() {
		return read;
	}

	public void setRead(boolean read) {
		this.read = read;
	}

	public CourseNotice() {

	}

	public CourseNotice(int senderId, int senderType, int receiverId, int receiverType,
			String noticeContent, int noticeType, String createTime, int courseId, int sceneId, int exerciseId) {
		super();
		this.senderId = senderId;
		this.senderType = senderType;
		this.receiverId = receiverId;
		this.receiverType = receiverType;
		this.noticeContent = noticeContent;
		this.noticeType = noticeType;
		this.createTime = createTime;
		this.courseId = courseId;
		this.sceneId = sceneId;
		this.exerciseId = exerciseId;
	}

}
