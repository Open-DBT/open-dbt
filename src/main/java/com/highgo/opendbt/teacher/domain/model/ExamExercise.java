package com.highgo.opendbt.teacher.domain.model;

import com.highgo.opendbt.course.domain.entity.Exercise;

public class ExamExercise {

	private int id = -1; // id
	private int examId; // 作业id
	private int exerciseId; // 习题id
	private Exercise exercise; // 习题

	private int ordinal; // 序号
	private int score; // 每道题的分值

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

	public int getExerciseId() {
		return exerciseId;
	}

	public void setExerciseId(int exerciseId) {
		this.exerciseId = exerciseId;
	}

	public Exercise getExercise() {
		return exercise;
	}

	public void setExercise(Exercise exercise) {
		this.exercise = exercise;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
