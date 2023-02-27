package com.highgo.opendbt.homework.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 作业详情返回
 * @Title: HomeWorkINfoModel
 * @Package com.highgo.opendbt.homework.domain.model.vo
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/12/6 11:12
 */
@Data
public class HomeWorkINfoModel {
    //未分类的作业列表
    private List<TStuHomeworkInfoVO> exercises;
    //分类后的排序列表
    private List<HomeWorklExerciseModel> classifyExercises;
    //作业名称
    private String homeworkName;
    //作业开始时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    //作业开始时间
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    //作业总分
    private Double score;
    //学生得分
    private Double mark;
    //习题总量
    private Integer exerciseCount;
    //是否允许补交
    private Integer allowAfter;
    //是否显现答案 1:存在 2：不存在
    private Integer whetherAnswer;
    //客观题得分
    private Double objectiveScore;
    /**
     * 题型归类1：是2：否
     */
    private Integer classify;
    //班级id
    private Integer classId;
    //班级名称
    private String className;
    //学号
    private String code;
    //学生名称
    private String studentName;
    //头像
    private String avatar;
    //评语
    private String comments;

    /**
     * 多选题未选全给一半分1：是2：否
     */
    private Integer unselectedGiven;

    //作业状态1：已提交2：未提交3：打回未提交
    private Integer homeworkStatus;

    //批阅状态1：已批阅2：待批阅
    private Integer checkStatus;

    public HomeWorkINfoModel() {
    }

    public HomeWorkINfoModel(List<TStuHomeworkInfoVO> exercises, List<HomeWorklExerciseModel> classifyExercises, String homeworkName, Date startTime, Date endTime, Double score, Double mark, Integer exerciseCount, Integer allowAfter, Integer classify, Double objectiveScore, Integer classId, String className, String code, String studentName, String avatar, Integer unselectedGiven, String comments,Integer homeworkStatus,Integer checkStatus) {
        this.exercises = exercises;
        this.classifyExercises = classifyExercises;
        this.homeworkName = homeworkName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.score = score;
        this.mark = mark;
        this.exerciseCount = exerciseCount;
        this.allowAfter = allowAfter;
        this.classify = classify;
        this.objectiveScore = objectiveScore;
        this.classId = classId;
        this.className = className;
        this.code = code;
        this.studentName = studentName;
        this.avatar = avatar;
        this.unselectedGiven = unselectedGiven;
        this.comments = comments;
        this.homeworkStatus=homeworkStatus;
        this.checkStatus=checkStatus;

    }
}
