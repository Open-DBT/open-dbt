package com.highgo.opendbt.score.domain.model;

import com.highgo.opendbt.common.bean.ResultSetInfo;

import java.util.HashMap;
import java.util.Map;

public class UpdateRowAndResultSetTO {

	private int updateRow = -1;// 更新的行数
	private Map<String, ResultSetInfo> resultSetInfoMap = new HashMap<String, ResultSetInfo>();// 结果集list

	public int getUpdateRow() {
		return updateRow;
	}

	public void setUpdateRow(int updateRow) {
		this.updateRow = updateRow;
	}

	public Map<String, ResultSetInfo> getResultSetInfoMap() {
		return resultSetInfoMap;
	}

	public void setResultSetInfoMap(Map<String, ResultSetInfo> resultSetInfoMap) {
		this.resultSetInfoMap = resultSetInfoMap;
	}

}
