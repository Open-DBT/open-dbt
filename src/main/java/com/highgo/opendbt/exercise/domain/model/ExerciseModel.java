package com.highgo.opendbt.exercise.domain.model;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Description: 习题目录树查询实体
 * @Title: ExerciseModel
 * @Package com.highgo.opendbt.exercise.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/31 13:25
 */
@Data
public class ExerciseModel {
    /**
     * 课程id集合
     */
    @NotEmpty(message = "课程id不能为空")
    private List<Integer> courseIdList;
}
