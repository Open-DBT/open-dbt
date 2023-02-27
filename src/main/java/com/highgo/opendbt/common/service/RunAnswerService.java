package com.highgo.opendbt.common.service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import com.highgo.opendbt.common.bean.DataTypeAndImg;
import com.highgo.opendbt.common.bean.ResultSetInfo;
import com.highgo.opendbt.common.bean.SchemaConnection;
import com.highgo.opendbt.system.domain.entity.UserInfo;

public interface RunAnswerService {

	public void getSchemaConnection(UserInfo loginUser, int sceneId, int exerciseId, SchemaConnection schemaConnection, int exerciseSource) throws Exception;

	public void getResultSetColumnAndData(ResultSet resultSet, List<String> columnList, List<DataTypeAndImg> dataTypeAndImgList, List<Map<Object, Object>> resultList) throws Exception;

	public ResultSetInfo resultSetConvertList(ResultSet resultSet) throws Exception;

	public void dropSchema(String schemaName);

	public void getSchemaConnection(UserInfo loginUser, SchemaConnection schemaConnection) throws Exception;

}
