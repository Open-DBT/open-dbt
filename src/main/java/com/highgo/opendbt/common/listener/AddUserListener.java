package com.highgo.opendbt.common.listener;

import com.highgo.opendbt.catalogue.service.TCourseCatalogueService;
import com.highgo.opendbt.common.events.AddUserEvent;
import com.highgo.opendbt.homeworkmodel.service.THomeworkModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @Description: 新增学生时监听处理事件
 * @Title: AddUserListener
 * @Package com.highgo.opendbt.common.listener
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/21 11:04
 */
@Component
public class AddUserListener implements ApplicationListener<AddUserEvent> {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    THomeworkModelService homeworkModelService;
    @Autowired
    TCourseCatalogueService TCourseCatalogueService;

    @Override
    public void onApplicationEvent(AddUserEvent event) {
        long start = System.currentTimeMillis();
        //修改学生时发布该学生的信息
        TCourseCatalogueService.addStudentPublish(event.getRequest(), event.getClassId(), event.getUserId());
        //初始化作业相关表
        homeworkModelService.InitHomeWorkTables(event.getRequest(), event.getClassId(), event.getUserId());
        long end = System.currentTimeMillis();
        logger.info("新增学生{}初始化完成目录发布信息，作业发布信息，共用时({})毫秒",event.getUserId(),(end-start));
    }

}
