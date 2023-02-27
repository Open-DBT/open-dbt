package com.highgo.opendbt.progress.model;

// 学生统计/tab1 正确率 习题列表
public class StudentCorrect {

	private int id; // 习题id
	private String exerciseName; // 习题名
	private int correctCount; // 当前学生本题答对次数
	private int answerCount; // 当前学生本题答题总次数
	private int allCorrectCount; // 本题答对学生人数
	private int allAnswerCount; // 本题做过的学生数
	private int stuCount; // 全班人数

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getExerciseName() {
		return exerciseName;
	}

	public void setExerciseName(String exerciseName) {
		this.exerciseName = exerciseName;
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public int getAllCorrectCount() {
		return allCorrectCount;
	}

	public void setAllCorrectCount(int allCorrectCount) {
		this.allCorrectCount = allCorrectCount;
	}

	public int getAllAnswerCount() {
		return allAnswerCount;
	}

	public void setAllAnswerCount(int allAnswerCount) {
		this.allAnswerCount = allAnswerCount;
	}

	public int getStuCount() {
		return stuCount;
	}

	public void setStuCount(int stuCount) {
		this.stuCount = stuCount;
	}

}
