package com.highgo.opendbt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Description: swagger
 * @Title: Swagger2Config
 * @Package com.highgo.opendbt
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/6 15:37
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket api(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.highgo.opendbt"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo getApiInfo(){
        return new ApiInfoBuilder()
                .title("Swagger2....")
                .description("Swagger2")
                .version("1.0")
                .license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0")
                .build();
    }
}
