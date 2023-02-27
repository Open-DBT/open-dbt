package com.highgo.opendbt.course.domain.model;

import com.highgo.opendbt.common.bean.PageTO;

public class ExercisePage extends PageTO {

	private int courseId; // 课程id
	private String exerciseDesc; // 习题描述
	private int sceneId = -1; // 场景id
	private int knowledgeId = -1; // 知识点id

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public String getExerciseDesc() {
		return exerciseDesc;
	}

	public void setExerciseDesc(String exerciseDesc) {
		this.exerciseDesc = exerciseDesc;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public int getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(int knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	@Override
	public String toString() {
		return "ExercisePage [courseId=" + courseId + ", exerciseDesc=" + exerciseDesc + ", sceneId=" + sceneId
				+ ", knowledgeId=" + knowledgeId + "]";
	}

}
