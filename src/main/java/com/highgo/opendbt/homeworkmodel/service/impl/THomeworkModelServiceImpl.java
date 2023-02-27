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
 * 作业模板服务类
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
     * @description: 作业模板列表
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
        //分页查询
        PageInfo<THomeworkModel> objectPageInfo = PageMethod.startPage(param.getPageNum(), param.getPageSize())
                .setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> list(param.getParam()));
        //查询该课程下未删除的作业数量
        int count = homeworkModelService.count(new QueryWrapper<THomeworkModel>()
                .eq("course_id", param.getParam().getCourseId())
                .eq("element_type", 0)
                .eq("delete_flag", 0)
                .like(StringUtils.isNotEmpty(param.getParam().getModelName()), "model_name", param.getParam().getModelName()));
        //整合返回数据
        Map<String, Object> res = new HashMap<>();
        res.put("count", count);
        res.put("objectPageInfo", objectPageInfo);
        return res;
    }

    /**
     * @description: 保存文件夹
     * @author:
     * @date: 2022/9/6 11:07
     * @param: [request, param, result]
     * @return: boolean
     **/
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean saveHomeWorkModelFolder(HttpServletRequest request, SaveHomeWorkModelFolder param) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //转换为homeWorkModel便于保存
        THomeworkModel homeWorkModel = new THomeworkModel();
        BeanUtils.copyProperties(param, homeWorkModel);
        //更新设置更新时间人员信息
        if (param.getId() != null && param.getId() != -1) {
            homeWorkModel.setUpdateTime(new Date()).setUpdateUser(loginUser.getUserId());
        } else {
            //保存设置创建时间人员信息
            homeWorkModel.setAuthType(2).setElementType(1).setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
        }
        boolean res = homeworkModelService.saveOrUpdate(homeWorkModel);
        //判断是否保存成功,不成功抛出异常
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 删除
     * @author:
     * @date: 2022/9/6 13:42
     * @param: [request, id]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean delHomeWorkModel(HttpServletRequest request, int id) {
        //需要删除的集合
        List<THomeworkModel> homeworkModelList = new ArrayList<>();
        List<THomeworkModel> homeworkList = new ArrayList<>();
        //验证
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //查询
        THomeworkModel homeWork = homeworkModelService.getById(id);
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(homeWork, id);
        homeworkList.add(homeWork);
        //遍历查询出child
        traverseModel(homeworkList, homeworkModelList);
        //需要删除的作业模板id
        List<Integer> ids = homeworkModelList.stream().map(THomeworkModel::getId).collect(Collectors.toList());
        //判断作业模板是否绑定了作业，绑定作业的模板不允许删除
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
        //删除更新
        boolean b = homeworkModelService.removeByIds(ids);
        logger.info("用户{}{}删除了{}", loginUser.getUserName(), loginUser.getUserId(), homeworkModelList.toString());
        //查询模板关联表
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>().in("model_id", ids));
        //删除模板关联表
        if ((modelExercises != null) && !modelExercises.isEmpty()) {
            modelExerciseService.removeByIds(modelExercises.stream().map(TModelExercise::getId).collect(Collectors.toList()));
        }
        return b;
    }

    /**
     * @description: 遍历查询出child
     * @author:
     * @date: 2022/12/2 10:58
     * @param: [homeworkList 需要遍历的作业, homeworkModelList遍历存储的作业]
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
     * @description: 发布
     * @author:
     * @date: 2022/9/7 19:15
     * @param: [request, param, result]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean publishHomeWork(HttpServletRequest request, PublishHomeWork param) {
        //验证
        UserInfo loginUser = Authentication.getCurrentUser(request);
        THomework homeWork = new THomework();
        BeanUtils.copyProperties(param, homeWork);
        //设置作业名称
        homeWork.setHomeworkName(homeWork.getModelName());
        //作业发放表集合
        List<THomeworkDistribution> distributions = param.getDistributions();
        //保存作业表
        saveHomeWork(homeWork, loginUser);
        //保存作业发放表，作业发放学生表
        saveDistribution(homeWork, distributions, loginUser);
        //保存学生作业表
        saveStuHomeWork(homeWork, distributions, loginUser);
        return true;
    }

    /**
     * @description: 初始化作业相关表
     * @author:
     * @date: 2022/11/10 10:36
     * @param: [request, class_id 班级id, user_id 人员id]
     * @return: void
     **/
    @Override
    public void InitHomeWorkTables(HttpServletRequest request, Integer classId, Integer userId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //1.查询target_type为1：班级为class_id的作业发放表
        List<THomeworkDistribution> homeworkDistributions = homeworkDistributionService.list(new QueryWrapper<THomeworkDistribution>()
                .eq("class_id", classId)
                .eq("target_type", 1)
                .eq("delete_flag", 0));
        if ((homeworkDistributions == null) || homeworkDistributions.isEmpty()) {
            return;
        }
        UserInfo user = userInfoService.getById(userId);
        //学生作业表集合
        List<TStuHomework> stuHomeworkList = new ArrayList<>();
        //学生作业明细表集合
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        homeworkDistributions.forEach(item -> {
            //根据作业id查询作业模板信息
            THomework homework = homeworkService.getById(item.getHomeworkId());
            //查询模板下的所有习题
            List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                    .eq("model_id", homework.getModelId())
                    .eq("delete_flag", 0));
            //学生作业表部分
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
            //学生作业明细表部分
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
        //插入学生作业表
        stuHomeworkMapper.insertBatch(stuHomeworkList);
        //插入学生作业明细表
        stuHomeworkInfoMapper.insertBatch(tStuHomeworkInfos);
    }

    @Override
    public void delInitHomeWorkTables(HttpServletRequest request, Integer classId, List<Integer> userId) {
        //发放到班级的
        List<THomeworkDistribution> homeworkDistributions = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("class_id", classId)
                        .eq("target_type", 1)
                        .eq("delete_flag", 0));
        if ((homeworkDistributions == null) || homeworkDistributions.isEmpty()) {
            homeworkDistributions = new ArrayList<>();
        }
        //发放到学生的
        List<THomeworkDistribution> homeworkDistributionsStu = homeworkDistributionService
                .list(new QueryWrapper<THomeworkDistribution>()
                        .eq("class_id", classId)
                        .eq("target_type", 2)
                        .eq("delete_flag", 0));
        if (homeworkDistributionsStu != null && !homeworkDistributionsStu.isEmpty()) {
            //更新发放班级表，和发放学生表
            updateDistributions(homeworkDistributionsStu, userId);
            homeworkDistributions.addAll(homeworkDistributionsStu);
        }
        if (homeworkDistributions.isEmpty()) {
            return;
        }

        //学生作业表集合
        List<TStuHomework> tStuHomeworkList = new ArrayList<>();
        //学生作业明细表集合
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        homeworkDistributions.forEach(item -> {
            // 查询学生作业表
            List<TStuHomework> stuHomeworks = stuHomeworkService.list(new QueryWrapper<TStuHomework>()
                    .eq("homework_id", item.getHomeworkId())
                    .in("student_id", userId));
            tStuHomeworkList.addAll(stuHomeworks);
            // 查询学生作业明细表
            List<TStuHomeworkInfo> stuHomeworkInfos = stuHomeworkInfoService.list(new QueryWrapper<TStuHomeworkInfo>()
                    .eq("homework_id", item.getHomeworkId())
                    .in("student_id", userId));
            tStuHomeworkInfos.addAll(stuHomeworkInfos);
        });
        //移除学生作业表
        if (!tStuHomeworkList.isEmpty()) {
            stuHomeworkService.removeByIds(tStuHomeworkList.stream().map(TStuHomework::getId).collect(Collectors.toList()));
        }
        //移除学生作业明细表
        if (!tStuHomeworkInfos.isEmpty()) {
            stuHomeworkInfoService.removeByIds(tStuHomeworkInfos.stream().map(TStuHomeworkInfo::getId).collect(Collectors.toList()));
        }
    }

    //更新发放班级表，和发放学生表
    private void updateDistributions(List<THomeworkDistribution> homeworkDistributionsStu, List<Integer> userId) {
        //要删除的THomeworkDistribution集合
        List<THomeworkDistribution> homeworkDistributionList = new ArrayList<>();
        List<THomeworkDistributionStudent> homeworkDistributionStudentList = new ArrayList<>();
        homeworkDistributionsStu.forEach(item -> {
            //查询发放学生表
            List<THomeworkDistributionStudent> homeworkDistributionStudents = homeworkDistributionStudentService
                    .list(new QueryWrapper<THomeworkDistributionStudent>()
                            .eq("homework_id", item.getHomeworkId())
                            .in("student_id", userId));
            if (homeworkDistributionStudents != null && !homeworkDistributionStudents.isEmpty()) {//班级发放表不为空
                homeworkDistributionStudentList.addAll(homeworkDistributionStudents);
                //该作业发放到的学生
                List<THomeworkDistributionStudent> students = homeworkDistributionStudentService
                        .list(new QueryWrapper<THomeworkDistributionStudent>()
                                .eq("homework_id", item.getHomeworkId())
                                .eq("delete_flag", 0));
                if (students.size() == 1) {
                    //只发放了要删除的一个学生，删除THomeworkDistribution
                    homeworkDistributionList.add(item);
                }
            }
        });
        //删除作业发放班级表
        if (!homeworkDistributionList.isEmpty()) {
            homeworkDistributionService.removeByIds(
                    homeworkDistributionList.stream()
                            .map(THomeworkDistribution::getId)
                            .collect(Collectors.toList()));
        }
        //删除作业发放班级表
        if (!homeworkDistributionStudentList.isEmpty()) {
            homeworkDistributionStudentService.removeByIds(
                    homeworkDistributionStudentList.stream()
                            .map(THomeworkDistributionStudent::getId)
                            .collect(Collectors.toList()));
        }
    }

    /**
     * @description: 复制作业模板
     * @author:
     * @date: 2022/9/8 17:41
     * @param: [request, id 作业模板id]
     * @return: com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public THomeworkModel copyHomeWorkModel(HttpServletRequest request, int id) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 查询作业模板
        THomeworkModel homeWork = homeworkModelService.getById(id);
        // 查询作业模板关联表
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("model_id", homeWork.getId())
                .eq("delete_flag", 0));
        // 复制作业模板
        homeWork.setModelName(homeWork.getModelName() + "_副本");
        homeWork.setId(null).setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
        // 保存复制的作业模板表
        saveCopyHomeWorkModel(homeWork);
        // 复制作业模板关联表
        List<TModelExercise> collect = modelExercises.stream()
                .peek(item -> item.setId(null)
                        .setModelId(homeWork.getId())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId()))
                .collect(Collectors.toList());
        // 保存作业模板关联表
        saveCopyModelExercise(modelExercises);
        // 添加关联表到作业模板表
        homeWork.setModelExercises(collect);
        //复制模板题型关联表
        saveCopyModelExerciseType(id, homeWork, loginUser.getUserId());
        return homeWork;
    }

    /**
     * @description: 复制模板题型关联表
     * @author:
     * @date: 2022/12/2 13:57
     * @param: [id 复制前的模板id, homeWork 复制后的模板信息]
     * @return: void
     **/
    private void saveCopyModelExerciseType(int id, THomeworkModel homeWork, int userId) {
        //查询复制前的习题类型
        List<TModelExerciseType> exerciseTypes = modelExerciseTypeService.list(new QueryWrapper<TModelExerciseType>()
                .eq("model_id", id));
        if ((exerciseTypes == null) || exerciseTypes.isEmpty()) {
            return;
        }
        //设置新的习题类型
        List<TModelExerciseType> modelExerciseTypes = exerciseTypes.stream()
                .peek(item -> item.setModelId(homeWork.getId())
                        .setId(null)
                        .setUpdateTime(new Date())
                        .setUpdateUser(userId))
                .collect(Collectors.toList());
        //批量插入
        modelExerciseTypeMapper.insertBatch(modelExerciseTypes);
    }

    /**
     * @description: 根据习题id判断是否绑定了作业模板
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
     * @description: 根据习题id查询习题详情
     * @author:
     * @date: 2022/11/8 9:25
     * @param: [request, id 习题id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    public TNewExercise getExerciseInfo(HttpServletRequest request, int exerciseId, int modelId) {
        TNewExercise exerciseInfo = exerciseService.getExerciseInfo(request, exerciseId);
        //习题为空抛异常
        BusinessResponseEnum.UNEXERCISEiNFOBYMODEL.assertNotNull(exerciseInfo, exerciseId, modelId);
        //查询模板习题信息
        TModelExercise modelExercise = modelExerciseService.getOne(new QueryWrapper<TModelExercise>()
                .eq("model_id", modelId)
                .eq("exercise_id", exerciseId)
                .eq("delete_flag", 0));
        BusinessResponseEnum.UNMODELEXERCISE.assertNotNull(modelExercise, modelId, exerciseId);
        //设置习题分数
        exerciseInfo.setExerciseScore(modelExercise.getExerciseScore());
        // 查询习题是否存绑定在作业中
        List<Integer> bandingModel = homeworkModelService.getIsBandingModel(exerciseId);
        boolean isBandingModel = ((bandingModel != null)
                && !bandingModel.isEmpty()
                && (bandingModel.size() != 1)
                || (bandingModel != null)
                && !bandingModel.isEmpty()
                && (bandingModel.get(0) != modelId));
        //设置是否绑定过作业模板
        exerciseInfo.setBandingModel(isBandingModel);
        return exerciseInfo;
    }

    /**
     * @description: 作业选题习题列表
     * @author:
     * @date: 2022/11/8 15:00
     * @param: [request, model_id 模板id]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    @Override
    public Map<String, Object> getHomeWorkModelExercises(HttpServletRequest request, PageParam<TNewExerciseDTO> param) {
        Map<String, Object> exercise = exerciseService.getExercise(request, param);
        Integer modelId = param.getParam().getModelId();
        //选题时未保存模板
        if (modelId == null) {
            return exercise;
        }
        //选题时已经保存模板
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("delete_flag", 0)
                .eq("model_id", modelId)
                .select("exercise_id"));
        if ((modelExercises == null) || modelExercises.isEmpty()) {
            //模板内没有习题
            return exercise;
        }
        //筛选出习题id集合
        List<Integer> ids = modelExercises.stream().map(TModelExercise::getExerciseId).collect(Collectors.toList());
        //查询出来的习题
        PageInfo<TNewExercise> pageList = (PageInfo<TNewExercise>) exercise.get("pageList");
        List<TNewExercise> list = pageList.getList();
        List<ModelExerciseDTO> modelExerciseDTOS = new ArrayList<>();

        //遍历习题 判断是否已经选中，选中的设置属性 checked 0:未选中 1：已选中
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
     * @description: 完成选题
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
        if (param.getModelId() == null) { // 模板id为空
            //作业模板
            tHomeworkModel = new THomeworkModel();
            BeanUtils.copyProperties(param, tHomeworkModel);
            tHomeworkModel.setElementType(0);
            tHomeworkModel.setGrandingStandard(2);
            tHomeworkModel.setAuthType(2);
            tHomeworkModel.setDeleteFlag(0);
        } else {
            //查询模板
            tHomeworkModel = this.getById(param.getModelId());
            tHomeworkModel.setModelName(param.getModelName());
            tHomeworkModel.setClassify(param.getClassify());
        }
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(tHomeworkModel, param.getModelId());

        //作业习题
        List<ModelExerciseDTO> modelExerciseDTOS = param.getModelExerciseDTOS();
        if ((modelExerciseDTOS != null) && !modelExerciseDTOS.isEmpty()) {
            //设置习题总分数
            tHomeworkModel.setTgp((tHomeworkModel.getTgp() == null ? 0 : tHomeworkModel.getTgp()) + (modelExerciseDTOS.size() * 5));
        }
        //保存更新作业模板
        homeworkModelService.saveOrUpdate((THomeworkModel) tHomeworkModel.setCreateTime(new Date()).setCreateUser(loginUser.getUserId()));
        if ((modelExerciseDTOS != null) && !modelExerciseDTOS.isEmpty()) {
            //获取modelExercises
            List<TModelExercise> modelExercises = getModelExercises(param, tHomeworkModel);
            //保存更新作业模板习题
            saveModelExercises(modelExercises, tHomeworkModel, loginUser);
            //更新模板下的习题类型
            saveModelExerciseType(modelExercises, tHomeworkModel, loginUser);
        }
        return tHomeworkModel;
    }

    /**
     * @description: 删除选中的习题
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
        //更新作业模板中习题总分
        homeworkModelService.updateById(model);
        //删除作业模板习题
        modelExerciseService.removeById(modelExercise.getId());
        //更新作业模板下的习题类型
        updateExerciseType(modelId, modelExercise.getExerciseType());
        return true;
    }

    /**
     * @description: 模板中习题保存
     * @author:
     * @date: 2022/11/21 13:47
     * @param: [request, param]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TNewExercise saveExercise(HttpServletRequest request, EditNewExerciseDTO param) {
        //转换
        TNewExerciseVo tNewExerciseVo = new TNewExerciseVo();
        BeanUtils.copyProperties(param, tNewExerciseVo);
        //更新习题
        TNewExercise exercise = updateExercise(request, tNewExerciseVo);
        //更新模板中习题分数
        TModelExercise modelExercise = modelExerciseService.getOne(new QueryWrapper<TModelExercise>()
                .eq("model_id", param.getModelId())
                .eq("exercise_id", param.getId())
                .eq("delete_flag", 0));
        modelExercise.setExerciseScore(param.getExerciseScore());
        modelExerciseService.saveOrUpdate(modelExercise);
        return exercise;
    }

    /**
     * @description: 模板中习题分数修改
     * @author:
     * @date: 2022/11/21 14:55
     * @param: [request, exerciseId 习题id, model_id 模板id, exercise_score 习题分数]
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
     * @description: 作业库目录查询
     * @author:
     * @date: 2022/11/23 13:31
     * @param: [request, course_id 课程id]
     * @return: java.util.List<com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel>
     **/
    @Override
    public List<THomeworkModel> getHomeWorkModelCatalogueTree(HttpServletRequest request, Integer courseId) {
        List<THomeworkModel> homeworkModelList = homeworkModelMapper.getHomeWorkModelCatalogueTree(courseId);
        //添加根节点
        THomeworkModel homeworkModel = new THomeworkModel().setId(0).setModelName("根节点").setChildrens(homeworkModelList);
        List<THomeworkModel> tHomeworkModelList = new ArrayList<>();
        tHomeworkModelList.add(homeworkModel);
        return tHomeworkModelList;
    }

    /**
     * @description: 作业库移动
     * @author:
     * @date: 2022/11/23 13:31
     * @param: [request, oid, tid]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveHomeWorkModel(HttpServletRequest request, int oid, int tid) {
        if (tid != 0) {//排除根目录
            //目标文件夹
            THomeworkModel thomeworkModel = homeworkModelService.getOne(new QueryWrapper<THomeworkModel>()
                    .eq("id", tid)
                    .eq("element_type", 1));
            //目标文件夹不能为空
            BusinessResponseEnum.UNHOMEWORKMODELPACKAGE.assertNotNull(thomeworkModel, tid);
        }
        //原始作业模板
        THomeworkModel oHomeWorkModel = homeworkModelService.getById(oid);
        //原始作业模板不能为空
        BusinessResponseEnum.UNHOMEWORKMODEL.assertNotNull(oHomeWorkModel, oid);
        //相同目录无法移动
        BusinessResponseEnum.EQUALSHOMEWORKMODELPACKAGE.assertIsFalse(oHomeWorkModel.getParentId() == tid);
        oHomeWorkModel.setParentId(tid);
        boolean res = homeworkModelService.saveOrUpdate(oHomeWorkModel);
        BusinessResponseEnum.FAILMOVE.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 发布班级-学生信息
     * @author:
     * @date: 2022/11/29 11:08
     * @param: [request, course_id]
     * @return: java.util.List<com.highgo.opendbt.homeworkmodel.domain.model.param.PublishClass>
     **/
    @Override
    public List<PublishClass> publishList(HttpServletRequest request, int courseId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        int userId = loginUser.getUserId();
        // 查询当前课程创建人
        Course course = courseService.getById(courseId);
        // 查询助教团队人员
        List<TCourseUser> courseUsers = courseUserService.list(new QueryWrapper<TCourseUser>()
                .eq("course_id", courseId));
        // 判断当前登录人是否在助教团队内
        if (courseUsers != null && !courseUsers.isEmpty()) {
            List<TCourseUser> collect = courseUsers.stream().filter(item -> item.getUserId().equals(loginUser.getUserId()))
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                userId = course.getCreator();
            }
        }
        return homeworkModelMapper.getPublishList(courseId, userId);
    }

    //更新习题
    private TNewExercise updateExercise(HttpServletRequest request, TNewExerciseVo param) {
        return exerciseService.saveExercise(request, param);
    }


    /**
     * @description: 新增习题
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
     * @description: 更新作业模板下的习题类型
     * @author:
     * @date: 2022/11/9 15:06
     * @param: [modelId 模板id, exerciseType 习题类型]
     * @return: void
     **/
    private void updateExerciseType(int modelId, Integer exerciseType) {
        boolean isDelete = false;
        //查询模板下的所有习题
        List<TModelExercise> list = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("modelId", modelId)
                .eq("delete_flag", 0));
        if (list == null || list.isEmpty()) {
            // 减少习题类型
            isDelete = true;
        } else {
            //筛选相同题型的模板习题
            List<TModelExercise> collect = list.stream().filter(item -> item.getExerciseType().equals(exerciseType))
                    .collect(Collectors.toList());
            if (collect.isEmpty()) {
                // 减少习题类型
                isDelete = true;
            }
        }
        if (isDelete) {
            //删除模板下的习题类型
            modelExerciseTypeService.remove(new QueryWrapper<TModelExerciseType>()
                    .eq("modelId", modelId)
                    .eq("exercise_type", exerciseType));
        }

    }

    /**
     * @description: 组装需要保存的选题
     * @author:
     * @date: 2022/11/8 17:29
     * @param: [param, tHomeworkModel]
     * @return: java.util.List<com.highgo.opendbt.homeworkmodel.domain.entity.TModelExercise>
     **/
    private List<TModelExercise> getModelExercises(SelectedExercisesDTO param, THomeworkModel tHomeworkModel) {
        List<ModelExerciseDTO> addModelExercises;
        List<ModelExerciseDTO> modelExerciseDTOS = param.getModelExerciseDTOS();
        //所有选题的id
        List<Integer> allIds = modelExerciseDTOS.stream().map(ModelExerciseDTO::getExerciseId).collect(Collectors.toList());
        //查询是否有重复习题
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("delete_flag", 0)
                .eq("model_id", param.getModelId())
                .in(!allIds.isEmpty(), "exercise_id", allIds));

        if (modelExercises.isEmpty()) {//全都是新增的选题
            addModelExercises = modelExerciseDTOS;
        } else {//部分新增的选题
            //查询出重复习题的id
            List<Integer> existsIds = modelExercises.stream().map(TModelExercise::getExerciseId).collect(Collectors.toList());
            //筛选出不重复的习题id
            addModelExercises = modelExerciseDTOS.stream().filter(item -> !existsIds.contains(item.getExerciseId())).collect(Collectors.toList());
        }

        // BusinessResponseEnum.UNMODELEXERCISES.assertIsEmpty(modelExercises, allIds);
        List<TModelExercise> list = new ArrayList<>();
        TModelExercise modelExercise;
        //组装模板习题
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
     * @description: 根据作业模板id查询习题列表
     * @author:
     * @date: 2022/9/9 11:07
     * @param: [request, id 作业模板id]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    @Override
    public SaveHomeWorkModel getExercisesByModelId(HttpServletRequest request, Integer id, int flag) {
        //查询作业模板
        THomeworkModel homeworkModel = homeworkModelService.getById(id);
        //作业模板为空
        BusinessResponseEnum.NOHOMEWORKMODEL.assertNotNull(homeworkModel, id);
        SaveHomeWorkModel saveHomeWorkModelVO = new SaveHomeWorkModel();
        BeanUtils.copyProperties(homeworkModel, saveHomeWorkModelVO);
        //根据作业模板查询习题关联表集合
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("model_id", id)
                .eq("delete_flag", 0));
        if ((modelExercises == null) || modelExercises.isEmpty()) {
            return saveHomeWorkModelVO;
        }
        //sum作业分数
        double sumScore = modelExercises.stream()
                .filter(a -> a.getExerciseScore() != null)
                .mapToDouble(TModelExercise::getExerciseScore)
                .sum();
        saveHomeWorkModelVO.setTgp(sumScore);
        //筛选出习题id集合
        List<Integer> ids = modelExercises.stream().map(TModelExercise::getExerciseId).collect(Collectors.toList());
        //根据习题id集合查询习题集合
        List<TNewExercise> exercises = exerciseService.getExercisesByIds(ids, id, flag);
        //所有习题
        saveHomeWorkModelVO.setExercises(exercises);
        //习题数量
        saveHomeWorkModelVO.setTotalExercise(exercises.size());

        //题型分类后集合
        List<ModelExerciseModel> classifyList = new ArrayList<>();
        //根据作业模板是否按题型归类查询习题
        if (homeworkModel.getClassify() == 1) {

            //查询该模板按照习题类型分类的排序
            List<TModelExerciseType> typeList = modelExerciseTypeService.list(new QueryWrapper<TModelExerciseType>()
                    .eq("model_id", id));
            List<Integer> types = typeList.stream().map(TModelExerciseType::getExerciseType).collect(Collectors.toList());
            //按题型归类
            List<TExerciseType> exerciseTypes = exerciseTypeService.list().stream().filter(item -> types.contains(item.getTypeCode()))
                    .collect(Collectors.toList());
            exerciseTypes.forEach(item -> {
                //各种题型存储
                ModelExerciseModel modelExerciseModel = new ModelExerciseModel();
                List<TNewExercise> collect = exercises.stream()
                        .filter(exercise -> exercise.getExerciseType().equals(item.getTypeCode()))
                        .sorted(Comparator.comparing(TNewExercise::getExerciseOrder))
                        .collect(Collectors.toList());
                modelExerciseModel.setCollect(collect);
                modelExerciseModel.setTypeCode(item.getTypeCode());
                modelExerciseModel.setTypeName(item.getTypeName());
                modelExerciseModel.setExerciseCount(collect.size());
                //该类型下的总分数
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
        //查询该模板按照习题类型分类后的集合
        saveHomeWorkModelVO.setClassifyExercises(classifyList.stream()
                .sorted(Comparator.comparing(ModelExerciseModel::getSortNum))
                .collect(Collectors.toList()));
        return saveHomeWorkModelVO;
    }


    /**
     * @description: 更新模板下的习题类型
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
        //当前习题所包含的类型
        List<Integer> finalExerciseTypes = exerciseTypes;
        //需要新增的题型
        List<Integer> currentTypes = modelExercises.stream().map(TModelExercise::getExerciseType).distinct()
                .filter(a -> !finalExerciseTypes.contains(a))
                .collect(Collectors.toList());
        List<TModelExerciseType> saveTypes = new ArrayList<>();
        //不为空新增
        Optional.of(currentTypes).ifPresent(ts -> {
            ts.forEach(item -> {
                TModelExerciseType exerciseType = new TModelExerciseType();
                exerciseType.setModelId(tHomeworkModel.getId())
                        .setExerciseType(item)
                        .setUpdateTime(new Date())
                        .setUpdateUser(loginUser.getUserId());
                saveTypes.add(exerciseType);
            });
            //保存
            modelExerciseTypeService.saveBatch(saveTypes);
            //设置排序序号和id相同
            modelExerciseTypeService.saveOrUpdateBatch(saveTypes.stream().map(item -> item.setSortNum(item.getId()))
                    .collect(Collectors.toList()));
        });
    }

    /**
     * @description: 保存作业模板关联表
     * @author:
     * @date: 2022/9/9 10:26
     * @param: [modelExerciseList]
     * @return: void
     **/
    private void saveCopyModelExercise(List<TModelExercise> modelExerciseList) {
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(modelExerciseService.saveBatch(modelExerciseList));
    }

    /**
     * @description: 保存复制的作业模板表
     * @author:
     * @date: 2022/9/9 10:23
     * @param: [homeWork]
     * @return: void
     **/
    private void saveCopyHomeWorkModel(THomeworkModel homeWork) {
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(homeworkModelService.save(homeWork));
    }

    /**
     * @description: 保存更新作业模板习题
     * @author:
     * @date: 2022/9/8 15:44
     * @param: [modelExercises, tHomeworkModel]
     * @return: void
     **/
    private void saveModelExercises(List<TModelExercise> modelExercises, THomeworkModel tHomeworkModel, UserInfo loginUser) {
        List<TModelExercise> collect = modelExercises.stream().peek(item -> {
            if (item.getId() == null) {
                //设置模板id，创建时间，创建人员
                item.setModelId(tHomeworkModel.getId())
                        .setExerciseStyle(getExerciseStyle(item))
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId());
            } else {
                //设置模板id，更新时间，更新人员
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
     * @description: 判断试题是否客观题
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
     * @description: 保存更新作业模板
     * @author:
     * @date: 2022/9/8 15:44
     * @param: [param, tHomeworkModel, loginUser]
     * @return: void
     **/
    private void saveModel(SaveHomeWorkModel param, THomeworkModel tHomeworkModel, UserInfo loginUser) {

        //判断更新/新增
        if (param.getId() != null) {
            //更新
            tHomeworkModel.setUpdateTime(new Date()).setUpdateUser(loginUser.getUserId());
        } else {
            //新增
            tHomeworkModel.setCreateUser(loginUser.getUserId()).setCreateTime(new Date());
        }
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(homeworkModelService.saveOrUpdate(tHomeworkModel));
    }


    /**
     * @description: 保存学生作业和明细表
     * @author:
     * @date: 2022/9/21 9:40
     * @param: [homeWork, distributions, loginUser]
     * @return: void
     **/
    private void saveStuHomeWork(THomework homeWork, List<THomeworkDistribution> distributions, UserInfo loginUser) {
        //学生作业表集合
        List<TStuHomework> tStuHomeworks = new ArrayList<>();
        //学生作业明细表集合
        List<TStuHomeworkInfo> tStuHomeworkInfos = new ArrayList<>();
        //查询模板下的所有习题
        List<TModelExercise> modelExercises = modelExerciseService.list(new QueryWrapper<TModelExercise>()
                .eq("model_id", homeWork.getModelId())
                .eq("delete_flag", 0));
        //学生作业表
        for (THomeworkDistribution item : distributions) {
            //发布到学生部分
            if (item.getTargetType() == 2) {
                //发布到学生的部分
                publishTOStudent(tStuHomeworks, tStuHomeworkInfos, modelExercises, item, loginUser, homeWork);

            } else {
                //发布到班级部分
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
     * @description: 发布到班级
     * @author:
     * @date: 2022/9/28 15:28
     * @param: [tStuHomeworks, tStuHomeworkInfos, modelExercises, item, loginUser, homeWork]
     * @return: void
     **/
    private void publicTOClass(List<TStuHomework> tStuHomeworks, List<TStuHomeworkInfo> tStuHomeworkInfos, List<TModelExercise> modelExercises, THomeworkDistribution item, UserInfo loginUser, THomework homeWork) {
        //学生作业
        TStuHomeworkInfo tStuHomeworkInfo;
        //学生作业明细
        TStuHomework tStuHomework;
        //根据班级id查询班级下学生
        List<UserInfo> studentList = sclassService.getStudentByClassId(item.getClassId());
        for (UserInfo stu : studentList) {
            //学生作业表部分
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
            //学生作业明细表部分
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
     * @description: 发布到学生
     * @author:
     * @date: 2022/9/28 15:27
     * @param: [tStuHomeworks, tStuHomeworkInfos, modelExercises, item, loginUser, homeWork]
     * @return: void
     **/
    private void publishTOStudent(List<TStuHomework> tStuHomeworks, List<TStuHomeworkInfo> tStuHomeworkInfos, List<TModelExercise> modelExercises, THomeworkDistribution item, UserInfo loginUser, THomework homeWork) {
        //学生作业
        TStuHomeworkInfo tStuHomeworkInfo;
        //学生作业明细
        TStuHomework tStuHomework;
        List<THomeworkDistributionStudent> distributionStydents = item.getDistributionStydents();
        for (THomeworkDistributionStudent distributionStydent : distributionStydents) {
            UserInfo user = userInfoService.getById(distributionStydent.getStudentId());
            //学生作业表部分
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
            //学生作业明细表部分
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
     * @description: 保存作业表
     * @author:
     * @date: 2022/9/8 10:17
     * @param: [homeWork, loginUser]
     * @return: void
     **/
    private void saveHomeWork(THomework homeWork, UserInfo loginUser) {
        //设置创建时间创建人员
        homeWork.setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
        // 保存作业表
        BusinessResponseEnum.FAILPUBLISH.assertIsTrue(homeworkService.save(homeWork));
    }

    /**
     * @description: 保存作业发放表
     * @author:
     * @date: 2022/9/8 10:17
     * @param: [distributions, loginUser]
     * @return: void
     **/
    private void saveDistribution(THomework homeWork, List<THomeworkDistribution> distributions, UserInfo loginUser) {
        //设置创建时间创建人员
        List<THomeworkDistribution> collect = distributions.stream()
                .peek(item -> item.setFlag(1)
                        .setHomeworkId(homeWork.getId())
                        .setCourseId(homeWork.getCourseId())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId())).collect(Collectors.toList());
        // 保存作业发放表
        BusinessResponseEnum.FAILPUBLISH.assertIsTrue(homeworkDistributionService.saveBatch(collect));
        //作业发放学生表
        List<THomeworkDistributionStudent> distributionStudents = new ArrayList<>();
        //筛选出发放到学生的集合
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
            //保存作业发放学生表
            BusinessResponseEnum.FAILPUBLISH.assertIsTrue(homeworkDistributionStudentService.saveOrUpdateBatch(distributionStudents));
        }

    }

    /**
     * @description: 作业模板列表具体查询操作
     * @author:
     * @date: 2022/9/6 9:51
     * @param: [param]
     * @return: void
     **/
    private List<THomeworkModel> list(ListHomeWorkModel param) {
        //根据课程查询所有未删除的作业模板
        return homeworkModelMapper.listHomeworkModel(param);
    }
}




