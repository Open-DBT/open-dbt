package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Description: 学生端作业列表查询参数
 * @Title: ListStudentHomeWork
 * @Package com.highgo.opendbt.homework.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/16 9:38
 */
@ApiModel(description = "学生端作业列表查询")
@Data
@ToString
@Accessors(chain = true)
public class ListStudentHomeWork {


    @ApiModelProperty(value = "班级id")
    private Integer classId;

    @ApiModelProperty(value = "课程id", required = true)
    @NotNull(message = "课程id不能为空")
    private Integer courseId;

    @ApiModelProperty(value = "作业状态1：已提交2：未提交3:打回未提交")
    private Integer homeworkStatus;

    @ApiModelProperty(value = "学生id")
    private Integer studentId;

}
