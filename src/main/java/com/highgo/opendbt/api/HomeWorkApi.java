package com.highgo.opendbt.api;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.common.utils.ValidationUtil;
import com.highgo.opendbt.homework.domain.entity.THomework;
import com.highgo.opendbt.homework.domain.entity.TStuHomework;
import com.highgo.opendbt.homework.domain.model.*;
import com.highgo.opendbt.homework.service.THomeworkService;
import com.highgo.opendbt.homeworkmodel.service.THomeworkModelService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 作业相关接口类
 * @Title: HomeWorkApi
 * @Package com.highgo.opendbt.api
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:29
 */
@Api(tags = "作业相关接口")
@RestController
@RequestMapping("/homeWork")
@CrossOrigin
public class HomeWorkApi {
    Logger logger = LoggerFactory.getLogger(getClass());
    final
    THomeworkModelService homeworkModelService;
    final
    THomeworkService homeworkService;

    public HomeWorkApi(THomeworkModelService homeworkModelService, THomeworkService homeworkService) {
        this.homeworkModelService = homeworkModelService;
        this.homeworkService = homeworkService;
    }

    @ApiOperation(value = "获取作业列表")
    @PostMapping("/getHomeWork")
    public PageInfo<THomework> getHomeWork(HttpServletRequest request, @RequestBody @Valid PageParam<ListHomeWork> param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        PageInfo<THomework> homeWork = homeworkService.getHomeWork(request, param);
        return homeWork;
    }

    @ApiOperation(value = "删除作业")
    @GetMapping("/delHomeWork/{id}")
    public boolean delHomeWork(HttpServletRequest request, @ApiParam(value = "作业id", required = true) @PathVariable("id") int id) {
        logger.debug("Enter,id={}", id);
        return homeworkService.delHomeWork(request, id);
    }

    @ApiOperation(value = "发放设置查询")
    @GetMapping("/getHomeWorkSet/{id}")
    public THomework getHomeWorkSet(HttpServletRequest request, @ApiParam(value = "作业id", required = true) @PathVariable("id") int id) {
        logger.debug("Enter,id={}", id);
        return homeworkService.getHomeWorkSet(request, id);
    }

    @ApiOperation(value = "发放设置保存")
    @PostMapping("/saveHomeWorkSet")
    public boolean saveHomeWorkSet(HttpServletRequest request, @ApiParam(value = "发放设置保存", required = true) @RequestBody @Valid THomework param, BindingResult result) {
        logger.debug("Enter,param={}", param.toString());
        //校验
        ValidationUtil.Validation(result);
        return homeworkService.saveHomeWorkSet(request, param);
    }

    @ApiOperation(value = "批阅列表")
    @PostMapping("/getApprovalList")
    public PageInfo<TStuHomework> getApprovalList(HttpServletRequest request, @ApiParam(value = "批阅列表", required = true) @RequestBody @Valid PageParam<ApprovalList> param, BindingResult result) {
        logger.debug("Enter,param={}", param.toString());
        //校验
        ValidationUtil.Validation(result);
        return homeworkService.getApprovalList(request, param);
    }

    @ApiOperation(value = "作业班级列表查询")
    @GetMapping("/getHomeWorkClazz/{id}")
    public List<Sclass> getHomeWorkClazz(HttpServletRequest request, @ApiParam(value = "作业id", required = true) @PathVariable("id") int id) {
        logger.debug("Enter,id={}", id);
        return homeworkService.getHomeWorkClazz(request, id);
    }

    @ApiOperation(value = "批阅列表数量统计")
    @PostMapping("/getApprovalCount")
    public ApprovalCountVO getApprovalCount(HttpServletRequest request, @ApiParam(value = "批阅列表", required = true) @RequestBody @Valid ApprovalCount param, BindingResult result) {
        logger.debug("Enter,param={}", param.toString());
        //校验
        ValidationUtil.Validation(result);
        return homeworkService.getApprovalCount(request, param);
    }

    @ApiOperation(value = "批阅查看")
    @PostMapping("/review")
    public HomeWorkINfoModel review(HttpServletRequest request, @RequestBody @Valid HomeWrokView param, BindingResult result) {
        logger.debug("Enter,param={}", param.toString());
        ValidationUtil.Validation(result);
        return homeworkService.review(request, param);
    }

    @ApiOperation(value = "批阅提交")
    @PostMapping("/approval")
    public boolean approval(HttpServletRequest request, @RequestBody @Valid SaveStuHomework param, BindingResult result) {
        logger.debug("Enter,param={}", param.toString());
        //校验
        ValidationUtil.Validation(result);
        return homeworkService.approval(request, param);
    }

    @ApiOperation(value = "批阅打回")
    @GetMapping("/callBack/{homeworkId}/{studentId}")
    public boolean callBack(HttpServletRequest request, @ApiParam(value = "作业id", required = true) @PathVariable("homeworkId") int homeworkId, @ApiParam(value = "学生id", required = true) @PathVariable("studentId") int studentId) {
        logger.debug("Enter,studentId={},homeworkId={}", studentId, homeworkId);
        return homeworkService.callBack(request, studentId, homeworkId);
    }

    @ApiOperation(value = "学生端作业列表查询")
    @PostMapping("/getHomeWrokByStudent")
    public List<HomeWrokByStudent> getHomeWorkByStudent(HttpServletRequest request, @RequestBody @Valid ListStudentHomeWork homeWork, BindingResult result) {
        logger.debug("Enter,homeWork={}", homeWork.toString());
        ValidationUtil.Validation(result);
        return homeworkService.getHomeWorkByStudent(request, homeWork);
    }

    @ApiOperation(value = "学生端作业保存")
    @PostMapping("/saveHomeWrok")
    public double saveHomeWork(HttpServletRequest request, @RequestBody @Valid SaveHomework homeWork, BindingResult result) {
        logger.debug("Enter,homeWork={}", homeWork.toString());
        ValidationUtil.Validation(result);
        return homeworkService.saveHomeWork(request, homeWork);
    }

    @ApiOperation(value = "学生端作业提交")
    @PostMapping("/submitHomeWrok")
    public boolean submitHomeWork(HttpServletRequest request, @RequestBody @Valid SaveHomework homeWork, BindingResult result) {
        logger.debug("Enter,homeWork={}", homeWork.toString());
        ValidationUtil.Validation(result);
        return homeworkService.submitHomeWork(request, homeWork);
    }

    @ApiOperation(value = "教师对学生加时操作")
    @PostMapping("/overTime")
    public boolean overTime(HttpServletRequest request, @RequestBody @Valid OverTimeForStudent overTimeForStudent, BindingResult result) {
        logger.debug("Enter,param={}", overTimeForStudent.toString());
        ValidationUtil.Validation(result);
        return homeworkService.overTime(request, overTimeForStudent);
    }

    @ApiOperation(value = "学生作业分数修改")
    @PostMapping("/reviseStudentScore")
    public TStuHomework reviseStudentScore(HttpServletRequest request, @RequestBody @Valid ResiveStudentScore resiveStudentScore, BindingResult result) {
        logger.debug("Enter,param={}", resiveStudentScore.toString());
        ValidationUtil.Validation(result);
        return homeworkService.reviseStudentScore(request, resiveStudentScore);
    }
}
