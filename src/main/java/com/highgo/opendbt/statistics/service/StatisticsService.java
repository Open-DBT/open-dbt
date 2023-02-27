package com.highgo.opendbt.statistics.service;

import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.statistics.domain.model.StudentStatistics;
import com.highgo.opendbt.statistics.domain.model.StatisticsParam;
import com.highgo.opendbt.statistics.domain.model.StudentProgressStatistics;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;


public interface StatisticsService {
    //学生进度统计
    List<StudentStatistics> statisticsStudent(HttpServletRequest request, StudentProgressStatistics statis);

    //学生进度查看
    Map<String, Object> statisticsStudentLook(HttpServletRequest request, int courseId, int catalogueId, int classId, int userId, String serialNum);

    //章节目录统计
    List<CourseCatalogue> catalogueStatistics(HttpServletRequest request, int courseId, int catalogueId, int classId, String serialNum);

    //章节统计查看
    List<TCatalogueProcess> statisticsLook(HttpServletRequest request, StatisticsParam param);

    //章节统计查看的任务信息
    Map<String, Object> getCatalogueInfoTitle(HttpServletRequest request, StatisticsParam param);

    //用于学生端目录学习进度保存后，资源完成情况查询
    Map<String, Integer> getCatalogueStatisticsInfo(HttpServletRequest request, int courseId, int catalogueId, int classId, int userId);
}
