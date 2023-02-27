package com.highgo.opendbt.course.domain.model;

import com.highgo.opendbt.common.bean.PageTO;

public class ScenePage extends PageTO {

	private int courseId; //课程id

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	@Override
	public String toString() {
		return "ScenePage [courseId=" + courseId + "]";
	}

}
