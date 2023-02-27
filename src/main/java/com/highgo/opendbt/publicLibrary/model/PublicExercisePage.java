package com.highgo.opendbt.publicLibrary.model;

import com.highgo.opendbt.common.bean.PageTO;

public class PublicExercisePage extends PageTO {

	private String exerciseDesc; // 习题描述
	private int sceneId = -1; // 场景id

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

	@Override
	public String toString() {
		return "PublicExercisePage [exerciseDesc=" + exerciseDesc + ", sceneId=" + sceneId + "]";
	}

}
