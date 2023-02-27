package com.highgo.opendbt.exam.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName t_exam_class
 */
@TableName(value ="t_exam_class")
@Data
public class TExamClass implements Serializable {
    /**
     *
     */
    @TableId(value = "id")
    private Integer id;

    /**
     *
     */
    @TableField(value = "exam_id")
    private Integer examId;

    /**
     *
     */
    @TableField(value = "class_id")
    private Integer classId;

    /**
     *
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     *
     */
    @TableField(value = "test_start")
    private String testStart;

    /**
     *
     */
    @TableField(value = "test_end")
    private String testEnd;

    /**
     *
     */
    @TableField(value = "test_is_open")
    private Boolean testIsOpen;

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

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
