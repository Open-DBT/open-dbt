package com.highgo.opendbt.homeworkmodel.domain.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @Description: 选中习题字段
 * @Title: ModelExerciseDTO
 * @Package com.highgo.opendbt.homeworkmodel.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/8 17:16
 */
@Data
@ToString
@Accessors(chain = true)
public class ModelExerciseDTO {
    //习题id
    private Integer exerciseId;
    //习题类型
    private Integer exerciseType;
}
