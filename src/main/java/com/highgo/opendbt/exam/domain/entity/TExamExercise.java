package com.highgo.opendbt.exam.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName t_exam_exercise
 */
@TableName(value ="t_exam_exercise")
@Data
public class TExamExercise implements Serializable {
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
    @TableField(value = "exercise_id")
    private Integer exerciseId;

    /**
     *
     */
    @TableField(value = "ordinal")
    private Integer ordinal;

    /**
     *
     */
    @TableField(value = "score")
    private Integer score;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
