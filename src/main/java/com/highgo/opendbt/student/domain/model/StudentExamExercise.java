package com.highgo.opendbt.student.domain.model;

public class StudentExamExercise {

	private int exerciseId = -1; // 习题id
	private String exerciseName; // 习题名
	private String exerciseDesc; // 习题描述
	private int exerciseScore = 10; // 习题分值

	private int sceneId = -1; // 场景id
	private String sceneName; // 场景名
	private String sceneDesc; // 场景描述
	private String initShell; // 初始化脚本

	private int exerciseSituation = -1; // 习题做题情况

	private String studentAnswer; // 学生答案

	public int getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}

	public String getExerciseName() {
		return exerciseName;
	}

	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}

	public String getExerciseDesc() {
		return exerciseDesc;
	}

	public void setExerciseDesc(String exerciseDesc) {
		this.exerciseDesc = exerciseDesc;
	}

	public int getExerciseScore() {
		return exerciseScore;
	}

	public void setExerciseScore(int exerciseScore) {
		this.exerciseScore = exerciseScore;
	}

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

	public int getExerciseSituation() {
		return exerciseSituation;
	}

	public void setExerciseSituation(int exerciseSituation) {
		this.exerciseSituation = exerciseSituation;
	}

	public String getStudentAnswer() {
		return studentAnswer;
	}

	public void setStudentAnswer(String studentAnswer) {
		this.studentAnswer = studentAnswer;
	}

	@Override
	public String toString() {
		return "StudentExamExercise [exerciseId=" + exerciseId + ", exerciseName=" + exerciseName + ", exerciseDesc="
				+ exerciseDesc + ", exerciseScore=" + exerciseScore + ", sceneId=" + sceneId + ", sceneName="
				+ sceneName + ", sceneDesc=" + sceneDesc + ", initShell=" + initShell + ", exerciseSituation="
				+ exerciseSituation + ", studentAnswer=" + studentAnswer + "]";
	}

}
