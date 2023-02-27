package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Description: 批阅列表参数
 * @Title: ApprovalListVO
 * @Package com.highgo.opendbt.homework.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
@Data
@ToString
@ApiModel(description = "批阅数量统计参数")
public class ApprovalCount {
    /**
     * 班级id
     */
    @ApiModelProperty(value = "班级id")
    private Integer classId;
    /**
     * 学生姓名
     */
    @ApiModelProperty(value = "学生姓名")
    private String studentName;
    /**
     * 学号
     */
    @ApiModelProperty(value = "学号")
    private String studentCode;
    /**
     * 作业id
     */
    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "作业id不能为空")
    private Integer homeworkId;
    /**
     * 作业状态作业状态1：已提交2：未提交3：打回未提交
     */
    @ApiModelProperty(value = "作业状态", required = true)
    private Integer homeworkStatus;
}
