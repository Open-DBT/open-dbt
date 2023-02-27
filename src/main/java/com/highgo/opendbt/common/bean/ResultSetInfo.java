package com.highgo.opendbt.common.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResultSetInfo {

	private int columnNumber = 0; // 字段数
	private int rowNumber = 0; // 数据行数
	private List<String> columnList = new ArrayList<String>(); // 字段名list
	private List<DataTypeAndImg> dataTypeAndImgList = new ArrayList<DataTypeAndImg>(); // 数据类型和图标list
	private List<Map<Object, Object>> dataList = new ArrayList<Map<Object, Object>>(); // 数据list

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public int getRowNumber() {
		return rowNumber;
	}

	public void setRowNumber(int rowNumber) {
		this.rowNumber = rowNumber;
	}

	public List<String> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<String> columnList) {
		this.columnList = columnList;
	}

	public List<DataTypeAndImg> getDataTypeAndImgList() {
		return dataTypeAndImgList;
	}

	public void setDataTypeAndImgList(List<DataTypeAndImg> dataTypeAndImgList) {
		this.dataTypeAndImgList = dataTypeAndImgList;
	}

	public List<Map<Object, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<Object, Object>> dataList) {
		this.dataList = dataList;
	}

}
