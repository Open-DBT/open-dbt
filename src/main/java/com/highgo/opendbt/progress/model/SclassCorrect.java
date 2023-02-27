package com.highgo.opendbt.progress.model;

/**
 * 班级统计tab1 正确率 习题列表 | 答对人数、答题人数、全班人数
 *
 */
public class SclassCorrect {

	private int id;
	private String exerciseName;
	private int correctCount;
	private int answerCount;
	private int stuCount;

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

	public int getStuCount() {
		return stuCount;
	}

	public void setStuCount(int stuCount) {
		this.stuCount = stuCount;
	}

}
