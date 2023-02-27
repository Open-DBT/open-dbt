package com.highgo.opendbt.common.bean;

public class DataTypeAndImg {

	private String dataType; // 数据类型
	private String imgUrl; // 图片路径

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public DataTypeAndImg() {

	}

	public DataTypeAndImg(String dataType, String imgUrl) {
		this.dataType = dataType;
		this.imgUrl = imgUrl;
	}

}
