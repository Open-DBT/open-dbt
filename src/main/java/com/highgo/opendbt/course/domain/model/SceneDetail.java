package com.highgo.opendbt.course.domain.model;
/*场景详情*/
public class SceneDetail {

	private int sceneDetailId; // 场景明细id
	private int sceneId; // 场景id
	private String tableName; // 表名
	private String tableDetail = ""; // 表的明细
	private String tableDesc = ""; // 标的描述

	public int getSceneDetailId() {
		return sceneDetailId;
	}

	public void setSceneDetailId(int sceneDetailId) {
		this.sceneDetailId = sceneDetailId;
	}

	public int getSceneId() {
		return sceneId;
	}

	public void setSceneId(int sceneId) {
		this.sceneId = sceneId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getTableDetail() {
		return tableDetail;
	}

	public void setTableDetail(String tableDetail) {
		this.tableDetail = tableDetail;
	}

	public String getTableDesc() {
		return tableDesc;
	}

	public void setTableDesc(String tableDesc) {
		this.tableDesc = tableDesc;
	}

	@Override
	public String toString() {
		return "SceneDetail [sceneDetailId=" + sceneDetailId + ", sceneId=" + sceneId + ", tableName=" + tableName
				+ ", tableDetail=" + tableDetail + ", tableDesc=" + tableDesc + "]";
	}

	public SceneDetail() {
		super();
	}

	public SceneDetail(int sceneId, String tableName) {
		super();
		this.sceneId = sceneId;
		this.tableName = tableName;
	}

}
