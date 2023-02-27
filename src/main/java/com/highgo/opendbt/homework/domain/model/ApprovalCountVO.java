package com.highgo.opendbt.homework.domain.model;

import lombok.Data;

/**
 * @Description: 批阅列表数量统计
 * @Title: ApprovalCountVO
 * @Package com.highgo.opendbt.homework.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/20 10:10
 */
@Data
public class ApprovalCountVO {
    /**总数*/
    private int totalNum=0;
    /**已提交数*/
    private int submitNum=0;
    /**未提交数*/
    private int unsubmitNum=0;
    /**作业名称*/
    private String homeworkName;
    /**班级名称*/
    private String className;
}
