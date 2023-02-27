package com.highgo.opendbt.homeworkmodel.domain.model;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;

import java.util.List;

/**
 * @Description: 选题列表
 * @Title: ListExerciseDTO
 * @Package com.highgo.opendbt.homeworkmodel.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/29 13:59
 */
public class ListExerciseDTO {
    private PageInfo<TNewExercise> pageList;
    private Integer exerciseCount;
    private List<ModelExerciseDTO> modelExerciseDTOS;
}
