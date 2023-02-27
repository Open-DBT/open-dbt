package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 * @Description: 作业查看编辑
 * @Title: HomeWrokView
 * @Package com.highgo.opendbt.homework.domain.model.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/12/9 10:19
 */
@ApiModel(description = "作业查看编辑")
@Data
@ToString
@Accessors(chain = true)
public class HomeWrokView {
    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "作业id不能为空")
    private Integer homeworkId;
    @ApiModelProperty(value = "学生id", required = true)
    @NotNull(message = "学生id不能为空")
    private Integer studentId;
    @ApiModelProperty(value = "是否显示答案解析", required = true)
    @NotNull(message = "标记不能为空")
    private Integer flag;
}
