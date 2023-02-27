package com.highgo.opendbt.homeworkmodel.domain.model;

import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.course.domain.model.Scene;
import com.highgo.opendbt.exercise.domain.entity.TExerciseInfo;
import lombok.Data;

import java.util.List;

/**
 * @Description: 习题详情查询结果
 * @Title: NewExerciseDTO
 * @Package com.highgo.opendbt.homeworkmodel.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/8 10:34
 */
@Data
public class NewExerciseDTO {
    private Integer id;

    /**
     * 课程id
     */
    private Integer courseId;


    /**
     * 场景id
     */
    private Integer sceneId;

    /**
     * 场景名称
     */
    private String sceneName;



    /**
     * 习题名称
     */
    private String exerciseName;


    /**
     * 试题类型 1：单选2：多选3：判断4：填空5：简答6：数据库题
     */
    private String exerciseType;

    /**
     * 试题难度 1：简单 2：一般3：困难
     */
    private Integer exerciseLevel;

    /**
     * 题干
     */
    private String stem;

    /**
     * 选择题为prefix，多选逗号隔开。判断题答案只有true false,简答程序题答具体案描
     */
    private String standardAnswser;


    /**
     * 答案解析
     */
    private String exerciseAnalysis;


    /**
     * 选项
     */
    private List<TExerciseInfo> exerciseInfos;

    /**
     * 知识点
     */
    private List<Knowledge> knowledges;

    /**
     * 习题分数
     */
    private Double exerciseScore;


    /**
     * 是否绑定作业
     */
    private boolean bandingModel;
    /**
     * 场景信息
     */
    private Scene  scene;
}
