package com.highgo.opendbt.exercise.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 批量删除VO
 * @Title: TNewExerciseDelVO
 * @Package com.highgo.opendbt.exercise.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/7 13:44
 */
@Data
@ToString
@ApiModel(description = "批量删除习题VO")
public class TNewExerciseDelVO {
    //习题id集合
    @ApiModelProperty(value = "习题id集合",required=true)
    @NotEmpty(message = "习题id集合不能为空")
    private List<Integer> exerciseIds;
}
