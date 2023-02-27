package com.highgo.opendbt.migration.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.entity.ExerciseKnowledge;
import com.highgo.opendbt.course.mapper.ExerciseKnowledgeMapper;
import com.highgo.opendbt.course.service.ExerciseService;
import com.highgo.opendbt.course.service.TExerciseKnowledgeService;
import com.highgo.opendbt.exam.domain.entity.*;
import com.highgo.opendbt.exam.mapper.TExamExerciseMapper;
import com.highgo.opendbt.exam.mapper.TExamScoreMapper;
import com.highgo.opendbt.exam.mapper.TScoreMapper;
import com.highgo.opendbt.exam.service.*;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.mapper.TNewExerciseMapper;
import com.highgo.opendbt.homework.domain.entity.THomework;
import com.highgo.opendbt.homework.domain.entity.THomeworkDistribution;
import com.highgo.opendbt.homework.domain.entity.TStuHomework;
import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;
import com.highgo.opendbt.homework.mapper.TStuHomeworkInfoMapper;
import com.highgo.opendbt.homework.mapper.TStuHomeworkMapper;
import com.highgo.opendbt.homework.service.THomeworkDistributionService;
import com.highgo.opendbt.homework.service.THomeworkService;
import com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel;
import com.highgo.opendbt.homeworkmodel.domain.entity.TModelExercise;
import com.highgo.opendbt.homeworkmodel.domain.entity.TModelExerciseType;
import com.highgo.opendbt.homeworkmodel.mapper.TModelExerciseMapper;
import com.highgo.opendbt.homeworkmodel.service.THomeworkModelService;
import com.highgo.opendbt.homeworkmodel.service.TModelExerciseTypeService;
import com.highgo.opendbt.migration.service.MigrationService;
import com.highgo.opendbt.sclass.domain.model.SclassUserPage;
import com.highgo.opendbt.sclass.service.SclassService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Description: 历史数据迁移
 * @Title: MigrationServiceImpl
 * @Package com.highgo.opendbt.migration.service.impl
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/12/5 9:47
 */
