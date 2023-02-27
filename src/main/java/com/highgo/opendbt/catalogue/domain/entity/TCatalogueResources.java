package com.highgo.opendbt.catalogue.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.highgo.opendbt.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @TableName t_catalogue_resources 目录资源表
 */
@TableName(value = "t_catalogue_resources")
@Data
@ToString
@Accessors(chain = true)
public class TCatalogueResources extends BaseEntity {
    /**
     * 主键
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id")
    private Long id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 目录id
     */
    @TableField(value = "catalogue_id")
    private Integer catalogueId;

    /**
     * 资源id
     */
    @TableField(value = "resources_id")
    private Integer resourcesId;
    /**
     * 任务类型0：资源1：作业
     */
    @TableField(value = "task_type")
    private Integer taskType;

    /**
     * 阈值
     */
    @TableField(value = "process_set")
    private BigDecimal processSet;

    /**
     * 下载权限
     * 0：可下载1：不可下载
     */
    @TableField(value = "download_auth")
    private Short downloadAuth;

    /**
     * 是否倍速
     * 0：可倍速1：不可倍速
     */
    @TableField(value = "is_speed")
    private Short isSpeed;

    /**
     * 是否任务点 0: 否 1：是
     */
    @TableField(value = "is_task")
    private Short isTask;

    /**
     * 是否快进 0：可快进 1：不可快进
     */
    @TableField(value = "fast_forward")
    private Short fastForward;

    /**
     * 完成状态用户学生端内容查询
     */
    @TableField(exist = false)
    private Integer studyStatus;
    /**
     * 学习时长学生端内容查询
     */
    @TableField(exist = false)
    private Integer duration;
    /**
     * 学习进度学生端内容查询
     */
    @TableField(exist = false)
    private Integer progress;

}
