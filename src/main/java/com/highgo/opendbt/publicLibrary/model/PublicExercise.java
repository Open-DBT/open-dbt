package com.highgo.opendbt.publicLibrary.model;

public class PublicExercise {

	private int exerciseId = -1; // 习题id
	private int sceneId = -1; // 场景id
	private String sceneName; // 场景名
	private String exerciseName; // 习题名
	private String exerciseDesc; // 习题描述
	private String exerciseAnalysis; // 习题描述
	private String answer; // 参考答案
	private int creator;
	private String creatorName;
	private String createTime;

	public int getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
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

	public String getExerciseAnalysis() {
		return exerciseAnalysis;
	}

	public void setExerciseAnalysis(String exerciseAnalysis) {
		this.exerciseAnalysis = exerciseAnalysis;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		this.answer = answer;
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

	@Override
	public String toString() {
		return "PublicExercise [exerciseId=" + exerciseId + ", sceneId=" + sceneId + ", sceneName=" + sceneName
				+ ", exerciseName=" + exerciseName + ", exerciseDesc=" + exerciseDesc + ", exerciseAnalysis="
				+ exerciseAnalysis + ", answer=" + answer + ", creator=" + creator + ", creatorName=" + creatorName
				+ ", createTime=" + createTime + "]";
	}

}
