package com.highgo.opendbt.exam.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName t_exam 历史作业表
 */
@TableName(value ="t_exam")
@Data
public class TExam implements Serializable {
    /**
     *
     */
    @TableId(value = "id")
    private Integer id;

    /**
     *
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     *
     */
    @TableField(value = "test_name")
    private String testName;

    /**
     *
     */
    @TableField(value = "test_desc")
    private String testDesc;

    /**
     *
     */
    @TableField(value = "creator")
    private Integer creator;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     *
     */
    @TableField(value = "delete_time")
    private Date deleteTime;

    /**
     *
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    /**
     *
     */
    @TableField(value = "exercise_source")
    private Integer exerciseSource;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
