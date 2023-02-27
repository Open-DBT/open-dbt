package com.highgo.opendbt.catalogue.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @TableName t_catalogue_auth_class  目录权限班级表
 */
@TableName(value ="t_catalogue_auth_class")
@Data
@Accessors(chain = true)
public class TCatalogueAuthClass implements Serializable {
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
     * 目录名称
     */
    @TableField(value = "catalogue_name")
    private String catalogueName;

    /**
     * 班级id
     */
    @TableField(value = "class_id")
    private Integer classId;

    /**
     * 权限类型：0：整个班级、1：部分学生
     */
    @TableField(value = "auth_type")
    private String authType;

    /**
     * 选中标记 0：未选中 1：已选中
     */
    @TableField(value = "flag")
    private Integer flag;

    /**
     * 课程名称
     */
    @TableField(value = "class_name")
    private String className;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 班级下拥有权限的学生
     */
    @TableField(exist = false)
    private List<TCatalogueAuthStu> stus;

    /**
     * mode是否变更0：未变更 1：已变更
     */
    @TableField(exist = false)
    private Integer isChange;

    public TCatalogueAuthClass(Integer courseId, Integer catalogueId, String catalogueName, Integer classId, String authType, Integer flag, String classNam) {
        this.courseId = courseId;
        this.catalogueId = catalogueId;
        this.catalogueName = catalogueName;
        this.classId = classId;
        this.authType = authType;
        this.flag = flag;
        this.className = className;
    }

    public TCatalogueAuthClass() {
    }
}
