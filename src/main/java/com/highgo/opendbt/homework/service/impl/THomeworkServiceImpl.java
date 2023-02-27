package com.highgo.opendbt.homework.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.google.common.util.concurrent.AtomicDouble;
import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.exercise.domain.entity.TExerciseType;
import com.highgo.opendbt.exercise.service.TExerciseTypeService;
import com.highgo.opendbt.homework.domain.entity.*;
import com.highgo.opendbt.homework.domain.model.*;
import com.highgo.opendbt.homework.manage.ExerciseFactory;
import com.highgo.opendbt.homework.mapper.THomeworkDistributionMapper;
import com.highgo.opendbt.homework.mapper.THomeworkMapper;
import com.highgo.opendbt.homework.mapper.TStuHomeworkInfoMapper;
import com.highgo.opendbt.homework.mapper.TStuHomeworkMapper;
import com.highgo.opendbt.homework.service.*;
import com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel;
import com.highgo.opendbt.homeworkmodel.domain.entity.TModelExercise;
import com.highgo.opendbt.homeworkmodel.domain.entity.TModelExerciseType;
import com.highgo.opendbt.homeworkmodel.service.THomeworkModelService;
import com.highgo.opendbt.homeworkmodel.service.TModelExerciseService;
import com.highgo.opendbt.homeworkmodel.service.TModelExerciseTypeService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.mapper.SclassMapper;
import com.highgo.opendbt.sclass.service.SclassService;
import com.highgo.opendbt.sclass.service.TClassStuService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 作业服务类
 */
