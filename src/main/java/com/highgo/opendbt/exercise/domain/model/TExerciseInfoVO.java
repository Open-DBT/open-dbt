package com.highgo.opendbt.exercise.domain.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description: 习题选型明细
 * @Title: TExerciseInfoVO
 * @Package com.highgo.opendbt.exercise.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/2 10:32
 */
@Data
public class TExerciseInfoVO implements Serializable {

    private Integer id;
    /**
     * 选项前缀
     */
    private String prefix;

    /**
     * 选项内容
     */
    private String content;
    /**
     * 删除标记
     */
    private Integer deleteFlag;
}
