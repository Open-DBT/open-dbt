package com.highgo.opendbt.homework.manage;

import com.highgo.opendbt.exercise.domain.entity.TExerciseInfo;
import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;
import lombok.NoArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Description: 填空题题判定
 * @Title: SingleChoiceDetermine
 * @Package com.highgo.opendbt.homework.manage.determine
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/22 15:35
 */
@NoArgsConstructor
public class FillInBlanksDetermine extends Determine {

    @Override
    public void determineExercise(HttpServletRequest request, TStuHomeworkInfo stuHomeworkInfo, String exerciseResult) {
        if(NUllJudgement(stuHomeworkInfo,exerciseResult)){
            return;
        }
        //添加空题空格数
        int cellNum = 0;
        //填空题正确空格数
        int rightNum = 0;
        //学生答案
        String[] studentAnswer = exerciseResult.split("@_@");
        //标准答案
        List<TExerciseInfo> actualAnswer = stuHomeworkInfo.getExerciseInfoList().stream().sorted(Comparator.comparing(TExerciseInfo::getPrefix)).collect(Collectors.toList());
        cellNum = actualAnswer.size();
        for (int i = 0; i < actualAnswer.size(); i++) {
            //一个空格可能有多个答案
            String[] answers = actualAnswer.get(i).getContent().split("@_@");
            for (int n = 0; n < answers.length; n++) {
                //有一个答案正确即为正确
                if (answers[n].equals(studentAnswer[i])) {
                    rightNum += 1;
                }
                break;
            }
        }
        if (cellNum == rightNum) {
            //全对
            stuHomeworkInfo.setIsCorrect(1);
            stuHomeworkInfo.setExerciseScore(stuHomeworkInfo.getExerciseActualScore());
        } else if (rightNum == 0) {
            //错误
            stuHomeworkInfo.setIsCorrect(2);
            stuHomeworkInfo.setExerciseScore(0.0);
        } else {
            //半对
            stuHomeworkInfo.setIsCorrect(3);
            stuHomeworkInfo.setExerciseScore(BigDecimal.valueOf(stuHomeworkInfo.getExerciseActualScore()).multiply(BigDecimal.valueOf(rightNum / (cellNum * 1.0))).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue());
        }
    }


}

