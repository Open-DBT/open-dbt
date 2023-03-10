package com.highgo.opendbt.homeworkmodel.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.course.domain.entity.TCourseUser;
import com.highgo.opendbt.course.service.CourseService;
import com.highgo.opendbt.course.service.TCourseUserService;
import com.highgo.opendbt.exercise.domain.entity.TExerciseInfo;
import com.highgo.opendbt.exercise.domain.entity.TExerciseType;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.TExerciseInfoVO;
import com.highgo.opendbt.exercise.domain.model.TNewExerciseDTO;
import com.highgo.opendbt.exercise.domain.model.TNewExerciseVo;
import com.highgo.opendbt.exercise.service.TExerciseTypeService;
import com.highgo.opendbt.exercise.service.TNewExerciseService;
import com.highgo.opendbt.homework.domain.entity.*;
import com.highgo.opendbt.homework.domain.model.PublishHomeWork;
import com.highgo.opendbt.homework.mapper.TStuHomeworkInfoMapper;
import com.highgo.opendbt.homework.mapper.TStuHomeworkMapper;
import com.highgo.opendbt.homework.service.*;
import com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel;
import com.highgo.opendbt.homeworkmodel.domain.entity.TModelExercise;
import com.highgo.opendbt.homeworkmodel.domain.entity.TModelExerciseType;
import com.highgo.opendbt.homeworkmodel.domain.model.*;
import com.highgo.opendbt.homeworkmodel.mapper.THomeworkModelMapper;
import com.highgo.opendbt.homeworkmodel.mapper.TModelExerciseTypeMapper;
import com.highgo.opendbt.homeworkmodel.service.THomeworkModelService;
import com.highgo.opendbt.homeworkmodel.service.TModelExerciseService;
import com.highgo.opendbt.homeworkmodel.service.TModelExerciseTypeService;
import com.highgo.opendbt.sclass.service.SclassService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.service.UserInfoService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ?????????????????????
 */
