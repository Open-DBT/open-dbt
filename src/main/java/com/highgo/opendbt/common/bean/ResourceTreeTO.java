package com.highgo.opendbt.common.bean;

import java.util.ArrayList;
import java.util.List;

public class ResourceTreeTO {

	private String title; // 模块名
	private int key; // 模块id
	private List<ResourceTreeTO> children = new ArrayList<ResourceTreeTO>(); // 模块的子模块

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getKey() {
		return key;
	}

	public void setKey(int key) {
		this.key = key;
	}

	public List<ResourceTreeTO> getChildren() {
		return children;
	}

	public void setChildren(List<ResourceTreeTO> children) {
		this.children = children;
	}

}
