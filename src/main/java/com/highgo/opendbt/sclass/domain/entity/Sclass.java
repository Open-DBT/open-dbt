package com.highgo.opendbt.sclass.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.course.domain.entity.Course;
@TableName(value ="t_class")
public class Sclass {
	@TableId(value = "id")
	private int id = -1; // id
	@TableField(value = "class_name")
	private String className; // 班级名
	@TableField(value = "class_desc")
	private String classDesc; // 编辑描述
	@TableField(value = "class_start")
	private String classStart; // 班级开课时间
	@TableField(value = "class_end")
	private String classEnd; // 班级结课时间
	@TableField(value = "course_id")
	private int courseId; // 课程id
	@TableField(value = "creator")
	private int creator; // 创建人
	@TableField(exist = false)
	private Course course; // 课程信息
	@TableField(exist = false)
	private double progress; // 学习进度
	@TableField(exist = false)
	private String stuStartTime; // 学生开始学习时间
	@TableField(exist = false)
	private int isEnd; // 班级是否已结束，1=>结束，0=>进行中，-1=>未开始
	@TableField(exist = false)
	private String stuNewLearnTime; // 学生最新答题时间
	@TableField(exist = false)
	private int stuNumber; // 班级学生总数
	@TableField(exist = false)
	private int knowledgeNumber; // 班级有习题的知识点数
	@TableField(exist = false)
	private int exerciseNumber; // 班级课程的习题数
	@TableField(exist = false)
	private double overPercentage; // 超过学生的百分比
	@TableField(value = "class_is_open")
	private int classIsOpen; // 班级是否开班，1=>开班，0=>不开班

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassDesc() {
		return classDesc;
	}

	public void setClassDesc(String classDesc) {
		this.classDesc = classDesc;
	}

	public String getClassStart() {
		return classStart;
	}

	public void setClassStart(String classStart) {
		this.classStart = classStart;
	}

	public String getClassEnd() {
		return classEnd;
	}

	public void setClassEnd(String classEnd) {
		this.classEnd = classEnd;
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

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public String getStuStartTime() {
		return stuStartTime;
	}

	public void setStuStartTime(String stuStartTime) {
		this.stuStartTime = stuStartTime;
	}

	public int getIsEnd() {
		return isEnd;
	}

	public void setIsEnd(int isEnd) {
		this.isEnd = isEnd;
	}

	public int getStuNumber() {
		return stuNumber;
	}

	public void setStuNumber(int stuNumber) {
		this.stuNumber = stuNumber;
	}

	public int getKnowledgeNumber() {
		return knowledgeNumber;
	}

	public void setKnowledgeNumber(int knowledgeNumber) {
		this.knowledgeNumber = knowledgeNumber;
	}

	public int getExerciseNumber() {
		return exerciseNumber;
	}

	public void setExerciseNumber(int exerciseNumber) {
		this.exerciseNumber = exerciseNumber;
	}

	public String getStuNewLearnTime() {
		return stuNewLearnTime;
	}

	public void setStuNewLearnTime(String stuNewLearnTime) {
		this.stuNewLearnTime = stuNewLearnTime;
	}

	public double getOverPercentage() {
		return overPercentage;
	}

	public void setOverPercentage(double overPercentage) {
		this.overPercentage = overPercentage;
	}

	public int getClassIsOpen() {
		return classIsOpen;
	}

	public void setClassIsOpen(int classIsOpen) {
		this.classIsOpen = classIsOpen;
	}

}
