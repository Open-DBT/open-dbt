package com.highgo.opendbt.homeworkmodel.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @TableName t_model_exercise
 * 模板习题关联表
 */
@TableName(value = "t_model_exercise")
@Data
@ToString
@Accessors(chain = true)
@ApiModel(description = "模板习题关联类")
public class TModelExercise extends BaseEntity {
    /**
     *主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 模板id
     */
    @TableField(value = "model_id")
    private Integer modelId;

    /**
     * 习题id
     */
    @ApiModelProperty(value = "习题id", required = true)
    @NotNull(message = "习题id不能为空")
    @TableField(value = "exercise_id")
    private Integer exerciseId;

    /**
     * 习题分数
     */
    @ApiModelProperty(value = "习题分数", required = true)
    @NotNull(message = "习题分数不能为空")
    @TableField(value = "exercise_score")
    private Double exerciseScore = 5.0;

    /**
     * 试题类型 1：主观 2：客观
     */
    @TableField(value = "exercise_style")
    private Integer exerciseStyle;
    /**
     * 试题类型 1：单选2：多选3：判断4：填空5：简答6：数据库题
     */
    @ApiModelProperty(value = "试题类型", required = true)
    @NotNull(message = "试题类型不能为空")
    @TableField(value = "exercise_type")
    private Integer exerciseType;
    /**
     * 排序
     */
    @TableField(value = "exercise_order")
    private Integer exerciseOrder;

    public TModelExercise() {
    }

    public TModelExercise(Integer modelId, @NotNull(message = "习题id不能为空") Integer exerciseId, @NotNull(message = "习题分数不能为空") Double exerciseScore, Integer exerciseStyle, @NotNull(message = "试题类型不能为空") Integer exerciseType, Date createTime, Integer createUser, Integer deleteFlag) {
        this.modelId = modelId;
        this.exerciseId = exerciseId;
        this.exerciseScore = exerciseScore;
        this.exerciseStyle = exerciseStyle;
        this.exerciseType = exerciseType;
        this.createTime = createTime;
        this.createUser = createUser;
        this.deleteFlag = deleteFlag;
    }
}
