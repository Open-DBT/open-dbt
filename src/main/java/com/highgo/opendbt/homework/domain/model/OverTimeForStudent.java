package com.highgo.opendbt.homework.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: 加时
 * @Title: OverTimeForStudent
 * @Package com.highgo.opendbt.homework.domain.model.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/12/9 10:19
 */
@ApiModel(description = "加时")
@Data
@ToString
@Accessors(chain = true)
public class OverTimeForStudent {
    @ApiModelProperty(value = "作业id", required = true)
    @NotNull(message = "作业id不能为空")
    private Integer homeworkId;
    @ApiModelProperty(value = "学生id", required = true)
    @NotNull(message = "学生id不能为空")
    private Integer studentId;
    @ApiModelProperty(value = "加时时间", required = true)
    @NotNull(message = "学生id不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
}
