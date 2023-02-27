package com.highgo.opendbt.common.service.impl;

import com.highgo.opendbt.common.bean.DataSourceBean;
import com.highgo.opendbt.common.bean.DataTypeAndImg;
import com.highgo.opendbt.common.bean.ResultSetInfo;
import com.highgo.opendbt.common.bean.SchemaConnection;
import com.highgo.opendbt.common.service.RunAnswerService;
import com.highgo.opendbt.common.utils.CloseUtil;
import com.highgo.opendbt.common.utils.DataTypeImgUtil;
import com.highgo.opendbt.common.utils.Message;
import com.highgo.opendbt.common.utils.PostgresqlKeyWord;
import com.highgo.opendbt.course.domain.model.Scene;
import com.highgo.opendbt.course.mapper.SceneMapper;
import com.highgo.opendbt.publicLibrary.mapper.PublicSceneMapper;
import com.highgo.opendbt.publicLibrary.model.PublicScene;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RunAnswerServiceImpl implements RunAnswerService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private SceneMapper sceneMapper;

	@Autowired
	private PublicSceneMapper publicSceneMapper;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private DataSourceBean dataSourceBean;

	/**
	 * 初始化脚本，返回schema和指定schema名的连接
	 */
	@Override
	public void getSchemaConnection(UserInfo loginUser, int sceneId, int exerciseId, SchemaConnection schemaConnection, int exerciseSource) throws Exception {
		if (sceneId != -1) {
			// 根据场景id获取场景信息
			String initShell = "";
			// 习题来源exerciseSource为1是公题库
			if (exerciseSource == 1) {
				PublicScene scene = publicSceneMapper.getSceneDetail(sceneId);
				if (null == scene) {
					throw new Exception(Message.get("GetSceneFail"));
				}
				initShell = scene.getInitShell();
			} else {
				Scene scene = sceneMapper.getSceneDetail(sceneId);
				if (null == scene) {
					throw new Exception(Message.get("GetSceneFail"));
				}
				initShell = scene.getInitShell();
			}

			Statement statement = null;

			try {
				// 拼接具有唯一标识的schema名，并创建schema
				String schemaName = "schema_" + exerciseSource + "_" + loginUser.getCode() + "_" + exerciseId;
				schemaName = PostgresqlKeyWord.convertorToPostgresql(schemaName);

				schemaConnection.setSchemaName(schemaName);
				jdbcTemplate.execute("create schema " + schemaName + ";");
				logger.info("create schema success");

				// 连接串后拼接schema名，可连接指定schema
				String schemaUrl = dataSourceBean.url + "?currentSchema=" + schemaName;

				// 获取指定schema的连接
				Class.forName(dataSourceBean.driverClassName);
				Connection connection = DriverManager.getConnection(schemaUrl, dataSourceBean.username, dataSourceBean.password);

				schemaConnection.setConnection(connection);

				statement = connection.createStatement();

				// 执行初始化场景的sql脚本
				statement.executeUpdate(initShell);
			} catch (Exception e) {
				throw new Exception(Message.get("InitSceneFail", e.getMessage()));
			} finally {
				CloseUtil.close(statement);
			}
		}
	}

	private void setColumnAndData(ResultSet resultSet, List<String> columnList, List<DataTypeAndImg> dataTypeAndImgList, List<Map<Object, Object>> resultList)  throws Exception {

		int columnCount = resultSet.getMetaData().getColumnCount();

		//添加字段名、数据类型以及数据类型图标
		columnList.add(Message.get("SerialNumber"));
		dataTypeAndImgList.add(new DataTypeAndImg(Message.get("SerialNumber"), null));
		for (int i = 1; i <= columnCount; i++) {
			columnList.add(resultSet.getMetaData().getColumnName(i));
			dataTypeAndImgList.add(new DataTypeAndImg(resultSet.getMetaData().getColumnTypeName(i), DataTypeImgUtil.getDataTypeImgUrl(resultSet.getMetaData().getColumnTypeName(i))));
		}

		// 前端需要的唯一标识列
		int rowKey = 0;
		while (resultSet.next()) {
			Map<Object, Object> resultSetMap = new LinkedHashMap<Object, Object>();
			rowKey++;
			resultSetMap.put(0, rowKey);
			for (int i = 1; i <= columnCount; i++) {
				resultSetMap.put(i, resultSet.getObject(i));
			}
			resultList.add(resultSetMap);
		}
	}
	/**
	 * 获取ResultSet结果集的字段名和数据
	 */
	@Override
	public void getResultSetColumnAndData(ResultSet resultSet, List<String> columnList, List<DataTypeAndImg> dataTypeAndImgList, List<Map<Object, Object>> resultList) throws Exception {
		setColumnAndData(resultSet, columnList, dataTypeAndImgList, resultList);
	}

	/**
	 * 把ResultSet结果集数据转成list
	 */
	@Override
	public ResultSetInfo resultSetConvertList(ResultSet resultSet) throws Exception {
		ResultSetInfo resultSetInfo = new ResultSetInfo();

		List<String> columnList = resultSetInfo.getColumnList();
		List<DataTypeAndImg> dataTypeAndImgList = resultSetInfo.getDataTypeAndImgList();
		List<Map<Object, Object>> dataList = resultSetInfo.getDataList();
		setColumnAndData(resultSet, columnList, dataTypeAndImgList, dataList);

		resultSetInfo.setColumnNumber(columnList.size());
		resultSetInfo.setRowNumber(dataList.size());

		return resultSetInfo;
	}

	/**
	 * 删除schema
	 */
	@Override
	public void dropSchema(String schemaName) {
		try {
			if (null != schemaName) {
				jdbcTemplate.execute("drop schema " + schemaName + " cascade;");
				logger.info("drop schema success");
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 返回schema和指定schema名的连接，不执行脚本
	 */
	@Override
	public void getSchemaConnection(UserInfo loginUser, SchemaConnection schemaConnection) throws Exception {
		// 拼接具有唯一标识的schema名，并创建schema
		String schemaName = "schema_" + loginUser.getCode();
		schemaName = PostgresqlKeyWord.convertorToPostgresql(schemaName);

		schemaConnection.setSchemaName(schemaName);
		jdbcTemplate.execute("create schema " + schemaName + ";");
		logger.info("create schema success");

		// 连接串后拼接schema名，可连接指定schema
		String schemaUrl = dataSourceBean.url + "?currentSchema=" + schemaName;

		// 获取指定schema的连接
		Class.forName(dataSourceBean.driverClassName);
		Connection connection = DriverManager.getConnection(schemaUrl, dataSourceBean.username, dataSourceBean.password);

		schemaConnection.setConnection(connection);
	}

}
