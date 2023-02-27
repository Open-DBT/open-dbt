package com.highgo.opendbt.homework.domain.entity;

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
 * @TableName t_homework_distribution_student 作业发放学生表
 */
@TableName(value ="t_homework_distribution_student")
@Data
@ToString
@Accessors(chain = true)
public class THomeworkDistributionStudent extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作业发放id
     */
    @TableField(value = "distribution_id")
    private Integer distributionId;

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


}
