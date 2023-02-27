package com.highgo.opendbt.course.domain.model;

import java.util.ArrayList;
import java.util.List;
/*场景*/
public class Scene {

	private int sceneId = -1; // 场景id
	private int courseId; // 课程id
	private String sceneName; // 场景名
	private String sceneDesc; // 场景描述
	private String initShell; // 初始化脚本

	private List<SceneDetail> sceneDetailList = new ArrayList<SceneDetail>(); // 场景明细list

	private int parentId = 0; // 复制的场景的id

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
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

	public List<SceneDetail> getSceneDetailList() {
		return sceneDetailList;
	}

	public void setSceneDetailList(List<SceneDetail> sceneDetailList) {
		this.sceneDetailList = sceneDetailList;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	@Override
	public String toString() {
		return "Scene [sceneId=" + sceneId + ", courseId=" + courseId + ", sceneName=" + sceneName + ", sceneDesc="
				+ sceneDesc + ", initShell=" + initShell + ", sceneDetailList=" + sceneDetailList + ", parentId="
				+ parentId + "]";
	}

}
