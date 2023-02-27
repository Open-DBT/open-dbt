package com.highgo.opendbt.progress.model;

// 班级统计tab2 覆盖率 学生列表
public class StudentCoverage {

	private int id; // 学生id
	private String userName; // 学生用户名
	private String code; // 学生学号
	private int correctCount;// 当前学生答对题目数
	private int answerCount;// 当前学生答过题目题总数
	private int exerciseCount;// 总题目数
	private Object avgCorrectCount;// 所有学生答对题目数平均值(答对人题数/总学生数)
	private Object avgAnswerCount;// 所有学生答过题目数平均值(做过人题数/总学生)

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

	public Object getAvgCorrectCount() {
		return avgCorrectCount;
	}

	public void setAvgCorrectCount(Object avgCorrectCount) {
		this.avgCorrectCount = avgCorrectCount;
	}

	public Object getAvgAnswerCount() {
		return avgAnswerCount;
	}

	public void setAvgAnswerCount(Object avgAnswerCount) {
		this.avgAnswerCount = avgAnswerCount;
	}

}
