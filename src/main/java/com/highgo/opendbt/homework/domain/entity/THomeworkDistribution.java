package com.highgo.opendbt.homework.domain.entity;

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
 * @TableName t_homework_distribution 作业发放表
 *
 */
@TableName(value = "t_homework_distribution")
@Data
@ToString
@Accessors(chain = true)
public class THomeworkDistribution extends BaseEntity {
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
     * 作业id
     */
    @TableField(value = "homework_id")
    private Integer homeworkId;

    /**
     * 班级id
     */
    @TableField(value = "class_id")
    private Integer classId;

    /**
     * 班级名字
     */
    @TableField(value = "class_name")
    private String className;


    /**
     * 类型1：班级2：学生
     */
    @TableField(value = "target_type")
    private Integer targetType;

    /**
     * 标记1：发放2：未发放
     */
    @TableField(value = "flag")
    private Integer flag;


    /**
     * 作业学生发放表
     */
    @TableField(exist = false)
    private List<THomeworkDistributionStudent> distributionStydents;

    public THomeworkDistribution() {
    }

    public THomeworkDistribution(Integer courseId, Integer homeworkId, Integer classId, Integer targetType, Date createTime, Integer createUser) {
        this.courseId = courseId;
        this.homeworkId = homeworkId;
        this.classId = classId;
        this.targetType = targetType;
        this.createTime = createTime;
        this.createUser = createUser;
    }
}
