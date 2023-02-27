package com.highgo.opendbt.statistics.domain.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * @Description: 学生进度查看传参
 * @Title: StudentProgressStatistics
 * @Package com.highgo.opendbt.statistics.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/1 17:52
 */
@ToString
@Data
public class StudentProgressStatistics {
    @NotNull(message = "课程id不允许为空")
    private int courseId;
    @NotNull(message = "目录id不允许为空")
    private int catalogueId;
    @NotNull(message = "班级id不允许为空")
    private int classId;

    private String userName;
    private String code;
    /*0:否 1:是*/
    @NotNull(message = "是否为一级目录")
    private int isFirstLevel;
}
