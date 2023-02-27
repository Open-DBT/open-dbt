package com.highgo.opendbt.exercise.domain.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 题目设置为可练习习题/非练习习题
 * @Title: PublishExercise
 * @Package com.highgo.opendbt.exercise.domain.model
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2023/2/17 15:56
 */
@Data
@ToString
@Accessors(chain = true)
public class PublishExercise {
    @NotEmpty(message = "题目id不能为空")
    private List<Integer> ids;
    @NotNull(message = "题目练习状态设置不能为空")
    private int  exerciseStatus;
    @NotNull(message = "练习答案显示状态设置不能为空")
    private int  showAnswer;
}
