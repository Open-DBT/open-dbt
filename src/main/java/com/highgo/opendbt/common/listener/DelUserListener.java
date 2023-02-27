package com.highgo.opendbt.common.listener;

import com.highgo.opendbt.catalogue.service.TCourseCatalogueService;
import com.highgo.opendbt.common.events.DelUserEvent;
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
public class DelUserListener implements ApplicationListener<DelUserEvent> {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    THomeworkModelService homeworkModelService;
    @Autowired
    TCourseCatalogueService TCourseCatalogueService;

    @Override
    public void onApplicationEvent(DelUserEvent event) {
        long start = System.currentTimeMillis();
        //删除发布学生信息
        TCourseCatalogueService.delStudentPublish(event.getRequest(), event.getClassId(), event.getUsers());
        //删除作业相关信息
        homeworkModelService.delInitHomeWorkTables(event.getRequest(), event.getClassId(), event.getUsers());
        long end = System.currentTimeMillis();
        logger.info("删除学生{}初始化完成目录发布信息，作业发布信息，共用时({})毫秒", event.getUsers(), (end - start));
    }

}
