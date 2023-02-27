package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Description: 修改学生分数中间类
 * @Title: ResiveStudentScore
 * @Package com.highgo.opendbt.homework.domain.model.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/12/21 10:24
 */
@ApiModel(description = "修改学生分数中间类")
@Data
@ToString
@Accessors(chain = true)
public class ResiveStudentScore {
    @ApiModelProperty(value = "学生id", required = true)
    @NotNull(message = "学生id不能为空")
    private Integer studentId;
    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "作业id不能为空")
    private Integer homeworkId;
    @ApiModelProperty(value = "学生分数", required = true)
    @NotNull(message = "学生分数不能为空")
    private Double mark;
}