@Service
public class THomeworkModelServiceImpl extends ServiceImpl<THomeworkModelMapper, THomeworkModel>
        implements THomeworkModelService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private THomeworkModelService homeworkModelService;
    @Autowired
    private THomeworkService homeworkService;
    @Autowired
    private THomeworkDistributionService homeworkDistributionService;
    @Autowired
    private SclassService sclassService;
    @Autowired
    private TStuHomeworkService stuHomeworkService;
    @Autowired
    private TStuHomeworkInfoService stuHomeworkInfoService;
    @Autowired
    private TModelExerciseService modelExerciseService;
    @Autowired
    private TNewExerciseService exerciseService;
    @Autowired
    private TExerciseTypeService exerciseTypeService;
    @Autowired
    private THomeworkDistributionStudentService homeworkDistributionStudentService;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private TModelExerciseTypeService modelExerciseTypeService;
    @Autowired
    private THomeworkModelMapper homeworkModelMapper;
    @Autowired
    private TStuHomeworkMapper stuHomeworkMapper;
    @Autowired
    private TStuHomeworkInfoMapper stuHomeworkInfoMapper;
    @Autowired
    private CourseService courseService;
    @Autowired
    private TCourseUserService courseUserService;
    @Autowired
    private TModelExerciseTypeMapper modelExerciseTypeMapper;

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/6 10:38
     * @param: [request, param, result]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    @Override
    public Map<String, Object> getHomeWorkModel(HttpServletRequest request, @Valid PageParam<ListHomeWorkModel> param) {
        if (StringUtils.isBlank(param.getOrderBy())) {
            param.setOrderBy("element_type desc,id desc");
        } else {
            param.setOrderBy("element_type desc," + param.getOrderBy() + ",id desc");
        }
        //????????????
        PageInfo<THomeworkModel> objectPageInfo = PageMethod.startPage(param.getPageNum(), param.getPageSize())
                .setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> list(param.getParam()));
        //??????????????????????????????????????????
        int count = homeworkModelService.count(new QueryWrapper<THomeworkModel>()
                .eq("course_id", param.getParam().getCourseId())
                .eq("element_type", 0)
                .eq("delete_flag", 0)
                .like(StringUtils.isNotEmpty(param.getParam().getModelName()), "model_name", param.getParam().getModelName()));
        //??????????????????
        Map<String, Object> res = new HashMap<>();
        res.put("count", count);
        res.put("objectPageInfo", objectPageInfo);
        return res;
    }

    /**
     * @description: ???????????????
     * @author:
     * @date: 2022/9/6 11:07
     * @param: [request, param, result]
     * @return: boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveHomeWorkModelFolder(HttpServletRequest request, SaveHomeWorkModelFolder param) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //?????????homeWorkModel????????????
        THomeworkModel homeWorkModel = new THomeworkModel();
        BeanUtils.copyProperties(param, homeWorkModel);
        //????????????????????????????????????
        if (param.getId() != null && param.getId() != -1) {
            homeWorkModel.setUpdateTime(new Date()).setUpdateUser(loginUser.getUserId());
        } else {
            //????????????????????????????????????
            homeWorkModel.setAuthType(2).setElementType(1).setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
        }
        boolean res = homeworkModelService.saveOrUpdate(homeWorkModel);
        //????????????????????????,?????????????????????
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: ??????
     * @author:
     * @date: 2022/9/6 13:42
     * @param: [request, id]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delHomeWorkModel(HttpServletRequest request, int id) {
        //?????????????????????
        List<THomeworkModel> homeworkModelList = new ArrayList<>();
        List<THomeworkModel> homeworkList = new ArrayList<>();
        //??????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //??????
        THomeworkModel homeWork = homeworkModelService.getById(id);
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(homeWork, id);
        homeworkList.add(homeWork);
        //???????????????child
        traverseModel(homeworkList, homeworkModelList);
        //???????????????????????????id
        List<Integer> ids = homeworkModelList.stream().map(THomeworkModel::getId).collect(Collectors.toList());
        //??????????????????????????????????????????????????????????????????????????????
        List<THomework> tHomeworks = homeworkService.list(new QueryWrapper<THomework>().in("model_id", ids)
                .eq("delete_flag", 0));
        StringBuilder modelname = new StringBuilder();
        tHomeworks.forEach(item -> {
            modelname.append(item.getModelName());
            modelname.append(":");
            modelname.append(item.getModelId());
            modelname.append(",");
        });
        BusinessResponseEnum.BINDINGHOMEWORK.assertIsEmpty(tHomeworks, modelname.toString());
        //????????????
        boolean b = homeworkModelService.removeByIds(ids);
        logger.info("??????{}{}?????????{}", loginUser.getUserName(), loginUser.getUserId(), homeworkModelList.toString());
        //?????????????????????
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>().in("model_id", ids));
        //?????????????????????
        if ((modelExercises != null) && !modelExercises.isEmpty()) {
            modelExerciseService.removeByIds(modelExercises.stream().map(TModelExercise::getId).collect(Collectors.toList()));
        }
        return b;
    }

    /**
     * @description: ???????????????child
     * @author:
     * @date: 2022/12/2 10:58
     * @param: [homeworkList ?????????????????????, homeworkModelList?????????????????????]
     * @return: void
     **/
    private void traverseModel(List<THomeworkModel> homeworkList, List<THomeworkModel> homeworkModelList) {
        if ((homeworkList != null) && !homeworkList.isEmpty()) {
            homeworkModelList.addAll(homeworkList);
            homeworkList.forEach(item -> {
                if (item.getElementType() == 1) {
                    List<THomeworkModel> list = homeworkModelService.list(new QueryWrapper<THomeworkModel>()
                            .eq("parent_id", item.getId())
                            .eq("delete_flag", 0));
                    traverseModel(list, homeworkModelList);
                }
            });
        }
    }


    /**
     * @description: ??????
     * @author:
     * @date: 2022/9/7 19:15
     * @param: [request, param, result]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishHomeWork(HttpServletRequest request, PublishHomeWork param) {
        //??????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        THomework homeWork = new THomework();
        BeanUtils.copyProperties(param, homeWork);
        //??????????????????
        homeWork.setHomeworkName(homeWork.getModelName());
        //?????????????????????
        List<THomeworkDistribution> distributions = param.getDistributions();
        //???????????????
        saveHomeWork(homeWork, loginUser);
        //?????????????????????????????????????????????
        saveDistribution(homeWork, distributions, loginUser);
        //?????????????????????
        saveStuHomeWork(homeWork, distributions, loginUser);
        return true;
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/11/10 10:36
     * @param: [request, class_id ??????id, user_id ??????id]
     * @return: void
     **/
    @Override
    public void InitHomeWorkTables(HttpServletRequest request, Integer classId, Integer userId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //1.??????target_type???1????????????class_id??????????????????
        List<THomeworkDistribution> homeworkDistributions = homeworkDistributionService.list(new QueryWrapper<THomeworkDistribution>()
                .eq("class_id", classId)
                .eq("target_type", 1)
                .eq("delete_flag", 0));
        if ((homeworkDistributions == null) || homeworkDistributions.isEmpty()) {
            return;
        }
        UserInfo user = userInfoService.getById(userId);
        //?????????????????????
        List<TStuHomework> stuHomeworkList = new ArrayList<>();
        //???????????????????????????
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        homeworkDistributions.forEach(item -> {
            //????????????id????????????????????????
            THomework homework = homeworkService.getById(item.getHomeworkId());
            //??????????????????????????????
            List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                    .eq("model_id", homework.getModelId())
                    .eq("delete_flag", 0));
            //?????????????????????
            TStuHomework tStuHomework = new TStuHomework();
            tStuHomework.setHomeworkId(homework.getId())
                    .setStudentCode(user.getCode())
                    .setCourseId(homework.getCourseId())
                    .setClassId(item.getClassId())
                    .setClassName(item.getClassName())
                    .setStudentId(userId)
                    .setStudentName(user.getUserName())
                    .setHomeworkStatus(2).setCheckStatus(2)
                    .setCreateTime(new Date())
                    .setCreateUser(loginUser.getUserId());
            stuHomeworkList.add(tStuHomework);
            //???????????????????????????
            for (TModelExercise modelExercise : modelExercises) {
                TStuHomeworkInfo tStuHomeworkInfo = new TStuHomeworkInfo();
                tStuHomeworkInfo.setExerciseId(modelExercise.getExerciseId())
                        .setHomeworkId(homework.getId())
                        .setHomeworkName(homework.getHomeworkName())
                        .setModelId(homework.getModelId())
                        .setModelName(homework.getModelName())
                        .setStudentId(userId).setStudentName(user.getUserName())
                        .setStudentCode(user.getCode())
                        .setCourseId(homework.getCourseId())
                        .setClassId(item.getClassId())
                        .setClassName(item.getClassName())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId());
                tStuHomeworkInfos.add(tStuHomeworkInfo);
            }
        });
        //?????????????????????
        stuHomeworkMapper.insertBatch(stuHomeworkList);
        //???????????????????????????
        stuHomeworkInfoMapper.insertBatch(tStuHomeworkInfos);
    }

    @Override
    public void delInitHomeWorkTables(HttpServletRequest request, Integer classId, List<Integer> userId) {
        //??????????????????
        List<THomeworkDistribution> homeworkDistributions = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("class_id", classId)
                        .eq("target_type", 1)
                        .eq("delete_flag", 0));
        if ((homeworkDistributions == null) || homeworkDistributions.isEmpty()) {
            homeworkDistributions = new ArrayList<>();
        }
        //??????????????????
        List<THomeworkDistribution> homeworkDistributionsStu = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("class_id", classId)
                        .eq("target_type", 2)
                        .eq("delete_flag", 0));
        if (homeworkDistributionsStu != null && !homeworkDistributionsStu.isEmpty()) {
            //??????????????????????????????????????????
            updateDistributions(homeworkDistributionsStu, userId);
            homeworkDistributions.addAll(homeworkDistributionsStu);
        }
        if (homeworkDistributions.isEmpty()) {
            return;
        }

        //?????????????????????
        List<TStuHomework> tStuHomeworkList = new ArrayList<>();
        //???????????????????????????
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        homeworkDistributions.forEach(item -> {
            // ?????????????????????
            List<TStuHomework> stuHomeworks = stuHomeworkService.list(new QueryWrapper<TStuHomework>()
                    .eq("homework_id", item.getHomeworkId())
                    .in("student_id", userId));
            tStuHomeworkList.addAll(stuHomeworks);
            // ???????????????????????????
            List<TStuHomeworkInfo> stuHomeworkInfos = stuHomeworkInfoService.list(new QueryWrapper<TStuHomeworkInfo>()
                    .eq("homework_id", item.getHomeworkId())
                    .in("student_id", userId));
            tStuHomeworkInfos.addAll(stuHomeworkInfos);
        });
        //?????????????????????
        if (!tStuHomeworkList.isEmpty()) {
            stuHomeworkService.removeByIds(tStuHomeworkList.stream().map(TStuHomework::getId).collect(Collectors.toList()));
        }
        //???????????????????????????
        if (!tStuHomeworkInfos.isEmpty()) {
            stuHomeworkInfoService.removeByIds(tStuHomeworkInfos.stream().map(TStuHomeworkInfo::getId).collect(Collectors.toList()));
        }
    }

    //??????????????????????????????????????????
    private void updateDistributions(List<THomeworkDistribution> homeworkDistributionsStu, List<Integer> userId) {
        //????????????THomeworkDistribution??????
        List<THomeworkDistribution> homeworkDistributionList = new ArrayList<>();
        List<THomeworkDistributionStudent> homeworkDistributionStudentList = new ArrayList<>();
        homeworkDistributionsStu.forEach(item -> {
            //?????????????????????
            List<THomeworkDistributionStudent> homeworkDistributionStudents = homeworkDistributionStudentService
                    .list(new QueryWrapper<THomeworkDistributionStudent>()
                            .eq("homework_id", item.getHomeworkId())
                            .in("student_id", userId));
            if (homeworkDistributionStudents != null && !homeworkDistributionStudents.isEmpty()) {//????????????????????????
                homeworkDistributionStudentList.addAll(homeworkDistributionStudents);
                //???????????????????????????
                List<THomeworkDistributionStudent> students = homeworkDistributionStudentService
                        .list(new QueryWrapper<THomeworkDistributionStudent>()
                                .eq("homework_id", item.getHomeworkId())
                                .eq("delete_flag", 0));
                if (students.size() == 1) {
                    //?????????????????????????????????????????????THomeworkDistribution
                    homeworkDistributionList.add(item);
                }
            }
        });
        //???????????????????????????
        if (!homeworkDistributionList.isEmpty()) {
            homeworkDistributionService.removeByIds(
                    homeworkDistributionList.stream()
                            .map(THomeworkDistribution::getId)
                            .collect(Collectors.toList()));
        }
        //???????????????????????????
        if (!homeworkDistributionStudentList.isEmpty()) {
            homeworkDistributionStudentService.removeByIds(
                    homeworkDistributionStudentList.stream()
                            .map(THomeworkDistributionStudent::getId)
                            .collect(Collectors.toList()));
        }
    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/8 17:41
     * @param: [request, id ????????????id]
     * @return: com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public THomeworkModel copyHomeWorkModel(HttpServletRequest request, int id) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ??????????????????
        THomeworkModel homeWork = homeworkModelService.getById(id);
        // ???????????????????????????
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("model_id", homeWork.getId())
                .eq("delete_flag", 0));
        // ??????????????????
        homeWork.setModelName(homeWork.getModelName() + "_??????");
        homeWork.setId(null).setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
        // ??????????????????????????????
        saveCopyHomeWorkModel(homeWork);
        // ???????????????????????????
        List<TModelExercise> collect = modelExercises.stream()
                .peek(item -> item.setId(null)
                        .setModelId(homeWork.getId())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId()))
                .collect(Collectors.toList());
        // ???????????????????????????
        saveCopyModelExercise(modelExercises);
        // ?????????????????????????????????
        homeWork.setModelExercises(collect);
        //???????????????????????????
        saveCopyModelExerciseType(id, homeWork, loginUser.getUserId());
        return homeWork;
    }

    /**
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/12/2 13:57
     * @param: [id ??????????????????id, homeWork ????????????????????????]
     * @return: void
     **/
    private void saveCopyModelExerciseType(int id, THomeworkModel homeWork, int userId) {
        //??????????????????????????????
        List<TModelExerciseType> exerciseTypes = modelExerciseTypeService.list(new QueryWrapper<TModelExerciseType>()
                .eq("model_id", id));
        if ((exerciseTypes == null) || exerciseTypes.isEmpty()) {
            return;
        }
        //????????????????????????
        List<TModelExerciseType> modelExerciseTypes = exerciseTypes.stream()
                .peek(item -> item.setModelId(homeWork.getId())
                        .setId(null)
                        .setUpdateTime(new Date())
                        .setUpdateUser(userId))
                .collect(Collectors.toList());
        //????????????
        modelExerciseTypeMapper.insertBatch(modelExerciseTypes);
    }

    /**
     * @description: ????????????id?????????????????????????????????
     * @author:
     * @date: 2022/10/31 14:51
     * @param: [exercise_id]
     * @return: boolean
     **/
    @Override
    public List<Integer> getIsBandingModel(int exerciseId) {
        return homeworkModelMapper.getIsBandingModel(exerciseId);

    }

    /**
     * @description: ????????????id??????????????????
     * @author:
     * @date: 2022/11/8 9:25
     * @param: [request, id ??????id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    public TNewExercise getExerciseInfo(HttpServletRequest request, int exerciseId, int modelId) {
        TNewExercise exerciseInfo = exerciseService.getExerciseInfo(request, exerciseId);
        //?????????????????????
        BusinessResponseEnum.UNEXERCISEiNFOBYMODEL.assertNotNull(exerciseInfo, exerciseId, modelId);
        //????????????????????????
        TModelExercise modelExercise = modelExerciseService.getOne(new QueryWrapper<TModelExercise>()
                .eq("model_id", modelId)
                .eq("exercise_id", exerciseId)
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNMODELEXERCISE.assertNotNull(modelExercise, modelId, exerciseId);
        //??????????????????
        exerciseInfo.setExerciseScore(modelExercise.getExerciseScore());
        // ???????????????????????????????????????
        List<Integer> bandingModel = homeworkModelService.getIsBandingModel(exerciseId);
        boolean isBandingModel = ((bandingModel != null)
                && !bandingModel.isEmpty()
                && (bandingModel.size() != 1)
                || (bandingModel != null)
                && !bandingModel.isEmpty()
                && (bandingModel.get(0) != modelId));
        //?????????????????????????????????
        exerciseInfo.setBandingModel(isBandingModel);
        return exerciseInfo;
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/11/8 15:00
     * @param: [request, model_id ??????id]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    @Override
    public Map<String, Object> getHomeWorkModelExercises(HttpServletRequest request, PageParam<TNewExerciseDTO> param) {
        Map<String, Object> exercise = exerciseService.getExercise(request, param);
        Integer modelId = param.getParam().getModelId();
        //????????????????????????
        if (modelId == null) {
            return exercise;
        }
        //???????????????????????????
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("delete_flag", 0)
                .eq("model_id", modelId)
                .select("exercise_id"));
        if ((modelExercises == null) || modelExercises.isEmpty()) {
            //?????????????????????
            return exercise;
        }
        //???????????????id??????
        List<Integer> ids = modelExercises.stream().map(TModelExercise::getExerciseId).collect(Collectors.toList());
        //?????????????????????
        PageInfo<TNewExercise> pageList = (PageInfo<TNewExercise>) exercise.get("pageList");
        List<TNewExercise> list = pageList.getList();
        List<ModelExerciseDTO> modelExerciseDTOS = new ArrayList<>();

        //???????????? ???????????????????????????????????????????????? checked 0:????????? 1????????????
        List<TNewExercise> collect = list.stream().peek(item -> {
            if (ids.contains(item.getId())) {
                item.setChecked(1);
                modelExerciseDTOS.add(new ModelExerciseDTO().setExerciseId(item.getId()).setExerciseType(item.getExerciseType()));
            }
        }).collect(Collectors.toList());
        pageList.setList(collect);
        exercise.put("pageList", pageList);
        exercise.put("modelExerciseDTOS", modelExerciseDTOS);
        return exercise;


    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2022/11/8 16:41
     * @param: [request, param]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public THomeworkModel completedSelectedExercises(HttpServletRequest request, SelectedExercisesDTO param) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        THomeworkModel tHomeworkModel;
        if (param.getModelId() == null) { // ??????id??????
            //????????????
            tHomeworkModel = new THomeworkModel();
            BeanUtils.copyProperties(param, tHomeworkModel);
            tHomeworkModel.setElementType(0);
            tHomeworkModel.setGrandingStandard(2);
            tHomeworkModel.setAuthType(2);
            tHomeworkModel.setDeleteFlag(0);
        } else {
            //????????????
            tHomeworkModel = this.getById(param.getModelId());
            tHomeworkModel.setModelName(param.getModelName());
            tHomeworkModel.setClassify(param.getClassify());
        }
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(tHomeworkModel, param.getModelId());

        //????????????
        List<ModelExerciseDTO> modelExerciseDTOS = param.getModelExerciseDTOS();
        if ((modelExerciseDTOS != null) && !modelExerciseDTOS.isEmpty()) {
            //?????????????????????
            tHomeworkModel.setTgp((tHomeworkModel.getTgp() == null ? 0 : tHomeworkModel.getTgp()) + (modelExerciseDTOS.size() * 5));
        }
        //????????????????????????
        homeworkModelService.saveOrUpdate((THomeworkModel) tHomeworkModel.setCreateTime(new Date()).setCreateUser(loginUser.getUserId()));
        if ((modelExerciseDTOS != null) && !modelExerciseDTOS.isEmpty()) {
            //??????modelExercises
            List<TModelExercise> modelExercises = getModelExercises(param, tHomeworkModel);
            //??????????????????????????????
            saveModelExercises(modelExercises, tHomeworkModel, loginUser);
            //??????????????????????????????
            saveModelExerciseType(modelExercises, tHomeworkModel, loginUser);
        }
        return tHomeworkModel;
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/11/9 14:42
     * @param: [request, model_id, exercise_id]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delSelectedExercises(HttpServletRequest request, int modelId, int exerciseId) {
        THomeworkModel model = homeworkModelService.getById(modelId);
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(model, modelId);
        TModelExercise modelExercise = modelExerciseService.getOne(new QueryWrapper<TModelExercise>()
                .eq("exercise_id", exerciseId)
                .eq("model_id", modelId)
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNMODELEXERCISE.assertNotNull(modelExercise, modelId, exerciseId);
        model.setTgp(model.getTgp() - modelExercise.getExerciseScore());
        //?????????????????????????????????
        homeworkModelService.updateById(model);
        //????????????????????????
        modelExerciseService.removeById(modelExercise.getId());
        //????????????????????????????????????
        updateExerciseType(modelId, modelExercise.getExerciseType());
        return true;
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/11/21 13:47
     * @param: [request, param]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TNewExercise saveExercise(HttpServletRequest request, EditNewExerciseDTO param) {
        //??????
        TNewExerciseVo tNewExerciseVo = new TNewExerciseVo();
        BeanUtils.copyProperties(param, tNewExerciseVo);
        //????????????
        TNewExercise exercise = updateExercise(request, tNewExerciseVo);
        //???????????????????????????
        TModelExercise modelExercise = modelExerciseService.getOne(new QueryWrapper<TModelExercise>()
                .eq("model_id", param.getModelId())
                .eq("exercise_id", param.getId())
                .eq("delete_flag", 0));
        modelExercise.setExerciseScore(param.getExerciseScore());
        modelExerciseService.saveOrUpdate(modelExercise);
        return exercise;
    }

    /**
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/11/21 14:55
     * @param: [request, exerciseId ??????id, model_id ??????id, exercise_score ????????????]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateScoreByModel(HttpServletRequest request, int exerciseId, int modelId, Double exerciseScore) {
        TModelExercise modelExercise = modelExerciseService.getOne(new QueryWrapper<TModelExercise>()
                .eq("model_id", modelId)
                .eq("exerciseId", exerciseId)
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNMODELEXERCISE.assertNotNull(modelExercise, modelId, exerciseId);
        modelExercise.setExerciseScore(exerciseScore);
        modelExerciseService.saveOrUpdate(modelExercise);
        return true;
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/11/23 13:31
     * @param: [request, course_id ??????id]
     * @return: java.util.List<com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel>
     **/
    @Override
    public List<THomeworkModel> getHomeWorkModelCatalogueTree(HttpServletRequest request, Integer courseId) {
        List<THomeworkModel> homeworkModelList = homeworkModelMapper.getHomeWorkModelCatalogueTree(courseId);
        //???????????????
        THomeworkModel homeworkModel = new THomeworkModel().setId(0).setModelName("?????????").setChildrens(homeworkModelList);
        List<THomeworkModel> tHomeworkModelList = new ArrayList<>();
        tHomeworkModelList.add(homeworkModel);
        return tHomeworkModelList;
    }

    /**
     * @description: ???????????????
     * @author:
     * @date: 2022/11/23 13:31
     * @param: [request, oid, tid]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveHomeWorkModel(HttpServletRequest request, int oid, int tid) {
        if (tid != 0) {//???????????????
            //???????????????
            THomeworkModel thomeworkModel = homeworkModelService.getOne(new QueryWrapper<THomeworkModel>()
                    .eq("id", tid)
                    .eq("element_type", 1));
            //???????????????????????????
            BusinessResponseEnum.UNHOMEWORKMODELPACKAGE.assertNotNull(thomeworkModel, tid);
        }
        //??????????????????
        THomeworkModel oHomeWorkModel = homeworkModelService.getById(oid);
        //??????????????????????????????
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(oHomeWorkModel, oid);
        //????????????????????????
        BusinessResponseEnum.EQUALSHOMEWORKMODELPACKAGE.assertIsFalse(oHomeWorkModel.getParentId() == tid);
        oHomeWorkModel.setParentId(tid);
        boolean res = homeworkModelService.saveOrUpdate(oHomeWorkModel);
        BusinessResponseEnum.FAILMOVE.assertIsTrue(res);
        return res;
    }

    /**
     * @description: ????????????-????????????
     * @author:
     * @date: 2022/11/29 11:08
     * @param: [request, course_id]
     * @return: java.util.List<com.highgo.opendbt.homeworkmodel.domain.model.param.PublishClass>
     **/
    @Override
    public List<PublishClass> publishList(HttpServletRequest request, int courseId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        int userId = loginUser.getUserId();
        // ???????????????????????????
        Course course = courseService.getById(courseId);
        // ????????????????????????
        List<TCourseUser> courseUsers = courseUserService.list(new QueryWrapper<TCourseUser>()
                .eq("course_id", courseId));
        // ?????????????????????????????????????????????
        if (courseUsers != null && !courseUsers.isEmpty()) {
            List<TCourseUser> collect = courseUsers.stream().filter(item -> item.getUserId().equals(loginUser.getUserId()))
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                userId = course.getCreator();
            }
        }
        return homeworkModelMapper.getPublishList(courseId, userId);
    }

    //????????????
    private TNewExercise updateExercise(HttpServletRequest request, TNewExerciseVo param) {
        return exerciseService.saveExercise(request, param);
    }


    /**
     * @description: ????????????
     * @author:
     * @date: 2022/9/28 14:50
     * @param: [tExerciseInfo, exerciseInfos, loginUser, exercise_id]
     * @return: void
     **/
    private void addExercise(List<TExerciseInfo> tExerciseInfo, List<TExerciseInfoVO> exerciseInfos, UserInfo loginUser, int exerciseId) {
        if (exerciseInfos == null || exerciseInfos.isEmpty()) {
            return;
        }
        for (TExerciseInfoVO vo : exerciseInfos) {
            TExerciseInfo info = (TExerciseInfo) new TExerciseInfo()
                    .setExerciseId(exerciseId)
                    .setContent(vo.getContent())
                    .setPrefix(vo.getPrefix())
                    .setCreateTime(new Date())
                    .setCreateUser(loginUser.getUserId());
            tExerciseInfo.add(info);
        }
    }

    /**
     * @description: ????????????????????????????????????
     * @author:
     * @date: 2022/11/9 15:06
     * @param: [modelId ??????id, exerciseType ????????????]
     * @return: void
     **/
    private void updateExerciseType(int modelId, Integer exerciseType) {
        boolean isDelete = false;
        //??????????????????????????????
        List<TModelExercise> list = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("modelId", modelId)
                .eq("delete_flag", 0));
        if (list == null || list.isEmpty()) {
            // ??????????????????
            isDelete = true;
        } else {
            //?????????????????????????????????
            List<TModelExercise> collect = list.stream().filter(item -> item.getExerciseType().equals(exerciseType))
                    .collect(Collectors.toList());
            if (collect.isEmpty()) {
                // ??????????????????
                isDelete = true;
            }
        }
        if (isDelete) {
            //??????????????????????????????
            modelExerciseTypeService.remove(new QueryWrapper<TModelExerciseType>()
                    .eq("modelId", modelId)
                    .eq("exercise_type", exerciseType));
        }

    }

    /**
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/11/8 17:29
     * @param: [param, tHomeworkModel]
     * @return: java.util.List<com.highgo.opendbt.homeworkmodel.domain.entity.TModelExercise>
     **/
    private List<TModelExercise> getModelExercises(SelectedExercisesDTO param, THomeworkModel tHomeworkModel) {
        List<ModelExerciseDTO> addModelExercises;
        List<ModelExerciseDTO> modelExerciseDTOS = param.getModelExerciseDTOS();
        //???????????????id
        List<Integer> allIds = modelExerciseDTOS.stream().map(ModelExerciseDTO::getExerciseId).collect(Collectors.toList());
        //???????????????????????????
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("delete_flag", 0)
                .eq("model_id", param.getModelId())
                .in(!allIds.isEmpty(), "exercise_id", allIds));

        if (modelExercises.isEmpty()) {//????????????????????????
            addModelExercises = modelExerciseDTOS;
        } else {//?????????????????????
            //????????????????????????id
            List<Integer> existsIds = modelExercises.stream().map(TModelExercise::getExerciseId).collect(Collectors.toList());
            //???????????????????????????id
            addModelExercises = modelExerciseDTOS.stream().filter(item -> !existsIds.contains(item.getExerciseId())).collect(Collectors.toList());
        }

        // BusinessResponseEnum.UNMODELEXERCISES.assertIsEmpty(modelExercises, allIds);
        List<TModelExercise> list = new ArrayList<>();
        TModelExercise modelExercise;
        //??????????????????
        for (ModelExerciseDTO dto : addModelExercises) {
            modelExercise = new TModelExercise()
                    .setExerciseId(dto.getExerciseId())
                    .setExerciseType(dto.getExerciseType())
                    .setModelId(tHomeworkModel.getId());
            list.add(modelExercise);
        }
        return list;
    }

    /**
     * @description: ??????????????????id??????????????????
     * @author:
     * @date: 2022/9/9 11:07
     * @param: [request, id ????????????id]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    @Override
    public SaveHomeWorkModel getExercisesByModelId(HttpServletRequest request, Integer id, int flag) {
        //??????????????????
        THomeworkModel homeworkModel = homeworkModelService.getById(id);
        //??????????????????
        BusinessResponseEnum.NOHOMEWORKMODEL.assertNotNull(homeworkModel, id);
        SaveHomeWorkModel saveHomeWorkModelVO = new SaveHomeWorkModel();
        BeanUtils.copyProperties(homeworkModel, saveHomeWorkModelVO);
        //?????????????????????????????????????????????
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("model_id", id)
                .eq("delete_flag", 0));
        if ((modelExercises == null) || modelExercises.isEmpty()) {
            return saveHomeWorkModelVO;
        }
        //sum????????????
        double sumScore = modelExercises.stream()
                .filter(a -> a.getExerciseScore() != null)
                .mapToDouble(TModelExercise::getExerciseScore)
                .sum();
        saveHomeWorkModelVO.setTgp(sumScore);
        //???????????????id??????
        List<Integer> ids = modelExercises.stream().map(TModelExercise::getExerciseId).collect(Collectors.toList());
        //????????????id????????????????????????
        List<TNewExercise> exercises = exerciseService.getExercisesByIds(ids, id, flag);
        //????????????
        saveHomeWorkModelVO.setExercises(exercises);
        //????????????
        saveHomeWorkModelVO.setTotalExercise(exercises.size());

        //?????????????????????
        List<ModelExerciseModel> classifyList = new ArrayList<>();
        //???????????????????????????????????????????????????
        if (homeworkModel.getClassify() == 1) {

            //????????????????????????????????????????????????
            List<TModelExerciseType> typeList = modelExerciseTypeService.list(new QueryWrapper<TModelExerciseType>()
                    .eq("model_id", id));
            List<Integer> types = typeList.stream().map(TModelExerciseType::getExerciseType).collect(Collectors.toList());
            //???????????????
            List<TExerciseType> exerciseTypes = exerciseTypeService.list().stream().filter(item -> types.contains(item.getTypeCode()))
                    .collect(Collectors.toList());
            exerciseTypes.forEach(item -> {
                //??????????????????
                ModelExerciseModel modelExerciseModel = new ModelExerciseModel();
                List<TNewExercise> collect = exercises.stream()
                        .filter(exercise -> exercise.getExerciseType().equals(item.getTypeCode()))
                        .sorted(Comparator.comparing(TNewExercise::getExerciseOrder))
                        .collect(Collectors.toList());
                modelExerciseModel.setCollect(collect);
                modelExerciseModel.setTypeCode(item.getTypeCode());
                modelExerciseModel.setTypeName(item.getTypeName());
                modelExerciseModel.setExerciseCount(collect.size());
                //????????????????????????
                modelExerciseModel.setScore(collect.stream()
                        .filter(a -> a.getExerciseScore() != null)
                        .mapToDouble(TNewExercise::getExerciseScore).sum());
                List<TModelExerciseType> modelExerciseType = typeList.stream()
                        .filter(m -> m.getExerciseType().equals(item.getTypeCode()))
                        .collect(Collectors.toList());
                modelExerciseModel.setSortNum(modelExerciseType.get(0).getSortNum());
                classifyList.add(modelExerciseModel);
            });
        }
        //???????????????????????????????????????????????????
        saveHomeWorkModelVO.setClassifyExercises(classifyList.stream()
                .sorted(Comparator.comparing(ModelExerciseModel::getSortNum))
                .collect(Collectors.toList()));
        return saveHomeWorkModelVO;
    }


    /**
     * @description: ??????????????????????????????
     * @author:
     * @date: 2022/10/25 17:06
     * @param: [modelExercises, tHomeworkModel, loginUser]
     * @return: void
     **/
    private void saveModelExerciseType(List<TModelExercise> modelExercises, THomeworkModel tHomeworkModel, UserInfo loginUser) {
        List<TModelExerciseType> types = modelExerciseTypeService.list(new QueryWrapper<TModelExerciseType>()
                .eq("model_id", tHomeworkModel.getId()));
        List<Integer> exerciseTypes = new ArrayList<>();
        if (types != null && !types.isEmpty()) {
            exerciseTypes = types.stream().map(TModelExerciseType::getExerciseType).collect(Collectors.toList());
        }
        //??????????????????????????????
        List<Integer> finalExerciseTypes = exerciseTypes;
        //?????????????????????
        List<Integer> currentTypes = modelExercises.stream().map(TModelExercise::getExerciseType).distinct()
                .filter(a -> !finalExerciseTypes.contains(a))
                .collect(Collectors.toList());
        List<TModelExerciseType> saveTypes = new ArrayList<>();
        //???????????????
        Optional.of(currentTypes).ifPresent(ts -> {
            ts.forEach(item -> {
                TModelExerciseType exerciseType = new TModelExerciseType();
                exerciseType.setModelId(tHomeworkModel.getId())
                        .setExerciseType(item)
                        .setUpdateTime(new Date())
                        .setUpdateUser(loginUser.getUserId());
                saveTypes.add(exerciseType);
            });
            //??????
            modelExerciseTypeService.saveBatch(saveTypes);
            //?????????????????????id??????
            modelExerciseTypeService.saveOrUpdateBatch(saveTypes.stream().map(item -> item.setSortNum(item.getId()))
                    .collect(Collectors.toList()));
        });
    }

    /**
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/9/9 10:26
     * @param: [modelExerciseList]
     * @return: void
     **/
    private void saveCopyModelExercise(List<TModelExercise> modelExerciseList) {
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(modelExerciseService.saveBatch(modelExerciseList));
    }

    /**
     * @description: ??????????????????????????????
     * @author:
     * @date: 2022/9/9 10:23
     * @param: [homeWork]
     * @return: void
     **/
    private void saveCopyHomeWorkModel(THomeworkModel homeWork) {
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(homeworkModelService.save(homeWork));
    }

    /**
     * @description: ??????????????????????????????
     * @author:
     * @date: 2022/9/8 15:44
     * @param: [modelExercises, tHomeworkModel]
     * @return: void
     **/
    private void saveModelExercises(List<TModelExercise> modelExercises, THomeworkModel tHomeworkModel, UserInfo loginUser) {
        List<TModelExercise> collect = modelExercises.stream().peek(item -> {
            if (item.getId() == null) {
                //????????????id??????????????????????????????
                item.setModelId(tHomeworkModel.getId())
                        .setExerciseStyle(getExerciseStyle(item))
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId());
            } else {
                //????????????id??????????????????????????????
                item.setModelId(tHomeworkModel.getId())
                        .setExerciseStyle(getExerciseStyle(item))
                        .setUpdateTime(new Date())
                        .setUpdateUser(loginUser.getUserId());
            }
        }).collect(Collectors.toList());
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(modelExerciseService.saveOrUpdateBatch(collect));
        List<TModelExercise> modelExerciseList = collect.stream()
                .map(item -> item.setExerciseOrder(item.getId()))
                .collect(Collectors.toList());
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(modelExerciseService.saveOrUpdateBatch(modelExerciseList));
    }

    /**
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/9/8 16:36
     * @param: [exercise]
     * @return: int
     **/
    private int getExerciseStyle(TModelExercise exercise) {
        TExerciseType exerciseType = exerciseTypeService.getOne(new QueryWrapper<TExerciseType>()
                .eq("type_code", exercise.getExerciseType()));
        BusinessResponseEnum.NOTFOUNDEXERCISETYPE.assertNotNull(exerciseType, exercise.getExerciseType());
        return exerciseType.getTypeStyle();
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/9/8 15:44
     * @param: [param, tHomeworkModel, loginUser]
     * @return: void
     **/
    private void saveModel(SaveHomeWorkModel param, THomeworkModel tHomeworkModel, UserInfo loginUser) {

        //????????????/??????
        if (param.getId() != null) {
            //??????
            tHomeworkModel.setUpdateTime(new Date()).setUpdateUser(loginUser.getUserId());
        } else {
            //??????
            tHomeworkModel.setCreateUser(loginUser.getUserId()).setCreateTime(new Date());
        }
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(homeworkModelService.saveOrUpdate(tHomeworkModel));
    }


    /**
     * @description: ??????????????????????????????
     * @author:
     * @date: 2022/9/21 9:40
     * @param: [homeWork, distributions, loginUser]
     * @return: void
     **/
    private void saveStuHomeWork(THomework homeWork, List<THomeworkDistribution> distributions, UserInfo loginUser) {
        //?????????????????????
        List<TStuHomework> tStuHomeworks = new ArrayList<>();
        //???????????????????????????
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        //??????????????????????????????
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("model_id", homeWork.getModelId())
                .eq("delete_flag", 0));
        //???????????????
        for (THomeworkDistribution item : distributions) {
            //?????????????????????
            if (item.getTargetType() == 2) {
                //????????????????????????
                publishTOStudent(tStuHomeworks, tStuHomeworkInfos, modelExercises, item, loginUser, homeWork);

            } else {
                //?????????????????????
                publicTOClass(tStuHomeworks, tStuHomeworkInfos, modelExercises, item, loginUser, homeWork);

            }
        }
        if (!tStuHomeworks.isEmpty()) {
            BusinessResponseEnum.FAILPUBLISH.assertIsTrue(stuHomeworkService.saveBatch(tStuHomeworks));
        }

        if (!tStuHomeworkInfos.isEmpty()) {
            BusinessResponseEnum.FAILPUBLISH.assertIsTrue(stuHomeworkInfoService.saveBatch(tStuHomeworkInfos));
        }
    }

    /**
     * @description: ???????????????
     * @author:
     * @date: 2022/9/28 15:28
     * @param: [tStuHomeworks, tStuHomeworkInfos, modelExercises, item, loginUser, homeWork]
     * @return: void
     **/
    private void publicTOClass(List<TStuHomework> tStuHomeworks, List<TStuHomeworkInfo> tStuHomeworkInfos, List<TModelExercise> modelExercises, THomeworkDistribution item, UserInfo loginUser, THomework homeWork) {
        //????????????
        TStuHomeworkInfo tStuHomeworkInfo;
        //??????????????????
        TStuHomework tStuHomework;
        //????????????id?????????????????????
        List<UserInfo> studentList = sclassService.getStudentByClassId(item.getClassId());
        for (UserInfo stu : studentList) {
            //?????????????????????
            tStuHomework = new TStuHomework();
            tStuHomework.setHomeworkId(homeWork.getId())
                    .setStudentCode(stu.getCode())
                    .setCourseId(homeWork.getCourseId())
                    .setClassId(item.getClassId())
                    .setClassName(item.getClassName())
                    .setStudentId(stu.getUserId())
                    .setStudentName(stu.getUserName())
                    .setHomeworkStatus(2)
                    .setCheckStatus(2)
                    .setCreateTime(new Date())
                    .setCreateUser(loginUser.getUserId());
            tStuHomeworks.add(tStuHomework);
            //???????????????????????????
            for (TModelExercise modelExercise : modelExercises) {
                tStuHomeworkInfo = new TStuHomeworkInfo();
                tStuHomeworkInfo.setExerciseId(modelExercise.getExerciseId())
                        .setHomeworkId(homeWork.getId())
                        .setHomeworkName(homeWork.getHomeworkName())
                        .setModelId(homeWork.getModelId())
                        .setModelName(homeWork.getModelName())
                        .setStudentId(stu.getUserId())
                        .setStudentName(stu.getUserName())
                        .setStudentCode(stu.getCode())
                        .setCourseId(homeWork.getCourseId())
                        .setClassId(item.getClassId())
                        .setClassName(item.getClassName())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId());
                tStuHomeworkInfos.add(tStuHomeworkInfo);
            }
        }
    }

    /**
     * @description: ???????????????
     * @author:
     * @date: 2022/9/28 15:27
     * @param: [tStuHomeworks, tStuHomeworkInfos, modelExercises, item, loginUser, homeWork]
     * @return: void
     **/
    private void publishTOStudent(List<TStuHomework> tStuHomeworks, List<TStuHomeworkInfo> tStuHomeworkInfos, List<TModelExercise> modelExercises, THomeworkDistribution item, UserInfo loginUser, THomework homeWork) {
        //????????????
        TStuHomeworkInfo tStuHomeworkInfo;
        //??????????????????
        TStuHomework tStuHomework;
        List<THomeworkDistributionStudent> distributionStydents = item.getDistributionStydents();
        for (THomeworkDistributionStudent distributionStydent : distributionStydents) {
            UserInfo user = userInfoService.getById(distributionStydent.getStudentId());
            //?????????????????????
            tStuHomework = new TStuHomework();
            tStuHomework.setHomeworkId(homeWork.getId())
                    .setStudentCode(user.getCode())
                    .setCourseId(homeWork.getCourseId())
                    .setClassId(item.getClassId())
                    .setClassName(item.getClassName())
                    .setStudentId(distributionStydent.getStudentId())
                    .setStudentName(user.getUserName())
                    .setHomeworkStatus(2).setCheckStatus(2)
                    .setCreateTime(new Date())
                    .setCreateUser(loginUser.getUserId());
            tStuHomeworks.add(tStuHomework);
            //???????????????????????????
            for (TModelExercise modelExercise : modelExercises) {
                tStuHomeworkInfo = new TStuHomeworkInfo();
                tStuHomeworkInfo.setExerciseId(modelExercise.getExerciseId())
                        .setHomeworkId(homeWork.getId())
                        .setHomeworkName(homeWork.getHomeworkName())
                        .setModelId(homeWork.getModelId())
                        .setModelName(homeWork.getModelName())
                        .setStudentId(distributionStydent.getStudentId())
                        .setStudentName(user.getUserName())
                        .setStudentCode(user.getCode())
                        .setCourseId(homeWork.getCourseId())
                        .setClassId(item.getClassId())
                        .setClassName(item.getClassName())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId());
                tStuHomeworkInfos.add(tStuHomeworkInfo);
            }
        }
    }

    /**
     * @description: ???????????????
     * @author:
     * @date: 2022/9/8 10:17
     * @param: [homeWork, loginUser]
     * @return: void
     **/
    private void saveHomeWork(THomework homeWork, UserInfo loginUser) {
        //??????????????????????????????
        homeWork.setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
        // ???????????????
        BusinessResponseEnum.FAILPUBLISH.assertIsTrue(homeworkService.save(homeWork));
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/9/8 10:17
     * @param: [distributions, loginUser]
     * @return: void
     **/
    private void saveDistribution(THomework homeWork, List<THomeworkDistribution> distributions, UserInfo loginUser) {
        //??????????????????????????????
        List<THomeworkDistribution> collect = distributions.stream()
                .peek(item -> item.setFlag(1)
                        .setHomeworkId(homeWork.getId())
                        .setCourseId(homeWork.getCourseId())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId())).collect(Collectors.toList());
        // ?????????????????????
        BusinessResponseEnum.FAILPUBLISH.assertIsTrue(homeworkDistributionService.saveBatch(collect));
        //?????????????????????
        List<THomeworkDistributionStudent> distributionStudents = new ArrayList<>();
        //?????????????????????????????????
        List<THomeworkDistribution> studentCollect = collect.stream().filter(item -> item.getTargetType() == 2)
                .collect(Collectors.toList());
        studentCollect.forEach(item -> distributionStudents.addAll(item.getDistributionStydents().stream()
                .peek(distributionStudent -> distributionStudent
                        .setDistributionId(item.getId())
                        .setHomeworkId(item.getHomeworkId())
                        .setCreateUser(loginUser.getUserId())
                        .setCreateTime(new Date()))
                .collect(Collectors.toList())));
        if (!distributionStudents.isEmpty()) {
            //???????????????????????????
            BusinessResponseEnum.FAILPUBLISH.assertIsTrue(homeworkDistributionStudentService.saveOrUpdateBatch(distributionStudents));
        }

    }

    /**
     * @description: ????????????????????????????????????
     * @author:
     * @date: 2022/9/6 9:51
     * @param: [param]
     * @return: void
     **/
    private List<THomeworkModel> list(ListHomeWorkModel param) {
        //????????????????????????????????????????????????
        return homeworkModelMapper.listHomeworkModel(param);
    }
}




