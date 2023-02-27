package com.highgo.opendbt.homework.manage;

import com.highgo.opendbt.common.exception.APIException;

/**
 * @Description: 题目工厂
 * @Title: ExerciseFactory
 * @Package com.highgo.opendbt.homework.manage.determine
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/10/18 15:50
 */
public class ExerciseFactory {

    public static Determine getDetermine(int exerciseType){
        switch (exerciseType) {
            case 1:
                return  new SingleChoiceDetermine();
            case 2:
                return  new MultipleChoiceDetermine();
            case 3:
                return  new JudgmentDetermine();
            case 4:
                return  new FillInBlanksDetermine();
            case 6:
                return  new SQLDetermine();
            default:
                throw new APIException("无效客观题题目类型");
        }
    }
}
