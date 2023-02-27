package com.highgo.opendbt.homeworkmodel.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description: 作业模板列表查询参数
 * @Title: ListHomeWorkModelVO
 * @Package com.highgo.opendbt.homeworkmodel.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/6 9:48
 */
@ApiModel(description = "作业模板列表查询参数实体类")
@Data
@ToString
public class ListHomeWorkModel {
    /**
     *
     */
    private Integer id;

    /**
     * 课程id
     */
    @ApiModelProperty(value = "课程id",required=true)
    @NotNull(message = "课程id不能为空")
    private Integer courseId;

    /**
     * 父类id
     */
    private Integer parentId;

    /**
     * 0:试题，1:文件夹
     */
    private Integer elementType;

    /**
     * 作业模板名称
     */
    @ApiModelProperty(value = "文件夹/作业模板名称",required=false)
    private String modelName;

    /**
     * 1:私有，2:共享
     */
    private Integer authType;

    /**
     * 评分机制 1：百分制2：自定义
     */
    private Integer grandingStandard;

    /**
     * 题型归类1：是2：否
     */
    private Integer classify;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人员
     */
    private Integer createUser;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人员
     */
    private Integer updateUser;

    /**
     * 删除标志0：未删除1：已删除
     */
    private Short deleteFlag;

    /**
     * 删除时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    /**
     * 删除人员
     */
    private Integer deleteUser;

}
