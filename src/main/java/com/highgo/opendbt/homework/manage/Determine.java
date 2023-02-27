package com.highgo.opendbt.homework.manage;

import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 试题判定对错抽象类
 * @author:
 * @date: 2022/9/22 15:34

 **/
public abstract class Determine {

    //判定试题对错
    public abstract  void determineExercise(HttpServletRequest request, TStuHomeworkInfo stuHomeworkInfo, String exerciseResult);

    public Determine() {
    }

    public boolean NUllJudgement(TStuHomeworkInfo stuHomeworkInfo, String exerciseResult){
        boolean res=false;
        if (exerciseResult == null || exerciseResult == "") {
            //错误
            stuHomeworkInfo.setIsCorrect(2);
            stuHomeworkInfo.setExerciseScore(0.0);
            res=true;
        }else{
            String result = exerciseResult.replaceAll("@_@", "").trim();
            if(result==null||"".equals(result)){
                //错误
                stuHomeworkInfo.setIsCorrect(2);
                stuHomeworkInfo.setExerciseScore(0.0);
                res=true;
            }
        }
        return res;
    }
}
