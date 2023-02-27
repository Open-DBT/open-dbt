package com.highgo.opendbt.contents.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @TableName t_course_contents
 */
@ApiModel(description = "内容保存")
@Data
@ToString
public class SaveCourseContents implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 课程id
     */
    @NotNull(message = "课程id不能为空")
    private Integer courseId;

    /**
     * 目录id
     */
    @NotNull(message = "目录id不能为空")
    private Integer catalogueId;

    /**
     * 内容
     */
    private String contents;

    /**
     * 排序
     */
    private Integer tabNum;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人员
     */
    private String createUser;

    /**
     * 更新时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新人员
     */
    private String updateUser;

    private List<TCatalogueResources> attachments;

}
