package com.highgo.opendbt.exercise.domain.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 习题查询VO
 * @Title: TNewExerciseVO
 * @Package com.highgo.opendbt.exercise.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/8 15:32
 */
@Data
@ToString
public class TNewExerciseDTO {


    /**
     * 父类id
     */
    @NotNull(message = "父类习题id不能为空")
    private Integer parentId = 0;


    /**
     * 课程id集合
     */
    private List<Integer> courseIdList;


    /**
     * 知识点名称
     */

    private String knowledge;

    /**
     * 题型集合
     */

    private List<Integer> exerciseTypeList;

    /**
     * 习题名称
     */
    private String exerciseName;

    /**
     * 模板id 用于作业模板选题时查询
     */
    private Integer modelId;

}
