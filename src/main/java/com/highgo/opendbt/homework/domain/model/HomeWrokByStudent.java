package com.highgo.opendbt.homework.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highgo.opendbt.common.annotation.IntervalFormatAnnotation;
import com.highgo.opendbt.common.annotation.IntervalTypeEnum;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Description: 学生端作业列表查询
 * @Title: HomeWrokByStudentVO
 * @Package com.highgo.opendbt.homework.domain.model.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/22 10:07
 */
@Data
@ToString
@Accessors(chain = true)
public class HomeWrokByStudent {
    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 作业名称
     */
    private String homeworkName;
    /**
     * 作业id
     */
    private Integer homeworkId;

    /**
     * 学生的id
     */
    private Integer studentId;
    /**
     * 作业状态1：已提交2：未提交 3:打回
     */
    private Integer homeworkStatus;
    /**
     * 是否可以重复提交
     */
    private Integer allowAfter;

    /**
     * 批阅状态1：已批阅2：待批阅
     */
    private Integer checkStatus;
    /**
     * 答案查看时间1：批阅后2：提交后3：作业结束后4：不允许
     */
    private Integer viewTime;
    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 开始时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    /**
     * 结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    /**
     * 时间间隔
     */
    @IntervalFormatAnnotation(type = IntervalTypeEnum.YEAR_MONTH_DAY_HOUR_MINUTER)
    private String intervalTime;

}
