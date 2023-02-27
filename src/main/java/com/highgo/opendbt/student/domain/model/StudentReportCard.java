package com.highgo.opendbt.student.domain.model;

import java.util.ArrayList;
import java.util.List;

public class StudentReportCard {

	private int studentId; // 学生用户id
	private String studentName; // 学生姓名
	private String studentCode; // 学生学号
	private int exerciseCount = 0; // 题目总数
	private int answerExerciseCount = 0; // 已答习题数
	private int exerciseGrossScore = 0; // 题目总分值
	private int studentGrossScore = 0; // 学生总分
	private List<ExerciseReportCard> exerciseReportCardList = new ArrayList<ExerciseReportCard>(); // 习题成绩单列表

	private String courseName; // 课程名
	private String examName; // 作业名
	private int examStatus; // 作业状态
	private String examStart; // 作业开始时间
	private String examEnd; // 作业结束时间

	private int isFinish; // 作业是否完成

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentCode() {
		return studentCode;
	}

	public void setStudentCode(String studentCode) {
		this.studentCode = studentCode;
	}

	public int getExerciseCount() {
		return exerciseCount;
	}

	public void setExerciseCount(int exerciseCount) {
		this.exerciseCount = exerciseCount;
	}

	public int getAnswerExerciseCount() {
		return answerExerciseCount;
	}

	public void setAnswerExerciseCount(int answerExerciseCount) {
		this.answerExerciseCount = answerExerciseCount;
	}

	public int getExerciseGrossScore() {
		return exerciseGrossScore;
	}

	public void setExerciseGrossScore(int exerciseGrossScore) {
		this.exerciseGrossScore = exerciseGrossScore;
	}

	public int getStudentGrossScore() {
		return studentGrossScore;
	}

	public void setStudentGrossScore(int studentGrossScore) {
		this.studentGrossScore = studentGrossScore;
	}

	public List<ExerciseReportCard> getExerciseReportCardList() {
		return exerciseReportCardList;
	}

	public void setExerciseReportCardList(List<ExerciseReportCard> exerciseReportCardList) {
		this.exerciseReportCardList = exerciseReportCardList;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getExamName() {
		return examName;
	}

	public void setExamName(String examName) {
		this.examName = examName;
	}

	public int getExamStatus() {
		return examStatus;
	}

	public void setExamStatus(int examStatus) {
		this.examStatus = examStatus;
	}

	public String getExamStart() {
		return examStart;
	}

	public void setExamStart(String examStart) {
		this.examStart = examStart;
	}

	public String getExamEnd() {
		return examEnd;
	}

	public void setExamEnd(String examEnd) {
		this.examEnd = examEnd;
	}

	public int getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(int isFinish) {
		this.isFinish = isFinish;
	}

}
