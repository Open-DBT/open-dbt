package com.highgo.opendbt.homework.manage;

import com.highgo.opendbt.ApplicationContextRegister;
import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.domain.model.SubmitResult;
import com.highgo.opendbt.score.service.impl.ScoreServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: sql题判定
 * @Title: SingleChoiceDetermine
 * @Package com.highgo.opendbt.homework.manage.determine
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/22 15:35
 */
public class SQLDetermine extends Determine {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void determineExercise(HttpServletRequest request, TStuHomeworkInfo stuHomeworkInfo, String exerciseResult) {
        if(NUllJudgement(stuHomeworkInfo,exerciseResult)){
            return;
        }
        Score score = new Score();
        score.setAnswer(exerciseResult==null?"":exerciseResult.replaceAll("<p>","").replaceAll("</p>",""));
        score.setExerciseId(stuHomeworkInfo.getExerciseId());
        try {
            ScoreServiceImpl scoreServiceImpl = (ScoreServiceImpl) ApplicationContextRegister.getBean(ScoreServiceImpl.class);
            SubmitResult result = scoreServiceImpl.startVerifyAnswerThread(request, score, 0, false, 0);
            boolean scoreRs = result.isScoreRs();
            boolean executeRs = result.isExecuteRs();
            if (scoreRs && executeRs) {
                stuHomeworkInfo.setIsCorrect(1);
                stuHomeworkInfo.setExerciseScore(stuHomeworkInfo.getExerciseActualScore());
            } else {
                stuHomeworkInfo.setIsCorrect(2);
                //判定题目得分
                stuHomeworkInfo.setExerciseScore(0.0);
            }
        } catch (Exception e) {
            logger.error("sql提批阅报错"+e.getMessage(), e);
            stuHomeworkInfo.setIsCorrect(2);
            //判定题目得分
            stuHomeworkInfo.setExerciseScore(0.0);
        }

    }

    public SQLDetermine() {
    }
}
