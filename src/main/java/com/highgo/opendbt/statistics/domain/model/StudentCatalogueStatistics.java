package com.highgo.opendbt.statistics.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class StudentCatalogueStatistics {
    /**
     * 资源id
     */
    private Integer resourcesId;
    /**
     * 资源名称
     */
    private String resourcesName;
    /**
     * 资源类型
     */
    private String resourcesType;
    /**
     * 观看时长
     */
    private Integer duration;
    /**
     * 学习状态：1：未学完 2：已学完
     */
    private Integer studyStatus;
    /**
     * 班级id
     */
    private Integer classId;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学生姓名
     */
    private String userName;
    /**
     * 学号
     */
    private String code;

    /**
     * 进度
     */
    private Integer proportion;

    /**
     * 完成时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date completeTime;
}
