package com.highgo.opendbt.api;

import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.statistics.domain.model.StatisticsParam;
import com.highgo.opendbt.statistics.domain.model.StudentProgressStatistics;
import com.highgo.opendbt.statistics.domain.model.StudentStatistics;
import com.highgo.opendbt.statistics.service.StatisticsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Description: 统计相关的接口
 * @Title: StatisticsApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@Api(tags = "统计相关接口")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/statistics")
public class StatisticsApi {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final StatisticsService statisticsService;


    /**
     * @description: 章节目录统计接口
     * @author:
     * @date: 2022/8/10 11:30
     * @param: courseId  课程id
     * @param: catalogueId 目录id
     * @param: classId 班级id
     * @return: List<CourseCatalogue>
     **/
    @ApiOperation(value = "章节目录统计")
    @GetMapping("/getCatalogueProgress/{courseId}/{catalogueId}/{classId}/{serialNum}")
    public List<CourseCatalogue> catalogueStatistics(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "目录id", required = true) @PathVariable("catalogueId") int catalogueId, @ApiParam(value = "班级id", required = true) @PathVariable("classId") int classId, @ApiParam(value = "目录编号", required = true) @PathVariable("serialNum") String serialNum) {
        logger.debug("Enter, catalogueId = {},courseId = {},classId = {},serialNum = {}", catalogueId, courseId, classId, serialNum);
        return statisticsService.catalogueStatistics(request, courseId, catalogueId, classId, serialNum);
    }

    /**
     * @description: 章节统计查看
     * @author:
     * @date: 2022/8/10 17:28
     * @param: null
     * @return:
     **/
    @ApiOperation(value = "章节统计查看")
    @PostMapping("/getCatalogueInfo")
    public List<TCatalogueProcess> statisticslook(HttpServletRequest request, @RequestBody @Valid StatisticsParam param, BindingResult result) {
      if (result.hasErrors()) {
            throw new APIException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        logger.debug("Enter, param={}", param.toString());
        return statisticsService.statisticsLook(request, param);
    }

    /**
     * @description: 章节统计查看的任务信息
     * @author:
     * @date: 2022/8/24 12:24
     * @param: [request, param, result]
     * @return: java.util.List<com.highgo.opendbt.process.model.TCatalogueProcess>
     **/
    @ApiOperation(value = "章节统计查看的任务信息")
    @PostMapping("/getCatalogueInfoTitle")
    public Map<String, Object> getCatalogueInfoTitle(HttpServletRequest request, @RequestBody @Valid StatisticsParam param, BindingResult result) {
        if (result.hasErrors()) {
            throw new APIException(result.getFieldError().getDefaultMessage());
        }
        logger.debug("Enter, param={}", param.toString());
        return statisticsService.getCatalogueInfoTitle(request, param);
    }

    /**
     * @description: 学生进度统计
     * @author:
     * @date: 2022/8/11 14:42
     * @param: statistics
     * @return:
     **/
    @ApiOperation(value = "学生进度统计")
    @PostMapping("/getStudentProgress")
    public List<StudentStatistics> statisticsStudent(HttpServletRequest request, @Valid @RequestBody StudentProgressStatistics statistics, BindingResult result) {
        logger.debug("Enter, statistics = {}", statistics.toString());
        if (result.hasErrors()) {
            throw new APIException(Objects.requireNonNull(result.getFieldError()).getDefaultMessage());
        }
        return statisticsService.statisticsStudent(request, statistics);
    }


    /**
     * @description: 学生进度查看
     * @author:
     * @date: 2023/1/13 13:41
     * @param: [request, courseId 课程id, catalogueId 目录id, classId班级id, userId用户id, serialNum章节序号]
     * @return: java.util.Map<java.lang.String,java.lang.Object>
     **/
    @ApiOperation(value = "学生进度查看")
    @GetMapping("/getStudentInfo/{courseId}/{catalogueId}/{classId}/{userId}/{serialNum}")
    public Map<String, Object> statisticsStudentLook(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "目录id", required = true) @PathVariable("catalogueId") int catalogueId, @ApiParam(value = "班级id", required = true) @PathVariable("classId") int classId, @ApiParam(value = "人员id", required = true) @PathVariable("userId") int userId, @ApiParam(value = "目录编号", required = true) @PathVariable("serialNum") String serialNum) {
        logger.debug("Enter, catalogueId = {},courseId = {},classId = {},userId = {},serialNum = {}", catalogueId, courseId, classId, userId, serialNum);
        return statisticsService.statisticsStudentLook(request, courseId, catalogueId, classId, userId, serialNum);
    }


    @ApiOperation(value = "学生端某目录完成情况")
    @GetMapping("/getCatalogueStatisticsInfo/{courseId}/{catalogueId}/{classId}/{userId}")
    public Map<String, Integer> getCatalogueStatisticsInfo(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "目录id", required = true) @PathVariable("catalogueId") int catalogueId, @ApiParam(value = "班级id", required = true) @PathVariable("classId") int classId, @ApiParam(value = "人员id", required = true) @PathVariable("userId") int userId) {
        logger.debug("Enter, catalogueId = {},courseId = {},classId = {},userId = {}", catalogueId, courseId, classId, userId);
        return statisticsService.getCatalogueStatisticsInfo(request, courseId, catalogueId, classId, userId);
    }
}
