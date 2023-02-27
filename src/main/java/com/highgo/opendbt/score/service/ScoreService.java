package com.highgo.opendbt.score.service;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.model.ExercisePage;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.domain.model.StuExerciseInfo;
import com.highgo.opendbt.score.domain.model.SubmitResult;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
 public interface ScoreService {
    //根据课程id和学生id获取习题（分页），模糊查询
     PageInfo<Exercise> getStuExercise(HttpServletRequest request, ExercisePage exercisePage);

    //查询学生的习题基本信息以及成绩
     StuExerciseInfo getExerciseInfoByStu(HttpServletRequest request, int sclassId, int courseId, int knowledgeId);

    //测试运行，用于学生答题测试提交答案，不需要记录成绩
     SubmitResult startVerifyAnswerThread(HttpServletRequest request, Score score, int exerciseSource, boolean isSaveSubmitData, int entranceType);

    //提交言验证答案返回结果
     SubmitResult submitAnswer(UserInfo loginUser, Score score, int exerciseSource, boolean isSaveSubmitData, int entranceType);
}
