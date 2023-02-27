package com.highgo.opendbt.progress.model;

public class StuKnowledgeExerciseInfo {

	private int knowledgeId; // 知识点id
	private String name; // 知识点名
	private int exerciseNumber; // 知识点拥有习题个数
	private double progress; // 知识点学习进度，该知识点做过的题的个数/该知识点所拥有的题总数

	public int getKnowledgeId() {
		return knowledgeId;
	}

	public void setKnowledgeId(int knowledgeId) {
		this.knowledgeId = knowledgeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getExerciseNumber() {
		return exerciseNumber;
	}

	public void setExerciseNumber(int exerciseNumber) {
		this.exerciseNumber = exerciseNumber;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	@Override
	public String toString() {
		return "StuKnowledgeExerciseInfo [knowledgeId=" + knowledgeId + ", name=" + name + ", exerciseNumber="
				+ exerciseNumber + ", progress=" + progress + "]";
	}

}
