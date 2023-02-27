package com.highgo.opendbt.student.service;

import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.domain.model.SubmitResult;
import com.highgo.opendbt.student.domain.model.StudentExamExercise;
import com.highgo.opendbt.student.domain.model.StudentReportCard;
import com.highgo.opendbt.teacher.domain.model.Exam;
import com.highgo.opendbt.teacher.domain.model.ExamClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface StudentExamService {
    //根据班级和课程id获取学生可见作业
    List<Exam> getExamListBySclass(HttpServletRequest request, int sclassId, int courseId);

    //获取作业的习题列表
    List<StudentExamExercise> getExamExerciseList(HttpServletRequest request, int sclassId, int examId);

    //获取作业题目的详细信息，包括是否做对与已做答案
    StudentExamExercise getExamExerciseById(HttpServletRequest request, int sclassId, int examId, int examClassId, int exerciseId);

    //学生测试运行作业的题目答案，不会记录成绩
    SubmitResult startVerifyAnswerThread(HttpServletRequest request, Score score, boolean isSaveSubmitData);

    //取习题列表并获取作业详细信息
    StudentReportCard exerciseReportCard(HttpServletRequest request, int examClassId);

    //作业和班级关联关系id
    ExamClass getExamById(int examClassId);

}
