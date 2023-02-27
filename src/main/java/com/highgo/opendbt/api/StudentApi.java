package com.highgo.opendbt.api;

import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.domain.model.SubmitResult;
import com.highgo.opendbt.student.domain.model.StudentExamExercise;
import com.highgo.opendbt.student.domain.model.StudentReportCard;
import com.highgo.opendbt.student.service.StudentExamService;
import com.highgo.opendbt.teacher.domain.model.Exam;
import com.highgo.opendbt.teacher.domain.model.ExamClass;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 旧版作业相关的接口  暂时不用
 * @Title: StudentApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/student/exam")
public class StudentApi {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final StudentExamService studentExamService;

    /**
     * 根据班级和课程id获取学生可见作业
     *
     * @param sclassId 班级id
     * @param courseId 课程id
     * @return List<Exam>
     */
    @RequestMapping("/getExamListBySclass/{sclassId}/{courseId}")
    public List<Exam> getExamListBySclass(HttpServletRequest request, @PathVariable("sclassId") int sclassId, @PathVariable("courseId") int courseId) {
        return studentExamService.getExamListBySclass(request, sclassId, courseId);
    }

    /**
     * 获取作业的习题列表
     *
     * @param sclassId 班级id
     * @param examId   作业id
     * @return List<StudentExamExercise>
     */
    @RequestMapping("/getExamExerciseList/{sclassId}/{examId}")
    public List<StudentExamExercise> getExamExerciseList(HttpServletRequest request, @PathVariable("sclassId") int sclassId, @PathVariable("examId") int examId) {
        return studentExamService.getExamExerciseList(request, sclassId, examId);
    }

    /**
     * 获取作业题目的详细信息，包括是否做对与已做答案
     *
     * @param sclassId    班级id
     * @param examId      作业id
     * @param examClassId 作业和班级关联关系id
     * @param exerciseId  习题id
     * @return StudentExamExercise
     */
    @RequestMapping("/getExamExerciseById/{sclassId}/{examId}/{examClassId}/{exerciseId}")
    public StudentExamExercise getExamExerciseById(HttpServletRequest request, @PathVariable("sclassId") int sclassId, @PathVariable("examId") int examId, @PathVariable("examClassId") int examClassId, @PathVariable("exerciseId") int exerciseId) {
        return studentExamService.getExamExerciseById(request, sclassId, examId, examClassId, exerciseId);
    }

    /**
     * 学生测试运行作业的题目答案，不会记录成绩
     *
     * @param score 答题信息
     * @return SubmitResult
     */
    @RequestMapping("/examStuTestRunAnswer")
    public SubmitResult stuTestRunAnswer(HttpServletRequest request, @RequestBody Score score) {
        logger.debug("Enter, score = " + score.toString());
        return studentExamService.startVerifyAnswerThread(request, score, false);
    }

    /**
     * 学生提交作业题目答案，会记录成绩
     * @param score 答题信息
     * @return SubmitResult
     */
    @RequestMapping("/examSubmitAnswer")
    public SubmitResult submitAnswer(HttpServletRequest request, @RequestBody Score score) {
        logger.debug("Enter, score = " + score.toString());
        return studentExamService.startVerifyAnswerThread(request, score, true);
    }

    /**
     * 获取习题列表并获取作业详细信息
     * @param examClassId 作业和班级关联关系id
     * @return StudentReportCard
     */
    @RequestMapping("/exerciseReportCard/{examClassId}")
    public StudentReportCard exerciseReportCard(HttpServletRequest request, @PathVariable("examClassId") int examClassId) {
        logger.debug("Enter, examClassId = " + examClassId);
        return studentExamService.exerciseReportCard(request, examClassId);
    }

    /**
     * 获取作业详细信息
     * @param examClassId 作业和班级关联关系id
     * @return ExamClass
     */
    @RequestMapping("/getExamById/{examClassId}")
    public ExamClass getExamById(@PathVariable("examClassId") int examClassId) {
        return studentExamService.getExamById(examClassId);
    }

}
