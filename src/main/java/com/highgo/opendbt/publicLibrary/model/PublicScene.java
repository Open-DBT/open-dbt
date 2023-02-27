package com.highgo.opendbt.publicLibrary.model;

import java.util.ArrayList;
import java.util.List;

public class PublicScene {

	private int sceneId = -1; // 场景id
	private String sceneName; // 场景名
	private String sceneDesc; // 场景描述
	private String initShell; // 初始化脚本
	private int creator; // 创建人id
	private String creatorName; // 创建人名
	private String createTime; // 创建时间
	private List<PublicSceneDetail> sceneDetailList = new ArrayList<PublicSceneDetail>(); // 场景明细list

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getSceneName() {
		return sceneName;
	}

	public void setSceneName(String sceneName) {
		this.sceneName = sceneName;
	}

	public String getSceneDesc() {
		return sceneDesc;
	}

	public void setSceneDesc(String sceneDesc) {
		this.sceneDesc = sceneDesc;
	}

	public String getInitShell() {
		return initShell;
	}

	public void setInitShell(String initShell) {
		this.initShell = initShell;
	}

	public List<PublicSceneDetail> getSceneDetailList() {
		return sceneDetailList;
	}

	public void setSceneDetailList(List<PublicSceneDetail> sceneDetailList) {
		this.sceneDetailList = sceneDetailList;
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

}
