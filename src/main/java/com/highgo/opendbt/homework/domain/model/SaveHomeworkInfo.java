package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 *
 *
 * 学生端保存作业
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(description = "学生端保存作业")
public class SaveHomeworkInfo {

    /**
     * 习题id
     */
    @ApiModelProperty(value = "习题id", required = true)
    @NotNull(message = "习题ID不能为空")
    private Integer exerciseId;

    /**
     * 习题答案
     */
    @ApiModelProperty(value = "习题答案")
    private String exerciseResult;
    /**
     * 习题id
     */
    @ApiModelProperty(value = "习题类型")
    private Integer exerciseType;

}
