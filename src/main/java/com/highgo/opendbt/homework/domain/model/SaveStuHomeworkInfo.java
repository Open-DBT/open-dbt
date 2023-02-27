package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

/**
 *
 * @TableName t_stu_homework_info
 * 学生作业明细表
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(description = "作业批阅提交homeworkinfo")
public class SaveStuHomeworkInfo  {

    /**
     * 习题id
     */
    @ApiModelProperty(value = "习题id", required = true)
    @NotNull(message = "习题ID不能为空")
    private Integer exerciseId;

    /**
     * 习题分数
     */
    @ApiModelProperty(value = "习题分数", required = true)
    @NotNull(message = "习题分数不能为空")
    private Double exerciseScore;

    /**
     * 习题答案
     */
    @ApiModelProperty(value = "习题答案", required = true)
    //@NotBlank(message = "习题答案不能为空")
    private String exerciseResult;

    /**
     * 是否正确1：是2：否
     */
    @ApiModelProperty(value = "答案判定", required = true)
    @NotNull(message = "习题答案判定不能为空")
    private Integer isCorrect;
}
