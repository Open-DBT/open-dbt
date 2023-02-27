package com.highgo.opendbt.process.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.highgo.opendbt.common.entity.BaseEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 *
 * @TableName t_catalogue_process 目录进度表
 */
@TableName(value ="t_catalogue_process")
@Data
@ToString
@NoArgsConstructor
@Accessors(chain = true)
public class TCatalogueProcess extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

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
     * 内容id
     */
    @TableField(value = "contents_id")
    private Integer contentsId;

    /**
     * 班级id
     */
    @TableField(value = "class_id")
    private Integer classId;

    /**
     * 学生id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 资源id
     */
    @TableField(value = "resources_id")
    private Integer resourcesId;

    /**
     * 学习状态1：未学完 2：已学完
     */
    @TableField(value = "study_status")
    private Short studyStatus;

    /**
     * 播放进度
     */
    @TableField(value = "progress")
    private Integer progress;

    /**
     * 观看时长
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @TableField(value = "duration")
    private Long duration;

    /**
     * 任务类型 0：资源1：作业
     */
    @TableField(value = "task_type")
    private Integer taskType;

    /**
     * 人员名称（用于统计显示学生名称）
     */
    @TableField(exist = false)
    private String  userName;
    /**
     * 学习进度百分比
     */
    @TableField(exist = false)
    private Integer  proportion;
    /**
     * 学号
     */
    @TableField(exist = false)
    private String  code;
    /**
     * 视频资源时长
     */
    @TableField(exist = false)
    private Integer  resourcesTime;
    /**
     * 资源名称
     */
    @TableField(exist = false)
    private String  resourcesName;
    /**
     * 完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Date  completeTime;

    public TCatalogueProcess(Integer courseId, Integer catalogueId, Integer classId, Integer userId, Integer resourcesId, Short studyStatus, Date createTime, Integer createUser) {
        this.courseId = courseId;
        this.catalogueId = catalogueId;
        this.classId = classId;
        this.userId = userId;
        this.resourcesId = resourcesId;
        this.studyStatus = studyStatus;
        this.createTime = createTime;
        this.createUser = createUser;
    }

    public TCatalogueProcess(Integer courseId, Integer catalogueId, Integer classId, Integer userId, Integer resourcesId, Short studyStatus, Integer progress, Long duration, Integer taskType, Date createTime, Integer createUser) {
        this.courseId = courseId;
        this.catalogueId = catalogueId;
        this.classId = classId;
        this.userId = userId;
        this.resourcesId = resourcesId;
        this.studyStatus = studyStatus;
        this.progress = progress;
        this.duration = duration;
        this.taskType = taskType;
        this.createTime = createTime;
        this.createUser = createUser;
    }
}
