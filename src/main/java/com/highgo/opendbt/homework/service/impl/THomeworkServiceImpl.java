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
 * ???????????????
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
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/16 10:26
     * @param: [request, param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.THomework>
     **/
    @Override
    public PageInfo<THomework> getHomeWork(HttpServletRequest request, @Valid PageParam<ListHomeWork> param) {
        // ??????????????????
        Authentication.getCurrentUser(request);
        param.setOrderBy("create_time desc");
        //????????????
        return PageMethod.startPage(param.getPageNum(), param.getPageSize()).setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> list(param.getParam()));
    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2022/9/16 17:13
     * @param: [request, id ??????id]
     * @return: boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean delHomeWork(HttpServletRequest request, int id) {
        //???????????????
        homeworkDelete(id);
        //  ?????????????????????
        homeworkDistributionDelete(id);
        // ???????????????????????????
        homeworkDistributionStudentDelete(id);
        // ?????????????????????
        studentHomeworkDelete(id);
        // ???????????????????????????
        studentHomeworkInfoDelete(id);
        return true;
    }


    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/19 13:33
     * @param: [request, id ??????id]
     * @return: com.highgo.opendbt.homework.domain.entity.THomework
     **/
    @Override
    public THomework getHomeWorkSet(HttpServletRequest request, int id) {
        //????????????
        THomework homework = this.getById(id);
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(homework, id);
        //????????????id??????????????????
        List<THomeworkDistribution> list = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("homework_id", id)
                        .eq("delete_flag", 0));
        //????????????id??????????????????
        List<THomeworkDistribution> distributions = list.stream().peek(item -> {
            List<THomeworkDistributionStudent> homeworkDistributionStudents = homeworkDistributionStydentService
                    .list(new QueryWrapper<THomeworkDistributionStudent>()
                            .eq("homework_id", id)
                            .eq("delete_flag", 0)
                            .eq("distribution_id", item.getId()));
            item.setDistributionStydents(homeworkDistributionStudents);
        }).collect(Collectors.toList());
        BusinessResponseEnum.HOMEWORKMODELUNCOMMIT.assertIsNotEmpty(distributions, id);
        //??????????????????
        homework.setDistributions(distributions);
        return homework;
    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/19 13:34
     * @param: [request, param]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveHomeWorkSet(HttpServletRequest request, THomework param) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //??????id????????????
        THomework tHomework = this.getById(param.getId());
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(tHomework, param.getId());
        //??????????????????????????????
        tHomework.setAllowAfter(param.getAllowAfter())
                .setStartTime(param.getStartTime())
                .setEndTime(param.getEndTime())
                .setViewTime(param.getViewTime())
                .setIgnoreCase(param.getIgnoreCase())
                .setUnselectedGiven(param.getUnselectedGiven())
                .setUpdateTime(new Date())
                .setUpdateUser(loginUser.getUserId());
        //??????
        boolean updateRes = this.updateById(tHomework);
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(updateRes);
        //?????????????????????
        List<TStuHomework> stuHomeworks = stuHomeworkService
                .list(new QueryWrapper<TStuHomework>()
                        .eq("homework_id", param.getId()));
        if (stuHomeworks != null && !stuHomeworks.isEmpty()) {
            //???????????????????????????????????????
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
     * @description: ?????????????????????
     * @author:
     * @date: 2022/9/19 15:35
     * @param: [request, param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.TStuHomework>
     **/
    @Override
    public PageInfo<TStuHomework> getApprovalList(HttpServletRequest request, @Valid PageParam<ApprovalList> param) {

        //????????????
        return PageMethod.startPage(param.getPageNum(), param.getPageSize()).setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> listApprovalList(param.getParam()));
    }

    /**
     * @description: ????????????????????????
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
     * @description: ????????????????????????
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
     * @description: ????????????
     * @author:
     * @date: 2022/9/21 14:16
     * @param: [request, student_id ??????id,  homework_id ??????id,flag 1?????????????????????????????????2?????????????????????????????????]
     * @return: com.highgo.opendbt.homework.domain.model.TStuHomeworkVO
     **/
    @Override
    public HomeWorkINfoModel review(HttpServletRequest request, HomeWrokView param) {
        //?????????????????? 1:?????? 2????????????
        int whetherAnswer = 1;
        //????????????????????????
        List<TStuHomeworkInfoVO> review;
        if (checkIsAnswer(param))
            review = stuHomeworkInfoMapper.review(param.getStudentId(), param.getHomeworkId());
        else {
            review = stuHomeworkInfoMapper.UnAnswerreview(param.getStudentId(), param.getHomeworkId());
            //?????????????????????
            excludeFillInBlankAnswer(review);
            whetherAnswer = 2;
        }
        HomeWorkINfoModel homeWorkInfo = getHomeWorkInfo(param.getStudentId(), param.getHomeworkId(), review);
        //???????????????????????????
        homeWorkInfo.setWhetherAnswer(whetherAnswer);
        return homeWorkInfo;
    }

    /**
     * @description: ?????????????????????
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
        //??????????????????
        Integer viewTime = homeWork.getViewTime();
        //????????????
        Integer checkStatus = 2;
        //??????????????????
        Date endTime = homeWork.getEndTime();
        //????????????????????????
        TStuHomework stuHomework = stuHomeworkService
                .getOne(new QueryWrapper<TStuHomework>()
                        .eq("homework_id", param.getHomeworkId())
                        .eq("student_id", param.getStudentId())
                        .eq("delete_flag", 0));
        if (stuHomework != null && stuHomework.getEndTime() != null) {
            endTime = stuHomework.getEndTime();
        }
        //????????????
        if (stuHomework != null)
            checkStatus = stuHomework.getCheckStatus();
        //????????????
        boolean before = endTime.before(new Date());
        //??????????????????1????????????2????????????3??????????????????4????????????
        switch (viewTime) {
            case 1:
                //????????????1????????????2????????????
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
     * @description: ????????????
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
        //??????????????????
        List<SaveStuHomeworkInfo> stuHomeworkInfos = param.getStuHomeworkInfos();
        // 1.???????????????
        double sumScore = param.getStuScore();
        // 2.?????????????????????
        TStuHomework stuHomeWork = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("delete_flag", 0)
                .eq("homework_id", param.getHomeworkId())
                .eq("student_id", param.getStudentId()));
        if (stuHomeWork == null) {
            throw new APIException("???????????????????????????");
        }
        //?????????
        stuHomeWork.setScore(sumScore)
                .setCheckStatus(1)
                .setApprovalTime(new Date())
                .setApprovalUserId(loginUser.getUserId())
                .setApprovalUser(loginUser.getUserName())
                .setComments(param.getComments())
                .setUpdateTime(new Date())
                .setUpdateUser(loginUser.getUserId());
        BusinessResponseEnum.FAILAPPROVAL.assertIsTrue(stuHomeworkService.saveOrUpdate(stuHomeWork));
        // 3.???????????????????????????
        stuHomeworkInfos.forEach(item -> {
            TStuHomeworkInfo stuHomeworkInfo = stuHomeworkInfoService.getOne(new QueryWrapper<TStuHomeworkInfo>()
                    .eq("homework_id", param.getHomeworkId())
                    .eq("student_id", param.getStudentId())
                    .eq("exercise_id", item.getExerciseId())
                    .eq("delete_flag", 0));
            if (stuHomeworkInfo != null) {
                //??????????????????????????????????????????????????????????????????
                stuHomeworkInfo.setExerciseScore(item.getExerciseScore())
                        .setExerciseResult(item.getExerciseResult())
                        .setIsCorrect(item.getIsCorrect())
                        .setUpdateTime(new Date())
                        .setUpdateUser(loginUser.getUserId());
                //???????????????
                tStuHomeworkInfos.add(stuHomeworkInfo);
            }
        });
        boolean res = stuHomeworkInfoService.saveOrUpdateBatch(tStuHomeworkInfos);
        BusinessResponseEnum.FAILAPPROVAL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2022/9/21 16:55
     * @param: [request, studentId, homeworkId]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean callBack(HttpServletRequest request, int studentId, int homeworkId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //??????????????????
        TStuHomework stuHomework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("homework_id", homeworkId)
                .eq("student_id", studentId)
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNSTUDENTHOMEWORK.assertNotNull(stuHomework, homeworkId, studentId);
        TStuHomework tStuHomework = (TStuHomework) stuHomework.setHomeworkStatus(3)
                .setCheckStatus(2)
                .setUpdateUser(loginUser.getUserId())
                .setUpdateTime(new Date());
        //??????????????????
        boolean res = stuHomeworkService.saveOrUpdate(tStuHomework);
        BusinessResponseEnum.FAILACALLBACK.assertIsTrue(res);
        return res;
    }

    /**
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/9/22 9:48
     * @param: [request, course_id]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.THomework>
     **/
    @Override
    public List<HomeWrokByStudent> getHomeWorkByStudent(HttpServletRequest request, ListStudentHomeWork param) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        int userId = loginUser.getUserId();
        // ??????????????????????????????id??????????????????
        List<Sclass> classes = sclassMapper.findClassByStuAndCourse(param.getCourseId(), userId);
        //??????????????????????????????
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
     * @description: ?????????????????????
     * @author:
     * @date: 2022/9/22 13:45
     * @param: [request, homeWork]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public double saveHomeWork(HttpServletRequest request, SaveHomework homeWork) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //????????????
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        //???????????????
        AtomicDouble score = new AtomicDouble();
        //??????????????????
        List<SaveHomeworkInfo> homeworkInfos = homeWork.getHomeworkInfos();
        // ????????????.???????????????????????????
        homeworkInfos.forEach(item -> {
            TStuHomeworkInfo stuHomeworkInfo = stuHomeworkInfoMapper
                    .getHomeworkInfoAndExercise(homeWork.getHomeworkId(), item.getExerciseId(), loginUser.getUserId());
            if (stuHomeworkInfo != null) {
                //??????????????????
                stuHomeworkInfo.setExerciseResult(item.getExerciseResult());
                //????????????????????????????????????
                if (stuHomeworkInfo.getExerciseStyle() == 2) {
                    determineExercise(request, stuHomeworkInfo, item);
                    score.addAndGet(stuHomeworkInfo.getExerciseScore());
                }
                stuHomeworkInfo.setUpdateUser(loginUser.getUserId()).setUpdateTime(new Date());
                tStuHomeworkInfos.add(stuHomeworkInfo);
            }
        });
        // ????????????
        if (!tStuHomeworkInfos.isEmpty())
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(stuHomeworkInfoService.saveOrUpdateBatch(tStuHomeworkInfos));
        return score.get();
    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/23 15:52
     * @param: [request, homeWork]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitHomeWork(HttpServletRequest request, SaveHomework homeWork) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //??????
        double score = saveHomeWork(request, homeWork);
        //??????
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
     * @description: ????????????
     * @author:
     * @date: 2022/12/9 10:12
     * @param: [request, student_id ??????id, homework_id ??????id]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean overTime(HttpServletRequest request, OverTimeForStudent overTimeForStudent) {
        //????????????????????????
        TStuHomework stuHomework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("homework_id", overTimeForStudent.getHomeworkId())
                .eq("student_id", overTimeForStudent.getStudentId())
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNSTUDENTHOMEWORK.assertNotNull(stuHomework, overTimeForStudent.getHomeworkId(), overTimeForStudent.getStudentId());
        //??????????????????
        stuHomework.setEndTime(overTimeForStudent.getEndTime());
        //??????
        boolean res = stuHomeworkService.saveOrUpdate(stuHomework);
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/12/21 10:20
     * @param: [request, student_id ??????id, homework_id??????id]
     * @return: TStuHomework
     **/
    @Override
    public TStuHomework reviseStudentScore(HttpServletRequest request, ResiveStudentScore resiveStudentScore) {
        //??????????????????
        TStuHomework homework = stuHomeworkService.getOne(new QueryWrapper<TStuHomework>()
                .eq("student_id", resiveStudentScore.getStudentId())
                .eq("homework_id", resiveStudentScore.getHomeworkId()));
        BusinessResponseEnum.UNSTUDENTHOMEWORK.assertNotNull(homework, resiveStudentScore.getHomeworkId(), resiveStudentScore.getStudentId());
        homework.setScore(resiveStudentScore.getMark());
        //??????
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(stuHomeworkService.saveOrUpdate(homework));
        return homework;
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/10/25 14:03
     * @param: [homework_id, res, review]
     * @return: void
     **/
    private HomeWorkINfoModel getHomeWorkInfo(int studentId, int homeworkId, List<TStuHomeworkInfoVO> review) {
        //????????????
        THomework homeWork = this.getById(homeworkId);
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(homeWork, homeworkId);
        THomeworkModel homeworkModel = homeworkModelService.getById(homeWork.getModelId());
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(homeworkModel, homeWork.getModelId());
        //??????id
        Integer classId = null;
        //????????????
        String className = null;
        //????????????
        double stuScore = 0.0;
        //????????????
        String homeworkName = homeWork.getHomeworkName();
        //??????????????????
        Date startTime = homeWork.getStartTime();
        //??????????????????
        Date endTime = homeWork.getEndTime();
        //????????????
        AtomicReference<Double> score = new AtomicReference<>(homeWork.getScore());
        //????????????
        AtomicReference<Double> mark = new AtomicReference<>(0.0);
        //??????????????????
        int allowAfter = homeWork.getAllowAfter();
        //????????????
        int exerciseCount = modelExerciseService.count(new QueryWrapper<TModelExercise>().eq("model_id", homeWork.getModelId()));
        //????????????????????????
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
        //????????????
        double actualScore = review.stream()
                .filter(a -> a.getExerciseActualScore() != null)
                .mapToDouble(TStuHomeworkInfoVO::getExerciseActualScore
                ).sum();
        //????????????
        score.set(actualScore);
        //?????????????????????
        List<Integer> typeCodes = exerciseTypeService.list().stream()
                .filter(a -> a.getTypeStyle() == 2)
                .map(TExerciseType::getTypeCode)
                .collect(Collectors.toList());
        //???????????????
        double objectiveScore = review.stream()
                .filter(a -> a.getExerciseScore() != null && typeCodes.contains(a.getExerciseType()))
                .mapToDouble(TStuHomeworkInfoVO::getExerciseScore
                ).sum();
        //??????????????????
        mark.set(stuScore);
        //?????????????????????
        List<HomeWorklExerciseModel> classifyList = new ArrayList<>();
        // ???????????????
        if (homeworkModel.getClassify() == 1) {
            //????????????????????????????????????????????????
            List<TModelExerciseType> typeList = modelExerciseTypeService.list(new QueryWrapper<TModelExerciseType>()
                    .eq("model_id", homeWork.getModelId()));
            List<Integer> types = typeList.stream().map(TModelExerciseType::getExerciseType).collect(Collectors.toList());
            //???????????????
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
                //??????????????????????????????
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
        //??????????????????
        UserInfo userInfo = userInfoService.getById(studentId);
        BusinessResponseEnum.UNUSERINFO.assertNotNull(userInfo, studentId);
        // ????????????????????????
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
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/9/28 15:06
     * @param: [loginUser, id]
     * @return: void
     **/
    private void studentHomeworkInfoDelete(int id) {
        //????????????????????????
        List<TStuHomeworkInfo> stuHomeworkInfos = stuHomeworkInfoService.list(new QueryWrapper<TStuHomeworkInfo>()
                .eq("homework_id", id));
        if (stuHomeworkInfos != null && !stuHomeworkInfos.isEmpty()) {
            //????????????????????????
            BusinessResponseEnum.DELFAIL
                    .assertIsTrue(stuHomeworkInfoService.removeByIds(stuHomeworkInfos.stream()
                            .map(TStuHomeworkInfo::getId)
                            .collect(Collectors.toList())));
        }
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/9/28 15:05
     * @param: [loginUser, id]
     * @return: void
     **/
    private void studentHomeworkDelete(int id) {
        //?????????????????????
        List<TStuHomework> stuHomework = stuHomeworkService.list(new QueryWrapper<TStuHomework>().eq("homework_id", id));
        if (stuHomework != null && !stuHomework.isEmpty()) {
            //?????????????????????
            BusinessResponseEnum.DELFAIL
                    .assertIsTrue(stuHomeworkService.removeByIds(stuHomework.stream()
                            .map(TStuHomework::getId)
                            .collect(Collectors.toList())));
        }
    }

    /**
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/9/28 15:04
     * @param: [loginUser, id]
     * @return: void
     **/
    private void homeworkDistributionStudentDelete(int id) {
        //???????????????????????????
        List<THomeworkDistributionStudent> homeworkDistributionStydents = homeworkDistributionStydentService
                .list(new QueryWrapper<THomeworkDistributionStudent>()
                        .eq("homework_id", id));
        if (homeworkDistributionStydents != null && !homeworkDistributionStydents.isEmpty()) {
            //???????????????????????????
            BusinessResponseEnum.DELFAIL
                    .assertIsTrue(homeworkDistributionStydentService.removeByIds(homeworkDistributionStydents.stream()
                            .map(THomeworkDistributionStudent::getId)
                            .collect(Collectors.toList())));
        }
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/9/28 15:02
     * @param: [loginUser, id]
     * @return: void
     **/
    private void homeworkDistributionDelete(int id) {
        //???????????????????????????
        List<THomeworkDistribution> homeworkDistributions = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("homework_id", id));
        //???????????????????????????
        BusinessResponseEnum.UNHOMEWORKDISTRIBUTION.assertIsNotEmpty(homeworkDistributions, id);
        //????????????
        BusinessResponseEnum.DELFAIL
                .assertIsTrue(homeworkDistributionService.removeByIds(homeworkDistributions.stream()
                        .map(THomeworkDistribution::getId)
                        .collect(Collectors.toList())));
    }

    /**
     * @description: ???????????????
     * @author:
     * @date: 2022/9/28 15:01
     * @param: [loginUser, id]
     * @return: void
     **/
    private void homeworkDelete(int id) {
        THomework homework = this.getById(id);
        BusinessResponseEnum.UNHOMEWORK.assertNotNull(homework, id);
        //????????????
        BusinessResponseEnum.DELFAIL
                .assertIsTrue(this.removeById(id));
    }

    //????????????????????????
    private void determineExercise(HttpServletRequest request, TStuHomeworkInfo stuHomeworkInfo, SaveHomeworkInfo item) {
        ExerciseFactory.getDetermine(stuHomeworkInfo.getExerciseType()).determineExercise(request, stuHomeworkInfo, item.getExerciseResult());
    }


    /**
     * @description: ????????????
     * @author:
     * @date: 2022/9/19 16:37
     * @param: [param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.TStuHomework>
     **/
    private List<TStuHomework> listApprovalList(ApprovalList param) {
        return stuHomeworkMapper.getApprovalList(param);
    }


    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/9/16 17:18
     * @param: [param]
     * @return: java.util.List<com.highgo.opendbt.homework.domain.entity.THomework>
     **/
    private List<THomework> list(ListHomeWork param) {
        return homeworkMapper.getHomeWorkLilst(param);
    }
}




