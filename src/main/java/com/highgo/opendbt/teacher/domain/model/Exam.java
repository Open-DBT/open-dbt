package com.highgo.opendbt.teacher.domain.model;

import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.sclass.domain.entity.Sclass;

import java.util.List;

public class Exam {

	private int id = -1; // id
	private String testName; // 名称
	private String testDesc; // 描述
	private int courseId; // 课程id
	private int creator; // 创建人
	private String createTime;// 创建时间
	private int exerciseSource = 0; // 题目来源，1=>公共题库，0=>当前课程
	private int exerciseCount = 0; // 作业题目总数

	private int isEnd = 0;// 是否已结束，1=>已结束，0=>进行中，-1=>未开始

	private Course course; // 课程信息

	private List<Sclass> selectedClassList;// 作业指定班级

	private String testStart; // 开课时间
	private String testEnd; // 结课时间

	private int examClassId = -1; // 作业和班级关联关系id

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTestName() {
		return testName;
	}

	public void setTestName(String testName) {
		this.testName = testName;
	}

	public String getTestDesc() {
		return testDesc;
	}

	public void setTestDesc(String testDesc) {
		this.testDesc = testDesc;
	}

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public int getCreator() {
		return creator;
	}

	public void setCreator(int creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getExerciseSource() {
		return exerciseSource;
	}

	public void setExerciseSource(int exerciseSource) {
		this.exerciseSource = exerciseSource;
	}

	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public List<Sclass> getSelectedClassList() {
		return selectedClassList;
	}

	public void setSelectedClassList(List<Sclass> selectedClassList) {
		this.selectedClassList = selectedClassList;
	}

	public int getExerciseCount() {
		return exerciseCount;
	}

	public void setExerciseCount(int exerciseCount) {
		this.exerciseCount = exerciseCount;
	}

	public String getTestStart() {
		return testStart;
	}

	public void setTestStart(String testStart) {
		this.testStart = testStart;
	}

	public String getTestEnd() {
		return testEnd;
	}

	public void setTestEnd(String testEnd) {
		this.testEnd = testEnd;
	}

	public int getExamClassId() {
		return examClassId;
	}

	public void setExamClassId(int examClassId) {
		this.examClassId = examClassId;
	}

}
