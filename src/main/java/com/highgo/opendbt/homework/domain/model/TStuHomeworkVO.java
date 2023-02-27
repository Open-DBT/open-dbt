package com.highgo.opendbt.homework.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


@Data
@ToString
@Accessors(chain = true)
public class TStuHomeworkVO implements Serializable {
    /**
     *
     */
    private Integer id;

    /**
     * 作业id
     */
    private Integer homeworkId;

    /**
     * 学生的id
     */
    private Integer studentId;

    /**
     * 学生的名字
     */
    private String studentName;

    /**
     * 班级id
     */
    private Integer classId;

    /**
     * 班级名称
     */
    private String className;

    /**
     * 分数
     */
    private Double score;

    /**
     * 作业状态1：已提交2：未提交3：打回未提交
     */
    private Integer homeworkStatus;

    /**
     * 批阅状态1：已批阅2：待批阅
     */
    private Integer checkStatus;
    /**
     * 提交时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;
    /**
     * 批阅时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date approvalTime;
    /**
     * 加时后结束时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    /**
     * 评语
     */
    private String comments;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 创建人员
     */
    private Integer createUser;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人员
     */
    private Integer updateUser;

    /**
     * 删除标志0：未删除1：已删除
     */
    private Short deleteFlag;

    /**
     * 删除时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    /**
     * 删除人员
     */
    private Integer deleteUser;

    /**
     * 课程id
     */
    private Integer courseId;

    /**
     * 学号
     */
    private String studentCode;

    /**
     * 学生作业详情
     */
    private List<TStuHomeworkInfoVO> stuHomeworkInfos;
}
