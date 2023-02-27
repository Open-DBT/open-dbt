package com.highgo.opendbt.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.page.PageMethod;
import com.google.gson.Gson;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueAuthClass;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import com.highgo.opendbt.catalogue.service.TCatalogueAuthClassService;
import com.highgo.opendbt.catalogue.service.TCatalogueResourcesService;
import com.highgo.opendbt.catalogue.service.TCourseCatalogueService;
import com.highgo.opendbt.common.bean.KnowledgeTreeTO;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.Message;
import com.highgo.opendbt.common.utils.SnowflakeIdWorker;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.contents.domain.entity.TCourseContents;
import com.highgo.opendbt.contents.service.TCourseContentsService;
import com.highgo.opendbt.course.domain.entity.*;
import com.highgo.opendbt.course.domain.model.Scene;
import com.highgo.opendbt.course.domain.model.SceneDetail;
import com.highgo.opendbt.course.mapper.*;
import com.highgo.opendbt.course.service.CourseService;
import com.highgo.opendbt.course.service.TCourseUserService;
import com.highgo.opendbt.login.mapper.NoticesMapper;
import com.highgo.opendbt.login.model.Notice;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.process.service.TCatalogueProcessService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.mapper.SclassMapper;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final CourseMapper courseMapper;

    private final ExerciseMapper exerciseMapper;

    private final KnowledgeMapper knowledgeMapper;


    private final ExerciseKnowledgeMapper exerciseKnowledgeMapper;

    private final SceneMapper sceneMapper;

    private final SceneDetailMapper sceneDetailMapper;

    private final NoticesMapper noticesMapper;

    private final SclassMapper sclassMapper;

    private final TCourseCatalogueService courseCatalogueService;

    private final TCourseContentsService courseContentsService;
    private final TCatalogueResourcesService catalogueResourcesService;

    private final TCatalogueProcessService catalogueProcessService;

    private final TCatalogueAuthClassService catalogueAuthClassService;

    private final TCourseUserService courseUserService;


    public CourseServiceImpl(SceneDetailMapper sceneDetailMapper, CourseMapper courseMapper, ExerciseMapper exerciseMapper, KnowledgeMapper knowledgeMapper, TCourseUserService courseUserService, ExerciseKnowledgeMapper exerciseKnowledgeMapper, TCatalogueProcessService catalogueProcessService, SceneMapper sceneMapper, NoticesMapper noticesMapper, SclassMapper sclassMapper, TCourseCatalogueService courseCatalogueService, TCourseContentsService courseContentsService, TCatalogueAuthClassService catalogueAuthClassService, TCatalogueResourcesService catalogueResourcesService) {
        this.sceneDetailMapper = sceneDetailMapper;
        this.courseMapper = courseMapper;
        this.exerciseMapper = exerciseMapper;
        this.knowledgeMapper = knowledgeMapper;
        this.courseUserService = courseUserService;
        this.exerciseKnowledgeMapper = exerciseKnowledgeMapper;
        this.catalogueProcessService = catalogueProcessService;
        this.sceneMapper = sceneMapper;
        this.noticesMapper = noticesMapper;
        this.sclassMapper = sclassMapper;
        this.courseCatalogueService = courseCatalogueService;
        this.courseContentsService = courseContentsService;
        this.catalogueAuthClassService = catalogueAuthClassService;
        this.catalogueResourcesService = catalogueResourcesService;
    }

    @Override
    public List<Course> getCourseList(HttpServletRequest request, int type, int number) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return courseMapper.getCourseList(loginUser.getUserId(), type, number);
    }

    @Override
    public List<Course> getOtherPublishCourse(HttpServletRequest request) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return courseMapper.getOtherPublishCourse(loginUser.getUserId());
    }

    @Override
    public List<Course> getOwnCourse(HttpServletRequest request, PageTO pageTO) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 分页查询配置
        PageMethod.startPage(pageTO.getCurrent(), pageTO.getPageSize());
        return courseMapper.getOwnCourse(loginUser.getUserId());
    }

    @Override
    public List<Course> getCoursePublish(HttpServletRequest request, PageTO pageTO) {
        // 分页查询配置
        PageMethod.startPage(pageTO.getCurrent(), pageTO.getPageSize());
        return courseMapper.getCoursePublish();
    }

    @Override
    public Course getCourseDetail(int courseId) {
        Course course = courseMapper.getCourseByCourseId(courseId);
        List<Integer> teacherList = courseMapper.getCourseTeachers(courseId);
        int listSize = teacherList.size();
        if (listSize > 0) {
            int[] teachers = new int[listSize];
            for (int i = 0; i < listSize; i++) {
                teachers[i] = teacherList.get(i);
            }
            course.setTeachers(teachers);
        }
        return course;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Course updateCourse(HttpServletRequest request, Course course) {
        // 用户查询不
        UserInfo loginUser = Authentication.getCurrentUser(request);
        List<Course> oldCourseList = courseMapper.getCourseByCourseName(course.getCourseName().trim());
        // 课程id等于-1，新增课程，否则就是修改课程
        if (course.getCourseId() == -1) {//新增
            //名称重复抛出异常
            BusinessResponseEnum.DUMPLICATECOURSE.assertIsTrue(oldCourseList.isEmpty(), course.getCourseName().trim());
            //名称不重复
            course.setCreator(loginUser.getUserId());
            course.setCreateTime(TimeUtil.getDateTime());
            // 新增能课程
            courseMapper.addCourse(course);
            //创建第一目录
            createFirstCatalogue(request, course.getCourseId());
        } else {
            //名称重复抛出异常
            BusinessResponseEnum.DUMPLICATECOURSE
                    .assertIsTrue(oldCourseList.isEmpty() || duplicateId(oldCourseList, course.getCourseId()), course.getCourseName().trim());
            // 修改课程
            courseMapper.updateCourse(course);
            courseMapper.deleteUserCourse(course.getCourseId());
            if (course.getTeachers().length > 0) {
                courseMapper.addUserCourseArray(course.getCourseId(), course.getTeachers());
            }
        }
        return course;
    }

    private boolean duplicateId(List<Course> oldCourseList, int courseId) {
        for (Course course : oldCourseList) {
            if (courseId == course.getCourseId()) {
                return true;// 有相同的id则返回true
            }
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteCourse(int courseId, HttpServletRequest request) {
        List<Sclass> classList = sclassMapper.getStartSclassByCourseId(courseId);
        BusinessResponseEnum.UNCLASSBYCOURSE.assertIsEmpty(classList);
        // 删除目录资源表
        catalogueResourcesService.remove(new QueryWrapper<TCatalogueResources>().eq("course_id", courseId));
        // 删除学习进度表
        catalogueProcessService.remove(new QueryWrapper<TCatalogueProcess>().eq("course_id", courseId));
        // 删除内容表
        courseContentsService.remove(new QueryWrapper<TCourseContents>().eq("course_id", courseId));
        // 删除目录班级发布表
        catalogueAuthClassService.remove(new QueryWrapper<TCatalogueAuthClass>().eq("course_id", courseId));
        //删除课程人员中间表
        courseUserService.remove(new QueryWrapper<TCourseUser>().eq("course_id", courseId));
        // 删除课程目录
        courseCatalogueService.remove(new QueryWrapper<CourseCatalogue>().eq("course_id", courseId));
        // 删除课程
        return courseMapper.deleteCourse(courseId);
    }

    /**
     * @description: 课程发布状态修改
     * @author:
     * @date: 2023/1/6 9:43
     * @param: [request, course]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateIsOpen(HttpServletRequest request, Course course) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 发布课程
        Integer isOpen = courseMapper.updateIsOpen(course);
        // 发布和取消发布课程生成通知
        if (course.getIsOpen() == 1) {
            String noticeContent = Message.get("CourseReleased", course.getCourseName());
            Notice notice = new Notice(loginUser.getUserId(), TimeUtil.getDateTime(), noticeContent, loginUser.getRoleType());
            noticesMapper.addNotice(notice);
        }
        return isOpen;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copyCourse(HttpServletRequest request, int oldCourseId) throws InterruptedException {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 获取要复制的课程的信息
        Course course = courseMapper.getCourseByCourseId(oldCourseId);
        //课程id不能为空
        BusinessResponseEnum.UNCOURSE.assertNotNull(course, oldCourseId);
        // 获取课程的知识点、场景、习题、习题和知识点关联关系
        List<Knowledge> knowledgeList = knowledgeMapper.getKnowledge(oldCourseId);
        List<Scene> sceneList = sceneMapper.getSceneASC(oldCourseId);
        List<Exercise> exerciseList = exerciseMapper.getExerciseByCourseId(oldCourseId, 0, 1);
        List<ExerciseKnowledge> exerciseKnowledgeList = exerciseKnowledgeMapper.getExerciseKnowledgeByCourseId(oldCourseId);
        // 获取课程内容表、目录表、目录资源维度表
        List<CourseCatalogue> courseCataloguelist = courseCatalogueService.list(new QueryWrapper<CourseCatalogue>()
                .eq("delete_flag", 0)
                .eq("course_id", oldCourseId));
        List<TCourseContents> courseContentslist = courseContentsService.list(new QueryWrapper<TCourseContents>()
                .eq("course_id", oldCourseId));
        List<TCatalogueResources> catalogueResourceslist = catalogueResourcesService.list(new QueryWrapper<TCatalogueResources>()
                .eq("delete_flag", 0)
                .eq("delete_flag", 0)
                .eq("course_id", oldCourseId));
        // 定义知识点、场景、习题的id的转换map，key为被复制的id，value为复制的新的id
        Map<Integer, Integer> knowledgeIdConvertMap = new HashMap<>();
        Map<Integer, Integer> sceneIdConvertMap = new HashMap<>();
        Map<Integer, Integer> exerciseIdConvertMap = new HashMap<>();
        //定义课程目录id转换map
        Map<Integer, Integer> catalogueIdConvertMap = new HashMap<>();
        Map<Long, Long> catalogueResourcesIdConvertMap = new HashMap<>();

        // 复制课程基本信息
        while (true) {
            String newCourseName = course.getCourseName().trim() + "-" + TimeUtil.getDateTimeStr1();
            List<Course> oldCourseList = courseMapper.getCourseByCourseName(newCourseName);
            if (!oldCourseList.isEmpty()) {
                Thread.sleep(1000);
            } else {
                course.setCourseName(newCourseName);
                break;
            }
        }
        course.setCreator(loginUser.getUserId());
        course.setCreateTime(TimeUtil.getDateTime());
        course.setIsOpen(0);
        course.setParentId(oldCourseId);
        Integer copyCourse = courseMapper.addCopyCourse(course);
        // 复制课程目录
        if (courseCataloguelist != null && !courseCataloguelist.isEmpty()) {
            copyCatalogue(course, courseCataloguelist, catalogueIdConvertMap);
            // 复制目录资源表
            if (catalogueResourceslist != null && !catalogueResourceslist.isEmpty()) {
                copyCatalogueResourceslist(course, catalogueResourceslist, catalogueIdConvertMap, catalogueResourcesIdConvertMap);
            }
            // 复制内容表
            if (courseContentslist != null && !courseContentslist.isEmpty()) {
                copyContents(course, courseContentslist, catalogueIdConvertMap, catalogueResourcesIdConvertMap);
            }
        }
        // 复制课程的知识点到数据库
        if (knowledgeList != null && !knowledgeList.isEmpty()) {
            copyCourseKnowledge(course, knowledgeList, knowledgeIdConvertMap);
        }
        // 复制课程的场景到数据库
        if (sceneList != null && !sceneList.isEmpty()) {
            copyCourseScene(course.getCourseId(), sceneList, sceneIdConvertMap);
        }
        // 复制课程的习题到数据库
        if (exerciseList != null && !exerciseList.isEmpty()) {
            copyCourseExercise(loginUser.getUserId(), course.getCourseId(), exerciseList, sceneIdConvertMap, exerciseIdConvertMap);
        }
        // 复制课程的习题和知识点关联数据到数据库
        if (exerciseKnowledgeList != null && !exerciseKnowledgeList.isEmpty()) {
            copyCourseExerciseKnowledge(course.getCourseId(), exerciseKnowledgeList, knowledgeIdConvertMap, exerciseIdConvertMap);
        }
        return copyCourse;
    }

    /**
     * @description: 复制目录资源表，备份老新目录资源表id 键值对格式
     * @author:
     * @date: 2022/10/8 14:40
     * @param: [course, catalogueResourceslist, catalogueIdConvertMap, catalogueResourcesIdConvertMap]
     * @return: void
     **/
    private void copyCatalogueResourceslist(Course course, List<TCatalogueResources> catalogueResourceslist, Map<Integer, Integer> catalogueIdConvertMap, Map<Long, Long> catalogueResourcesIdConvertMap) {
        ArrayList<Long> oldIds = new ArrayList<>();
        SnowflakeIdWorker snowflakeIdWorker = new SnowflakeIdWorker(0, 0);
        List<TCatalogueResources> collect = catalogueResourceslist.stream().peek(item -> {
            oldIds.add(item.getId());
            item.setCourseId(course.getCourseId())
                    .setCatalogueId(catalogueIdConvertMap.get(item.getCatalogueId()))
                    .setId(snowflakeIdWorker.nextId());
        }).collect(Collectors.toList());
        boolean res = catalogueResourcesService.saveBatch(collect);
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res);
        for (int i = 0; i < collect.size(); i++) {
            //old课程目录id-》新的课程目录id 键值对格式存储
            catalogueResourcesIdConvertMap.put(oldIds.get(i), collect.get(i).getId());
        }
    }

    /**
     * @description: 复制内容（设置新的课程id，目录id，id置空）
     * @author:
     * @date: 2022/10/8 14:18
     * @param: [course, courseContentsList, catalogueIdConvertMap]
     * @return: void
     **/
    private void copyContents(Course course, List<TCourseContents> courseContentsList, Map<Integer, Integer> catalogueIdConvertMap, Map<Long, Long> catalogueResourcesIdConvertMap) {
        List<TCourseContents> collect = courseContentsList.stream()
                .map(item -> {
                    //目录资源表oldIds
                    Set<Long> keySet = catalogueResourcesIdConvertMap.keySet();
                    keySet.forEach(oldId -> {
                        if (item.getContents() != null && item.getContents().contains(oldId.toString())) {
                            //替换内容中old目录资源表id
                            item.setContents(item.getContents()
                                    .replaceAll(oldId.toString(), catalogueResourcesIdConvertMap.get(oldId).toString()));
                        }
                    });
                    //替换课程id，目录id
                    if (item.getContents() != null) {
                        String contents = item.getContents()
                                .replaceAll(String.format("course_id=%d", item.getCourseId()), String.format("course_id=%d", course.getCourseId()))
                                .replaceAll(String.format("catalogue_id=%d", item.getCatalogueId()), String.format("catalogue_id=%d", catalogueIdConvertMap.get(item.getCatalogueId())));
                        item.setContents(contents);
                    }
                    //设置课程id
                    item.setCourseId(course.getCourseId());
                    //设置目录id
                    item.setCatalogueId(catalogueIdConvertMap.get(item.getCatalogueId()));
                    //id置空
                    item.setId(null);
                    return item;
                }).collect(Collectors.toList());
        //新增目录资源表
        boolean res = courseContentsService.saveBatch(collect);
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res);
    }

    /**
     * @description: 复制课程目录表，并将课程目录的新老id存储到Map中
     * @author:
     * @date: 2022/10/8 14:11
     * @param: [course, courseCataloguelist, catalogueIdConvertMap]
     * @return: void
     **/
    private void copyCatalogue(Course course, List<CourseCatalogue> courseCataloguelist, Map<Integer, Integer> catalogueIdConvertMap) {
        ArrayList<Integer> oldIds = new ArrayList<>();
        List<CourseCatalogue> newCourseCataloguelist = courseCataloguelist.stream().map(item -> {
            //old目录id
            oldIds.add(item.getId());
            //设置新的课程id
            item.setCourseId(course.getCourseId()).setId(null);
            return item;
        }).collect(Collectors.toList());
        //保存新的课程目录
        boolean saveRes = courseCatalogueService.saveBatch(newCourseCataloguelist);
        //保存校验
        BusinessResponseEnum.FAILCOPYCOURSE.assertIsTrue(saveRes);
        for (int i = 0; i < newCourseCataloguelist.size(); i++) {
            //old课程目录id-》新的课程目录id 键值对格式存储
            catalogueIdConvertMap.put(oldIds.get(i), newCourseCataloguelist.get(i).getId());
        }
        List<CourseCatalogue> collect = newCourseCataloguelist.stream().map(item -> {
            if (item.getParentId() != 0) {
                item.setParentId(catalogueIdConvertMap.get(item.getParentId()));
            }
            return item;
        }).collect(Collectors.toList());
        //更新父类id
        boolean res = courseCatalogueService.saveOrUpdateBatch(collect);
        //更新校验
        BusinessResponseEnum.FAILCOPYCOURSE.assertIsTrue(res);
    }


    private void createFirstCatalogue(HttpServletRequest request, int courseId) {
        // 添加课程目录
        CourseCatalogue catalogue = new CourseCatalogue();
        catalogue.setCourseId(courseId);
        catalogue.setParentId(0);
        catalogue.setCatalogueName("新建课程目录");
        catalogue.setDeleteFlag(0);
        courseCatalogueService.saveCatalogue(request, catalogue);
    }

    private void copyCourseKnowledge(Course course, List<Knowledge> knowledgeList, Map<Integer, Integer> knowledgeIdConvertMap) {
        // 新复制的课程id
        int newCourseId = course.getCourseId();
        // 复制知识点到数据库
        for (int i = 0; i < knowledgeList.size(); i++) {
            Knowledge knowledge = knowledgeList.get(i);
            int oldKnowledgeId = knowledge.getKnowledgeId();
            knowledge.setCourseId(newCourseId);
            knowledgeMapper.addKnowledge(knowledge);
            // 知识点id转换放到map中
            knowledgeIdConvertMap.put(oldKnowledgeId, knowledge.getKnowledgeId());
        }
        // 修改新复制的课程的知识树json串中的知识点的id为新复制的知识点的id
        Gson gson = new Gson();
        KnowledgeTreeTO knowledgeTree = gson.fromJson(course.getKnowledgeTree(), KnowledgeTreeTO.class);
        knowledgeTree.setId(knowledgeIdConvertMap.get(knowledgeTree.getId()));
        if (knowledgeTree.getChildren() != null && !knowledgeTree.getChildren().isEmpty()) {
            // 用递归转换知识树中知识点的id为新复制后的id
            convertKnowledgeTreeId(knowledgeIdConvertMap, knowledgeTree.getChildren());
        }
        String knowledgeTreeStr = gson.toJson(knowledgeTree);
        Course newCourse = new Course();
        newCourse.setCourseId(newCourseId);
        newCourse.setKnowledgeTree(knowledgeTreeStr);
        courseMapper.updateCourse(newCourse);
        // 修改新复制的知识点的上级id为新复制的知识点的id
        List<Knowledge> newKnowledgeList = knowledgeMapper.getKnowledge(newCourseId);
        for (int i = 0; i < newKnowledgeList.size(); i++) {
            if (newKnowledgeList.get(i).getParentId() != 0) {
                Knowledge knowledge = new Knowledge();
                knowledge.setKnowledgeId(newKnowledgeList.get(i).getKnowledgeId());
                knowledge.setParentId(knowledgeIdConvertMap.get(newKnowledgeList.get(i).getParentId()));
                knowledgeMapper.updateKnowledge(knowledge);
            }
        }
    }

    private void convertKnowledgeTreeId(Map<Integer, Integer> knowledgeIdConvertMap, List<KnowledgeTreeTO> treeDataList) {
        for (int i = 0; i < treeDataList.size(); i++) {
            KnowledgeTreeTO treeData = treeDataList.get(i);
            treeData.setId(knowledgeIdConvertMap.get(treeData.getId()));
            if (treeData.getChildren() != null && !treeData.getChildren().isEmpty()) {
                convertKnowledgeTreeId(knowledgeIdConvertMap, treeData.getChildren());
            }
        }
    }

    private void copyCourseScene(int newCourseId, List<Scene> sceneList, Map<Integer, Integer> sceneIdConvertMap) {
        for (int i = 0; i < sceneList.size(); i++) {
            Scene scene = sceneList.get(i);
            int oldSceneId = scene.getSceneId();
            List<SceneDetail> sceneDetailList = sceneDetailMapper.getSceneDetailById(oldSceneId);
            scene.setCourseId(newCourseId);
            scene.setParentId(oldSceneId);
            sceneMapper.addCopyScene(scene);
            // 场景id转换放到map中
            sceneIdConvertMap.put(oldSceneId, scene.getSceneId());
            if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
                sceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
            }
        }
    }

    private void copyCourseExercise(int userId, int newCourseId, List<Exercise> exerciseList, Map<Integer, Integer> sceneIdConvertMap, Map<Integer, Integer> exerciseIdConvertMap) {
        for (int i = 0; i < exerciseList.size(); i++) {
            Exercise exercise = exerciseList.get(i);
            int oldExercise = exercise.getExerciseId();
            // 习题的场景id不等于-1说明有场景，否则没有场景
            if (exercise.getSceneId() != -1) {
                if (null == sceneIdConvertMap.get(exercise.getSceneId())) {
                    continue;
                }
                exercise.setSceneId(sceneIdConvertMap.get(exercise.getSceneId()));
            }
            exercise.setCourseId(newCourseId);
            exercise.setCreateTime(TimeUtil.getDateTime());
            exercise.setCreator(userId);
            exercise.setParentId(oldExercise);
            exerciseMapper.addCopyExercise(exercise);
            // 习题id转换放到map中
            exerciseIdConvertMap.put(oldExercise, exercise.getExerciseId());
        }
    }

    private void copyCourseExerciseKnowledge(int newCourseId, List<ExerciseKnowledge> exerciseKnowledgeList, Map<Integer, Integer> knowledgeIdConvertMap, Map<Integer, Integer> exerciseIdConvertMap) {
        List<ExerciseKnowledge> newList = new ArrayList<>();
        for (int i = 0; i < exerciseKnowledgeList.size(); i++) {
            ExerciseKnowledge exerciseKnowledge = exerciseKnowledgeList.get(i);
            if (null == knowledgeIdConvertMap.get(exerciseKnowledge.getKnowledgeId())
                    || null == exerciseIdConvertMap.get(exerciseKnowledge.getExerciseId())) {
                continue;
            }
            exerciseKnowledge.setKnowledgeId(knowledgeIdConvertMap.get(exerciseKnowledge.getKnowledgeId()));
            exerciseKnowledge.setExerciseId(exerciseIdConvertMap.get(exerciseKnowledge.getExerciseId()));
            exerciseKnowledge.setCourseId(newCourseId);
            newList.add(exerciseKnowledge);
        }
        if (!newList.isEmpty()) {
            exerciseKnowledgeMapper.addExerciseKnowledgeList(newList);
        }
    }


    @Override
    public List<String> getCourseCoverImageList(HttpServletRequest request) {
        List<String> courseCoverImageList = new ArrayList<>();
        String projectPath = request.getSession().getServletContext().getRealPath("/");
        logger.info("projectPath = " + projectPath);
        // 获得指定文件对象
        File file = new File(projectPath + "cover");
        // 获得该文件夹内的所有文件
        File[] fileArray = file.listFiles();
        for (int i = 0; i < Objects.requireNonNull(fileArray).length; i++) {
            // 验证是否是文件
            if (fileArray[i].isFile()) {
                courseCoverImageList.add("/cover/" + fileArray[i].getName());
            }
        }
        return courseCoverImageList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer courseAddTeacher(Course course) {
        courseMapper.deleteUserCourse(course.getCourseId());
        return courseMapper.addUserCourseArray(course.getCourseId(), course.getTeachers());
    }

    @Override
    public void addFirstCatalogue() {
        //查询所有没有目录的课程
        List<Course> courses = this.list(new QueryWrapper<Course>().eq("delete_flag", 0));
        if (courses != null && !courses.isEmpty()) {
            courses.forEach(item -> {
                List<CourseCatalogue> catalogues = courseCatalogueService
                        .list(new QueryWrapper<CourseCatalogue>()
                                .eq("course_id", item.getCourseId())
                                .eq("delete_flag", 0));
                if (catalogues == null || catalogues.isEmpty()) {
                    // 添加课程目录
                    CourseCatalogue catalogue = new CourseCatalogue();
                    catalogue.setCourseId(item.getCourseId());
                    catalogue.setParentId(0);
                    catalogue.setCatalogueName("新建课程目录");
                    catalogue.setSortNum(1);
                    catalogue.setDeleteFlag(0);
                    catalogue.setCreateTime(new Date());
                    boolean catalogueRes = courseCatalogueService.save(catalogue);
                    BusinessResponseEnum.SAVEFAIL.assertIsTrue(catalogueRes);
                    TCourseContents tCourseContents = new TCourseContents();
                    tCourseContents.setCreateTime(new Date());
                    tCourseContents.setTabNum(1);
                    tCourseContents.setCatalogueId(catalogue.getId());
                    tCourseContents.setCourseId(item.getCourseId());
                    boolean contentsRes = courseContentsService.save(tCourseContents);
                    BusinessResponseEnum.SAVEFAIL.assertIsTrue(contentsRes);
                    logger.info("新增目录{}", catalogue.toString());
                }
            });
        }
    }
}
