package com.highgo.opendbt.homeworkmodel.domain.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Description: 作业模板中选题
 * @Title: SelectedExercisesDTO
 * @Package com.highgo.opendbt.homeworkmodel.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/8 16:35
 */
@Data
@ToString
public class SelectedExercisesDTO {
    //父类id
    private Integer parentId;
    //课程id
    private Integer courseId;
    //作业模板id
    private Integer modelId;
    //是否按题型归类
    @NotNull
    private Integer classify;
    //模板名称
    @NotBlank
    private String modelName;
    //选中的习题
    private List<ModelExerciseDTO> modelExerciseDTOS;
}
