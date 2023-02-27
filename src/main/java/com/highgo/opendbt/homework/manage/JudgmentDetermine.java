package com.highgo.opendbt.homework.manage;

import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 判断题判定
 * @Title: SingleChoiceDetermine
 * @Package com.highgo.opendbt.homework.manage.determine
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/22 15:35
 */
public class JudgmentDetermine extends Determine {

    @Override
    public void determineExercise(HttpServletRequest request, TStuHomeworkInfo stuHomeworkInfo, String exerciseResult) {
        if(NUllJudgement(stuHomeworkInfo,exerciseResult)){
            return;
        }
        if (stuHomeworkInfo.getStandardAnswser().trim().equalsIgnoreCase(exerciseResult.trim())) {
            stuHomeworkInfo.setIsCorrect(1);
            stuHomeworkInfo.setExerciseScore(stuHomeworkInfo.getExerciseActualScore());
        } else {
            stuHomeworkInfo.setIsCorrect(2);
            //判定题目得分
            stuHomeworkInfo.setExerciseScore(0.0);
        }
    }


    public JudgmentDetermine() {
    }
}
