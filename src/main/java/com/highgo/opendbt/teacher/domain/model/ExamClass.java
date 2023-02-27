package com.highgo.opendbt.teacher.domain.model;

import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.sclass.domain.entity.Sclass;

public class ExamClass {

	private int id = -1; // id
	private int examId; // 作业id
	private Exam exam; // 作业
	private int classId; // 班级id
	private Sclass sclass; // 班级
	private int courseId; // 课程id
	private Course course; // 课程
	private int creator; // 创建人

	private String testStart; // 开课时间
	private String testEnd; // 结课时间
	private boolean testIsOpen = false; // 是否可见，true=>可见，false=>不可见

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getExamId() {
		return examId;
	}

	public void setExamId(int examId) {
		this.examId = examId;
	}

	public int getClassId() {
		return classId;
	}

	public void setClassId(int classId) {
		this.classId = classId;
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

	public Exam getExam() {
		return exam;
	}

	public void setExam(Exam exam) {
		this.exam = exam;
	}

	public Sclass getSclass() {
		return sclass;
	}

	public void setSclass(Sclass sclass) {
		this.sclass = sclass;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public boolean isTestIsOpen() {
		return testIsOpen;
	}

	public void setTestIsOpen(boolean testIsOpen) {
		this.testIsOpen = testIsOpen;
	}

}
