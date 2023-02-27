package com.highgo.opendbt.teacher.service.impl;

import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.mapper.ScoreMapper;
import com.highgo.opendbt.student.domain.model.StudentReportCard;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.teacher.domain.model.Exam;
import com.highgo.opendbt.teacher.domain.model.ExamClass;
import com.highgo.opendbt.teacher.mapper.ExamClassMapper;
import com.highgo.opendbt.teacher.mapper.ExamMapper;
import com.highgo.opendbt.teacher.service.ExamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExamServiceImpl implements ExamService {


    private final ExamMapper examMapper;

    private final ExamClassMapper examClassMapper;

    private final ScoreMapper scoreMapper;


    public List<Exam> getExamList(HttpServletRequest request, int courseId) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return examMapper.getExamList(loginUser.getUserId(), courseId);
    }

    @Override
    public Exam getExamById(HttpServletRequest request, int examId) {
        return examMapper.getExamById(examId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateExam(HttpServletRequest request, Exam exam) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 作业id为-1为新增作业
        if (exam.getId() == -1) {
            exam.setCreator(loginUser.getUserId());
            exam.setCreateTime(TimeUtil.getDateTime());
            examMapper.addExam(exam);
            return exam.getId();
        } else {
            return examMapper.updateExam(exam);
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExam(int examId) {
        return examMapper.deleteExam(examId);
    }

    @Override
    public List<StudentReportCard> getExamStudentReportCard(int examClassId) {
        ExamClass examClass = examClassMapper.getExamClassById(examClassId);
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(examClass);
        Exam exam = examMapper.getExamById(examClass.getExamId());
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(exam);
        return examMapper.getExamStudentReportCard(examClass.getClassId(), examClass.getExamId(), examClassId, exam.getExerciseSource());

    }

    @Override
    public StudentReportCard getExamDetailByExamClassId(int examClassId) {
        ExamClass examClass = examClassMapper.getExamClassById(examClassId);
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(examClass);
        Exam exam = examMapper.getExamById(examClass.getExamId());
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(exam);
        return examMapper.getExamDetailByExamClassId(examClassId, exam.getExerciseSource());

    }

    @Override
    public Score getExerciseScoreById(int scoreId) {
        return scoreMapper.getStuExamScoreById(scoreId);
    }

}
