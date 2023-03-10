package com.highgo.opendbt.score.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.ResultSetInfo;
import com.highgo.opendbt.common.bean.SchemaConnection;
import com.highgo.opendbt.common.constant.Constant;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.service.RunAnswerService;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.CloseUtil;
import com.highgo.opendbt.common.utils.Message;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.course.domain.model.ExercisePage;
import com.highgo.opendbt.course.domain.model.SceneDetail;
import com.highgo.opendbt.course.mapper.CourseMapper;
import com.highgo.opendbt.course.mapper.ExerciseMapper;
import com.highgo.opendbt.course.mapper.KnowledgeMapper;
import com.highgo.opendbt.course.mapper.SceneDetailMapper;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.service.TNewExerciseService;
import com.highgo.opendbt.score.domain.model.*;
import com.highgo.opendbt.score.mapper.ScoreMapper;
import com.highgo.opendbt.score.service.ScoreService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.FutureTask;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService {

    Logger logger = LoggerFactory.getLogger(getClass());

    final ScoreMapper scoreMapper;

    final ExerciseMapper exerciseMapper;

    final KnowledgeMapper knowledgeMapper;

    final RunAnswerService runAnswerService;

    final SceneDetailMapper sceneDetailMapper;

    final CourseMapper courseMapper;

    final TNewExerciseService exerciseService;


    @Override
    public PageInfo<Exercise> getStuExercise(HttpServletRequest request, ExercisePage exercisePage) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ????????????????????????
        PageHelper.startPage(exercisePage.getCurrent(), exercisePage.getPageSize());
        List<Exercise> exerciseList = exerciseMapper.getExerciseByStu(exercisePage.getCourseId(), loginUser.getUserId());
        return new PageInfo<>(exerciseList);

    }

    @Override
    public StuExerciseInfo getExerciseInfoByStu(HttpServletRequest request, int sclassId, int courseId, int knowledgeId) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ??????????????????
        Course course = courseMapper.getCourseByCourseId(courseId);
        // ?????????????????????
        Knowledge knowledge = knowledgeMapper.getKnowledgeByKnowledgeId(knowledgeId);
        // ??????????????????
        List<Exercise> exerciseList = exerciseMapper.getExerciseInfoByStu(loginUser.getUserId(), sclassId, courseId, knowledgeId);

        // ???????????????????????????
        int doneExerciseNumber = 0;
        for (Exercise exercise : exerciseList) {
            if (!exercise.getScore().equals("-1")) {
                doneExerciseNumber++;
            }
        }
        return new StuExerciseInfo(exerciseList.size(), doneExerciseNumber, course, knowledge, exerciseList);

    }

    @Override
    public SubmitResult startVerifyAnswerThread(HttpServletRequest request, Score score, int exerciseSource, boolean isSaveSubmitData, int entranceType) {
        try {
            // ??????????????????
            UserInfo loginUser = Authentication.getCurrentUser(request);

            String threadName = "thread_" + exerciseSource + "_" + loginUser.getCode() + "_" + score.getExerciseId();
            //??????????????????
            score.setAnswer(score.getAnswer() == null ? "" : score.getAnswer()
                    .replaceAll("<p>", "")
                    .replaceAll("</p>", ""));
            // ???????????????????????????????????????????????????????????????
            ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
            int threadActiveCount = threadGroup.activeCount();
            Thread[] threadArray = new Thread[threadActiveCount];
            threadGroup.enumerate(threadArray);
            for (int i = 0; i < threadActiveCount; i++) {
                if (threadName.equals(threadArray[i].getName())) {
                    throw new Exception(Message.get("VerifyAnswerPleaseWait"));
                }
            }
            // ???????????????????????????
            VerifyAnswerCallable verifyAnswerCallable = new VerifyAnswerCallable(
                    loginUser, score, exerciseSource, isSaveSubmitData, entranceType);
            FutureTask<SubmitResult> futureTask = new FutureTask<>(verifyAnswerCallable);
            Thread thread = new Thread(futureTask);
            thread.setName(threadName);
            thread.start();
            return futureTask.get();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
    }

    @Override
    public SubmitResult submitAnswer(UserInfo loginUser, Score score, int exerciseSource, boolean isSaveSubmitData, int entranceType) {
        try {
            List<Knowledge> coverageKnowledgeList = new ArrayList<>();
            SubmitResult result = new SubmitResult(coverageKnowledgeList, score.getUsageTime(), 0);

            // ??????????????????
            TNewExercise exercise;
            exercise = exerciseService.getById(score.getExerciseId());
            BusinessResponseEnum.UNEXERCISE.assertNotNull(exercise, score.getExerciseId());
            // ???????????????????????????????????????????????????
            String[] teacherAnswerArray = exercise.getStandardAnswser().trim().split(" ");
            String[] studentAnswerArray = score.getAnswer().trim().split(" ");
            if (!teacherAnswerArray[0].toLowerCase().equals(studentAnswerArray[0].toLowerCase())) {
                result.setExecuteRs(false);
                result.setScoreRs(false);
                result.setLog(Message.get("SQLNotMatchKnowledge"));
            } else {
                // ????????????
                boolean answerRs = verifyAnswer(loginUser, exercise, score, result, exerciseSource, isSaveSubmitData);
                result.setScoreRs(answerRs);// ?????????????????????
            }

            // ??????????????????????????????????????????????????????
            if (isSaveSubmitData) {
                // ??????????????????
                Date date = new Date(System.currentTimeMillis() - score.getUsageTime() * 1000);
                score.setCreateTime(TimeUtil.convertDateTime(date));
                score.setScore(result.isScoreRs() ? 100 : 0);
                score.setUserId(loginUser.getUserId());
                score.setAnswerExecuteTime(result.getAnswerExecuteTime());

                if (entranceType == 1) {
                    scoreMapper.addExamScore(score);
                } else {
                    scoreMapper.add(score);
                }
            }

            return result;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
    }


    private boolean verifyAnswer(UserInfo loginUser, TNewExercise exercise, Score score, SubmitResult result, int exerciseSource, boolean isSaveSubmitData) {
        if (exercise.getStandardAnswser().toLowerCase().startsWith("select")) {
            // ????????????
            StudentAndTeacherResult studentAndTeacherResult = getStudentAndTeacherResult(loginUser,
                    exercise.getSceneId(), exercise.getId(), exercise.getStandardAnswser(),
                    score.getAnswer(), result, exerciseSource, isSaveSubmitData);
            if (studentAndTeacherResult.getIsCompare()) {
                return compareResultSet(studentAndTeacherResult.getTeacherResult(), studentAndTeacherResult.getStudentResult(), result);
            } else {
                return false;
            }
        } else {
            // ???????????????????????????????????????????????????
            UpdateRowAndResultSetTO studentData = getUpdateRowAndResultSet(loginUser, exercise.getSceneId(),
                    exercise.getId(), score.getAnswer(), true, result, exerciseSource);
            if (studentData.getUpdateRow() == -1) {
                return false;
            }

            // ???????????????????????????????????????????????????
            UpdateRowAndResultSetTO teacherData = getUpdateRowAndResultSet(loginUser,
                    exercise.getSceneId(), exercise.getId(), exercise.getStandardAnswser(),
                    true, result, exerciseSource);

            // ??????????????????????????????????????????map??????????????????
            if (studentData.getUpdateRow() != teacherData.getUpdateRow()
                    || studentData.getResultSetInfoMap().size() != teacherData.getResultSetInfoMap().size()) {
                return false;
            }

            // ?????????map?????????????????????????????????0??????????????????
            if (teacherData.getResultSetInfoMap().size() == 0) {
                return true;
            }

            // ???????????????????????????????????????????????????
            for (Entry<String, ResultSetInfo> entry : teacherData.getResultSetInfoMap().entrySet()) {
                Map<String, ResultSetInfo> studentResultSetListMap = studentData.getResultSetInfoMap();
                if (studentResultSetListMap.get(entry.getKey()) == null) {
                    return false;
                } else {
                    if (!compareResultSet(entry.getValue(), studentResultSetListMap.get(entry.getKey()), null)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private StudentAndTeacherResult getStudentAndTeacherResult(UserInfo loginUser, int sceneId, int exerciseId, String referAnswer, String studentAnswer, SubmitResult result, int exerciseSource, boolean isSaveSubmitData) {
        StudentAndTeacherResult studentAndTeacherResult = new StudentAndTeacherResult();
        Connection connection = null;
        Statement statement = null;
        ResultSet teacherResultSet = null;
        ResultSet studentResultSet = null;
        SchemaConnection schemaConnection = new SchemaConnection();
        try {
            // ??????????????????????????????schema?????????
            runAnswerService.getSchemaConnection(loginUser, sceneId, exerciseId, schemaConnection, exerciseSource);
            if (null != schemaConnection.getConnection()) {
                connection = schemaConnection.getConnection();
                statement = connection.createStatement();

                boolean isGetTeacherResultSet = true;

                // ?????????????????????????????????????????????list
                try {
                    long startTime = System.currentTimeMillis();
                    studentResultSet = statement.executeQuery(studentAnswer);
                    long endTime = System.currentTimeMillis();
                    result.setAnswerExecuteTime((int) (endTime - startTime));

                    ResultSetInfo studentResult = runAnswerService.resultSetConvertList(studentResultSet);
                    studentAndTeacherResult.setStudentResult(studentResult);

                    result.setExecuteRs(true); // ??????SQL????????????

                    if (!isSaveSubmitData) {
                        Map<String, Object> studentResultMap = result.getStudentResultMap();
                        studentResultMap.put(Constant.TEST_RUN_DATATYPE, studentResult.getDataTypeAndImgList());
                        studentResultMap.put(Constant.TEST_RUN_COLUMN, studentResult.getColumnList());
                        studentResultMap.put(Constant.TEST_RUN_RESULT, studentResult.getDataList());
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    // ??????????????????????????????????????????????????????????????????jdbc????????????
                    isGetTeacherResultSet = false;
                    studentAndTeacherResult.setIsCompare(false);
                    result.setLog(e.getMessage());
                }

                // ??????????????????????????????????????????????????????????????????
                if (isGetTeacherResultSet) {
                    try {
                        // ?????????????????????????????????????????????list
                        teacherResultSet = statement.executeQuery(referAnswer);
                        ResultSetInfo teacherResult = runAnswerService.resultSetConvertList(teacherResultSet);
                        studentAndTeacherResult.setTeacherResult(teacherResult);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        BusinessResponseEnum.ANSWERISPROBLEM.assertIsTrue(false, e.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        } finally {
            runAnswerService.dropSchema(schemaConnection.getSchemaName());
            CloseUtil.close(teacherResultSet);
            CloseUtil.close(studentResultSet);
            CloseUtil.close(statement);
            CloseUtil.close(connection);
        }
        return studentAndTeacherResult;
    }

    private UpdateRowAndResultSetTO getUpdateRowAndResultSet(UserInfo loginUser, int sceneId, int exerciseId, String answer, boolean isStudent, SubmitResult result, int exerciseSource) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        SchemaConnection schemaConnection = new SchemaConnection();

        UpdateRowAndResultSetTO updateRowAndResultSet = new UpdateRowAndResultSetTO();

        try {
            // ??????????????????????????????schema?????????
            runAnswerService.getSchemaConnection(loginUser, sceneId, exerciseId, schemaConnection, exerciseSource);
            if (null != schemaConnection.getConnection()) {
                connection = schemaConnection.getConnection();
                statement = connection.createStatement();

                // ??????????????????????????????????????????????????????????????????????????????????????????
                if (isStudent) {
                    try {
                        int updateRow = statement.executeUpdate(answer);
                        updateRowAndResultSet.setUpdateRow(updateRow);
                        result.setExecuteRs(true); // ??????SQL????????????
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        result.setLog(e.getMessage());
                        return updateRowAndResultSet;
                    }
                } else {
                    try {
                        int updateRow = statement.executeUpdate(answer);
                        updateRowAndResultSet.setUpdateRow(updateRow);
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                        BusinessResponseEnum.ANSWERISPROBLEM.assertIsTrue(false, e.getMessage());
                    }

                }

                // ????????????????????????????????????????????????
                try {
                    Map<String, ResultSetInfo> resultSetListMap = new HashMap<>();

                    List<SceneDetail> sceneDetailList;
                    if (exerciseSource == 1) {
                        sceneDetailList = sceneDetailMapper.getPublicSceneDetailById(sceneId);
                    } else {
                        sceneDetailList = sceneDetailMapper.getSceneDetailById(sceneId);
                    }

                    for (SceneDetail sceneDetail : sceneDetailList) {
                        resultSet = statement.executeQuery("select * from " + sceneDetail.getTableName());

                        ResultSetInfo resultSetInfo = runAnswerService.resultSetConvertList(resultSet);

                        resultSetListMap.put(sceneDetail.getTableName(), resultSetInfo);
                    }
                    updateRowAndResultSet.setResultSetInfoMap(resultSetListMap);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    BusinessResponseEnum.EXECUTANSWERGETRESULTFILE.assertIsTrue(false, e.getMessage());
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        } finally {
            runAnswerService.dropSchema(schemaConnection.getSchemaName());
            CloseUtil.close(resultSet);
            CloseUtil.close(statement);
            CloseUtil.close(connection);
        }
        return updateRowAndResultSet;
    }

    /**
     * ???????????????
     *
     * @param source ??????????????????
     * @param target ??????????????????
     * @return boolean
     */
    private boolean compareResultSet(ResultSetInfo source, ResultSetInfo target, SubmitResult result) {

        if (null == target || null == source) {
            return false;
        }

        // ????????????
        if (target.getRowNumber() != source.getRowNumber()) {
            if (null != result) {
                result.setErrorMessage(Message.get("RowNumberDiff", source.getRowNumber()));
            }
            return false;
        }

        // ????????????
        if (target.getColumnNumber() != source.getColumnNumber()) {
            if (null != result) {
                result.setErrorMessage(Message.get("ColumnNumberDiff", source.getColumnNumber() - 1));
            }
            return false;
        }

        // ???????????????
        List<String> sourceColumnList = source.getColumnList();
        List<String> targetColumnList = target.getColumnList();
        for (int i = 1; i < sourceColumnList.size(); i++) {
            if (null == sourceColumnList.get(i) || null == sourceColumnList.get(i)) {
                return false;
            }

            if (!sourceColumnList.get(i).equals(targetColumnList.get(i))) {
                if (null != result) {
                    result.setErrorMessage(Message.get("ColumnNameDiff"));
                }
                return false;
            }
        }

        // ????????????
        List<Map<Object, Object>> sourceDataList = source.getDataList();
        List<Map<Object, Object>> targetDataList = target.getDataList();
        for (int i = 0; i < sourceDataList.size(); i++) {
            Map<Object, Object> sourceData = sourceDataList.get(i);
            Map<Object, Object> targetData = targetDataList.get(i);
            if (!compareData(sourceData, targetData)) {
                if (null != result) {
                    result.setErrorMessage(Message.get("DataDiff"));
                }
                return false;
            }
        }
        return true;
    }


    /**
     * ??????????????????
     *
     * @param sourceData ???????????????
     * @param targetData ???????????????
     * @return boolean
     */
    private boolean compareData(Map<Object, Object> sourceData, Map<Object, Object> targetData) {
        for (Entry<Object, Object> entry : sourceData.entrySet()) {
            // ????????????????????????????????????????????????null??????????????????????????????null????????????????????????
            if (null == entry.getValue() && null == targetData.get(entry.getKey())) {
                continue;
            }

            // ???????????????????????????null???????????????????????????????????????null????????????????????????
            if (null == entry.getValue() || null == targetData.get(entry.getKey())) {
                return false;
            }

            // ??????????????????null???????????????????????????
            if (!entry.getValue().equals(targetData.get(entry.getKey()))) {
                return false;
            }
        }
        return true;
    }

}