@Service
public class THomeworkServiceImpl extends ServiceImpl<THomeworkMapper, THomework>
        implements THomeworkService {

    @Autowired
    private THomeworkMapper homeworkMapper;
    @Autowired
    private THomeworkDistributionService homeworkDistributionService;
    @Autowired
    private THomeworkDistributionStudentService homeworkDistributionStydentService;
    @Autowired
    private TStuHomeworkService stuHomeworkService;
    @Autowired
    private TStuHomeworkInfoService stuHomeworkInfoService;
    @Autowired
    private TStuHomeworkMapper stuHomeworkMapper;
    @Autowired
    private TStuHomeworkInfoMapper stuHomeworkInfoMapper;
    @Autowired
    private THomeworkDistributionMapper homeworkDistributionMapper;
    @Autowired
    private SclassMapper sclassMapper;
    @Autowired
    private THomeworkModelService homeworkModelService;
    @Autowired
    private TModelExerciseService modelExerciseService;
    @Autowired
    private TModelExerciseTypeService modelExerciseTypeService;
    @Autowired
    private TExerciseTypeService exerciseTypeService;
    @Autowired
    private THomeworkService homeworkService;
    @Autowired
    private SclassService sclassService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TClassStuService classStuService;

    /**
     * @description: 查询作业列表
     * @author:
     * @date: 2022/9/16 10:26
     * @param: [request, param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.THomework>
     **/
    @Override
    public PageInfo<THomework> getHomeWork(HttpServletRequest request, @Valid PageParam<ListHomeWork> param) {
        // 获取用户信息
        Authentication.getCurrentUser(request);
        param.setOrderBy("create_time desc");
        //分页查询
        return PageMethod.startPage(param.getPageNum(), param.getPageSize()).setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> list(param.getParam()));
    }

    /**
     * @description: 删除作业
     * @author:
     * @date: 2022/9/16 17:13
     * @param: [request, id 作业id]
     * @return: boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delHomeWork(HttpServletRequest request, int id) {
        //删除作业表
        homeworkDelete(id);
        //  删除作业发放表
        homeworkDistributionDelete(id);
        // 删除作业发放学生表
        homeworkDistributionStudentDelete(id);
        // 删除学生作业表
        studentHomeworkDelete(id);
        // 删除学生作业明细表
        studentHomeworkInfoDelete(id);
        return true;
    }


    /**
     * @description: 发放设置查询
     * @author:
     * @date: 2022/9/19 13:33
     * @param: [request, id 作业id]
     * @return: com.highgo.opendbt.homework.domain.entity.THomework
     **/
    @Override
    public THomework getHomeWorkSet(HttpServletRequest request, int id) {
        //作业查询
        THomework homework = this.getById(id);
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(homework, id);
        //根据作业id查询发放班级
        List<THomeworkDistribution> list = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("homework_id", id)
                        .eq("delete_flag", 0));
        //根据作业id查询发放学生
        List<THomeworkDistribution> distributions = list.stream().peek(item -> {
            List<THomeworkDistributionStudent> homeworkDistributionStudents = homeworkDistributionStydentService
                    .list(new QueryWrapper<THomeworkDistributionStudent>()
                            .eq("homework_id", id)
                            .eq("delete_flag", 0)
                            .eq("distribution_id", item.getId()));
            item.setDistributionStydents(homeworkDistributionStudents);
        }).collect(Collectors.toList());
        BusinessResponseEnum.HOMEWORKMODELUNCOMMIT.assertIsNotEmpty(distributions, id);
        //设置发放范围
        homework.setDistributions(distributions);
        return homework;
    }

    /**
     * @description: 发放设置保存
     * @author:
     * @date: 2022/9/19 13:34
     * @param: [request, param]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHomeWorkSet(HttpServletRequest request, THomework param) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //根据id查询作业
        THomework tHomework = this.getById(param.getId());
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(tHomework, param.getId());
        //设置作业相关参数设置
        tHomework.setAllowAfter(param.getAllowAfter())
                .setStartTime(param.getStartTime())
                .setEndTime(param.getEndTime())
                .setViewTime(param.getViewTime())
                .setIgnoreCase(param.getIgnoreCase())
                .setUnselectedGiven(param.getUnselectedGiven())
                .setUpdateTime(new Date())
                .setUpdateUser(loginUser.getUserId());
        //更新
        boolean updateRes = this.updateById(tHomework);
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(updateRes);
        //查询学生作业表
        List<TStuHomework> stuHomeworks = stuHomeworkService
                .list(new QueryWrapper<TStuHomework>()
                        .eq("homework_id", param.getId()));
        if (stuHomeworks != null && !stuHomeworks.isEmpty()) {
            //个别学生加时的时间将会置空
            List<TStuHomework> stuHomeworkList = stuHomeworks.stream()
                    .filter(item -> item.getEndTime() != null)
                    .map(stuHomework -> {
                        stuHomework.setEndTime(null)
                                .setUpdateTime(new Date())
                                .setUpdateUser(loginUser.getUserId());
                        return stuHomework;
                    }).collect(Collectors.toList());
            if (!stuHomeworkList.isEmpty()) {
                BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(stuHomeworkService.saveOrUpdateBatch(stuHomeworkList));
            }
        }
        return updateRes;
    }

    /**
     * @description: 批阅列表已提交
     * @author:
     * @date: 2022/9/19 15:35
     * @param: [request, param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.TStuHomework>
     **/
    @Override
    public PageInfo<TStuHomework> getApprovalList(HttpServletRequest request, @Valid PageParam<ApprovalList> param) {

        //分页查询
        return PageMethod.startPage(param.getPageNum(), param.getPageSize()).setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> listApprovalList(param.getParam()));
    }

    /**
     * @description: 查询作业发放班级
     * @author:
     * @date: 2022/9/20 9:43
     * @param: [request, id]
     * @return: java.util.List<com.highgo.opendbt.sclass.domain.entity.Sclass>
     **/
    @Override
    public List<Sclass> getHomeWorkClazz(HttpServletRequest request, int id) {
        return homeworkDistributionMapper.getHomeWorkClazz(id);
    }

    /**
     * @description: 批阅列表数量统计
     * @author:
     * @date: 2022/9/20 10:14
     * @param: [request, param]
     * @return: com.highgo.opendbt.homework.vo.ApprovalCount
     **/
    @Override
    public ApprovalCountVO getApprovalCount(HttpServletRequest request, ApprovalCount param) {
        ApprovalCountVO approvalCount = stuHomeworkMapper.getApprovalCount(param);
        THomework homework = homeworkService.getById(param.getHomeworkId());
        approvalCount.setHomeworkName(homework.getHomeworkName());
        Sclass sclass = sclassService.getById(param.getClassId());
        approvalCount.setClassName(sclass.getClassName());
        return approvalCount;
    }

    /**
     * @description: 批阅查看
     * @author:
     * @date: 2022/9/21 14:16
     * @param: [request, student_id 学生id,  homework_id 作业id,flag 1：学生端详情不带答案，2：加时端或结束后带答案]
     * @return: com.highgo.opendbt.homework.domain.model.TStuHomeworkVO
     **/
    @Override
    public HomeWorkINfoModel review(HttpServletRequest request, HomeWrokView param) {
        //是否显现答案 1:存在 2：不存在
        int whetherAnswer = 1;
        //查询作业详情信息
        List<TStuHomeworkInfoVO> review;
        if (checkIsAnswer(param))
            review = stuHomeworkInfoMapper.review(param.getStudentId(), param.getHomeworkId());
        else {
            review = stuHomeworkInfoMapper.UnAnswerreview(param.getStudentId(), param.getHomeworkId());
            //去除填空题答案
            excludeFillInBlankAnswer(review);
            whetherAnswer = 2;
        }
        HomeWorkINfoModel homeWorkInfo = getHomeWorkInfo(param.getStudentId(), param.getHomeworkId(), review);
        //设置是否存在的参数
        homeWorkInfo.setWhetherAnswer(whetherAnswer);
        return homeWorkInfo;
    }

    /**
     * @description: 排除填空题答案
     * @author:
     * @date: 2023/1/3 13:32
     * @param: [review]
     * @return: void
     **/
    private void excludeFillInBlankAnswer(List<TStuHomeworkInfoVO> review) {
        if ((review != null) && !review.isEmpty()) {
            review.stream()
                    .filter(item -> item.getExerciseType() == 4)
                    .peek(info -> info.getExercise().getExerciseInfos()
                            .forEach(exerciseInfo -> exerciseInfo.setContent(null)))
                    .collect(Collectors.toList());
        }
    }

    private boolean checkIsAnswer(HomeWrokView param) {
        if (param.getFlag() == 2)
            return true;
        THomework homeWork = this.getById(param.getHomeworkId());
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(homeWork, param.getHomeworkId());
        //答案查看时间
        Integer viewTime = homeWork.getViewTime();
        //批阅状态
        Integer checkStatus = 2;
        //作答结束时间
        Date endTime = homeWork.getEndTime();
        //查询加时后的时间
        TStuHomework stuHomework = stuHomeworkService
                .getOne(new QueryWrapper<TStuHomework>()
                        .eq("homework_id", param.getHomeworkId())
                        .eq("student_id", param.getStudentId())
                        .eq("delete_flag", 0));
        if (stuHomework != null && stuHomework.getEndTime() != null) {
            endTime = stuHomework.getEndTime();
        }
        //批阅状态
        if (stuHomework != null)
            checkStatus = stuHomework.getCheckStatus();
        //作业关闭
        boolean before = endTime.before(new Date());
        //答案查看时间1：批阅后2：提交后3：作业结束后4：不允许
        switch (viewTime) {
            case 1:
                //批阅状态1：已批阅2：待批阅
                return checkStatus == 1;
            case 2:
                return true;
            case 3:
                return before;
            case 4:
            default:
                return false;
        }
    }


    /**
     * @description: 批阅提交
     * @author:
     * @date: 2022/9/21 14:16
     * @param: [request, param]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approval(HttpServletRequest request, SaveStuHomework param) {
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //学生作业明细
        List<SaveStuHomeworkInfo> stuHomeworkInfos = param.getStuHomeworkInfos();
        // 1.作业总分数
        double sumScore = param.getStuScore();
        // 2.更新学生作业表
        TStuHomework stuHomeWork = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("delete_flag", 0)
                .eq("homework_id", param.getHomeworkId())
                .eq("student_id", param.getStudentId()));
        if (stuHomeWork == null) {
            throw new APIException("未查询到学生作业表");
        }
        //已批阅
        stuHomeWork.setScore(sumScore)
                .setCheckStatus(1)
                .setApprovalTime(new Date())
                .setApprovalUserId(loginUser.getUserId())
                .setApprovalUser(loginUser.getUserName())
                .setComments(param.getComments())
                .setUpdateTime(new Date())
                .setUpdateUser(loginUser.getUserId());
        BusinessResponseEnum.FAILAPPROVAL.assertIsTrue(stuHomeworkService.saveOrUpdate(stuHomeWork));
        // 3.更新学生作业明细表
        stuHomeworkInfos.forEach(item -> {
            TStuHomeworkInfo stuHomeworkInfo = stuHomeworkInfoService.getOne(new QueryWrapper<TStuHomeworkInfo>()
                    .eq("homework_id", param.getHomeworkId())
                    .eq("student_id", param.getStudentId())
                    .eq("exercise_id", item.getExerciseId())
                    .eq("delete_flag", 0));
            if (stuHomeworkInfo != null) {
                //设置习题参数值，习题分数，习题判定，习题答案
                stuHomeworkInfo.setExerciseScore(item.getExerciseScore())
                        .setExerciseResult(item.getExerciseResult())
                        .setIsCorrect(item.getIsCorrect())
                        .setUpdateTime(new Date())
                        .setUpdateUser(loginUser.getUserId());
                //添加到集合
                tStuHomeworkInfos.add(stuHomeworkInfo);
            }
        });
        boolean res = stuHomeworkInfoService.saveOrUpdateBatch(tStuHomeworkInfos);
        BusinessResponseEnum.FAILAPPROVAL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 批阅打回
     * @author:
     * @date: 2022/9/21 16:55
     * @param: [request, studentId, homeworkId]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean callBack(HttpServletRequest request, int studentId, int homeworkId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //查询学生作业
        TStuHomework stuHomework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("homework_id", homeworkId)
                .eq("student_id", studentId)
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNSTUDENTHOMEWORK.assertNotNull(stuHomework, homeworkId, studentId);
        TStuHomework tStuHomework = (TStuHomework) stuHomework.setHomeworkStatus(3)
                .setCheckStatus(2)
                .setUpdateUser(loginUser.getUserId())
                .setUpdateTime(new Date());
        //更新学生作业
        boolean res = stuHomeworkService.saveOrUpdate(tStuHomework);
        BusinessResponseEnum.FAILACALLBACK.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 学生端查询作业列表
     * @author:
     * @date: 2022/9/22 9:48
     * @param: [request, course_id]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.THomework>
     **/
    @Override
    public List<HomeWrokByStudent> getHomeWorkByStudent(HttpServletRequest request, ListStudentHomeWork param) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        int userId = loginUser.getUserId();
        // 根据当前登录人和课程id查询所在班级
        List<Sclass> classes = sclassMapper.findClassByStuAndCourse(param.getCourseId(), userId);
        //未查询到班级抛出异常
        BusinessResponseEnum.UNCLASSIFIED.assertIsNotEmpty(classes, param.getCourseId(), userId);
        Integer classId;
        if (classes.size() > 1) {
            classId = classStuService.getCurrentClass(userId, classes);
        } else {
            classId = classes.get(0).getId();
        }
        param.setClassId(classId)
                .setStudentId(userId);
        return stuHomeworkMapper.getHomeWrokByStudent(param);

    }

    /**
     * @description: 学生端保存作业
     * @author:
     * @date: 2022/9/22 13:45
     * @param: [request, homeWork]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public double saveHomeWork(HttpServletRequest request, SaveHomework homeWork) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //保存集合
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        //客观题得分
        AtomicDouble score = new AtomicDouble();
        //学生作业答案
        List<SaveHomeworkInfo> homeworkInfos = homeWork.getHomeworkInfos();
        // 写入答案.设置作业状态为提交
        homeworkInfos.forEach(item -> {
            TStuHomeworkInfo stuHomeworkInfo = stuHomeworkInfoMapper
                    .getHomeworkInfoAndExercise(homeWork.getHomeworkId(), item.getExerciseId(), loginUser.getUserId());
            if (stuHomeworkInfo != null) {
                //设置学生答案
                stuHomeworkInfo.setExerciseResult(item.getExerciseResult());
                //判断客观题对错和获得分数
                if (stuHomeworkInfo.getExerciseStyle() == 2) {
                    determineExercise(request, stuHomeworkInfo, item);
                    score.addAndGet(stuHomeworkInfo.getExerciseScore());
                }
                stuHomeworkInfo.setUpdateUser(loginUser.getUserId()).setUpdateTime(new Date());
                tStuHomeworkInfos.add(stuHomeworkInfo);
            }
        });
        // 更新保存
        if (!tStuHomeworkInfos.isEmpty())
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(stuHomeworkInfoService.saveOrUpdateBatch(tStuHomeworkInfos));
        return score.get();
    }

    /**
     * @description: 学生作业提交
     * @author:
     * @date: 2022/9/23 15:52
     * @param: [request, homeWork]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitHomeWork(HttpServletRequest request, SaveHomework homeWork) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //保存
        double score = saveHomeWork(request, homeWork);
        //提交
        TStuHomework stuHomework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("delete_flag", 0)
                .eq("homework_id", homeWork.getHomeworkId())
                .eq("student_id", loginUser.getUserId()));
        stuHomework.setHomeworkStatus(1)
                .setSubmitTime(new Date())
                .setScore(score)
                .setUpdateTime(new Date())
                .setUpdateUser(loginUser.getUserId());
        boolean res = stuHomeworkService.saveOrUpdate(stuHomework);
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 加时操作
     * @author:
     * @date: 2022/12/9 10:12
     * @param: [request, student_id 学生id, homework_id 作业id]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean overTime(HttpServletRequest request, OverTimeForStudent overTimeForStudent) {
        //查询学生作业信息
        TStuHomework stuHomework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("homework_id", overTimeForStudent.getHomeworkId())
                .eq("student_id", overTimeForStudent.getStudentId())
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNSTUDENTHOMEWORK.assertNotNull(stuHomework, overTimeForStudent.getHomeworkId(), overTimeForStudent.getStudentId());
        //设置加时时间
        stuHomework.setEndTime(overTimeForStudent.getEndTime());
        //更新
        boolean res = stuHomeworkService.saveOrUpdate(stuHomework);
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 学生作业修改
     * @author:
     * @date: 2022/12/21 10:20
     * @param: [request, student_id 学生id, homework_id作业id]
     * @return: TStuHomework
     **/
    @Override
    public TStuHomework reviseStudentScore(HttpServletRequest request, ResiveStudentScore resiveStudentScore) {
        //查询学生作业
        TStuHomework homework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("student_id", resiveStudentScore.getStudentId())
                .eq("homework_id", resiveStudentScore.getHomeworkId()));
        BusinessResponseEnum.UNSTUDENTHOMEWORK.assertNotNull(homework, resiveStudentScore.getHomeworkId(), resiveStudentScore.getStudentId());
        homework.setScore(resiveStudentScore.getMark());
        //更新
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(stuHomeworkService.saveOrUpdate(homework));
        return homework;
    }

    /**
     * @description: 组装作业相关信息
     * @author:
     * @date: 2022/10/25 14:03
     * @param: [homework_id, res, review]
     * @return: void
     **/
    private HomeWorkINfoModel getHomeWorkInfo(int studentId, int homeworkId, List<TStuHomeworkInfoVO> review) {
        //查询作业
        THomework homeWork = this.getById(homeworkId);
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(homeWork, homeworkId);
        THomeworkModel homeworkModel = homeworkModelService.getById(homeWork.getModelId());
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(homeworkModel, homeWork.getModelId());
        //班级id
        Integer classId = null;
        //班级名称
        String className = null;
        //学生得分
        double stuScore = 0.0;
        //作业名称
        String homeworkName = homeWork.getHomeworkName();
        //作答开始时间
        Date startTime = homeWork.getStartTime();
        //作答结束时间
        Date endTime = homeWork.getEndTime();
        //作业满分
        AtomicReference<Double> score = new AtomicReference<>(homeWork.getScore());
        //学生得分
        AtomicReference<Double> mark = new AtomicReference<>(0.0);
        //是否允许补交
        int allowAfter = homeWork.getAllowAfter();
        //题目数量
        int exerciseCount = modelExerciseService.count(new QueryWrapper<TModelExercise>().eq("model_id", homeWork.getModelId()));
        //查询加时后的时间
        TStuHomework stuHomework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("homework_id", homeworkId)
                .eq("student_id", studentId)
                .eq("delete_flag", 0));
        if (stuHomework != null && stuHomework.getEndTime() != null) {
            endTime = stuHomework.getEndTime();
        }
        if (stuHomework != null) {
            stuScore = stuHomework.getScore();
            classId = stuHomework.getClassId();
            className = stuHomework.getClassName();
        }
        //查询总分
        double actualScore = review.stream()
                .filter(a -> a.getExerciseActualScore() != null)
                .mapToDouble(TStuHomeworkInfoVO::getExerciseActualScore
                ).sum();
        //设置总分
        score.set(actualScore);
        //查询客观题类型
        List<Integer> typeCodes = exerciseTypeService.list().stream()
                .filter(a -> a.getTypeStyle() == 2)
                .map(TExerciseType::getTypeCode)
                .collect(Collectors.toList());
        //客观题总分
        double objectiveScore = review.stream()
                .filter(a -> a.getExerciseScore() != null && typeCodes.contains(a.getExerciseType()))
                .mapToDouble(TStuHomeworkInfoVO::getExerciseScore
                ).sum();
        //设置学生总分
        mark.set(stuScore);
        //题型分类后集合
        List<HomeWorklExerciseModel> classifyList = new ArrayList<>();
        // 按题型分类
        if (homeworkModel.getClassify() == 1) {
            //查询该模板按照习题类型分类的排序
            List<TModelExerciseType> typeList = modelExerciseTypeService.list(new QueryWrapper<TModelExerciseType>()
                    .eq("model_id", homeWork.getModelId()));
            List<Integer> types = typeList.stream().map(TModelExerciseType::getExerciseType).collect(Collectors.toList());
            //按题型归类
            List<TExerciseType> exerciseTypes = exerciseTypeService.list().stream()
                    .filter(item -> types.contains(item.getTypeCode()))
                    .collect(Collectors.toList());
            exerciseTypes.forEach(item -> {
                HomeWorklExerciseModel homeWorklExerciseModel = new HomeWorklExerciseModel();
                List<TStuHomeworkInfoVO> collect = review.stream()
                        .filter(exercise -> exercise.getExerciseType().equals(item.getTypeCode()))
                        .sorted(Comparator.comparing(TStuHomeworkInfoVO::getExerciseOrder))
                        .collect(Collectors.toList());
                homeWorklExerciseModel.setCollect(collect)
                        .setTypeCode(item.getTypeCode())
                        .setTypeName(item.getTypeName())
                        .setExerciseCount(collect.size());
                //该类型下的实际总分数
                homeWorklExerciseModel.setScore(collect.stream()
                        .filter(a -> a.getExerciseActualScore() != null)
                        .mapToDouble(TStuHomeworkInfoVO::getExerciseActualScore
                        ).sum());
                List<TModelExerciseType> modelExerciseType = typeList.stream()
                        .filter(m -> m.getExerciseType().equals(item.getTypeCode()))
                        .collect(Collectors.toList());
                homeWorklExerciseModel.setSortNum(modelExerciseType.get(0).getSortNum());
                classifyList.add(homeWorklExerciseModel);
            });
        }
        //查询学生信息
        UserInfo userInfo = userInfoService.getById(studentId);
        BusinessResponseEnum.UNUSERINFO.assertNotNull(userInfo, studentId);
        // 组装作业相关信息
        return new HomeWorkINfoModel(review
                , classifyList
                , homeworkName
                , startTime
                , endTime
                , score.get()
                , mark.get()
                , exerciseCount
                , allowAfter
                , homeworkModel.getClassify()
                , objectiveScore
                , classId
                , className
                , userInfo.getCode()
                , userInfo.getUserName()
                , userInfo.getAvatar()
                , homeWork.getUnselectedGiven()
                , stuHomework == null ? "" : stuHomework.getComments()
                , stuHomework.getHomeworkStatus()
                , stuHomework.getCheckStatus());
    }

    /**
     * @description: 删除学生作业明细表
     * @author:
     * @date: 2022/9/28 15:06
     * @param: [loginUser, id]
     * @return: void
     **/
    private void studentHomeworkInfoDelete(int id) {
        //查询学生作业明细
        List<TStuHomeworkInfo> stuHomeworkInfos = stuHomeworkInfoService.list(new QueryWrapper<TStuHomeworkInfo>()
                .eq("homework_id", id));
        if (stuHomeworkInfos != null && !stuHomeworkInfos.isEmpty()) {
            //删除学生作业明细
            BusinessResponseEnum.DELFAIL
                    .assertIsTrue(stuHomeworkInfoService.removeByIds(stuHomeworkInfos.stream()
                            .map(TStuHomeworkInfo::getId)
                            .collect(Collectors.toList())));
        }
    }

    /**
     * @description: 删除学生作业表
     * @author:
     * @date: 2022/9/28 15:05
     * @param: [loginUser, id]
     * @return: void
     **/
    private void studentHomeworkDelete(int id) {
        //查询学生作业表
        List<TStuHomework> stuHomework = stuHomeworkService.list(new QueryWrapper<TStuHomework>().eq("homework_id", id));
        if (stuHomework != null && !stuHomework.isEmpty()) {
            //删除学生作业表
            BusinessResponseEnum.DELFAIL
                    .assertIsTrue(stuHomeworkService.removeByIds(stuHomework.stream()
                            .map(TStuHomework::getId)
                            .collect(Collectors.toList())));
        }
    }

    /**
     * @description: 删除作业发放学生表
     * @author:
     * @date: 2022/9/28 15:04
     * @param: [loginUser, id]
     * @return: void
     **/
    private void homeworkDistributionStudentDelete(int id) {
        //查询作业发放学生表
        List<THomeworkDistributionStudent> homeworkDistributionStydents = homeworkDistributionStydentService
                .list(new QueryWrapper<THomeworkDistributionStudent>()
                        .eq("homework_id", id));
        if (homeworkDistributionStydents != null && !homeworkDistributionStydents.isEmpty()) {
            //删除作业发放学生表
            BusinessResponseEnum.DELFAIL
                    .assertIsTrue(homeworkDistributionStydentService.removeByIds(homeworkDistributionStydents.stream()
                            .map(THomeworkDistributionStudent::getId)
                            .collect(Collectors.toList())));
        }
    }

    /**
     * @description: 作业发放表删除
     * @author:
     * @date: 2022/9/28 15:02
     * @param: [loginUser, id]
     * @return: void
     **/
    private void homeworkDistributionDelete(int id) {
        //查询作业发布表信息
        List<THomeworkDistribution> homeworkDistributions = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("homework_id", id));
        //作业发放表不能为空
        BusinessResponseEnum.UNHOMEWORKDISTRIBUTION.assertIsNotEmpty(homeworkDistributions, id);
        //批量删除
        BusinessResponseEnum.DELFAIL
                .assertIsTrue(homeworkDistributionService.removeByIds(homeworkDistributions.stream()
                        .map(THomeworkDistribution::getId)
                        .collect(Collectors.toList())));
    }

    /**
     * @description: 作业表删除
     * @author:
     * @date: 2022/9/28 15:01
     * @param: [loginUser, id]
     * @return: void
     **/
    private void homeworkDelete(int id) {
        THomework homework = this.getById(id);
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(homework, id);
        //删除作业
        BusinessResponseEnum.DELFAIL
                .assertIsTrue(this.removeById(id));
    }

    //判断题目答案对错
    private void determineExercise(HttpServletRequest request, TStuHomeworkInfo stuHomeworkInfo, SaveHomeworkInfo item) {
        ExerciseFactory.getDetermine(stuHomeworkInfo.getExerciseType()).determineExercise(request, stuHomeworkInfo, item.getExerciseResult());
    }


    /**
     * @description: 查询列表
     * @author:
     * @date: 2022/9/19 16:37
     * @param: [param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.TStuHomework>
     **/
    private List<TStuHomework> listApprovalList(ApprovalList param) {
        return stuHomeworkMapper.getApprovalList(param);
    }


    /**
     * @description: 分页查询实际执行
     * @author:
     * @date: 2022/9/16 17:18
     * @param: [param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.THomework>
     **/
    private List<THomework> list(ListHomeWork param) {
        return homeworkMapper.getHomeWorkLilst(param);
    }
}




