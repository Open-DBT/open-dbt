package com.highgo.opendbt.contents.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import com.highgo.opendbt.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;

import java.util.Date;
import java.util.List;

/**
 * @TableName t_course_contents 课程内容
 */
@Data
@ToString
@TableName(value = "t_course_contents")
public class TCourseContents extends BaseEntity {
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
     * 目录id
     */
    @TableField(value = "catalogue_id")
    private Integer catalogueId;

    /**
     * 内容
     */
    @TableField(value = "contents")
    private String contents;

    /**
     * 排序
     */
    @TableField(value = "tab_num")
    private Integer tabNum;

    @TableField(exist = false)
    private List<TCatalogueResources> attachments;

    public TCourseContents() {
    }

    public TCourseContents(Integer courseId, Integer catalogueId, Integer tabNum, Date createTime, Integer createUser) {
        this.courseId = courseId;
        this.catalogueId = catalogueId;
        this.tabNum = tabNum;
        this.createTime=createTime;
        this.createUser=createUser;

    }
}
