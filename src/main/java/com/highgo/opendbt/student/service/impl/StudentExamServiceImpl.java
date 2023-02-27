package com.highgo.opendbt.student.service.impl;

import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.domain.model.SubmitResult;
import com.highgo.opendbt.score.service.ScoreService;
import com.highgo.opendbt.student.domain.model.ExerciseReportCard;
import com.highgo.opendbt.student.domain.model.StudentExamExercise;
import com.highgo.opendbt.student.domain.model.StudentReportCard;
import com.highgo.opendbt.student.mapper.StudentExamMapper;
import com.highgo.opendbt.student.service.StudentExamService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.teacher.domain.model.Exam;
import com.highgo.opendbt.teacher.domain.model.ExamClass;
import com.highgo.opendbt.teacher.mapper.ExamClassMapper;
import com.highgo.opendbt.teacher.mapper.ExamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentExamServiceImpl implements StudentExamService {

    private final ExamMapper examMapper;

    private final ExamClassMapper examClassMapper;

    private final StudentExamMapper studentExamMapper;

    private final ScoreService scoreService;


    @Override
    public List<Exam> getExamListBySclass(HttpServletRequest request, int sclassId, int courseId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return examMapper.getExamListBySclass(loginUser.getUserId(), sclassId, courseId);

    }

    @Override
    public List<StudentExamExercise> getExamExerciseList(HttpServletRequest request, int sclassId, int examId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        Exam exam = examMapper.getExamById(examId);
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(exam);
        return studentExamMapper.getExamExerciseList(loginUser.getUserId()
                , sclassId, examId, -1, exam.getExerciseSource());
    }

    @Override
    public StudentExamExercise getExamExerciseById(HttpServletRequest request
            , int classId, int examId, int examClassId, int exerciseId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        Exam exam = examMapper.getExamById(examId);
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(exam);
        return studentExamMapper.getExamExerciseById(loginUser.getUserId()
                , classId, examId, examClassId, exerciseId, exam.getExerciseSource());

    }

    @Override
    public SubmitResult startVerifyAnswerThread(HttpServletRequest request, Score score, boolean isSaveSubmitData) {
        Exam exam = examMapper.getExamById(score.getExamId());
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(exam);
        return scoreService.startVerifyAnswerThread(request, score
                , exam.getExerciseSource(), isSaveSubmitData, 1);
    }

    @Override
    public StudentReportCard exerciseReportCard(HttpServletRequest request, int examClassId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        ExamClass exam = examClassMapper.getExamDetailById(examClassId);
        BusinessResponseEnum.EXAMINFOGETFILE.assertNotNull(exam);
        StudentReportCard studentReportCard = new StudentReportCard();
        studentReportCard.setStudentName(loginUser.getUserName());
        studentReportCard.setStudentCode(loginUser.getCode());
        studentReportCard.setCourseName(exam.getCourse().getCourseName());
        studentReportCard.setExamName(exam.getExam().getTestName());
        studentReportCard.setExamStatus(exam.getExam().getIsEnd());
        studentReportCard.setExamStart(exam.getTestStart());
        studentReportCard.setExamEnd(exam.getTestEnd());

        int answerExerciseCount = 0;
        int exerciseGrossScore = 0;
        int studentGrossScore = 0;
        List<ExerciseReportCard> exerciseReportCardList = studentReportCard.getExerciseReportCardList();

        List<StudentExamExercise> studentExamExerciseList = studentExamMapper
                .getExamExerciseList(loginUser.getUserId()
                        , exam.getClassId(), exam.getExamId(), examClassId, exam.getExam().getExerciseSource());
        for (StudentExamExercise itme : studentExamExerciseList) {
            if (itme.getExerciseSituation() == 100) {
                exerciseReportCardList.add(new ExerciseReportCard(itme.getExerciseId()
                        , itme.getExerciseName(), itme.getExerciseScore(), itme.getExerciseScore(), itme.getExerciseSituation()));
                studentGrossScore += itme.getExerciseScore();
                answerExerciseCount++;
            } else {
                exerciseReportCardList.add(new ExerciseReportCard(itme.getExerciseId()
                        , itme.getExerciseName(), itme.getExerciseScore(), 0, itme.getExerciseSituation()));
                if (itme.getExerciseSituation() == 0) {
                    answerExerciseCount++;
                }
            }
            exerciseGrossScore += itme.getExerciseScore();
        }

        studentReportCard.setExerciseCount(studentExamExerciseList.size());
        studentReportCard.setAnswerExerciseCount(answerExerciseCount);
        studentReportCard.setExerciseGrossScore(exerciseGrossScore);
        studentReportCard.setStudentGrossScore(studentGrossScore);

        return studentReportCard;

    }

    @Override
    public ExamClass getExamById(int examClassId) {
        return examClassMapper.getExamDetailById(examClassId);
    }

}
