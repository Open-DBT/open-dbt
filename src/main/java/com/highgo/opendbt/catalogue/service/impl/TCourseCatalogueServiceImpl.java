package com.highgo.opendbt.catalogue.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueAuthClass;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueAuthStu;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import com.highgo.opendbt.catalogue.mapper.TCourseCatalogueMapper;
import com.highgo.opendbt.catalogue.service.TCatalogueAuthClassService;
import com.highgo.opendbt.catalogue.service.TCatalogueResourcesService;
import com.highgo.opendbt.catalogue.service.TCourseCatalogueService;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.SerialNumCount;
import com.highgo.opendbt.contents.domain.entity.TCourseContents;
import com.highgo.opendbt.contents.service.TCourseContentsService;
import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.course.domain.entity.TCourseUser;
import com.highgo.opendbt.course.service.CourseService;
import com.highgo.opendbt.course.service.TCourseUserService;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.process.mapper.TCatalogueProcessMapper;
import com.highgo.opendbt.process.service.TCatalogueProcessService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.mapper.SclassMapper;
import com.highgo.opendbt.sclass.service.SclassService;
import com.highgo.opendbt.sclass.service.TClassStuService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.mapper.UserInfoMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @Description: 课程目录类
 * @Title: Authentication
 * @Package com.highgo.opendbt.catalogue.service.impl
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 13:44
 */
