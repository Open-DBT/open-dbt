package com.highgo.opendbt.sclass.domain.model;

public class BatchDelClass {
	private int[] userId;
	private int sclassId;

	public int getSclassId() {
		return sclassId;
	}
	public void setSclassId(int sclassId) {
		this.sclassId = sclassId;
	}
	@Override
	public String toString() {
		return "BatchDelClass [userId=" + userId.toString() + ", sclassId=" + sclassId + "]";
	}
	public int[] getUserId() {
		return userId;
	}
	public void setUserId(int[] userId) {
		this.userId = userId;
	}

}
