package com.highgo.opendbt.common.bean;

import java.sql.Connection;

public class SchemaConnection {

	private String schemaName;// schema名
	private Connection connection;// 数据库连接

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

}
