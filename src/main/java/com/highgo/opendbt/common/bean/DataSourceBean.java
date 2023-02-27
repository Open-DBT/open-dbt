package com.highgo.opendbt.common.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataSourceBean {

	@Value("${spring.datasource.url}")
	public String url;

	@Value("${spring.datasource.driver-class-name}")
	public String driverClassName;

	@Value("${spring.datasource.username}")
	public String username;

	@Value("${spring.datasource.password}")
	public String password;

}
