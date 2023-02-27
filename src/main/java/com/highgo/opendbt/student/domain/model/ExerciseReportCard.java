package com.highgo.opendbt.student.domain.model;

public class ExerciseReportCard {

	private int exerciseId = -1; // 习题id
	private String exerciseName; // 习题名
	private int exerciseScore = 0; // 习题分值
	private int exerciseGoal = 0; // 习题得分
	private int exerciseSituation = -1; // 习题做题情况

	private int scoreId = -1; // 答题记录id
	private String answerTime; // 答题时间
	private int answerExecuteTime = 0; // 答案执行时间
	private int answerLength = 0; // 答案长度

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

	public int getExerciseScore() {
		return exerciseScore;
	}

	public void setExerciseScore(int exerciseScore) {
		this.exerciseScore = exerciseScore;
	}

	public int getExerciseGoal() {
		return exerciseGoal;
	}

	public void setExerciseGoal(int exerciseGoal) {
		this.exerciseGoal = exerciseGoal;
	}

	public int getExerciseSituation() {
		return exerciseSituation;
	}

	public void setExerciseSituation(int exerciseSituation) {
		this.exerciseSituation = exerciseSituation;
	}

	public int getScoreId() {
		return scoreId;
	}

	public void setScoreId(int scoreId) {
		this.scoreId = scoreId;
	}

	public String getAnswerTime() {
		return answerTime;
	}

	public void setAnswerTime(String answerTime) {
		this.answerTime = answerTime;
	}

	public int getAnswerExecuteTime() {
		return answerExecuteTime;
	}

	public void setAnswerExecuteTime(int answerExecuteTime) {
		this.answerExecuteTime = answerExecuteTime;
	}

	public int getAnswerLength() {
		return answerLength;
	}

	public void setAnswerLength(int answerLength) {
		this.answerLength = answerLength;
	}

	public ExerciseReportCard() {

	}

	public ExerciseReportCard(int exerciseId, String exerciseName, int exerciseScore, int exerciseGoal, int exerciseSituation) {
		this.exerciseId = exerciseId;
		this.exerciseName = exerciseName;
		this.exerciseScore = exerciseScore;
		this.exerciseGoal = exerciseGoal;
		this.exerciseSituation = exerciseSituation;
	}

}
