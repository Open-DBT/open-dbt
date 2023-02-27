package com.highgo.opendbt.homework.manage;

import com.highgo.opendbt.common.utils.ArraysUtils;
import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

/**
 * @Description: 多选题判定
 * @Title: SingleChoiceDetermine
 * @Package com.highgo.opendbt.homework.manage.determine
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/22 15:35
 */
public class MultipleChoiceDetermine extends Determine {

    @Override
    public void determineExercise(HttpServletRequest request, TStuHomeworkInfo stuHomeworkInfo, String exerciseResult) {
        if(NUllJudgement(stuHomeworkInfo,exerciseResult)){
            return;
        }
        //全对
        if (ArraysUtils.equals(stuHomeworkInfo.getStandardAnswser().trim().split(","), exerciseResult.trim().split(","))) {
            stuHomeworkInfo.setIsCorrect(1);
            stuHomeworkInfo.setExerciseScore(stuHomeworkInfo.getExerciseActualScore());
        } else {
            //半对
            if (stuHomeworkInfo.getUnselectedGiven() == 1 && ArraysUtils.contains(stuHomeworkInfo.getStandardAnswser().trim().split(","), exerciseResult.trim().split(","))) {
                stuHomeworkInfo.setIsCorrect(3);
                stuHomeworkInfo.setExerciseScore(BigDecimal.valueOf(stuHomeworkInfo.getExerciseActualScore()).divide(BigDecimal.valueOf(2), 1, BigDecimal.ROUND_HALF_UP).doubleValue());
            } else {
                //错误
                stuHomeworkInfo.setIsCorrect(2);
                stuHomeworkInfo.setExerciseScore(0.0);
            }
        }
    }


    public MultipleChoiceDetermine() {
    }
}
