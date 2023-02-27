package com.highgo.opendbt.exercise.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @TableName t_exercise_type 习题类型表
 */
@TableName(value ="t_exercise_type")
@Data
public class TExerciseType implements Serializable {
    /**
     *
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 题型编码
     */
    @TableField(value = "type_code")
    private Integer typeCode;

    /**
     * 题型名称
     */
    @TableField(value = "type_name")
    private String typeName;

    /**
     * 题型类型 1:主观题2:客观题
     */
    @TableField(value = "type_style")
    private Integer typeStyle;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
