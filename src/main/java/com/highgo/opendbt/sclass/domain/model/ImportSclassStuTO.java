package com.highgo.opendbt.sclass.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ImportSclassStuTO {

	private int sclassId; //班级id
	private List<String> filePathList = new ArrayList<String>(); //导入文件的路径

	public int getSclassId() {
		return sclassId;
	}

	public void setSclassId(int sclassId) {
		this.sclassId = sclassId;
	}

	public List<String> getFilePathList() {
		return filePathList;
	}

	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}

}
