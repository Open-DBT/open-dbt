package com.highgo.opendbt.sclass.domain.model;

import com.highgo.opendbt.system.domain.model.UserInfoPage;

public class SclassUserPage extends UserInfoPage {

	private int sclassId = -1; // 班级id
	private String keyWord; // 学生姓名或学号

	public int getSclassId() {
		return sclassId;
	}

	public void setSclassId(int sclassId) {
		this.sclassId = sclassId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Override
	public String toString() {
		return "SclassUserPage [sclassId=" + sclassId + ", keyWord=" + keyWord + "]";
	}

}