@Service
public class TCourseCatalogueServiceImpl extends ServiceImpl<TCourseCatalogueMapper, CourseCatalogue>
        implements TCourseCatalogueService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private  TCourseCatalogueMapper courseCatalogueMapper;
    @Autowired
    private  TCatalogueAuthClassService catalogueAuthClassService;
    @Autowired
    private  SclassMapper sclassMapper;
    @Autowired
    private  UserInfoMapper userInfoMapper;
    @Autowired
    private  TCatalogueResourcesService catalogueResourcesService;
    @Autowired
    private  TCatalogueProcessService catalogueProcessService;
    @Autowired
    private  TCatalogueProcessMapper catalogueProcessMapper;
    @Autowired
    private  TCourseContentsService courseContentsService;
    @Autowired
    private  CourseService courseService;
    @Autowired
    private  TCourseUserService courseUserService;
    @Autowired
    private  TClassStuService classStuService;
    @Autowired
    private  SclassService sclassService;


    /**
     * @description: 查询带有发布状态的目录树接口
     * @author:
     * @date: 2022/7/25 13:29
     * @param: courseId 课程id
     * @param: classId 班级id
     * @return:
     **/
    @Override
    public Map<String, Object> getCatalogueByClass(HttpServletRequest request, int courseId, int classId) {
        List<CourseCatalogue> collect = null;
        //查询目录列表
        List<CourseCatalogue> courseCataloguesList = getCatalogueList(courseId, classId);
        //设置排序的序号
        SerialNumCount serialNumCount = new SerialNumCount();
        serialNumCount.setNum("1");
        //迭代生成树结构目录并查询任务点和进度
        if (courseCataloguesList != null && !courseCataloguesList.isEmpty()) {
            collect = getCatalogueTreeAndTaskSchedule(courseCataloguesList, serialNumCount, classId);
        }
        //拼装返回参数
        Map<String, Object> catalogues = new HashMap<>();
        //目录树
        catalogues.put("catalogueTreeList", collect);
        //班级id
        catalogues.put("class_id", classId);
        return catalogues;
    }

    /**
     * @description: 学生端目录查询
     * @author:
     * @date: 2022/8/15 15:02
     * @param: courseId 课程id
     * @return:
     **/
    @Override
    public Map<String, Object> getCatalogueByStu(HttpServletRequest request, int courseId) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        int userId = loginUser.getUserId();
        Integer classId;
        // 根据当前登录人和课程id查询所在班级
        List<Sclass> classes = sclassMapper.findClassByStuAndCourse(courseId, userId);
        //班级不能为空
        BusinessResponseEnum.UNFINDCLASS.assertIsNotEmpty(classes, userId, courseId);
        //存在多个班级，默认显示最后加入的班级
        if (classes.size() > 1) {
            classId = classStuService.getCurrentClass(userId, classes);
        } else classId = classes.get(0).getId();
        //查询目录列表
        List<CourseCatalogue> courseCataloguesList = getCatalogueList(courseId, classId);
        List<CourseCatalogue> collect = null;
        //所有任务数
        AtomicInteger stuAllTaskNum = new AtomicInteger();
        //完成任务数
        AtomicInteger stuCompleteTaskNum = new AtomicInteger();
        AtomicInteger taskNum = new AtomicInteger();
        //设置排序的序号
        SerialNumCount serialNumCount = new SerialNumCount();
        serialNumCount.setNum("1");
        //迭代生成树结构目录并查询任务点和进度和完成情况统计
        if ((courseCataloguesList != null) && !courseCataloguesList.isEmpty()) {
            collect = getCatalogueTreeAndTaskScheduleByStu(courseCataloguesList, serialNumCount, stuAllTaskNum, stuCompleteTaskNum, taskNum, userId, classId);
        }
        Map<String, Object> catalogues = new HashMap<>();
        //封装返回参数
        packages(catalogues, collect, taskNum, stuAllTaskNum, stuCompleteTaskNum);
        return catalogues;
    }



    /**
     * @description: 默认发布
     * @author:
     * @date: 2022/10/11 11:09
     * @param: [request, catalogue]
     * @return: void
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void defaultPublish(HttpServletRequest request, CourseCatalogue catalogue) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //班级发布了有任务点
        List<TCatalogueAuthClass> authClassUpdateList = new ArrayList<>();
        //班级发布了但没有任务点
        List<TCatalogueAuthClass> progressUpdateList = new ArrayList<>();
        //需要发布的班级
        List<TCatalogueAuthClass> classes = catalogue.getClasses();
        if (classes == null || classes.isEmpty()) {
            logger.info("默认发布未查询到发布班级，发布的目录信息为{}", catalogue.toString());
            return;
        }
        //查询上层目录信息
        CourseCatalogue parentCatalogue = this.getById(catalogue.getParentId());
        classes.forEach(item -> {
            //获取当前目录不同班级的发布状态 0未发布1已发布
            Integer flag = getCurrentFlag(catalogue, item, parentCatalogue);
            //flag为null表示:发布过不会再次自动发布
            if (flag != null) {
                //获取当前班级发布信息
                TCatalogueAuthClass currentAuthClass = getCurrentAuthClass(catalogue, item, flag);
                //添加到集合中
                authClassUpdateList.add(currentAuthClass);
            } else {
                TCatalogueAuthClass currentAuthClass = catalogueAuthClassService
                        .getOne(new QueryWrapper<TCatalogueAuthClass>()
                                .eq("course_id", catalogue.getCourseId())
                                .eq("catalogue_id", catalogue.getId())
                                .eq("class_id", item.getClassId()));
                progressUpdateList.add(currentAuthClass);
            }
        });
        //更新
        if (!authClassUpdateList.isEmpty()) {
            boolean res = catalogueAuthClassService.saveOrUpdateBatch(authClassUpdateList);
            BusinessResponseEnum.FAILPUBLISH.assertIsTrue(res);
        }
        if (!progressUpdateList.isEmpty()) {
            //添加没有进度统计的发布班级
            authClassUpdateList.addAll(progressUpdateList);
        }
        //更新学习进度删除状态
        updateStuProcess(authClassUpdateList, catalogue, loginUser);

    }

    /**
     * @description: 添加学生时添加该学生学生进度表
     * @author:
     * @date: 2022/10/12 13:56
     * @param: [request, classId, userId]
     * @return: void
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addStudentPublish(HttpServletRequest request, Integer classId, Integer userId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        List<TCatalogueProcess> processList = new ArrayList<>();
        // 该班级所在的课程id
        int courseId = sclassService.getById(classId).getCourseId();
        //查询目录下所有资源
        List<TCatalogueResources> resourcesList = catalogueResourcesService.list(new QueryWrapper<TCatalogueResources>()
                .eq("delete_flag", 0)
                .eq("course_id", courseId));
        resourcesList.forEach(catalogueResources -> {
            TCatalogueProcess process = new TCatalogueProcess(
                    courseId
                    , catalogueResources.getCatalogueId()
                    , classId, userId
                    , catalogueResources.getResourcesId()
                    , (short) 1
                    , 0
                    , 0L
                    , catalogueResources.getTaskType()
                    , new Date()
                    , loginUser.getUserId());
            process.setDeleteFlag(1).setDeleteTime(new Date()).setDeleteUser(loginUser.getUserId());
            processList.add(process);
        });
        if (!processList.isEmpty()) {
            boolean res = catalogueProcessService.saveBatch(processList);
            BusinessResponseEnum.FAILINIT.assertIsTrue(res);
        }
    }

    /**
     * @description: 删除学生时重新发布
     * @author:
     * @date: 2022/10/12 14:04
     * @param: [request, class_id, user_id]
     * @return: void
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delStudentPublish(HttpServletRequest request, Integer classId, List<Integer> userId) {
        // 该班级所在的课程id
        int courseId = sclassService.getById(classId).getCourseId();
        //删除该学生该课程下的发布信息
        catalogueProcessService.remove(new QueryWrapper<TCatalogueProcess>()
                .eq("course_id", courseId)
                .eq("class_id", classId)
                .in("user_id", userId));
    }

    /**
     * @description: 自动发布时更新学习进度
     * @author:
     * @date: 2022/10/11 14:03
     * @param: [classes, courseCatalogue, loginUser]
     * @return: void
     **/
    private void updateStuProcess(List<TCatalogueAuthClass> classes, CourseCatalogue courseCatalogue, UserInfo loginUser) {
        List<TCatalogueProcess> tCatalogueProcesses = new ArrayList<>();
        // 更新学习进度表
        if (classes != null && !classes.isEmpty()) {
            // 查询目录下有哪些资源
            List<TCatalogueResources> catalogueResources = catalogueResourcesService.list(new QueryWrapper<TCatalogueResources>()
                    .eq("course_id", courseCatalogue.getCourseId())
                    .eq("catalogue_id", courseCatalogue.getId())
                    .eq("is_task", 1)
                    .eq("delete_flag", 0));
            if (catalogueResources != null && !catalogueResources.isEmpty()) {
                //  遍历班级查询每个班级下的学生列表
                for (TCatalogueAuthClass clazz : classes) {
                    List<UserInfo> userList = userInfoMapper.getStudentListByClassId(clazz.getClassId());
                    List<TCatalogueProcess> catalogueProcessList = catalogueProcessService.list(new QueryWrapper<TCatalogueProcess>()
                            .eq("course_id", courseCatalogue.getCourseId())
                            .eq("catalogue_id", courseCatalogue.getId())
                            .eq("class_id", clazz.getClassId()));
                    // 遍历学生资源更新进度表
                    userList.forEach(user ->
                            catalogueResources.forEach(res -> {
                                // 根据课程目录班级学生资源信息筛选进度
                                List<TCatalogueProcess> processes = catalogueProcessList.stream()
                                        .filter(item -> item.getUserId().equals(user.getUserId())
                                                && item.getResourcesId().equals(res.getResourcesId()))
                                        .collect(Collectors.toList());
                                TCatalogueProcess catalogueProcess;
                                //进度不存在
                                if (processes.size() != 1) {
                                    catalogueProcess = new TCatalogueProcess(courseCatalogue.getCourseId()
                                            , courseCatalogue.getId()
                                            , clazz.getClassId()
                                            , user.getUserId()
                                            , res.getResourcesId()
                                            , (short) 1
                                            , new Date()
                                            , loginUser.getUserId());
                                } else {
                                    catalogueProcess = processes.get(0);
                                }
                                //未发布模式或班级未被选中
                                if (clazz.getFlag() == 0) {
                                    catalogueProcess.setDeleteFlag(1).setDeleteTime(new Date()).setDeleteUser(loginUser.getUserId());
                                } else {
                                    //发布模式且班级被选中
                                    catalogueProcess.setDeleteFlag(0);
                                }
                                tCatalogueProcesses.add(catalogueProcess);
                            })
                    );
                }

            }

        }
        if (!tCatalogueProcesses.isEmpty()) {
            catalogueProcessService.saveOrUpdateBatch(tCatalogueProcesses);
        }
    }

    //获取当前发布班级信息
    private TCatalogueAuthClass getCurrentAuthClass(CourseCatalogue catalogue, TCatalogueAuthClass item, int flag) {
        // 查询本级目录的发布班级
        TCatalogueAuthClass currentAuthClass = catalogueAuthClassService
                .getOne(new QueryWrapper<TCatalogueAuthClass>()
                        .eq("course_id", catalogue.getCourseId())
                        .eq("catalogue_id", catalogue.getId())
                        .eq("class_id", item.getClassId()));
        if (currentAuthClass != null) {
            currentAuthClass.setFlag(flag);
        } else {
            //未查询到新增一个
            currentAuthClass = addCurrentAuthClass(item, flag, catalogue);
        }
        return currentAuthClass;
    }

    //根据父目录获取发布状态
    private Integer getCurrentFlag(CourseCatalogue catalogue, TCatalogueAuthClass item, CourseCatalogue parentCatalogue) {
        int flag = 0;
        //查看本级目录在班级发布表是否存在
        TCatalogueAuthClass currentAuthClass = catalogueAuthClassService
                .getOne(new QueryWrapper<TCatalogueAuthClass>()
                        .eq("class_id", item.getClassId())
                        .eq("course_id", catalogue.getCourseId())
                        .eq("catalogue_id", catalogue.getId()));
        //本级班级发布信息不存在
        if (currentAuthClass == null) {
            if (catalogue.getParentId() == 0) {
                flag = 1;
            } else {
                TCatalogueAuthClass parentAuthClass = catalogueAuthClassService
                        .getOne(new QueryWrapper<TCatalogueAuthClass>()
                                .eq("class_id", item.getClassId())
                                .eq("course_id", parentCatalogue.getCourseId())
                                .eq("catalogue_id", parentCatalogue.getId()));
                //数据表中存在并且值改变，数据表中不存在且设置为发布
                if (parentAuthClass != null && parentAuthClass.getFlag() == 1) {
                    flag = 1;
                }
            }
        } else {
            return null;
        }

        return flag;
    }

    /**
     * @description: 自动发布时新增发布班级信息
     * @author:
     * @date: 2022/10/11 13:36
     * @param: [item, flag, catalogue]
     * @return: com.highgo.opendbt.catalogue.model.TCatalogueAuthClass
     **/
    private TCatalogueAuthClass addCurrentAuthClass(TCatalogueAuthClass item, int flag, CourseCatalogue catalogue) {
        return new TCatalogueAuthClass(catalogue.getCourseId()
                , catalogue.getId()
                , catalogue.getCatalogueName()
                , item.getClassId()
                , "0"
                , flag
                , item.getClassName());
    }


    /**
     * @description: 目录更新
     * @author:
     * @date: 2022/8/25 16:04
     * @param: [request, courseCatalogue]
     * @return: com.highgo.opendbt.catalogue.model.CourseCatalogue
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCatalogue updateCatalogue(HttpServletRequest request, CourseCatalogue courseCatalogue) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        courseCatalogue.setUpdateUser(loginUser.getUserId());
        courseCatalogue.setUpdateTime(new Date());
        int updateRes = courseCatalogueMapper.updateById(courseCatalogue);
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(updateRes > 0);
        //查询内容
        TCourseContents contents = courseContentsService
                .getOne(new QueryWrapper<TCourseContents>()
                        .eq("course_id", courseCatalogue.getCourseId())
                        .eq("catalogue_id", courseCatalogue.getId()));
        if (contents == null) {
            //添加模板内容（第一个空内容用于页面显示）
            addContents(loginUser, courseCatalogue);
        }
        return courseCatalogue;
    }

    /**
     * @description: 目录逻辑删除
     * @author:
     * @date: 2022/8/25 16:05
     * @param: [request, id 目录id]
     * @return: com.highgo.opendbt.catalogue.model.CourseCatalogue
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCatalogue delCatalogue(HttpServletRequest request, int id) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        CourseCatalogue courseCatalogue = courseCatalogueMapper.selectById(id);
        int count = this.count(new QueryWrapper<CourseCatalogue>()
                .eq("course_id", courseCatalogue.getCourseId())
                .eq("delete_flag", 0));
        BusinessResponseEnum.KEEPDIRECTORY.assertIsTrue(count > 0);
        //查询子节点
        Map<String, Object> param = new HashMap<>();
        param.put("parent_id", courseCatalogue.getId());
        param.put("delete_flag", 0);
        List<CourseCatalogue> courseCataloguesList = courseCatalogueMapper.selectByMap(param);
        BusinessResponseEnum.UNALLOWDELETE.assertIsEmpty(courseCataloguesList);
        courseCatalogue.setDeleteTime(new Date());
        courseCatalogue.setDeleteFlag(1);
        courseCatalogue.setDeleteUser(loginUser.getUserId());
        int res = courseCatalogueMapper.updateById(courseCatalogue);
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res > 0);
        // 目录删除后删除学习进度表相关信息
        delCatalogueRes(id, loginUser);
        // 目录删除后删除目录资源表相关信息
        delCatalogueProcess(id, loginUser);
        return courseCatalogue;
    }

    /**
     * @description: 目录保存
     * @author:
     * @date: 2022/7/15 15:47
     * @param: courseCatalogue
     * @return:
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCatalogue saveCatalogue(HttpServletRequest request, CourseCatalogue courseCatalogue) {
        //获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        courseCatalogue.setCreateUser(loginUser.getUserId());
        courseCatalogue.setCreateTime(new Date());
        //获取目录表id最大值
        int sortNum = getCatalogueId();
        courseCatalogue.setSortNum(sortNum + 1);
        int saveCatalogueRes = courseCatalogueMapper.insert(courseCatalogue);
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(saveCatalogueRes > 0);
        // 新增目录内容模板
        addContents(loginUser, courseCatalogue);
        // 默认发布到所有班级
        // 查询班级发布信息
        CourseCatalogue catalogueAuth = getCatalogueAuth(request, courseCatalogue.getCourseId(), courseCatalogue.getId());
        //默认设置班级为已选中
        List<TCatalogueAuthClass> collect = catalogueAuth.getClasses().stream()
                .peek(item -> item.setFlag(1)).collect(Collectors.toList());
        courseCatalogue.setClasses(collect);
        //默认发布
        defaultPublish(request, courseCatalogue);
        return courseCatalogue;
    }

    /**
     * @description: 目录移动 备用
     * @author:
     * @date: 2022/8/25 16:27
     * @param: [request, sid 原始id, did 目标id]
     * @return: com.highgo.opendbt.catalogue.model.CourseCatalogue
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCatalogue moveCatalogue(HttpServletRequest request, int sid, int did) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        CourseCatalogue courseCatalogue = courseCatalogueMapper.selectById(sid);
        courseCatalogue.setUpdateTime(new Date());
        courseCatalogue.setUpdateUser(loginUser.getUserId());
        courseCatalogue.setParentId(did);
        return courseCatalogue;
    }

    /**
     * @description: 目录排序
     * @author:
     * @date: 2022/8/25 16:27
     * @param: [request, catalogue_id 目录id, direction 移动类型 up down]
     * @return: com.highgo.opendbt.catalogue.model.CourseCatalogue
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCatalogue moveSortNum(HttpServletRequest request, int catalogueId, String direction) {
        int updateNum = 0;
        //目标目录信息
        CourseCatalogue courseCatalogue = courseCatalogueMapper.selectById(catalogueId);
        int sortNum = courseCatalogue.getSortNum();
        //同级目录信息
        List<CourseCatalogue> courseCatalogues = this.list(new QueryWrapper<CourseCatalogue>()
                .eq("parent_id", courseCatalogue.getParentId())
                .eq("delete_flag", 0));
        BusinessResponseEnum.FAILMOVE.assertIsNotEmpty(courseCatalogues);
        //同级目录信息排序
        List<CourseCatalogue> collect = courseCatalogues.stream()
                .sorted(Comparator.comparing(CourseCatalogue::getSortNum))
                .collect(Collectors.toList());
        //上移
        if ("up".equalsIgnoreCase(direction)) {
            upMove(collect, courseCatalogue, sortNum);
        }
        //下移
        if ("down".equalsIgnoreCase(direction)) {
            downMove(collect, courseCatalogue, sortNum);
        }
        //更新到数据库
        int updateCatalogue = courseCatalogueMapper.updateById(courseCatalogue);
        logger.error("updateNum={},updateCatalogue={}", updateNum, updateCatalogue);
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(updateCatalogue > 0);
        return courseCatalogue;
    }


    /**
     * @description: 根据课程id和当前登陆人信息查询所有班级
     * @author:
     * @date: 2022/8/25 16:23
     * @param: [request, courseId 课程id]
     * @return: java.util.List<com.highgo.opendbt.sclass.domain.entity.Sclass>
     **/
    @Override
    public List<Sclass> getClassByLoginUser(HttpServletRequest request, int courseId) {

        UserInfo loginUser = Authentication.getCurrentUser(request);
        int userId = loginUser.getUserId();
        // 查询当前课程创建人
        Course course = courseService.getById(courseId);
        // 查询助教团队人员
        List<TCourseUser> courseUsers = courseUserService.list(new QueryWrapper<TCourseUser>()
                .eq("course_id", courseId));
        // 判断当前登录人是否在助教团队内
        if (courseUsers != null && !courseUsers.isEmpty()) {
            List<TCourseUser> collect = courseUsers.stream()
                    .filter(item -> item.getUserId()
                            .equals(loginUser.getUserId()))
                    .collect(Collectors.toList());
            if (!collect.isEmpty()) {
                userId = course.getCreator();
            }
        }
        return sclassMapper.getClassByLoginUserAndCourse(userId, courseId);
    }

    /**
     * @description: 根据课程id，目录id，查询目录的相关信息和目录下的班级
     * @author:
     * @date: 2022/7/20 13:08
     * @param: courseId 课程id
     * @param: catalogueId 目录id
     * @return: courseCatalogue 目录信息
     **/
    @Override
    public CourseCatalogue getCatalogueAuth(HttpServletRequest request, int courseId, int catalogueId) {
        //当前登录人名下的所有班级
        List<Sclass> classes = this.getClassByLoginUser(request, courseId);
        //目录信息
        CourseCatalogue courseCatalogue = courseCatalogueMapper.selectById(catalogueId);
        //当前目录发布过的班级
        Map<String, Object> param = new HashMap<>();
        param.put("course_id", courseId);
        param.put("catalogue_id", catalogueId);
        List<TCatalogueAuthClass> authClasses = catalogueAuthClassService.listByMap(param);
        //查询班级信息
        List<TCatalogueAuthClass> collect = findClasses(courseCatalogue, authClasses, classes);
        //班级信息添加到目录
        courseCatalogue.setClasses(collect);
        return courseCatalogue;
    }

    /**
     * @description: 保存班级发布信息
     * @author:
     * @date: 2022/8/9 16:38
     * @param: courseCatalogue
     * @return:
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CourseCatalogue saveCatalogueAuthAll(HttpServletRequest request, CourseCatalogue courseCatalogue) {
        //校验并获取当前登录信息
        UserInfo loginUser = Authentication.getCurrentUser(request);

        //用于存储上层目录
        List<CourseCatalogue> parentCatalogues = new ArrayList<>();
        //模式是否改变 0未改变 1：以改变
        Integer isChange = getChange(courseCatalogue);
        //模式改变
        if (isChange != null && isChange == 1) {
            // 所有目录
            List<CourseCatalogue> list = this.list(new QueryWrapper<CourseCatalogue>()
                    .eq("delete_flag", 0)
                    .eq("course_id", courseCatalogue.getCourseId()));
            //获取子目录
            List<CourseCatalogue> childCatalogues = getAllChildrenCatalotue(courseCatalogue, list);
            // 获取所有满足条件的上层目录（查询上级目录若不是一级目录，状态为未发布，修改为发布，迭代查询上级目录直到一级目录若为未发布修改为发布状态）
            getAllParentCatalogue(parentCatalogues, courseCatalogue, list);
            //添加上本级目录
            childCatalogues.add(courseCatalogue);
            //更新下层目录勾选状态（包括模式变为发布和不发布两种情况）
            updateCatalogueAuthClass(courseCatalogue, childCatalogues);
            //下层目录更新到学习进度表
            updateStuProcess(courseCatalogue, childCatalogues, loginUser);
            //设置为发布
            if (courseCatalogue.getPublishStatus() == 1) {
                if (!parentCatalogues.isEmpty()) {
                    //更新上层目录勾选状态
                    updateCatalogueAuthClass(courseCatalogue, parentCatalogues);
                    //上层目录更新到学习进度表
                    updateStuProcess(courseCatalogue, parentCatalogues, loginUser);
                }
            }
        }

        return courseCatalogue;
    }

    /**
     * @description: 判断是否改变
     * @author:
     * @date: 2022/10/11 10:15
     * @param: [courseCatalogue]
     * @return: java.lang.Integer
     **/
    private Integer getChange(CourseCatalogue courseCatalogue) {
        int isChange = 0;
        TCatalogueAuthClass authClass = catalogueAuthClassService.getOne(new QueryWrapper<TCatalogueAuthClass>()
                .eq("class_id", courseCatalogue.getClassId())
                .eq("course_id", courseCatalogue.getCourseId())
                .eq("catalogue_id", courseCatalogue.getId()));
        //数据表中存在并且值改变或者数据表中不存在且设置为发布
        if ((authClass != null && !authClass.getFlag().equals(courseCatalogue.getPublishStatus()))
                || authClass == null
                && courseCatalogue.getPublishStatus() == 1) {
            isChange = 1;
        }
        return isChange;
    }

    /**
     * @description: 保存新增的班级
     * @author:
     * @date: 2022/10/10 16:37
     * @param: [clsses, loginUser]
     * @return: void
     **/
    private void addClass(List<TCatalogueAuthClass> clsses) {
        List<TCatalogueAuthClass> collect = clsses.stream().filter(item -> item.getId() == null).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            BusinessResponseEnum.SAVEFAIL.assertIsTrue(catalogueAuthClassService.saveBatch(collect));
        }
    }

    /**
     * @description: 修改所有目录所有班级勾选状态
     * @author:
     * @date: 2022/10/10 15:15
     * @param: [clsses 所有班级, catalogues 目录, loginUser, model 发布状态]
     * @return: void
     **/
    private void updateCatalogueAuthClass(CourseCatalogue courseCatalogue, List<CourseCatalogue> catalogues) {
        //目录ids
        List<Integer> catalogueIds = catalogues.stream().map(CourseCatalogue::getId).collect(Collectors.toList());
        //课程id
        Integer courseId = catalogues.get(0).getCourseId();
        //查询所有目录班级发布信息
        List<TCatalogueAuthClass> authClassList = catalogueAuthClassService.list(new QueryWrapper<TCatalogueAuthClass>()
                .eq("course_id", courseId)
                .eq("class_id", courseCatalogue.getClassId())
                .in(CollectionUtils.isNotEmpty(catalogueIds), "catalogue_id", catalogueIds));
        //遍历 目录筛选不存在班级发布表中的发布信息添加到集合用于新增
        catalogues.forEach(item -> {
            boolean isExist = authClassList.stream().anyMatch(authClass -> authClass.getCatalogueId().equals(item.getId()));
            //不存在发布课程，需要新增
            if (!isExist) {
                //追加新增发布班级到authClassList集合
                addAuthClass(courseCatalogue, authClassList, item);
            }
        });
        //选中状态设置
        List<TCatalogueAuthClass> authClassUpdateList = authClassList.stream()
                .map(item -> item.setFlag(courseCatalogue.getPublishStatus()))
                .collect(Collectors.toList());
        //更新
        boolean success = catalogueAuthClassService.saveOrUpdateBatch(authClassUpdateList);
        BusinessResponseEnum.FAILPUBLISH.assertIsTrue(success);
    }

    /**
     * @description: 追加新增发布班级到authClassList集合
     * @author:
     * @date: 2022/10/11 10:02
     * @param: [catalogue, authClassList, item, loginUser]
     * @return: void
     **/
    private void addAuthClass(CourseCatalogue catalogue, List<TCatalogueAuthClass> authClassList, CourseCatalogue courseCatalogue) {
        TCatalogueAuthClass tCatalogueAuthClass = new TCatalogueAuthClass();
        //目录id
        tCatalogueAuthClass.setCatalogueId(courseCatalogue.getId());
        //课程id
        tCatalogueAuthClass.setCourseId(courseCatalogue.getCourseId());
        //发布状态
        tCatalogueAuthClass.setFlag(0);
        //目录名称
        tCatalogueAuthClass.setCatalogueName(courseCatalogue.getCatalogueName());
        //班级id
        tCatalogueAuthClass.setClassId(catalogue.getClassId());
        //班级名称
        Sclass clazz = sclassService.getById(catalogue.getClassId());
        if (clazz != null) {
            tCatalogueAuthClass.setClassName(clazz.getClassName());
        }
        //发布类型（学生 班级）
        tCatalogueAuthClass.setAuthType("0");
        authClassList.add(tCatalogueAuthClass);
    }

    /**
     * @description: 查询上级目录若不是一级目录，状态为未发布，迭代查询上级目录直到一级目录若为未发布修，添加到集合中
     * @author:
     * @date: 2022/10/10 13:43
     * @param: [courseCatalogue]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.CourseCatalogue>
     **/
    private void getAllParentCatalogue(List<CourseCatalogue> parentCatalogues, CourseCatalogue courseCatalogue, List<CourseCatalogue> list) {
        //班级id
        Integer classId = courseCatalogue.getClassId();
        //上级目录若不是一级目录
        if (courseCatalogue.getParentId() != 0) {
            List<CourseCatalogue> collect = list.stream().filter(item -> item.getId().equals(courseCatalogue.getParentId()))
                    .collect(Collectors.toList());
            BusinessResponseEnum.NOTFOUNDDIRECTORY.assertIsNotEmpty(collect, courseCatalogue.getParentId());
            //查询该目录下班级发布信息
            TCatalogueAuthClass authClass = catalogueAuthClassService.getOne(new QueryWrapper<TCatalogueAuthClass>()
                    .eq("course_id", courseCatalogue.getCourseId())
                    .eq("catalogue_id", collect.get(0).getId())
                    .eq("class_id", classId));
            //状态为未发布
            if (authClass == null || authClass.getFlag() == 0) {
                //存储到集合
                parentCatalogues.add(courseCatalogue);
                //迭代上层目录
                getAllParentCatalogue(parentCatalogues, collect.get(0), list);
            }
        }
    }


    /**
     * @description: 查询所有子目录
     * @author:
     * @date: 2022/10/10 10:38
     * @param: [courseCatalogue当前目录]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.CourseCatalogue>
     **/
    private List<CourseCatalogue> getAllChildrenCatalotue(CourseCatalogue courseCatalogue, List<CourseCatalogue> list) {
        //所有目录
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }
        //筛选子目录
        List<CourseCatalogue> collect = list.stream().filter(item -> item.getParentId()
                .equals(courseCatalogue.getId()))
                .collect(Collectors.toList());
        if (collect.isEmpty()) {
            return new ArrayList<>();
        }
        //添加到子目录集合
        List<CourseCatalogue> childCatalogues = new ArrayList<>(collect);
        //迭代查询子目录
        getChildes(childCatalogues, collect, list);
        return childCatalogues;
    }

    /**
     * @description: 迭代查询子目录
     * @author:
     * @date: 2022/10/10 10:52
     * @param: [childCatalogues, collect, list]
     * @return: void
     **/
    private void getChildes(List<CourseCatalogue> childCatalogues, List<CourseCatalogue> collect, List<CourseCatalogue> list) {
        //遍历目录
        for (CourseCatalogue catalogue : collect) {
            List<CourseCatalogue> childCollect = list.stream()
                    .filter(item -> item.getParentId()
                            .equals(catalogue.getId()))
                    .collect(Collectors.toList());
            if (!childCollect.isEmpty()) {
                //子目录存储到集合中
                childCatalogues.addAll(childCollect);
                //子目录迭代
                getChildes(childCatalogues, childCollect, list);
            }
        }
    }


    /**
     * @description: 迭代生成树结构目录并查询任务点和进度和完成情况统计
     * @author:
     * @date: 2022/8/25 14:54
     * @param: [courseCataloguesList 目录列表, serialNumCount 显示序号, stuAllTaskNum 当前学生该课程下的总任务数, stuCompleteTaskNum 当前学生该课程下的完成任务数, taskNum 总任务数,userId 用户id]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.CourseCatalogue>
     **/
    private List<CourseCatalogue> getCatalogueTreeAndTaskScheduleByStu(List<CourseCatalogue> courseCataloguesList, SerialNumCount serialNumCount, AtomicInteger stuAllTaskNum, AtomicInteger stuCompleteTaskNum, AtomicInteger taskNum, int userId, Integer classId) {
        return courseCataloguesList.stream()
                .filter(item -> item.getParentId() == 0)
                .sorted(Comparator.comparing(CourseCatalogue::getSortNum))
                .map(item -> {
                    item.setCatalogueLevel(1);
                    //序号
                    item.setSerialNum(serialNumCount.getNum());
                    if (item.getPublishStatus() == null) {
                        item.setPublishStatus(0);
                    }

                    //处理子目录
                    List<CourseCatalogue> children = getChildrenStu(item, courseCataloguesList, userId, serialNumCount, classId);
                    if (children != null && !children.isEmpty()) {
                        item.setChildrens(children);
                        item.setIsleaf(false);
                    } else {
                        item.setIsleaf(true);
                    }
                    //一级目录需要统计子目录的任务总数和完成比例
                    sumStuTaskAndProcess(item);
                    taskNum.addAndGet(item.getCatalogueTaskNum());
                    stuAllTaskNum.addAndGet(item.getTotalNum());
                    stuCompleteTaskNum.addAndGet(item.getCompleteNum());
                    //序号增加
                    serialNumCount.addSerralNum(item.getSerialNum());
                    return sumStuTaskAndProcess(item);
                }).collect(Collectors.toList());
    }

    /**
     * @description: 迭代生成树结构目录并查询任务点和进度
     * @author:
     * @date: 2022/8/25 14:47
     * @param: [courseCataloguesList, serialNumCount, classId]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.CourseCatalogue>
     **/
    private List<CourseCatalogue> getCatalogueTreeAndTaskSchedule(List<CourseCatalogue> courseCataloguesList, SerialNumCount serialNumCount, int classId) {
        return courseCataloguesList.stream().filter(item -> item.getParentId() == 0)
                .sorted(Comparator.comparing(CourseCatalogue::getSortNum)).map(item -> {
                    //层级
                    item.setCatalogueLevel(1);
                    //序号
                    item.setSerialNum(serialNumCount.getNum());
                    if (item.getPublishStatus() == null) {
                        item.setPublishStatus(0);
                    }

                    //设置子目录
                    List<CourseCatalogue> children = getChildren(item, courseCataloguesList, serialNumCount, classId);
                    if (children != null && !children.isEmpty()) {
                        item.setChildrens(children);
                        item.setIsleaf(false);
                    } else {
                        item.setIsleaf(true);
                    }
                    //序号增加
                    serialNumCount.addSerralNum(item.getSerialNum());
                    //一级目录需要统计子目录的任务总数和完成比例
                    return sumTaskAndProcess(item);
                }).collect(Collectors.toList());
    }

    /**
     * @description: 查询目录列表
     * @author:
     * @date: 2022/8/25 14:31
     * @param: [courseId, classId]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.CourseCatalogue>
     **/
    private List<CourseCatalogue> getCatalogueList(int courseId, int classId) {
        Map<String, Object> param = new HashMap<>();
        param.put("courseId", courseId);
        param.put("deleteFlag", 0);
        param.put("authType", "0");
        param.put("classId", classId);
        return courseCatalogueMapper.selectCataloguePublish(param);
    }


    /**
     * @description: 相关统计数据写入目录中
     * @author:
     * @date: 2022/8/25 15:03
     * @param: [item]
     * @return: com.highgo.opendbt.catalogue.model.CourseCatalogue
     **/
    private CourseCatalogue sumStuTaskAndProcess(CourseCatalogue item) {
        //目录任务数
        AtomicInteger catalogueTaskNum = new AtomicInteger();
        //总学生数
        AtomicInteger totalNum = new AtomicInteger();
        //完成学生数
        AtomicInteger completeNum = new AtomicInteger();
        //完成比例
        AtomicInteger catalogueScale = new AtomicInteger();
        List<CourseCatalogue> childes = item.getChildrens();
        //迭代累加相关统计数据
        Map<String, Object> map = sumStuChildren(childes, completeNum, totalNum, catalogueScale, catalogueTaskNum);
        item.setTotalNum(((AtomicInteger) map.get("totalNum")).get());
        item.setCompleteNum(((AtomicInteger) map.get("completeNum")).get());
        item.setCatalogueTaskNum(((AtomicInteger) map.get("catalogueTaskNum")).get());
        item.setCatalogueScale(((AtomicInteger) map.get("catalogueScale")).get());
        return item;
    }

    /**
     * @description: 迭代累加相关统计数据
     * @author:
     * @date: 2022/8/25 15:01
     * @param: [childrens 目录列表, complete_num 完成学生数, total_num总学生数, catalogue_scale 完成比例, catalogue_task_num 总任务数]
     * @return: java.util.Map
     **/
    private Map<String, Object> sumStuChildren(List<CourseCatalogue> childrens, AtomicInteger completeNum, AtomicInteger totalNum, AtomicInteger catalogueScale, AtomicInteger catalogueTaskNum) {
        if (childrens != null && !childrens.isEmpty()) {
            childrens.forEach(catalogue -> {
                if (catalogue.getTotalNum() != null) {
                    totalNum.addAndGet(catalogue.getTotalNum());
                }
                if (catalogue.getCompleteNum() != null) {
                    completeNum.addAndGet(catalogue.getCompleteNum());
                }
                if (catalogue.getCatalogueTaskNum() != null) {
                    catalogueTaskNum.addAndGet(catalogue.getCatalogueTaskNum());
                }
                if (catalogue.getCatalogueScale() != null) {
                    catalogueScale.addAndGet(catalogue.getCatalogueScale());
                }

                List<CourseCatalogue> childrenLower = catalogue.getChildrens();
                sumStuChildren(childrenLower, completeNum, totalNum, catalogueScale, catalogueTaskNum);

            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("totalNum", totalNum);
        map.put("completeNum", completeNum);
        map.put("catalogueTaskNum", catalogueTaskNum);
        map.put("catalogueScale", catalogueScale);
        return map;
    }

    /**
     * @description: 获取子目录结构和任务数和完成情况
     * @author:
     * @date: 2022/8/25 15:00
     * @param: [courseCatalogue 目录, courseCataloguesList 目录列表, user_id 学生id, serialNumCount 显示序号]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.CourseCatalogue>
     **/
    private List<CourseCatalogue> getChildrenStu(CourseCatalogue courseCatalogue, List<CourseCatalogue> courseCataloguesList, int userId, SerialNumCount serialNumCount, Integer classId) {
        serialNumCount.countlevel(serialNumCount.getNum());
        return courseCataloguesList.stream().filter(item -> item.getParentId()
                .equals(courseCatalogue.getId()))
                .sorted(Comparator.comparing(CourseCatalogue::getSortNum))
                .map(item -> {
                    item.setSerialNum(serialNumCount.getNum());
                    item.setCatalogueLevel(courseCatalogue.getCatalogueLevel() + 1);
                    if (item.getPublishStatus() == null) {
                        item.setPublishStatus(0);
                    }

                    List<CourseCatalogue> children = getChildrenStu(item, courseCataloguesList, userId, serialNumCount, classId);
                    if (children != null && !children.isEmpty()) {
                        item.setChildrens(children);
                        item.setIsleaf(false);
                    } else {
                        item.setIsleaf(true);
                    }
                    serialNumCount.addSerralNum(item.getSerialNum());
                    //获取任务数和完成情况
                    return findTaskAndProcessByStu(item, userId, classId);
                }).collect(Collectors.toList());
    }

    /**
     * @description: 查询任务点数和学生的学习进度
     * @author:
     * @date: 2022/8/25 14:59
     * @param: [item, user_id]
     * @return: com.highgo.opendbt.catalogue.model.CourseCatalogue
     **/
    private CourseCatalogue findTaskAndProcessByStu(CourseCatalogue item, int userId, Integer classId) {
        // 查询任务点
        if (item.getPublishStatus() == 1) {
            Integer count = catalogueResourcesService.count(new QueryWrapper<TCatalogueResources>()
                    .eq("course_id", item.getCourseId())
                    .eq("catalogue_id", item.getId())
                    .eq("is_task", 1)
                    .eq("delete_flag", 0));
            item.setCatalogueTaskNum(count);
            //  查询学习进度，总数修改为查询班级下学生总数
            int totalNum = catalogueProcessService.count(new QueryWrapper<TCatalogueProcess>()
                    .eq("delete_flag", 0)
                    .eq("course_id", item.getCourseId())
                    .eq("catalogue_id", item.getId())
                    .eq("user_id", userId)
                    .eq("class_id", classId));
            int completeNum = catalogueProcessService.count(new QueryWrapper<TCatalogueProcess>()
                    .eq("delete_flag", 0)
                    .eq("course_id", item.getCourseId())
                    .eq("catalogue_id", item.getId())
                    .eq("study_status", 2)
                    .eq("user_id", userId)
                    .eq("class_id", classId));
            if (totalNum != 0) {
                item.setTotalNum(totalNum);
                if (completeNum == 0) {
                    item.setCatalogueScale(0);
                } else {
                    item.setCatalogueScale((int) Math.round((completeNum / (totalNum * 1.00)) * 100));
                }
            }
            if (completeNum != 0) {
                item.setCompleteNum(completeNum);
            }
        }
        return item;
    }

    /**
     * @description: 一级目录统计子目录的任务总数和完成比例
     * @author:
     * @date: 2022/8/25 14:49
     * @param: [item 目录]
     * @return: com.highgo.opendbt.catalogue.model.CourseCatalogue
     **/
    private CourseCatalogue sumTaskAndProcess(CourseCatalogue item) {
        //总任务数
        AtomicInteger catalogueTaskNum = new AtomicInteger();
        //总学生数
        AtomicInteger totalNum = new AtomicInteger();
        //完成的学生数
        AtomicInteger completeNum = new AtomicInteger();
        //每个目录完成占比
        AtomicInteger catalogueScale = new AtomicInteger();
        //有任务点的目录数
        AtomicInteger num = new AtomicInteger();
        List<CourseCatalogue> childrens = item.getChildrens();
        //合并统计数据
        Map<String, Object> map = sumChildren(childrens, catalogueTaskNum, catalogueScale, totalNum, completeNum, num);
        item.setCatalogueTaskNum(((AtomicInteger) map.get("catalogueTaskNum")).get());
        int scale = ((AtomicInteger) map.get("catalogueScale")).get();
        int scaleNum = ((AtomicInteger) map.get("num")).get();
        item.setCatalogueScale((int) Math.round((scale / (scaleNum * 100.00)) * 100));
        return item;
    }

    /**
     * @description: 合并统计数据
     * @author:
     * @date: 2022/8/25 16:49
     * @param: [children, catalogueTaskNum 章节目录任务数, catalogueScale 目录任务比例, totalNum, completeNum, num 有任务点的目录数]
     * @return: java.util.Map
     **/
    private Map<String, Object> sumChildren(List<CourseCatalogue> children, AtomicInteger catalogueTaskNum, AtomicInteger catalogueScale, AtomicInteger totalNum, AtomicInteger completeNum, AtomicInteger num) {
        if (children != null && !children.isEmpty()) {
            children.forEach(catalogue -> {
                if (Optional.ofNullable(catalogue.getCatalogueTaskNum()).isPresent()) {
                    //章节目录任务数相加
                    catalogueTaskNum.addAndGet(catalogue.getCatalogueTaskNum());
                    if (catalogue.getCatalogueTaskNum() > 0) {
                        //有任务点的目录数加一
                        num.addAndGet(1);
                    }
                }
                if (Optional.ofNullable(catalogue.getCatalogueScale()).isPresent()) {
                    //目录任务比例相加
                    catalogueScale.addAndGet(catalogue.getCatalogueScale());
                }
                if (Optional.ofNullable(catalogue.getCompleteNum()).isPresent()) {
                    //完成学生数相加
                    completeNum.addAndGet(catalogue.getCompleteNum());
                }
                if (Optional.ofNullable(catalogue.getTotalNum()).isPresent()) {
                    //总数相加
                    totalNum.addAndGet(catalogue.getTotalNum());
                }
                //子目录
                List<CourseCatalogue> childrensLower = catalogue.getChildrens();
                //迭代
                sumChildren(childrensLower, catalogueTaskNum, catalogueScale, totalNum, completeNum, num);

            });
        }
        Map<String, Object> map = new HashMap<>();
        map.put("catalogueTaskNum", catalogueTaskNum);
        map.put("catalogueScale", catalogueScale);
        map.put("num", num);
        return map;
    }

    /**
     * @description: 查询任务点学习进度
     * @author:
     * @date: 2022/8/15 15:35
     * @param: item
     * @return:
     **/
    private CourseCatalogue findTaskAndProcess(CourseCatalogue item, int classId) {
        // 查询任务点
        Integer count = catalogueResourcesService.count(new QueryWrapper<TCatalogueResources>()
                .eq("course_id", item.getCourseId())
                .eq("catalogue_id", item.getId())
                .eq("is_task", 1)
                .eq("delete_flag", 0));
        item.setCatalogueTaskNum(count);
        // 查询学习进度
        if (classId != -1) {
            Map<String, Object> param = new HashMap<>();
            param.put("courseId", item.getCourseId());
            param.put("catalogueId", item.getId());
            param.put("classId", classId);
            CourseCatalogue process = catalogueProcessMapper.countProcess(param);
            //总任务数 通过查询班级下有多少学生来统计，防止新增学生未发布的情况下统计不到
            Integer totalNum = process.getTotalNum();
            item.setTotalNum(totalNum);
            Integer completeNum = process.getCompleteNum();
            item.setCompleteNum(completeNum);
            if (totalNum != null && totalNum != 0) {
                if (completeNum == null || completeNum == 0) {
                    item.setCatalogueScale(0);
                } else {
                    item.setCatalogueScale((int) Math.round((completeNum / (totalNum * 1.00)) * 100));
                }
            } else {
                item.setCatalogueScale(0);
            }
        }

        return item;
    }

    /**
     * @description: 迭代设置子目录
     * @author:
     * @date: 2022/8/25 16:51
     * @param: [courseCatalogue, courseCataloguesList, serialNumCount 显示序号, classId 班级id]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.CourseCatalogue>
     **/
    private List<CourseCatalogue> getChildren(CourseCatalogue courseCatalogue, List<CourseCatalogue> courseCataloguesList, SerialNumCount serialNumCount, int classId) {
        serialNumCount.countlevel(serialNumCount.getNum());
        return courseCataloguesList.stream().filter(item -> item.getParentId().equals(courseCatalogue.getId()))
                .sorted(Comparator.comparing(CourseCatalogue::getSortNum)).map(item -> {
                    item.setCatalogueLevel(courseCatalogue.getCatalogueLevel() + 1);
                    item.setSerialNum(serialNumCount.getNum());
                    if (item.getPublishStatus() == null) {
                        item.setPublishStatus(0);
                    }

                    List<CourseCatalogue> children = getChildren(item, courseCataloguesList, serialNumCount, classId);
                    if (children != null && !children.isEmpty()) {
                        item.setChildrens(children);
                        item.setIsleaf(false);
                    } else {
                        item.setIsleaf(true);
                    }
                    serialNumCount.addSerralNum(item.getSerialNum());
                    //查询设置任务点学习进度
                    return findTaskAndProcess(item, classId);
                }).collect(Collectors.toList());
    }

    /**
     * @description: 添加模板内容（第一个空内容用于页面显示）
     * @author:
     * @date: 2022/8/25 16:41
     * @param: [loginUser, courseCatalogue]
     * @return: void
     **/
    private void addContents(UserInfo loginUser, CourseCatalogue courseCatalogue) {
        TCourseContents tCourseContents = new TCourseContents(courseCatalogue.getCourseId()
                , courseCatalogue.getId()
                , 1
                , new Date()
                , loginUser.getUserId());
        boolean saveRes = courseContentsService.save(tCourseContents);
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(saveRes);
    }


    /*目录删除后删除学习进度表相关信息*/
    private void delCatalogueProcess(int id, UserInfo loginUser) {
        List<TCatalogueProcess> processes = catalogueProcessService.list(new QueryWrapper<TCatalogueProcess>()
                .eq("catalogue_id", id));
        if (processes != null && !processes.isEmpty()) {
            List<TCatalogueProcess> processesUpdate = processes.stream()
                    .peek(process -> {
                process.setDeleteFlag(1);
                process.setDeleteTime(new Date());
                process.setDeleteUser(loginUser.getUserId());
            }).collect(Collectors.toList());
            boolean updateProcessRes = catalogueProcessService.saveOrUpdateBatch(processesUpdate);
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(updateProcessRes);
            logger.info("删除目录{}后同时逻辑删除学习进度表数据", id);
        }
    }

    /*目录删除后删除目录资源表相关信息*/
    private void delCatalogueRes(int id, UserInfo loginUser) {
        List<TCatalogueResources> resources = catalogueResourcesService.list(new QueryWrapper<TCatalogueResources>()
                .eq("catalogue_id", id));
        if (resources != null && !resources.isEmpty()) {
            List<TCatalogueResources> resourcesUpdate = resources.stream().peek(res -> {
                res.setDeleteFlag(1);
                res.setDeleteTime(new Date());
                res.setDeleteUser(loginUser.getUserId());
            }).collect(Collectors.toList());
            boolean saveOrUpdateBatchRes = catalogueResourcesService.saveOrUpdateBatch(resourcesUpdate);
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(saveOrUpdateBatchRes);
            logger.info("删除目录{}后同时逻辑删除目录资源表数据", id);
        }
    }


    /**
     * @description: 更新到学习进度表
     * @author:
     * @date: 2022/8/25 16:20
     * @param: [clsses 发布的班级信息列表, model 模式：0：发布模式，1：闯关模式，2：隐藏模式，3：定时开放模式, courseCatalogue目录, loginUser登录人员信息]
     * @return: void
     **/
    private void updateStuProcess(CourseCatalogue courseCatalogue, List<CourseCatalogue> catalogues, UserInfo loginUser) {
        List<TCatalogueProcess> insertCatalogueProcesses = new ArrayList<>();
        List<TCatalogueProcess> updateCatalogueProcesses = new ArrayList<>();
        // 班级id
        Integer classId = courseCatalogue.getClassId();
        //  遍历班级查询每个班级下的学生列表
        List<UserInfo> userList = userInfoMapper.getStudentListByClassId(classId);
        //发布状态
        Integer flag = courseCatalogue.getPublishStatus();
        //课程下的所有资源
        List<TCatalogueResources> courseResources = catalogueResourcesService.list(new QueryWrapper<TCatalogueResources>()
                .eq("course_id", courseCatalogue.getCourseId())
                .eq("is_task", 1)
                .eq("delete_flag", 0));
        for (CourseCatalogue catalogue : catalogues) {
            //目录下的资源
            List<TCatalogueResources> catalogueResources = courseResources.stream().filter(item -> item.getCatalogueId()
                    .equals(catalogue.getId()))
                    .collect(Collectors.toList());
            if (!catalogueResources.isEmpty()) {
                List<TCatalogueProcess> courseProcess = catalogueProcessService.list(new QueryWrapper<TCatalogueProcess>()
                        .eq("course_id", catalogue.getCourseId())
                        .eq("catalogue_id", catalogue.getId())
                        .eq("class_id", classId));
                // 遍历学生资源更新进度表
                userList.forEach(user ->
                        catalogueResources.forEach(res -> {
                            // 根据课程目录班级学生资源信息查询进度表
                            List<TCatalogueProcess> collect = courseProcess.stream()
                                    .filter(item -> item.getUserId()
                                            .equals(user.getUserId()) && item.getResourcesId()
                                            .equals(res.getResourcesId()))
                                    .collect(Collectors.toList());
                            TCatalogueProcess catalogueProcess = collect.isEmpty() ? null : collect.get(0);
                            if (catalogueProcess == null) {
                                catalogueProcess = new TCatalogueProcess(catalogue.getCourseId()
                                        , catalogue.getId()
                                        , classId
                                        , user.getUserId()
                                        , res.getResourcesId()
                                        , (short) 1
                                        , new Date()
                                        , loginUser.getUserId());
                            }
                            //未发布状态
                            if (flag == 0) {
                                catalogueProcess.setDeleteFlag(1);
                                catalogueProcess.setDeleteTime(new Date());
                                catalogueProcess.setDeleteUser(loginUser.getUserId());
                            } else {
                                //发布模式且班级被选中
                                catalogueProcess.setDeleteFlag(0);
                            }
                            if (catalogueProcess.getId() == null) {
                                insertCatalogueProcesses.add(catalogueProcess);
                            } else {
                                updateCatalogueProcesses.add(catalogueProcess);
                            }

                        })
                );
            }
        }
        if (!insertCatalogueProcesses.isEmpty()) {
            catalogueProcessMapper.saveBatch(insertCatalogueProcesses);
            logger.info("新增的学习进度表{}", JSON.toJSONString(insertCatalogueProcesses));
        }
        if (!updateCatalogueProcesses.isEmpty()) {
            catalogueProcessMapper.updateBatch(updateCatalogueProcesses);
            logger.info("更新的学习进度表{}", JSON.toJSONString(updateCatalogueProcesses));
        }
    }


    /**
     * @description: 查询组装班级学生信息
     * @author:
     * @date: 2022/8/25 16:53
     * @param: [courseCatalogue, classes 有发布信息的班级（包括取消发布的）, allclasses 当前登录人名下的所有班级]
     * @return: java.util.List<com.highgo.opendbt.catalogue.model.TCatalogueAuthClass>
     **/
    private List<TCatalogueAuthClass> findClasses(CourseCatalogue courseCatalogue, List<TCatalogueAuthClass> classes, List<Sclass> allclasses) {
        List<Integer> classIds = new ArrayList<>();
        List<TCatalogueAuthClass> collect = classes.stream()
                .peek(item -> classIds.add(item.getClassId())).collect(Collectors.toList());
        //添加未选中的班级及班级中的学生信息，用于展示
        List<TCatalogueAuthClass> uncheckClasses = getUncheckClasses(allclasses, classIds, courseCatalogue);
        if (uncheckClasses != null && !uncheckClasses.isEmpty()) {
            collect.addAll(uncheckClasses);
        }
        return collect;
    }

    /**
     * @description: 目录下移
     * @author:
     * @date: 2022/8/25 16:38
     * @param: [collect, courseCatalogue, sort_num 序号]
     * @return: void
     **/
    private void downMove(List<CourseCatalogue> collect, CourseCatalogue courseCatalogue, int sortNum) {
        List<CourseCatalogue> collectDown = collect.stream().filter(item -> item.getSortNum() > courseCatalogue.getSortNum())
                .collect(Collectors.toList());
        BusinessResponseEnum.UNABLETOMOVEDOWN.assertIsNotEmpty(collectDown);
        CourseCatalogue courseCatalogueDown = collectDown.get(0);
        int downSortNum = courseCatalogueDown.getSortNum();
        courseCatalogueDown.setSortNum(sortNum);
        courseCatalogue.setSortNum(downSortNum);
        long updateNum = courseCatalogueMapper.updateById(courseCatalogueDown);
        BusinessResponseEnum.FAILMOVE.assertIsTrue(updateNum > 0);
    }

    /**
     * @description: 目录上移
     * @author:
     * @date: 2022/8/25 16:36
     * @param: [collect, courseCatalogue, sort_num 序号]
     * @return: void
     **/
    private void upMove(List<CourseCatalogue> collect, CourseCatalogue courseCatalogue, int sortNum) {
        List<CourseCatalogue> collectUP = collect.stream().filter(item -> item.getSortNum() < courseCatalogue.getSortNum())
                .collect(Collectors.toList());
        BusinessResponseEnum.UNABLETOMOVEUP.assertIsNotEmpty(collectUP);
        CourseCatalogue courseCatalogueUp = collectUP.get(collectUP.size() - 1);
        int upSortNum = courseCatalogueUp.getSortNum();
        courseCatalogueUp.setSortNum(sortNum);
        courseCatalogue.setSortNum(upSortNum);
        long updateNum = courseCatalogueMapper.updateById(courseCatalogueUp);
        BusinessResponseEnum.FAILMOVE.assertIsTrue(updateNum > 0);
    }

    /**
     * @description: 获取目录表id最大值
     * @author:
     * @date: 2022/8/25 16:11
     * @param: []
     * @return: int
     **/
    private int getCatalogueId() {
        Integer sortNum = courseCatalogueMapper.maxSortNum();
        if (sortNum == null)
            sortNum = 0;
        return sortNum;
    }

    /**
     * @description: 查询未被选中的班级信息
     * @author:
     * @date: 2022/7/20 11:03
     * @param: allclasses 所有的班级信息
     * @param: classIds 被选中的班级id集合
     * @param: courseCatalogue 当前所在的目录信息
     * @return: collect 返回的班级信息集合包含班级内的学生集合
     **/
    private List<TCatalogueAuthClass> getUncheckClasses(List<Sclass> allclasses, List<Integer> classIds, CourseCatalogue courseCatalogue) {
        return allclasses.stream().filter(item -> (classIds == null || classIds.isEmpty() || !classIds.contains(item.getId()))
        ).map(item -> {
            TCatalogueAuthClass tCatalogueAuthClass = new TCatalogueAuthClass();
            tCatalogueAuthClass.setCatalogueId(courseCatalogue.getId())
                    .setCourseId(courseCatalogue.getCourseId())
                    .setFlag(0)
                    .setCatalogueName(courseCatalogue.getCatalogueName())
                    .setClassId(item.getId())
                    .setClassName(item.getClassName())
                    .setAuthType("0");
            return tCatalogueAuthClass;
        }).collect(Collectors.toList());
    }

    /**
     * @description: 根据班级id查询班级下的所有学生并转换为TCatalogueAuthStu对象
     * @author:
     * @date: 2022/7/20 10:56
     * @param: item 班级信息
     * @param: course_id 课程id
     * @param: catalogue_id 目录id
     * @return: List<TCatalogueAuthStu>
     **/
    private List<TCatalogueAuthStu> switchUserTOCatalogueAuthStu(int sclassId, int courseId, int catalogueId) {
        List<UserInfo> userList = userInfoMapper.getStudentListByClassId(sclassId);
        return userList.stream().map(user -> new TCatalogueAuthStu()
                .setCatalogueId(catalogueId)
                .setClassId(sclassId)
                .setCode(user.getCode())
                .setCourseId(courseId)
                .setUserId(user.getUserId())
                .setUserName(user.getUserName())
                .setDelFlag(1)).collect(Collectors.toList());
    }

    /**
     * @description: 封装返回参数
     * @author:
     * @date: 2022/8/25 17:02
     * @param: [catalogues, collect, stuAllTaskNum 当前学生该课程下的总任务数, stuCompleteTaskNum 当前学生该课程下的完成任务数, taskNum 总任务数]
     * @return: void
     **/
    private void packages(Map<String, Object> catalogues, List<CourseCatalogue> collect, AtomicInteger taskNum, AtomicInteger stuAllTaskNum, AtomicInteger stuCompleteTaskNum) {
        //目录树
        catalogues.put("catalogueTreeList", collect);
        //总任务数
        catalogues.put("taskNum", taskNum.get());
        //学生总任务数
        catalogues.put("stuAllTaskNum", stuAllTaskNum.get());
        //学生完成任务数
        catalogues.put("stuCompleteTaskNum", stuCompleteTaskNum.get());
    }
}




