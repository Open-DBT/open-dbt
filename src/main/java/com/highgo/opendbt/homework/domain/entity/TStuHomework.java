package com.highgo.opendbt.homework.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.highgo.opendbt.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @TableName t_stu_homework 学生作业表
 */
@TableName(value = "t_stu_homework")
@Data
@ToString
@Accessors(chain = true)
public class TStuHomework extends BaseEntity {
    /**
     *主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业id
     */
    @TableField(value = "homework_id")
    private Integer homeworkId;

    /**
     * 学生的id
     */
    @TableField(value = "student_id")
    private Integer studentId;

    /**
     * 学生的名字
     */
    @TableField(value = "student_name")
    private String studentName;

    /**
     * 班级id
     */
    @TableField(value = "class_id")
    private Integer classId;

    /**
     * 班级名称
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * 分数
     */
    @TableField(value = "score")
    private Double score;

    /**
     * 作业状态1：已提交2：未提交3：打回未提交
     */
    @TableField(value = "homework_status")
    private Integer homeworkStatus;

    /**
     * 批阅状态1：已批阅2：待批阅
     */
    @TableField(value = "check_status")
    private Integer checkStatus;
    /**
     * 提交时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "submit_time")
    private Date submitTime;
    /**
     * 批阅时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "approval_time")
    private Date approvalTime;
    /**
     * 加时后结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "end_time")
    private Date endTime;
    /**
     * 评语
     */
    @TableField(value = "comments")
    private String comments;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 学号
     */
    @TableField(value = "student_code")
    private String studentCode;

    /**
     * 批阅人
     */
    @TableField(value = "approval_user_id")
    private Integer approvalUserId;
    /**
     * 作业开始时间
     */
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date homeworkStartTime;
    /**
     * 作业结束时间
     */
    @TableField(exist = false)
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date homeworkEndTime;

    /**
     * 批阅人
     */
    @TableField(exist = false)
    private String approvalUser;

    public TStuHomework() {
    }

    public TStuHomework(Integer homeworkId, Integer studentId, String studentName, Integer classId, Double score, Integer homeworkStatus, Integer checkStatus, Integer deleteFlag) {
        this.homeworkId = homeworkId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.classId = classId;
        this.score = score;
        this.homeworkStatus = homeworkStatus;
        this.checkStatus = checkStatus;
        this.deleteFlag = deleteFlag;
    }
}
