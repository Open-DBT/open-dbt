package com.highgo.opendbt.api;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.student.domain.model.ExerciseReportCard;
import com.highgo.opendbt.student.domain.model.StudentReportCard;
import com.highgo.opendbt.teacher.domain.model.*;
import com.highgo.opendbt.teacher.service.ExamClassService;
import com.highgo.opendbt.teacher.service.ExamExerciseService;
import com.highgo.opendbt.teacher.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 旧版教师端作业相关的接口  暂时不用
 * @Title: TeacherApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/teacher/exam")
public class TeacherApi {

    Logger logger = LoggerFactory.getLogger(getClass());

    final ExamService examService;

    final ExamClassService examClassService;

    final ExamExerciseService examExerciseService;

    /**
     * 获取课程的作业列表
     *
     * @param courseId 课程id
     * @return List<Exam>
     */
    @RequestMapping("/getExamList/{courseId}")
    public List<Exam> getTestList(HttpServletRequest request, @PathVariable("courseId") int courseId) {
        return examService.getExamList(request, courseId);
    }

    /**
     * 根据作业id查询作业信息
     *
     * @param examId 作业id
     * @return Exam
     */
    @RequestMapping("/getExamById/{examId}")
    public Exam getExamById(HttpServletRequest request, @PathVariable("examId") int examId) {
        return examService.getExamById(request, examId);
    }

    /**
     * 更新作业信息
     *
     * @param exam 作业信息
     * @return Integer
     */
    @RequestMapping("/updateExam")
    public Integer updateExam(HttpServletRequest request, @RequestBody Exam exam) {
        logger.info("Enter,  exam = " + exam.toString());
        return examService.updateExam(request, exam);
    }

    /**
     * 删除作业表
     *
     * @param examId 作业id
     * @return Integer
     */
    @RequestMapping("/deleteExam/{examId}")
    public Integer deleteExam(@PathVariable("examId") int examId) {
        logger.info("Enter,  examId = " + examId);
        return examService.deleteExam(examId);
    }

    /**
     * 根据作业id获取作业的习题列表
     *
     * @param examId 作业id
     * @return List<ExamExercise>
     */
    @RequestMapping("/getExamExerciseByExamId/{examId}")
    public ResultTO<List<ExamExercise>> getExamExerciseByExamId(@PathVariable("examId") int examId) {
        return examExerciseService.getExamExerciseByExamId(examId);
    }

    /**
     * 批量保存作业习题
     *
     * @param examId           作业id
     * @param examExerciseForm 作业中的习题id集合
     * @return Void
     */
    @RequestMapping("/saveExamExercise/{examId}")
    public ResultTO<Void> saveExamExercise(@PathVariable("examId") int examId, @RequestBody ExamExerciseForm examExerciseForm) {
        return examExerciseService.saveExamExercise(examId, examExerciseForm);
    }

    /**
     * 批量删除作业习题
     *
     * @param examId           作业id
     * @param examExerciseForm 作业中的习题id集合
     * @return Void
     */
    @RequestMapping("/batchDelExamExercise/{examId}")
    public ResultTO<Void> batchDelExamExercise(@PathVariable("examId") int examId, @RequestBody ExamExerciseForm examExerciseForm) {
        return examExerciseService.batchDelExamExercise(examExerciseForm);
    }

    /**
     * 拖拽切换作业习题位置
     *
     * @param id       作业和习题关联关系id
     * @param newIndex 题目新顺序
     * @return Void
     */
    @RequestMapping("/sortExercise/{id}/{newIndex}")
    public ResultTO<Void> sortExercise(@PathVariable("id") int id, @PathVariable("newIndex") int newIndex) {
        return examExerciseService.sortExercise(id, newIndex);
    }

    /**
     * 根据作业id，批量保存习题得分
     *
     * @param examId 作业id
     * @return Void
     */
    @RequestMapping("/saveExamExerciseScore/{examId}")
    public ResultTO<Void> saveExamExerciseScore(@PathVariable("examId") int examId, @RequestBody ExamForm examForm) {
        return examExerciseService.saveExamExerciseScore(examId, examForm);
    }

    /**
     * 作业指定班级
     *
     * @param examClass 作业班级信息
     * @return Void
     */
    @RequestMapping("/saveExamClass")
    public ResultTO<Void> saveExamClass(HttpServletRequest request, @RequestBody ExamClass examClass) {
        return examClassService.saveExamClass(request, examClass);
    }

    /**
     * 根据课程id，查询现有作业班级
     *
     * @param courseId 课程id
     * @return List<ExamClass>
     */
    @RequestMapping("/getExamClassListByCourseId/{courseId}")
    public ResultTO<List<ExamClass>> getExamClassListByCourseId(HttpServletRequest request, @PathVariable("courseId") int courseId) {
        return examClassService.getExamClassListByCourseId(request, courseId);
    }

    @RequestMapping("/getExamClassListByClassId/{classId}")
    public ResultTO<List<ExamClass>> getExamClassListByClassId(HttpServletRequest request, @PathVariable("classId") int classId) {
        return examClassService.getExamClassListByClassId(request, classId);
    }

    /**
     * 根据id，删除作业班级表
     *
     * @param examClassId 作业和班级关联关系id
     * @return Integer
     */
    @RequestMapping("/deleteExamClassById/{examClassId}")
    public ResultTO<Integer> deleteExamClassById(@PathVariable("examClassId") int examClassId) {
        return examClassService.deleteExamClassById(examClassId);
    }

    /**
     * 获取全班学生成绩
     *
     * @param examClassId 作业班级关联id
     * @return List<StudentReportCard>
     */
    @RequestMapping("/getExamStudentReportCard/{examClassId}")
    public List<StudentReportCard> getExamStudentReportCard(@PathVariable("examClassId") int examClassId) {
        return examService.getExamStudentReportCard(examClassId);
    }

    /**
     * 获取某个学生每个题目做题情况
     *
     * @param examClassId 作业班级关联id
     * @param userId      用户id
     * @return List<ExerciseReportCard>
     */
    @RequestMapping("/getStudentExerciseReportCard/{examClassId}/{userId}")
    public ResultTO<List<ExerciseReportCard>> getStudentExerciseReportCard(@PathVariable("examClassId") int examClassId, @PathVariable("userId") int userId) {
        return examExerciseService.getStudentExerciseReportCard(examClassId, userId);
    }

    /**
     * 获取作业详细信息
     *
     * @param examClassId 作业班级关联id
     * @return StudentReportCard
     */
    @RequestMapping("/getExamDetailByExamClassId/{examClassId}")
    public StudentReportCard getExamDetailByExamClassId(@PathVariable("examClassId") int examClassId) {
        logger.debug("Enter, examClassId = " + examClassId);
        return examService.getExamDetailByExamClassId(examClassId);
    }

    /**
     * 获取答题记录
     *
     * @param scoreId 作业分数id
     * @return Score
     */
    @RequestMapping("/getExerciseScoreById/{scoreId}")
    public Score getExerciseScoreById(@PathVariable("scoreId") int scoreId) {
        logger.debug("Enter, scoreId = " + scoreId);
        return examService.getExerciseScoreById(scoreId);
    }

}
