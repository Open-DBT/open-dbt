package com.highgo.opendbt.course.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName t_course_user 课程人员关联表
 */
@TableName(value = "t_course_user")
@Data
public class TCourseUser implements Serializable {

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @TableId(value = "id")
    private Integer id;

    /**
     *用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     *课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

}
