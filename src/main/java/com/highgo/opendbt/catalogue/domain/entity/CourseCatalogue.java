package com.highgo.opendbt.catalogue.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.highgo.opendbt.common.entity.BaseEntity;
import com.highgo.opendbt.statistics.domain.model.CatalogueStatistics;
import com.highgo.opendbt.statistics.domain.model.StudentCatalogueStatistics;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @TableName t_course_catalogue  目录表
 */
@Data
@ToString
@TableName(value = "t_course_catalogue")
@Accessors(chain = true)
public class CourseCatalogue extends BaseEntity {
    /**
     * 目录id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;
    /**
     * 父目录id
     */
    @TableField(value = "parent_id")
    private Integer parentId;
    /**
     * 目录名称
     */
    @TableField(value = "catalogue_name")
    private String catalogueName;
    /**
     * 排序
     */
    @TableField(value = "sort_num")
    private Integer sortNum;
    /**
     * 开放模式开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "start_time")
    private Date startTime;
    /**
     * 开放模式结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "end_time")
    private Date endTime;
    /**
     * 子目录
     */
    @TableField(exist = false)
    private List<CourseCatalogue> childrens;
    /**
     * 是否为叶子节点
     */
    @TableField(exist = false)
    private boolean isleaf;
    /**
     * 拥有目录权限的班级集合
     */
    @TableField(exist = false)
    private List<TCatalogueAuthClass> classes;
    /**
     * 某个班级的发布状态,用于参数传递 0未发布 1：已发布
     */
    @TableField(exist = false)
    private Integer publishStatus;
    /**
     * 目录层级
     */
    @TableField(exist = false)
    private Integer catalogueLevel;
    /**
     * 目录总任务数
     */
    @TableField(exist = false)
    private Integer catalogueTaskNum;

    /**
     * 目录进度比例
     */
    @TableField(exist = false)
    private Integer catalogueScale;
    /**
     * 章节任务数
     */
    @TableField(exist = false)
    private Integer totalNum;
    /**
     * 章节完成任务数
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(exist = false)
    private Integer completeNum;
    /**
     * 章节进度查询
     */
    @TableField(exist = false)
    private List<CatalogueStatistics> catalogueStatistics;
    /**
     * 学生进度
     */
    @TableField(exist = false)
    private List<StudentCatalogueStatistics> studentCatalogueStatistics;
    /**
     * 显示序号
     */
    @TableField(exist = false)
    private String serialNum;
    /**
     * 用于参数传递
     */
    @TableField(exist = false)
    private int classId;


}
