package com.highgo.opendbt.progress.service;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.exercise.domain.model.PublishExercise;
import com.highgo.opendbt.progress.model.*;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.score.domain.model.Score;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ProgressService {
    //学生端习题列表及习题进度
    Sclass getCourseProgressByStu(HttpServletRequest request, int classId, int courseId);

    //根据班级课程，查询知识点习题数量和学习进度
    List<StuKnowledgeExerciseInfo> getStuKnowledgeExerciseInfo(HttpServletRequest request, int sclassId, int courseId, int number);

    //根据课程，查询知识点习题数量，用于知识树显示习题数量
    ResultTO<List<KnowledgeExerciseCount>> getKnowExerciseCountByCourseId(int courseId);

    //查询当前学生某课程每个知识点的进度，用于<学生/学习进度>显示习题完成情况，知识点反查询习题
    ResultTO<List<KnowledgeExerciseCount>> getStuCourseKnowledgeItemProgress(HttpServletRequest request, int courseId);

    /**
     * 班级统计--tab1 正确率 习题列表
     *
     * @param request request
     * @param classId 班级id
     * @return List<SclassCorrect>
     */
    ResultTO<List<SclassCorrect>> getSclassCorrect(HttpServletRequest request, int classId);

    /**
     * 班级统计--tab2 覆盖率 学生列表
     *
     * @param request request
     * @param classId 班级id
     * @return List<SclassCoverage>
     */
    ResultTO<List<SclassCoverage>> getSclassCoverage(HttpServletRequest request, int classId, int isFuzzyQuery, String searchValue);

    /**
     * 学生统计--tab1 正确率 习题列表
     *
     * @param request request
     * @param classId 班级id
     * @param userId  用户id
     * @return List<StudentCorrect>
     */
    ResultTO<List<StudentCorrect>> getStudentCorrect(HttpServletRequest request, int classId, int userId);

    /**
     * 学生统计--tab2 覆盖率 当前学生
     *
     * @param request request
     * @param classId 班级id
     * @param userId  用户id
     * @return List<StudentCoverage>
     */
    ResultTO<List<StudentCoverage>> getStudentCoverage(HttpServletRequest request, int classId, int userId);

    //获取学生答题情况
    ResultTO<List<Score>> getStuAnswerSituation(int classId, int userId);

    //根据成绩id获取学生答题成绩记录
    ResultTO<Score> getStuScoreById(int scoreId);

    //导出学生习题做题情况
    ResultTO<String> exportStatisticsInfo(HttpServletRequest request, int classId, int type, int isFuzzyQuery, String searchValue);

    //学生练习非sql题提交
    boolean noSqlSubmitAnswer(HttpServletRequest request, Score score);

    //学生练习重置
    boolean exerciseReset(HttpServletRequest request, Integer courseId);

    //题库习题设置为练习/取消练习
    Integer publishExercise(HttpServletRequest request, PublishExercise param);
}
