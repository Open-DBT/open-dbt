package com.highgo.opendbt.api;

import com.highgo.opendbt.migration.service.MigrationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 历史数据迁移（慎用）
 * @Title: ExerciseApi
 * @Package com.highgo.opendbt.api
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@Api(tags = "历史数据迁移接口")
@RestController
@RequestMapping("/migration")
@CrossOrigin
public class DataMigrationApi {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    MigrationService migrationService;

    @ApiOperation(value = "历史数据迁移")
    @GetMapping("/migrationExercise")
    public boolean migrationExercise(HttpServletRequest request) {
        logger.debug("Enter,");
        return migrationService.migrationExercise(request);
    }

}
