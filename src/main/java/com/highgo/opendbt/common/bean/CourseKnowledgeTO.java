package com.highgo.opendbt.common.bean;

public class CourseKnowledgeTO {

	private int courseId; // 课程id
	private KnowledgeTreeTO knowledgeTree = new KnowledgeTreeTO(); // 知识树TO

	public int getCourseId() {
		return courseId;
	}

	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}

	public KnowledgeTreeTO getKnowledgeTree() {
		return knowledgeTree;
	}

	public void setKnowledgeTree(KnowledgeTreeTO knowledgeTree) {
		this.knowledgeTree = knowledgeTree;
	}

}
