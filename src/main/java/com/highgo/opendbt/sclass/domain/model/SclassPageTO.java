package com.highgo.opendbt.sclass.domain.model;

import com.highgo.opendbt.common.bean.PageTO;

public class SclassPageTO extends PageTO {

	private int type = 0; // 0进行中，-1未开始，1已毕业

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "SclassPageTO [type=" + type + "]";
	}

}
