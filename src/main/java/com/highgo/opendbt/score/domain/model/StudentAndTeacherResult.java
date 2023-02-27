package com.highgo.opendbt.score.domain.model;

import com.highgo.opendbt.common.bean.ResultSetInfo;

public class StudentAndTeacherResult {

	private ResultSetInfo teacherResult = null; // 老师答案结果集
	private ResultSetInfo studentResult = null; // 学生答案结果集
	private boolean isCompare = true; // 是否对比

	public ResultSetInfo getTeacherResult() {
		return teacherResult;
	}

	public void setTeacherResult(ResultSetInfo teacherResult) {
		this.teacherResult = teacherResult;
	}

	public ResultSetInfo getStudentResult() {
		return studentResult;
	}

	public void setStudentResult(ResultSetInfo studentResult) {
		this.studentResult = studentResult;
	}

	public boolean getIsCompare() {
		return isCompare;
	}

	public void setIsCompare(boolean isCompare) {
		this.isCompare = isCompare;
	}

}
