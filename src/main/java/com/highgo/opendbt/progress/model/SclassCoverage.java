package com.highgo.opendbt.progress.model;

/**
 * 班级统计tab2 覆盖率 学生列表 | 答对题数量、答过题数量、总题目数
 *
 */
public class SclassCoverage {

	private int id;
	private String userName;
	private String code;
	private int correctCount;// 答对题
	private int answerCount;// 答题总数
	private int submitAnswerCount;// 提交次数
	private int exerciseCount;// 总题目数

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getCorrectCount() {
		return correctCount;
	}

	public void setCorrectCount(int correctCount) {
		this.correctCount = correctCount;
	}

	public int getExerciseCount() {
		return exerciseCount;
	}

	public void setExerciseCount(int exerciseCount) {
		this.exerciseCount = exerciseCount;
	}

	public int getAnswerCount() {
		return answerCount;
	}

	public void setAnswerCount(int answerCount) {
		this.answerCount = answerCount;
	}

	public int getSubmitAnswerCount() {
		return submitAnswerCount;
	}

	public void setSubmitAnswerCount(int submitAnswerCount) {
		this.submitAnswerCount = submitAnswerCount;
	}

}
