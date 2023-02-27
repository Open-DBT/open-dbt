package com.highgo.opendbt.exercise.domain.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description: 题库导入导出表
 * @Title: TNewExerciseExcel
 * @Package com.highgo.opendbt.exercise.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/14 9:43
 */
@Data
@ExcelTarget("newExercise")
public class TNewExerciseExcel implements Serializable {
    @Excel(name = "id", width = 10, needMerge = true, isImportField = "true")
    private Integer id;
    @Excel(name = "父类id", width = 10, needMerge = true, isImportField = "true")
    private Integer parentId;
    @Excel(name = "题目名称", width = 30, needMerge = true, isImportField = "true")
    private String exerciseName;
    @Excel(name = "类型", width = 10, needMerge = true, replace = {"习题_0", "文件夹_1"}, addressList = true, isImportField = "true")
    private Integer elementType;
    @Excel(name = "题目类别", width = 20, needMerge = true, replace = {"单选题_1", "多选题_2", "判断题_3", "填空题_4", "简答题_5", "SQL编程题_6","_null"}, addressList = true)
    private Integer exerciseType;
    @Excel(name = "难度", width = 10, needMerge = true, replace = {"简单_1", "一般_2", "困难_3"}, addressList = true)
    private Integer exerciseLevel;
    @Excel(name = "题干", width = 40, needMerge = true)
    private String stem;
    @Excel(name = "答案", width = 40, needMerge = true)
    private String standardAnswser;
    @Excel(name = "答案解析", width = 40, needMerge = true)
    private String exerciseAnalysis;
    @ExcelCollection(name = "习题选项/填空题答案")
    private List<TNewExerciseInfoExcel> exerciseInfos;
    @Excel(name = "场景", width = 20, needMerge = true)
    private String sceneName;
    @Excel(name = "创建人", width = 30, needMerge = true)
    private String createUser;
    @Excel(name = "创建时间", width = 30, needMerge = true, databaseFormat = "yyyy-MM-dd HH:mm:ss", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;


}
