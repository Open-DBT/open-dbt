package com.highgo.opendbt.homework.domain.model;

import com.highgo.opendbt.homeworkmodel.domain.model.NewExerciseDTO;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @TableName t_stu_homework_info
 * 学生作业明细表
 */
@Data
@ToString
@Accessors(chain = true)
public class TStuHomeworkInfoVO implements Serializable {

    /**
     * 习题id
     */
    private Integer exerciseId;

    /**
     * 学生答题获得分数
     */
    private Double exerciseScore;
    /**
     * 习题原始分数
     */
    private Double  exerciseActualScore;

    /**
     * 习题答案
     */
    private String exerciseResult;

    /**
     * 是否正确1：是2：否
     */
    private Integer isCorrect;
    /**
     * 习题类型
     */
    private Integer exerciseType;
    /**
     * 习题排序
     */
    private Integer exerciseOrder;
    /**
     * 模板id
     */
    private Integer modelId;
    /**
     * 习题
     */
    private NewExerciseDTO exercise;

}
