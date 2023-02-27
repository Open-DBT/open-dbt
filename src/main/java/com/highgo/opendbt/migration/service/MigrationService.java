package com.highgo.opendbt.migration.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
@Service
public interface MigrationService {
    //历史数据迁移
    boolean migrationExercise(HttpServletRequest request);
}
