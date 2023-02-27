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

@ApiModel(description = "学生端保存作业")
@Data
@ToString
@Accessors(chain = true)
public class SaveHomework {

    /**
     * 作业id
     */
    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "作业id不能为空")
    private Integer homeworkId;


    /**
     * 学生作业详情
     */
    @Valid
    @NotEmpty
    private List<SaveHomeworkInfo> homeworkInfos;
}
