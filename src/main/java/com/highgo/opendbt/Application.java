package com.highgo.opendbt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Description: 项目启动类
 * @Title: Application
 * @Package com.highgo.opendbt
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/6 15:37
 */
@SpringBootApplication
@ServletComponentScan
@MapperScan(basePackages = {"com.highgo.opendbt.*.dao", "com.highgo.opendbt.*.mapper"})
public class Application extends SpringBootServletInitializer {
    //springboot项目通过war包方式启动
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

   /* 	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}*/
}
