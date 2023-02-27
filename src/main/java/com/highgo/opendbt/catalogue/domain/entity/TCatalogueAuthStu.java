package com.highgo.opendbt.catalogue.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 *
 * @TableName t_catalogue_auth_stu  目录权限学生表
 */
@TableName(value ="t_catalogue_auth_stu")
@Data
@Accessors(chain = true)
@ToString
public class TCatalogueAuthStu implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 目录id
     */
    @TableField(value = "catalogue_id")
    private Integer catalogueId;

    /**
     * 班级id
     */
    @TableField(value = "class_id")
    private Integer classId;

    /**
     * 学生id
     */
    @TableField(value = "user_id")
    private Integer userId;

    /**
     * 学号
     */
    @TableField(value = "code")
    private String code;

    /**
     * 姓名
     */
    @TableField(value = "user_name")
    private String userName;

    /**
     * 选中标记 0：未选中 1：已选中
     */
    @TableField(value = "del_flag")
    private Integer delFlag;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}
