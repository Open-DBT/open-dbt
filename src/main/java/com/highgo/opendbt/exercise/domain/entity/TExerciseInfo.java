package com.highgo.opendbt.exercise.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 *
 * @TableName t_exercise_info
 * 新习题明细表
 */
@TableName(value ="t_exercise_info")
@Data
@ToString
@Accessors(chain = true)
public class TExerciseInfo extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 试题id
     */
    @TableField(value = "exercise_id")
    private Integer exerciseId;

    /**
     * 选项前缀
     */
    @TableField(value = "prefix")
    private String prefix;

    /**
     * 选项内容
     */
    @TableField(value = "content")
    private String content;

}
