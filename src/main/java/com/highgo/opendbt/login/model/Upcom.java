package com.highgo.opendbt.login.model;

public class Upcom {

	private int id;// 待办id
	private String taskName;// 任务名
	private String taskDesc;// 任务描述

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getTaskDesc() {
		return taskDesc;
	}

	public void setTaskDesc(String taskDesc) {
		this.taskDesc = taskDesc;
	}

}
