package com.highgo.opendbt.exercise.domain.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 习题明细导入导出表
 * @Title: TNewExerciseExcel
 * @Package com.highgo.opendbt.exercise.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/14 9:43
 */
@Data
public class TNewExerciseInfoExcel implements Serializable {
    /**
     * 选项前缀
     */
    @Excel(name = "前缀", width = 30, isImportField = "true")
    private String prefix;

    /**
     * 选项内容
     */
    @Excel(name = "内容", width = 30, isImportField = "true")
    private String content;


}
