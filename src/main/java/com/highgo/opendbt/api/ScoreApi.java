package com.highgo.opendbt.api;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.model.ExercisePage;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.domain.model.StuExerciseInfo;
import com.highgo.opendbt.score.domain.model.SubmitResult;
import com.highgo.opendbt.score.service.ScoreService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 分数相关接口
 * @Title: ScoreApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/score")
public class ScoreApi {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final ScoreService scoreService;

    /**
     * 根据课程id和学生id获取习题（分页），模糊查询  暂时不用
     *
     * @param exercisePage 分页和模糊查询参数
     * @return PageInfo<Exercise>
     */
    @RequestMapping("/getStuExercise")
    public PageInfo<Exercise> getStuExercise(HttpServletRequest request, @RequestBody ExercisePage exercisePage) {
        logger.debug("Enter, exercisePage = " + exercisePage.toString());
        return scoreService.getStuExercise(request, exercisePage);
    }

    /**
     * 查询学生的习题基本信息以及成绩   暂时不用
     *
     * @param sclassId 班级id
     * @param courseId 课程id
     * @param knowledgeId 知识点id
     * @return StuExerciseInfo
     */
    @RequestMapping("/getExerciseInfoByStu/{sclassId}/{courseId}/{knowledgeId}")
    public StuExerciseInfo getExerciseInfoByStu(HttpServletRequest request, @PathVariable("sclassId") int sclassId, @PathVariable("courseId") int courseId, @PathVariable("knowledgeId") int knowledgeId) {
        logger.info("Enter, sclassId = " + sclassId + ", courseId = " + courseId + ", knowledgeId = " + knowledgeId);
        return scoreService.getExerciseInfoByStu(request, sclassId, courseId, knowledgeId);
    }

    /**
     * 测试运行，用于学生答题测试提交答案，不需要记录成绩
     *
     * @param score 答题信息
     * @return SubmitResult
     */
    @RequestMapping("/stuTestRunAnswer")
    public SubmitResult stuTestRunAnswer(HttpServletRequest request, @RequestBody Score score) {
        logger.debug("Enter, score = " + score.toString());
        return scoreService.startVerifyAnswerThread(request, score, 0, false, 0);
    }

    /**
     * 学生提交答案，会记录成绩
     *
     * @param score 答题信息
     * @return SubmitResult
     */
    @RequestMapping("/submitAnswer")
    public SubmitResult submitAnswer(HttpServletRequest request, @RequestBody Score score) {
        logger.debug("Enter, score = " + score.toString());
        return scoreService.startVerifyAnswerThread(request, score, 0, true, 0);
    }

}
