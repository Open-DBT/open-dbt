package com.highgo.opendbt.homeworkmodel.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @TableName t_model_exercise_type  模板题型排序表
 */
@TableName(value ="t_model_exercise_type")
@Data
@ToString
@Accessors(chain = true)
public class TModelExerciseType implements Serializable {
    /**
     *
     */
    @TableId(value = "id",type= IdType.AUTO)
    private Integer id;

    /**
     * 模板id
     */
    @TableField(value = "model_id")
    private Integer modelId;

    /**
     * 习题类型id
     */
    @TableField(value = "exercise_type")
    private Integer exerciseType;

    /**
     * 排序
     */
    @TableField(value = "sort_num")
    private Integer sortNum;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 修改人员
     */
    @TableField(value = "update_user")
    private Integer updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public TModelExerciseType() {
    }

    public TModelExerciseType(Integer modelId, Integer exerciseType, Integer sortNum, Date updateTime, Integer updateUser) {
        this.modelId = modelId;
        this.exerciseType = exerciseType;
        this.sortNum = sortNum;
        this.updateTime = updateTime;
        this.updateUser = updateUser;
    }
}
