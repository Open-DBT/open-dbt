package com.highgo.opendbt.score.domain.model;

import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.entity.Knowledge;

import java.util.ArrayList;
import java.util.List;

public class StuExerciseInfo {

	private int exerciseNumber; // 习题总数
	private int doneExerciseNumber; // 做过的习题数
	private Course course; // 课程信息
	private Knowledge knowledge; // 知识点信息
	private List<Exercise> exerciseList = new ArrayList<Exercise>(); // 习题列表

	public int getExerciseNumber() {
		return exerciseNumber;
	}

	public void setExerciseNumber(int exerciseNumber) {
		this.exerciseNumber = exerciseNumber;
	}

	public int getDoneExerciseNumber() {
		return doneExerciseNumber;
	}

	public void setDoneExerciseNumber(int doneExerciseNumber) {
		this.doneExerciseNumber = doneExerciseNumber;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public Knowledge getKnowledge() {
		return knowledge;
	}

	public void setKnowledge(Knowledge knowledge) {
		this.knowledge = knowledge;
	}

	public List<Exercise> getExerciseList() {
		return exerciseList;
	}

	public void setExerciseList(List<Exercise> exerciseList) {
		this.exerciseList = exerciseList;
	}

	public StuExerciseInfo() {

	}

	public StuExerciseInfo(int exerciseNumber, int doneExerciseNumber, Course course, Knowledge knowledge, List<Exercise> exerciseList) {
		this.exerciseNumber = exerciseNumber;
		this.doneExerciseNumber = doneExerciseNumber;
		this.course = course;
		this.knowledge = knowledge;
		this.exerciseList = exerciseList;
	}

}
