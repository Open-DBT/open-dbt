package com.highgo.opendbt.common.bean;

import java.util.ArrayList;
import java.util.List;

public class KnowledgeTreeTO {

	private int id; // 知识点id
	private String text; // 知识点名
	private String keyword; // 知识点关键字
	private List<KnowledgeTreeTO> children = new ArrayList<KnowledgeTreeTO>(); // 知识点子类

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<KnowledgeTreeTO> getChildren() {
		return children;
	}

	public void setChildren(List<KnowledgeTreeTO> children) {
		this.children = children;
	}

	@Override
	public String toString() {
		return "KnowledgeTreeTO [id=" + id + ", text=" + text + ", keyword=" + keyword + "]";
	}

}
