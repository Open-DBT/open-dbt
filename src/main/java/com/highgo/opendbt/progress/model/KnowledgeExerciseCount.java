package com.highgo.opendbt.progress.model;

public class KnowledgeExerciseCount {

	private int knowledgeId; // 知识点id
	private int progress; // 知识点进度或题目数

	public int getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(int knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

}
