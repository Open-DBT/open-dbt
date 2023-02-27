package com.highgo.opendbt.homework.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.homework.domain.entity.THomework;
import com.highgo.opendbt.homework.domain.entity.TStuHomework;
import com.highgo.opendbt.homework.domain.model.*;
import com.highgo.opendbt.sclass.domain.entity.Sclass;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 *
 */
public interface THomeworkService extends IService<THomework> {

    //查询作业列表
    PageInfo<THomework> getHomeWork(HttpServletRequest request, @Valid PageParam<ListHomeWork> param);

    //删除作业
    boolean delHomeWork(HttpServletRequest request, int id);

    //发放设置查询
    THomework getHomeWorkSet(HttpServletRequest request, int id);

    //发放设置保存
    boolean saveHomeWorkSet(HttpServletRequest request, THomework param);

    //批阅列表查询
    PageInfo<TStuHomework> getApprovalList(HttpServletRequest request, @Valid PageParam<ApprovalList> param);

    //作业班级列表查询
    List<Sclass> getHomeWorkClazz(HttpServletRequest request, int id);

    //批阅列表数量统计
    ApprovalCountVO getApprovalCount(HttpServletRequest request, ApprovalCount param);

    //学生作业查看
    HomeWorkINfoModel review(HttpServletRequest request, HomeWrokView param);

    //批阅提交
    boolean approval(HttpServletRequest request, @Valid SaveStuHomework param);

    //批阅打回
    boolean callBack(HttpServletRequest request, int studentId, int homeworkId);

    //学生端查询作业列表
    List<HomeWrokByStudent> getHomeWorkByStudent(HttpServletRequest request, ListStudentHomeWork param);

    //学生端保存作业
    double saveHomeWork(HttpServletRequest request, SaveHomework homeWork);

    //学生端提交作业
    boolean submitHomeWork(HttpServletRequest request, SaveHomework homeWork);

    //作业加时操作
    boolean overTime(HttpServletRequest request, OverTimeForStudent overTimeForStudent);

    //学生作业修改
    TStuHomework reviseStudentScore(HttpServletRequest request, ResiveStudentScore resiveStudentScore);
}
