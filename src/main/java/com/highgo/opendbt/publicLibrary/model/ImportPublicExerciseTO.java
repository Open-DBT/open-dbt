package com.highgo.opendbt.publicLibrary.model;

import java.util.ArrayList;
import java.util.List;

public class ImportPublicExerciseTO {

	private List<String> filePathList = new ArrayList<String>(); //导入文件的路径

	public List<String> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}

}
