package com.highgo.opendbt.progress.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.ExcelUtil;
import com.highgo.opendbt.common.utils.Message;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.exam.service.TScoreService;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.PublishExercise;
import com.highgo.opendbt.exercise.mapper.TNewExerciseMapper;
import com.highgo.opendbt.exercise.service.TNewExerciseService;
import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;
import com.highgo.opendbt.homework.manage.ExerciseFactory;
import com.highgo.opendbt.progress.mapper.ProgressMapper;
import com.highgo.opendbt.progress.model.*;
import com.highgo.opendbt.progress.service.ProgressService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.mapper.SclassMapper;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.mapper.ScoreMapper;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    Logger logger = LoggerFactory.getLogger(getClass());

    final ProgressMapper progressMapper;

    final SclassMapper sclassMapper;

    final ScoreMapper scoreMapper;

    final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    final TNewExerciseService exerciseService;

    final TScoreService scoreService;

    final TNewExerciseMapper exerciseMapper;

    @Override
    public Sclass getCourseProgressByStu(HttpServletRequest request, int classId, int courseId) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return sclassMapper.getCourseProgressByStu(loginUser.getUserId(), classId, courseId);

    }

    @Override
    public List<StuKnowledgeExerciseInfo> getStuKnowledgeExerciseInfo(HttpServletRequest request, int classId, int courseId, int number) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ??????????????????????????????list
        List<StuKnowledgeExerciseInfo> stuKnowledgeExerciseInfoList = progressMapper
                .getStuKnowledgeExerciseInfo(loginUser.getUserId(), classId, courseId, number);
        // ????????????????????????????????????0??????????????????????????????list???????????????????????????????????????????????????????????????????????????
        if (number == 0 || stuKnowledgeExerciseInfoList.size() < number) {
            StuKnowledgeExerciseInfo exerciseNumberTO = progressMapper.getNotKnowledgeExerciseCount(courseId);
            if (null != exerciseNumberTO) {
                int exerciseNumber = exerciseNumberTO.getExerciseNumber();
                if (exerciseNumber != 0) {
                    StuKnowledgeExerciseInfo stuKnowledgeExerciseInfo = progressMapper
                            .getNotKnowledgeExerciseInfo(loginUser.getUserId(), classId, courseId);
                    stuKnowledgeExerciseInfo.setName(Message.get("NotGroup"));
                    stuKnowledgeExerciseInfoList.add(stuKnowledgeExerciseInfo);
                }
            }
        }
        return stuKnowledgeExerciseInfoList;
    }

    @Override
    public ResultTO<List<KnowledgeExerciseCount>> getKnowExerciseCountByCourseId(int courseId) {
        try {
            return ResultTO.OK(progressMapper.getKnowExerciseCountByCourseId(courseId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<List<KnowledgeExerciseCount>> getStuCourseKnowledgeItemProgress(HttpServletRequest request, int courseId) {
        try {
            // ??????????????????
            UserInfo loginUser = Authentication.getCurrentUser(request);
            return ResultTO.OK(progressMapper.getStuCourseKnowledgeItemProgress(courseId, loginUser.getUserId()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<List<SclassCorrect>> getSclassCorrect(HttpServletRequest request, int sclassId) {
        try {
            // ?????????
            return ResultTO.OK(progressMapper.getSclassCorrect(sclassId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<List<SclassCoverage>> getSclassCoverage(HttpServletRequest request, int sclassId, int isFuzzyQuery, String searchValue) {
        try {
            // ?????????
            if (isFuzzyQuery == 0 || searchValue.trim().equals("")) {
                return ResultTO.OK(progressMapper.getSclassCoverage(sclassId));
            } else {
                return ResultTO.OK(progressMapper.getSclassCoverageByNameAndCode(sclassId, searchValue.trim()));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<List<StudentCorrect>> getStudentCorrect(HttpServletRequest request, int sclassId, int userId) {
        try {
            // ?????????
            return ResultTO.OK(progressMapper.getStudentCorrect(sclassId, userId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<List<StudentCoverage>> getStudentCoverage(HttpServletRequest request, int sclassId, int userId) {
        try {
            // ?????????
            return ResultTO.OK(progressMapper.getStudentCoverage(sclassId, userId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<List<Score>> getStuAnswerSituation(int classId, int userId) {
        try {
            return ResultTO.OK(scoreMapper.getStuAnswerSituation(classId, userId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<Score> getStuScoreById(int scoreId) {
        try {
            return ResultTO.OK(scoreMapper.getStuScoreById(scoreId));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<String> exportStatisticsInfo(HttpServletRequest request, int sclassId, int type, int isFuzzyQuery, String searchValue) {
        try {
            // ??????????????????
            UserInfo loginUser = Authentication.getCurrentUser(request);

            // ?????????????????????
            String fileName = null;
            // ???????????????
            String[] columnNameArray = new String[0];
            List<Map<Integer, Object>> mapList = new ArrayList<Map<Integer, Object>>();

            if (type == 2) {
                fileName = "student_statistics_" + loginUser.getCode() + "_" + TimeUtil.getDateTimeStr() + ".xls";
                columnNameArray = new String[]{"??????", "??????", "???????????????", "????????????", "????????????", "????????????"};

                // ??????????????????????????????
                List<SclassCoverage> sclassCoverageList = new ArrayList<SclassCoverage>();
                if (isFuzzyQuery == 0 || searchValue.trim().equals("")) {
                    sclassCoverageList = progressMapper.getSclassCoverage(sclassId);
                } else {
                    sclassCoverageList = progressMapper.getSclassCoverageByNameAndCode(sclassId, searchValue.trim());
                }

                // ?????????map???list???????????????????????????????????????????????????????????????????????????
                for (int i = 0; i < sclassCoverageList.size(); i++) {
                    SclassCoverage sclassCoverage = sclassCoverageList.get(i);

                    Map<Integer, Object> dataMap = new HashMap<Integer, Object>();
                    dataMap.put(0, sclassCoverage.getCode());
                    dataMap.put(1, sclassCoverage.getUserName());
                    dataMap.put(2, sclassCoverage.getCorrectCount());
                    dataMap.put(3, sclassCoverage.getAnswerCount());
                    dataMap.put(4, sclassCoverage.getSubmitAnswerCount());
                    dataMap.put(5, sclassCoverage.getExerciseCount());
                    mapList.add(dataMap);
                }
            } else {
                fileName = "class_statistics_" + loginUser.getCode() + "_" + TimeUtil.getDateTimeStr() + ".xls";
                columnNameArray = new String[]{"????????????", "????????????", "????????????", "???????????????", "????????????"};

                // ??????????????????????????????
                List<SclassCorrect> sclassCorrectList = progressMapper.getSclassCorrect(sclassId);

                // ?????????map???list???????????????????????????????????????????????????????????????????????????
                for (int i = 0; i < sclassCorrectList.size(); i++) {
                    SclassCorrect sclassCorrect = sclassCorrectList.get(i);

                    Map<Integer, Object> dataMap = new HashMap<Integer, Object>();
                    dataMap.put(0, sclassCorrect.getId());
                    dataMap.put(1, sclassCorrect.getExerciseName());
                    dataMap.put(2, sclassCorrect.getCorrectCount());
                    dataMap.put(3, sclassCorrect.getAnswerCount());
                    dataMap.put(4, sclassCorrect.getStuCount());
                    mapList.add(dataMap);
                }
            }

            // ????????????????????????
            ExcelUtil.writeXLS(fileName, columnNameArray, mapList);

            // ?????????????????????????????????????????????
            return ResultTO.OK(fileName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean noSqlSubmitAnswer(HttpServletRequest request, Score score) {
        // ??????????????????
       // UserInfo loginUser = Authentication.getCurrentUser(request);
        //????????????id??????????????????
        TNewExercise exercise = exerciseService.getExerciseInfo(request, score.getExerciseId());
        TStuHomeworkInfo homeworkInfo = new TStuHomeworkInfo();
        homeworkInfo.setExerciseInfoList(exercise.getExerciseInfos());
        homeworkInfo.setExerciseActualScore(100.0);//??????0???????????????
        homeworkInfo.setStandardAnswser(exercise.getStandardAnswser());
        //????????????????????????????????????????????????????????????
        ExerciseFactory.getDetermine(score.getExerciseType()).determineExercise(request, homeworkInfo, score.getAnswer());
        // ??????????????????
        score.setCreateTime(TimeUtil.convertDateTime(new Date()));
        score.setScore(homeworkInfo.getIsCorrect() == 1 ? 100 : 0);
       // score.setUserId(loginUser.getUserId());
        scoreMapper.add(score);
        return homeworkInfo.getIsCorrect() == 1;
    }


    /**
     * @description: ??????????????????????????????
     * @author:
     * @date: 2023/2/20 14:44
     * @param: [request, courseId ??????id]
     * @return: java.lang.Integer
     **/
    @Override
    public boolean exerciseReset(HttpServletRequest request, Integer courseId) {
        boolean removeByIds=true;
        //??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //????????????????????????????????????id??????
        List<Integer> ids = exerciseMapper.findExerciseScoreInfos(loginUser.getUserId(), courseId);
        //??????????????????????????????
        if(ids!=null&&!ids.isEmpty()){
            removeByIds = scoreService.removeByIds(ids);
            BusinessResponseEnum.DELFAIL.assertIsTrue(removeByIds);
        }
        return removeByIds;
    }

    /**
     * @description: ???????????????????????????/????????????
     * @author:
     * @date: 2023/2/17 16:03
     * @param: [request, param]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    public Integer publishExercise(HttpServletRequest request, PublishExercise param) {
        //???????????????????????????
        List<TNewExercise> exercises = exerciseService.listByIds(param.getIds());
        //?????????????????????
        List<TNewExercise> publicise = new ArrayList<>();
        //??????
        BusinessResponseEnum.UNEXERCISE.assertIsNotEmpty(exercises, Arrays.toString(param.getIds().toArray()));
        //???????????????????????????
        childExercises(exercises, publicise);
        //??????????????????????????????????????????
        Integer saveNum = null;
        if (!publicise.isEmpty()) {
            List<TNewExercise> exerciseList = publicise.stream()
                    .map(item -> item.setExerciseStatus(param.getExerciseStatus())
                            .setExerciseStatus(param.getShowAnswer()))
                    .collect(Collectors.toList());
            //??????
            saveNum = exerciseMapper.pgInsertOrUpdateBatch(exerciseList);
        }
        //????????????
        return saveNum;
    }





    private void childExercises(List<TNewExercise> exercises, List<TNewExercise> publicise) {
        //??????????????????
        List<TNewExercise> exercisesPackage = exercises.stream().filter(item -> item.getElementType() == 1).collect(Collectors.toList());
        //???????????????
        List<TNewExercise> exerciseList = exercises.stream().filter(item -> item.getElementType() == 0).collect(Collectors.toList());
        if (!exerciseList.isEmpty()) {
            publicise.addAll(exerciseList);
        }
        if (!exercisesPackage.isEmpty()) {
            for (TNewExercise exercise : exercisesPackage) {
                //???????????????
                List<TNewExercise> childExerciseList = exerciseService.list(new QueryWrapper<TNewExercise>()
                        .eq("parent_id", exercise.getId())
                        .eq("delete_flag", 0));
                if (!childExerciseList.isEmpty()) {
                    //???????????????
                    childExercises(childExerciseList, publicise);
                }
            }
        }
    }
}