@Service
public class MigrationServiceImpl implements MigrationService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TExerciseKnowledgeService exerciseKnowledgeService;
    @Autowired
    private THomeworkModelService homeworkModelService;
    @Autowired
    private ExerciseService oldExerciseService;
    @Autowired
    private TExamExerciseService examExerciseService;
    @Autowired
    private TExamScoreService examScoreService;
    @Autowired
    private TScoreService scoreService;
    @Autowired
    private TNewExerciseMapper newExerciseMapper;
    @Autowired
    private ExerciseKnowledgeMapper exerciseKnowledgeMapper;
    @Autowired
    private TExamExerciseMapper examExerciseMapper;
    @Autowired
    private TExamScoreMapper examScoreMapper;
    @Autowired
    private TScoreMapper scoreMapper;
    @Autowired
    private TExamService examService;
    @Autowired
    private TExamClassService examClassService;
    @Autowired
    private TModelExerciseMapper modelExerciseMapper;
    @Autowired
    private TModelExerciseTypeService modelExerciseTypeService;
    @Autowired
    private THomeworkService homeworkService;
    @Autowired
    private THomeworkDistributionService tHomeworkDistributionService;
    @Autowired
    private SclassService sclassService;
    @Autowired
    private TStuHomeworkMapper stuHomeworkMapper;
    @Autowired
    private TStuHomeworkInfoMapper stuHomeworkInfoMapper;
    private final int numberBatch = 32767; //每一次插入的最大数

    /**
     * @description:习题历史数据迁移
     * @author:
     * @date: 2022/11/3 9:32
     * @param: [request]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean migrationExercise(HttpServletRequest request) {
        Map<Integer, Integer> cache = new HashMap<>();
        //查询历史数据
        List<Exercise> oldExercises = oldExerciseService.list(new QueryWrapper<Exercise>().eq("delete_flag", 0));
        //为空抛出异常
        BusinessResponseEnum.NOMIGRATIONEXERCISE.assertIsNotEmpty(oldExercises);
        List<TNewExercise> newExercises = new ArrayList<>();
        //循环映射到新表
        newExercisesMigration(oldExercises, newExercises);
        logger.info("1.习题映射完成~~~~~~");
        //新老习题表对照写入缓存
        cache(cache, newExercises, oldExercises);
        // 习题知识点关联表
        knowledgesMigration(cache);
        // 习题作业关联表
        exerciseMigration(cache);
        //  作业分数表
        examScoreMigration(cache);
        // 分数表
        //scoreMigration(cache);
        // 作业相关迁移
        homeWorkMigration();
        return true;
    }

    private void newExercisesMigration(List<Exercise> oldExercises, List<TNewExercise> newExercises) {
        oldExercises.forEach(exercise -> {
            TNewExercise ex = new TNewExercise();
            ex.setCourseId(exercise.getCourseId())
                    .setParentId(0)
                    .setSceneId(exercise.getSceneId())
                    .setElementType(0)
                    .setExerciseName(exercise.getExerciseName())
                    .setStem(exercise.getExerciseDesc())
                    .setAuthType(2)
                    .setExerciseType(6)
                    .setExerciseLevel(1)
                    .setStandardAnswser(exercise.getAnswer())
                    .setCreateTime(TimeUtil.converTODate(exercise.getCreateTime()))
                    .setCreateUser(exercise.getCreator())
                    .setUpdateTime(TimeUtil.converTODate(exercise.getUpdateTime()))
                    .setDeleteFlag(0);
            newExercises.add(ex);
        });
        //插入新表
        detachInsert(newExercises, 23);
    }

    private void cache(Map<Integer, Integer> cache, List<TNewExercise> newExercises, List<Exercise> oldExercises) {
        for (int i = 0; i < newExercises.size(); i++) {
            cache.put(oldExercises.get(i).getExerciseId(), newExercises.get(i).getId());
        }
        logger.info("新旧习题表id对应关系{}", JSON.toJSONString(cache));
    }

    private void knowledgesMigration(Map<Integer, Integer> cache) {
        List<ExerciseKnowledge> knowledges = exerciseKnowledgeService.list();
        if (knowledges != null && !knowledges.isEmpty()) {
            List<ExerciseKnowledge> collect = knowledges.stream().peek(item -> {
                if (cache.get(item.getExerciseId()) != null) {
                    item.setExerciseId(cache.get(item.getExerciseId()));
                } else {
                    logger.info("习题id{}未找到", item.getExerciseId());
                }

            }).collect(Collectors.toList());
            detachSaveOrUpdateExerciseKnowledge(collect, 4);
            logger.info("2.知识点映射完成~~~~~~");
        }
    }

    private void exerciseMigration(Map<Integer, Integer> cache) {
        List<TExamExercise> exercises = examExerciseService.list();
        if (exercises != null && !exercises.isEmpty()) {
            List<TExamExercise> collect = exercises.stream().peek(item -> {
                if (cache.get(item.getExerciseId()) != null) {
                    item.setExerciseId(cache.get(item.getExerciseId()));
                }
            }).collect(Collectors.toList());
            detachSaveOrUpdateExamExercise(collect, 5);
            logger.info("3.作业习题映射完成~~~~~~");
        }
    }

    private void scoreMigration(Map<Integer, Integer> cache) {
        List<TScore> scores = scoreService.list();
        if (scores != null && !scores.isEmpty()) {
            List<TScore> collect = scores.stream().peek(item -> {
                if (cache.get(item.getExerciseId()) != null) {
                    item.setExerciseId(cache.get(item.getExerciseId()));
                }
            }).collect(Collectors.toList());

            detachSaveOrUpdateScore(collect, 9);
            logger.info("5.分数表映射完成~~~~~~");
        }
    }

    private void examScoreMigration(Map<Integer, Integer> cache) {
        List<TExamScore> examScores = examScoreService.list();
        if (examScores != null && !examScores.isEmpty()) {
            List<TExamScore> collect = examScores.stream().peek(item -> {
                if (cache.get(item.getExerciseId()) != null) {
                    item.setExerciseId(cache.get(item.getExerciseId()));
                }
            }).collect(Collectors.toList());

            detachSaveOrUpdateExamScore(collect, 11);
            logger.info("4作业分数表映射完成~~~~~~");
        }
    }

    private void homeWorkMigration() {
        //历史作业
        List<TExam> examList = examService.list();
        //存放每个课程的文件夹id
        Map<Integer, Integer> cacheId = new HashMap<>();
        //构建历史作业文件夹
        saveHistoryPackage(cacheId);
        //作业相关迁移
        saveMigrationHomeWork(examList, cacheId);
    }

    private void saveMigrationHomeWork(List<TExam> examList, Map<Integer, Integer> cacheId) {
        List<TStuHomework> stuHomeworkList = new ArrayList<>();
        List<TStuHomeworkInfo> stuHomeworkInfoList = new ArrayList<>();
        //遍历历史作业
        examList.forEach(item -> {
            //迁移作业模板
            THomeworkModel tHomeworkModel = new THomeworkModel(item.getCourseId(), cacheId.get(item.getCourseId()), 0, item.getTestName(), 2, 1, item.getCreateTime(), item.getCreator());
            homeworkModelService.save(tHomeworkModel);
            //作业模板关联表
            saveModelExercises(item.getId(), tHomeworkModel.getId());
            // 模板习题类型表
            saveModelExerciseType(tHomeworkModel.getId());
            //历史作业班级关联表
            List<TExamClass> examclasses = examClassService.list(new QueryWrapper<TExamClass>().eq("id", item.getId()).eq("delete_flag", 0));
            examclasses.forEach(examclass -> {
                //作业表
                THomework tHomework = new THomework(item.getCourseId(), item.getTestName(), tHomeworkModel.getId(), tHomeworkModel.getModelName(), TimeUtil.converTODate(examclass.getTestStart()), TimeUtil.converTODate(examclass.getTestEnd()), 1, 1, 1, item.getCreateTime(), item.getCreator());
                //保存作业
                homeworkService.save(tHomework);
                //作业发放表
                THomeworkDistribution tHomeworkDistribution = new THomeworkDistribution(item.getCourseId(), tHomework.getId(), examclass.getClassId(), 1, new Date(), 3);
                //保存作业发放表
                tHomeworkDistributionService.save(tHomeworkDistribution);
                //学生作业表
                //查询班级下的所有学生
                SclassUserPage sclassUserPage = new SclassUserPage();
                sclassUserPage.setSclassId(examclass.getClassId());
                //所有学生
                List<UserInfo> studentList = sclassService.getSclassStudentList(sclassUserPage);
                //查询每个学生每个习题的分数
                studentList.forEach(stu -> {
                    //作业分数
                    AtomicReference<Double> homeWorkscore = new AtomicReference<>(0.0);
                    List<TExamScore> tExamScores = examScoreService.list(new QueryWrapper<TExamScore>().eq("exam_id", item.getId()).eq("user_id", stu.getUserId()).eq("class_id", examclass.getClassId()).eq("exam_class_id", examclass.getId()));
                    tExamScores.forEach(score -> {
                        //学生作业明细表
                        TStuHomeworkInfo tStuHomeworkInfo = new TStuHomeworkInfo(item.getCourseId(), tHomework.getId(), tHomework.getHomeworkName(), tHomework.getModelId(), tHomework.getModelName(), stu.getUserId(), stu.getUserName(), stu.getCode(), examclass.getClassId(), score.getExerciseId(), score.getScore() == 0 ? 0.0 : 5.0, score.getAnswer(), score.getScore() == 0 ? 2 : 1, TimeUtil.converTODate(score.getCreateTime()), 0);
                        homeWorkscore.updateAndGet(v -> v + tStuHomeworkInfo.getExerciseScore());
                        stuHomeworkInfoList.add(tStuHomeworkInfo);
                    });
                    //学生作业表
                    TStuHomework tStuHomework = new TStuHomework(tHomework.getId(), stu.getUserId(), stu.getUserName(), examclass.getClassId(), homeWorkscore.get(), 1, 1, 0);
                    stuHomeworkList.add(tStuHomework);
                });

            });
        });
        if (!stuHomeworkList.isEmpty()) {
            stuHomeworkMapper.insertBatch(stuHomeworkList);
        }
        if (!stuHomeworkInfoList.isEmpty()) {
            stuHomeworkInfoMapper.insertBatch(stuHomeworkInfoList);
        }
    }

    private void saveHistoryPackage(Map<Integer, Integer> cacheId) {
        //1.作业模板
        List<TExam> list = examService.list(new QueryWrapper<TExam>().select("course_id").groupBy("course_id"));
        //每个课程中新建一个“历史作业”的文件夹用于存放历史作业
        list.forEach(exam -> {
            THomeworkModel tHomeworkModel = new THomeworkModel(exam.getCourseId(), 0, 1, "历史作业", 2, 1, new Date(), 3);
            homeworkModelService.save(tHomeworkModel);
            cacheId.put(exam.getCourseId(), tHomeworkModel.getId());
        });
    }

    /**
     * @description: 模板习题类型迁移
     * @author:
     * @date: 2022/12/5 11:13
     * @param: [model_id]
     * @return: void
     **/
    private void saveModelExerciseType(Integer modelId) {
        TModelExerciseType tModelExerciseType = new TModelExerciseType(modelId, 6, 1, new Date(), 3);
        modelExerciseTypeService.save(tModelExerciseType);
    }

    /**
     * @description: 迁移模板习题表
     * @author:
     * @date: 2022/12/5 11:00
     * @param: [exam_id, model_id]
     * @return: void
     **/
    private void saveModelExercises(Integer examId, Integer modelId) {
        //查询历史作业模板关联
        List<TExamExercise> examExercises = examExerciseService.list(new QueryWrapper<TExamExercise>().eq("exam_id", examId));
        List<TModelExercise> modelExercises = new ArrayList<>();
        examExercises.forEach(examExercise -> {
            TModelExercise modelExercise = new TModelExercise(modelId, examExercise.getExerciseId(), examExercise.getScore().doubleValue(), 1, 6, new Date(), 3, 0);
            modelExercises.add(modelExercise);
        });
        //保存模板习题关联表
        if (!modelExercises.isEmpty()) {
            modelExerciseMapper.insertBatch(modelExercises);
            //设置排序序号
            List<TModelExercise> collect = modelExercises.stream()
                    .peek(item -> item.setExerciseOrder(item.getId())).collect(Collectors.toList());
            modelExerciseMapper.pgInsertOrUpdateBatch(collect);
        }


    }


    /**
     * @description:
     * @author:
     * @date: 2022/11/4 14:57
     * @param: [list, fieldCount:列数]
     * @return: void
     **/
    public void detachSaveOrUpdateScore(List<TScore> list, int fieldCount) {
        //每一次插入的最大行数 ， 向下取整
        int v = ((Double) Math.floor(numberBatch / (fieldCount * 1.0))).intValue();
        double number = list.size() * 1.0 / v;
        int n = ((Double) Math.ceil(number)).intValue(); //向上取整
        for (int i = 0; i < n; i++) {
            int end = v * (i + 1);
            if (end > list.size()) {
                end = list.size(); //如果end不能超过最大索引值
            }
            scoreMapper.pgInsertOrUpdateBatch(list.subList(v * i, end)); //插入数据库
            logger.info("更新一次~~~{}-{}", v * i, end);
        }
    }

    public void detachSaveOrUpdateExamScore(List<TExamScore> list, int fieldCount) {
        //每一次插入的最大行数 ， 向下取整
        int v = ((Double) Math.floor(numberBatch / (fieldCount * 1.0))).intValue();
        double number = list.size() * 1.0 / v;
        int n = ((Double) Math.ceil(number)).intValue(); //向上取整
        for (int i = 0; i < n; i++) {
            int end = v * (i + 1);
            if (end > list.size()) {
                end = list.size(); //如果end不能超过最大索引值
            }
            examScoreMapper.pgInsertOrUpdateBatch(list.subList(v * i, end)); //插入数据库
            logger.info("更新一次~~~{}-{}", v * i, end);
        }
    }


    public void detachSaveOrUpdateExamExercise(List<TExamExercise> list, int fieldCount) {
        //每一次插入的最大行数 ， 向下取整
        int v = ((Double) Math.floor(numberBatch / (fieldCount * 1.0))).intValue();
        double number = list.size() * 1.0 / v;
        int n = ((Double) Math.ceil(number)).intValue(); //向上取整
        for (int i = 0; i < n; i++) {
            int end = v * (i + 1);
            if (end > list.size()) {
                end = list.size(); //如果end不能超过最大索引值
            }
            examExerciseMapper.pgInsertOrUpdateBatch(list.subList(v * i, end)); //插入数据库
            logger.info("更新一次~~~{}-{}", v * i, end);
        }
    }

    /**
     * @description: 分离插入
     * @author:
     * @date: 2022/11/3 17:37
     * @param: [newExercises]
     * @return: void
     **/
    public void detachInsert(List<TNewExercise> list, int fieldCount) {
        //每一次插入的最大行数 ， 向下取整
        int v = ((Double) Math.floor(numberBatch / (fieldCount * 1.0))).intValue();
        double number = list.size() * 1.0 / v;
        int n = ((Double) Math.ceil(number)).intValue(); //向上取整
        for (int i = 0; i < n; i++) {
            int end = v * (i + 1);
            if (end > list.size()) {
                end = list.size(); //如果end不能超过最大索引值
            }
            newExerciseMapper.insertBatch(list.subList(v * i, end)); //插入数据库
            logger.info("更新一次~~~{}-{}", v * i, end);
        }
    }

    public void detachSaveOrUpdateExerciseKnowledge(List<ExerciseKnowledge> list, int fieldCount) {

        //每一次插入的最大行数 ， 向下取整
        int v = ((Double) Math.floor(numberBatch / (fieldCount * 1.0))).intValue();
        double number = list.size() * 1.0 / v;
        int n = ((Double) Math.ceil(number)).intValue(); //向上取整
        for (int i = 0; i < n; i++) {
            int end = v * (i + 1);
            if (end > list.size()) {
                end = list.size(); //如果end不能超过最大索引值
            }
            exerciseKnowledgeMapper.pgInsertOrUpdateBatch(list.subList(v * i, end)); //插入数据库
            logger.info("更新一次~~~{}-{}", v * i, end);
        }
    }
}
