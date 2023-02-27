package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Description: 作业列表查询参数
 * @Title: ListHomeWorkVO
 * @Package com.highgo.opendbt.homework.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/16 9:38
 */
@ApiModel(description = "作业俩表查询VO")
@Data
@ToString
public class ListHomeWork {

    /**
     * 班级id
     */
    @ApiModelProperty(value = "班级id")
    private Integer classId;

    /**
     * 状态：0：全部  1：未开始 2：进行中  3：已结束
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id", required = true)
    @NotNull(message = "课程id不能为空")
    private Integer courseId;

    @ApiModelProperty(value = "作业名称", required = false)
    private String homeworkName;
}
