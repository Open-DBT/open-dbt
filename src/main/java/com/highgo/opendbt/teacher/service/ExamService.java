package com.highgo.opendbt.teacher.service;

import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.student.domain.model.StudentReportCard;
import com.highgo.opendbt.teacher.domain.model.Exam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ExamService {
    //获取课程的作业列表
    List<Exam> getExamList(HttpServletRequest request, int courseId);

    //根据作业id查询作业信息
    Exam getExamById(HttpServletRequest request, int examId);

    //更新作业信息
    Integer updateExam(HttpServletRequest request, Exam exam);

    //删除作业表
    Integer deleteExam(int examId);

    //获取全班学生成绩
    List<StudentReportCard> getExamStudentReportCard(int examClassId);

    //获取作业详细信息
    StudentReportCard getExamDetailByExamClassId(int examClassId);

    //获取答题记录
    Score getExerciseScoreById(int scoreId);

}
