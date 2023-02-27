package com.highgo.opendbt.homework.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highgo.opendbt.homework.domain.entity.THomeworkDistribution;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Description: 发布传参
 * @Title: PublishHomeWorkVO
 * @Package com.highgo.opendbt.homework.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/7 19:07
 */
@Data
@ToString
@Accessors(chain = true)
@ApiModel(description = "作业发布")
public class PublishHomeWork {
    /**
     *主键
     */
    private Integer id;

    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id", required = true)
    @NotNull(message = "课程id不能为空")
    private Integer courseId;

    /**
     * 作业名称
     */
    @ApiModelProperty(value = "作业名称", required = true)
    @NotBlank(message = "作业名称不能为空")
    private String homeworkName;

    /**
     * 作业模板id
     */
    @ApiModelProperty(value = "作业模板id", required = true)
    @NotNull(message = "作业模板id不能为空")
    private Integer modelId;

    /**
     * 作业模板名称
     */
    @ApiModelProperty(value = "作业模板名称", required = true)
    @NotBlank(message = "作业模板名称不能为空")
    private String modelName;

    /**
     * 开始时间
     */
    @ApiModelProperty(value = "开始时间", required = true)
    @NotNull(message = "开始时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(value = "结束时间", required = true)
    @NotNull(message = "结束时间不能为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 1:允许补交2：不允许补交
     */
    @ApiModelProperty(value = "1:允许补交2：不允许补交", required = true)
    @NotNull(message = "是否允许补交不能为空")
    private Integer allowAfter;

    /**
     * 及格分数
     */
    private Short passMark;

    /**
     * 总分数
     */
    private Double score;

    /**
     * 重做次数
     */
    private Integer redoTimes;

    /**
     * 是否取最高成绩1：是2：否
     */
    private Integer maxScore;

    /**
     * 答案查看时间1：批阅后2：提交后3：作业结束后4：不允许
     */
    @ApiModelProperty(value = "答案查看时间1：批阅后2：提交后3：作业结束后4：不允许", required = true)
    @NotNull(message = "答案查看时间不能为空")
    private Integer viewTime;
    /**
     * 是否填空题不区分大小写1：是2：否
     */
    private Integer ignoreCase;

    /**
     * 多选题未选全给一半分1：是2：否
     */
    private Integer unselectedGiven;

    /**
     * 发放范围
     */
    @ApiModelProperty(value = "发放范围", required = true)
    @NotEmpty(message = "发放范围不能为空")
    private List<THomeworkDistribution> distributions;
}
