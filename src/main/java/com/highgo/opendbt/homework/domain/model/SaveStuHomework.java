package com.highgo.opendbt.homework.domain.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "作业批阅提交homework")
@Data
@ToString
@Accessors(chain = true)
public class SaveStuHomework {

    /**
     * 作业id
     */
    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "作业id不能为空")
    private Integer homeworkId;


    /**
     * 学生ID
     */
    @ApiModelProperty(value = "学生id", required = true)
    @NotNull(message = "学生ID不能为空")
    private Integer studentId;
    /**
     * 评语
     */
    @ApiModelProperty(value = "作业评语")
    private String comments;
    @ApiModelProperty(value = "学生得分")
    private Double stuScore;
    /**
     * 学生作业详情
     */
    @Valid
    @NotEmpty
    private List<SaveStuHomeworkInfo> stuHomeworkInfos;
}
