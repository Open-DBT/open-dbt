package com.highgo.opendbt.course.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ImportExerciseTO {

	private int courseId; //课程id
	private List<String> filePathList = new ArrayList<String>(); //导入文件的路径

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public List<String> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}

}
