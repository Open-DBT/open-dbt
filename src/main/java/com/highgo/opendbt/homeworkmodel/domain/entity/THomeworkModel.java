package com.highgo.opendbt.homeworkmodel.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @TableName t_homework_model 作业模板实体类
 */
@TableName(value = "t_homework_model")
@Data
@ToString
@Accessors(chain = true)
public class THomeworkModel extends BaseEntity {
    /**
     *主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 父类id
     */
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 0:试题，1:文件夹
     */
    @TableField(value = "element_type")
    private Integer elementType;

    /**
     * 作业模板名称
     */
    @TableField(value = "model_name")
    private String modelName;

    /**
     * 1:私有，2:共享
     */
    @TableField(value = "auth_type")
    private Integer authType;

    /**
     * 评分机制 1：百分制2：自定义
     */
    @TableField(value = "granding_standard")
    private Integer grandingStandard;

    /**
     * 题型归类1：是2：否
     */
    @TableField(value = "classify")
    private Integer classify;
    /**
     * 总分
     */
    @TableField(value = "tgp")
    private Double tgp;

    /**
     * 模板习题关联
     */
    @TableField(exist = false)
    private List<TModelExercise> modelExercises;
    /**
     * 用户名
     */
    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private List<THomeworkModel> childrens;
    /**
     * 发布状态 0：未发布 1：已发布
     */
    @TableField(exist = false)
    private Integer publishStatus;
    /**
     * 是否存在习题 0：不存在 1：存在
     */
    @TableField(exist = false)
    private Integer exist;

    //用于添加文件夹
    public THomeworkModel(Integer courseId, Integer parentId, Integer elementType, String modelName, Integer grandingStandard, Integer classify, Date createTime, Integer createUser) {
        this.courseId = courseId;
        this.parentId = parentId;
        this.elementType = elementType;
        this.modelName = modelName;
        this.grandingStandard = grandingStandard;
        this.classify = classify;
        this.createTime = createTime;
        this.createUser = createUser;
    }

    public THomeworkModel() {
    }
}
