package com.highgo.opendbt.homeworkmodel.domain.model;

import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @Description: 查询模板习题详情中间类
 * @Title: findModelExerciseModel
 * @Package com.highgo.opendbt.homeworkmodel.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/10/26 11:17
 */
@Data
@ToString
@Accessors(chain = true)
public class ModelExerciseModel {
    //分类后的集合
    private List<TNewExercise> collect;
    //题型编码
    private int typeCode;
    //题型名称
    private String typeName;
    //题型下题目数量
    private int exerciseCount;
    //题型下题目分数
    private double score;
    //题型排序
    private int sortNum;
}
