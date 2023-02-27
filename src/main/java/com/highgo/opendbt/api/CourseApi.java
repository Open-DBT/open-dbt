package com.highgo.opendbt.api;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.CourseKnowledgeTO;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.course.domain.model.*;
import com.highgo.opendbt.course.service.CourseService;
import com.highgo.opendbt.course.service.ExerciseService;
import com.highgo.opendbt.course.service.KnowledgeService;
import com.highgo.opendbt.course.service.SceneService;
import com.highgo.opendbt.teacher.domain.model.ExamExerciseForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 课程相关接口
 * @Title: CourseApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
@Api(tags = "课程相关接口")
@RestController
@CrossOrigin
@RequestMapping("/course")
public class CourseApi {

    Logger logger = LoggerFactory.getLogger(getClass());
    private final CourseService courseService;
    private final ExerciseService exerciseService;
    private final KnowledgeService knowledgeService;
    private final SceneService sceneService;

    public CourseApi(CourseService courseService, ExerciseService exerciseService, KnowledgeService knowledgeService, SceneService sceneService) {
        this.courseService = courseService;
        this.exerciseService = exerciseService;
        this.knowledgeService = knowledgeService;
        this.sceneService = sceneService;
    }

    /**
     * 查询课程基本信息列表
     *
     * @param request
     * @param type    查询类型，0为查询自己的课程，1为查询发布的课程
     * @param number  查询个数，0为查询全部
     * @return
     */
    @ApiOperation(value = "查询课程基本信息列表")
    @PostMapping("/getCourseList/{type}/{number}")
    public List<Course> getCourseList(HttpServletRequest request, @ApiParam(value = "查询类型，0为查询自己的课程，1为查询发布的课程", required = true) @PathVariable("type") int type, @ApiParam(value = "查询个数，0为查询全部", required = true) @PathVariable("number") int number) throws Exception {
        logger.debug("Enter, type = " + type + ", number = " + number);
        return courseService.getCourseList(request, type, number);
    }

    /**
     * 查询其他人公开的课程基本信息列表
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "查询其他人公开的课程基本信息列表")
    @RequestMapping("/getOtherPublishCourse")
    public List<Course> getOtherPublishCourse(HttpServletRequest request) {
        logger.debug("Enter, ");
        return courseService.getOtherPublishCourse(request);
    }

    /**
     * 查询用户自己拥有的课程(分页)
     *
     * @param request
     * @param pageTO  分页参数
     * @return
     */
    @ApiOperation(value = "查询用户自己拥有的课程(分页)")
    @PostMapping("/getOwnCourse")
    public List<Course> getOwnCourse(HttpServletRequest request, @RequestBody PageTO pageTO) {
        logger.debug("Enter, pageTO = " + pageTO.toString());
        return courseService.getOwnCourse(request, pageTO);
    }

    /**
     * 查询所有人发布的课程(分页)
     *
     * @param request
     * @param pageTO  分页参数
     * @return
     */
    @ApiOperation(value = "查询所有人发布的课程(分页)")
    @PostMapping("/getCoursePublish")
    public List<Course> getCoursePublish(HttpServletRequest request, @RequestBody PageTO pageTO) {
        logger.debug("Enter, pageTO = " + pageTO.toString());
        return courseService.getCoursePublish(request, pageTO);
    }

    /**
     * 根据课程id获取课程信息
     *
     * @param courseId
     * @return
     */
    @ApiOperation(value = "根据课程id获取课程信息")
    @GetMapping("/getCourseDetail/{courseId}")
    public Course getCourseDetail(@ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) {
        logger.debug("Enter, courseId = " + courseId);
        return courseService.getCourseDetail(courseId);
    }

    /**
     * 新增和修改课程
     *
     * @param request
     * @param course
     * @return
     */
    @ApiOperation(value = "新增和修改课程")
    @PostMapping("/updateCourse")
    public Course updateCourse(HttpServletRequest request, @RequestBody Course course) {
        logger.debug("Enter, course = " + course.toString());
        return courseService.updateCourse(request, course);
    }

    /**
     * 删除课程
     *
     * @param courseId
     * @return
     */
    @ApiOperation(value = "删除课程")
    @GetMapping("/deleteCourse/{courseId}")
    public Integer deleteCourse(@ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, HttpServletRequest request) {
        logger.debug("Enter, courseId = " + courseId);
        return courseService.deleteCourse(courseId, request);
    }

    /**
     * 修改课程是否发布
     *
     * @param request
     * @param course
     * @return
     */
    @ApiOperation(value = "修改课程是否发布")
    @PostMapping("/updateIsOpen")
    public Integer updateIsOpen(HttpServletRequest request, @RequestBody Course course) {
        logger.debug("Enter, course = " + course.toString());
        return courseService.updateIsOpen(request, course);
    }

    /**
     * 复制课程
     *
     * @param request
     * @param courseId
     * @return
     */
    @ApiOperation(value = "复制课程")
    @GetMapping("/copyCourse/{courseId}")
    public Integer copyCourse(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) throws InterruptedException {
        logger.info("Enter, courseId = " + courseId);
        return courseService.copyCourse(request, courseId);
    }

    /**
     * 模糊查询课程的所有习题
     *
     * @param exercisePage 模糊查询参数
     * @return
     */
    @ApiOperation(value = "模糊查询课程的所有习题")
    @PostMapping("/getExerciseList")
    public List<Exercise> getExerciseList(@RequestBody ExercisePage exercisePage) {
        logger.info("Enter, exercisePage = " + exercisePage.toString());
        return exerciseService.getExerciseList(exercisePage);
    }

    /**
     * 根据课程id查询全部习题
     *
     * @param courseId
     * @return
     */
    @ApiOperation(value = "根据课程id查询全部习题")
    @GetMapping("/getExerciseListByCourseId/{courseId}")
    public List<Exercise> getExerciseListByCourseId(@ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) {
        logger.debug("Enter, courseId = " + courseId);
        return exerciseService.getExerciseListByCourseId(courseId);
    }

    /**
     * 根据课程分页获取习题
     *
     * @param exercisePage 分页和模糊查询参数
     * @return
     */
    @ApiOperation(value = "根据课程分页获取习题")
    @PostMapping("/getExercise")
    public PageInfo<Exercise> getExercise(@RequestBody ExercisePage exercisePage) {
        logger.info("Enter, exercisePage = " + exercisePage.toString());
        return exerciseService.getExercise(exercisePage);
    }

    /**
     * 根据习题ID查询习题
     *
     * @param exerciseId
     * @return
     */
    @ApiOperation(value = "根据习题ID查询习题")
    @GetMapping("/getExerciseById/{exerciseId}")
    public Exercise getExerciseById(@ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId) {
        return exerciseService.getExerciseById(exerciseId);
    }

    /**
     * 新增和修改习题
     *
     * @param request
     * @param exercise
     * @return
     */
    @ApiOperation(value = "新增和修改习题")
    @PostMapping("/updateExercise")
    public Integer updateExercise(HttpServletRequest request, @RequestBody Exercise exercise) {
        logger.debug("Enter, exercise = " + exercise.toString());
        return exerciseService.updateExercise(request, exercise);
    }

    /**
     * 删除习题
     *
     * @param request
     * @param exerciseId
     * @return
     */
    @ApiOperation(value = "删除习题")
    @GetMapping("/deleteExercise/{exerciseId}")
    public Integer deleteExercise(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId) {
        logger.debug("Enter, exerciseId = " + exerciseId);
        return exerciseService.deleteExercise(request, exerciseId);
    }

    /**
     * 批量删除习题
     *
     * @param request
     * @param examExerciseForm
     * @return
     */
    @ApiOperation(value = "批量删除习题")
    @PostMapping("/batchDeleteExercise")
    public Integer batchDeleteExercise(HttpServletRequest request, @RequestBody ExamExerciseForm examExerciseForm) {
        logger.debug("Enter, exerciseIds = " + examExerciseForm);
        return exerciseService.batchDeleteExercise(request, examExerciseForm.getExerciseIds());
    }

    /**
     * 习题批量绑定场景
     *
     * @param sceneId
     * @param examExerciseForm
     * @return
     */
    @ApiOperation(value = "习题批量绑定场景")
    @PostMapping("/batchBuildScene/{sceneId}")
    public Integer batchBuildScene(@ApiParam(value = "场景id", required = true) @PathVariable("sceneId") int sceneId, @RequestBody ExamExerciseForm examExerciseForm) {
        logger.debug("Enter, exerciseIds = " + examExerciseForm);
        return exerciseService.batchBuildScene(sceneId, examExerciseForm.getExerciseIds());
    }

    /**
     * 复制习题
     *
     * @param exerciseId
     * @return
     */
    @ApiOperation(value = "复制习题")
    @GetMapping("/copyExercise/{exerciseId}")
    public Integer copyExercise(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId) {
        logger.debug("Enter, exerciseId = " + exerciseId);
        return exerciseService.copyExercise(request, exerciseId);
    }

    /**
     * 复制习题到我的课程下
     *
     * @param exerciseId
     * @param courseId
     * @return
     */
    @ApiOperation(value = "复制习题到我的课程下")
    @GetMapping("/copyExerciseToMyCourse/{exerciseId}/{courseId}")
    public Integer copyExerciseToMyCourse(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) {
        return exerciseService.copyExerciseToMyCourse(request, exerciseId, courseId);
    }

    /**
     * 根据获取习题信息列表以及知识点和学生最新一次答题的答案
     *
     * @param request
     * @param sclassId    班级id
     * @param courseId    课程id
     * @param knowledgeId 知识点id
     * @return
     */
    @ApiOperation(value = "根据获取习题信息列表以及知识点和学生最新一次答题的答案")
    @GetMapping("/getExerciseInfoList/{sclassId}/{courseId}/{knowledgeId}")
    public List<Exercise> getExerciseInfoList(HttpServletRequest request, @ApiParam(value = "班级id", required = true) @PathVariable("sclassId") int sclassId, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "知识点Id", required = true) @PathVariable("knowledgeId") int knowledgeId) {
        logger.info("Enter, sclassId = " + sclassId + ", courseId = " + courseId + ", knowledgeId = " + knowledgeId);
        return exerciseService.getExerciseInfoList(request, sclassId, courseId, knowledgeId);
    }

    /**
     * 根据获取习题信息列表以及知识点和学生最新一次答题的答案
     *
     * @param request
     * @param sclassId   班级id
     * @param courseId   课程id
     * @param exerciseId 习题id
     * @return
     */
    @ApiOperation(value = "根据获取习题信息列表以及知识点和学生最新一次答题的答案")
    @GetMapping("/getExerciseInfo/{sclassId}/{courseId}/{exerciseId}")
    public ExerciseDisplay getExerciseInfo(HttpServletRequest request, @PathVariable("sclassId") int sclassId, @PathVariable("courseId") int courseId, @PathVariable("exerciseId") int exerciseId) {
        logger.info("Enter, sclassId = " + sclassId + ", courseId = " + courseId + ", exerciseId = " + exerciseId);
        return exerciseService.getExerciseInfo(request, sclassId, courseId, exerciseId);
    }

    /**
     * 测试运行习题参考答案
     *
     * @param request
     * @param exercise
     * @return
     */
    @RequestMapping("/testRunAnswer")
    public Map<String, Object> testRunAnswer(HttpServletRequest request, @RequestBody Exercise exercise) {
        logger.info("Enter, exercise = " + exercise.toString());
        return exerciseService.testRunAnswer(request, exercise);
    }

    /**
     * 根据课程id获取知识点，树结构数据
     *
     * @param courseId
     * @return
     */
    @RequestMapping("/getKnowledge/{courseId}")
    public String getKnowledge(@PathVariable("courseId") int courseId) {
        logger.debug("Enter, courseId = " + courseId);
        return knowledgeService.getKnowledge(courseId);
    }

    /**
     * 获取所有的知识点（不是树结构），用于习题模糊查询，知识点的下拉
     *
     * @param courseId
     * @return
     */
    @RequestMapping("/getKnowledgeNotTree/{courseId}")
    public List<Knowledge> getKnowledgeNotTree(@PathVariable("courseId") int courseId) {
        logger.debug("Enter, courseId = " + courseId);
        return knowledgeService.getKnowledgeNotTree(courseId);
    }

    /**
     * 新增和修改知识点
     *
     * @param request
     * @param courseKnowledge
     * @return
     */
    @RequestMapping("/updateKnowledge")
    public Integer updateKnowledge(HttpServletRequest request, @RequestBody CourseKnowledgeTO courseKnowledge) {
        logger.info("Enter, updateKnowledge courseId = " + courseKnowledge.getCourseId());
        return knowledgeService.updateKnowledge(request, courseKnowledge);
    }

    /**
     * 根据课程id分页获取场景
     *
     * @param scenePage
     * @return
     */
    @RequestMapping("/getScene")
    public PageInfo<Scene> getScene(@RequestBody ScenePage scenePage) {
        logger.debug("Enter, pageTO = " + scenePage.toString());
        return sceneService.getScene(scenePage);
    }

    /**
     * 根据课程id获取所有场景
     *
     * @param courseId
     * @return
     */
    @RequestMapping("/getShareScene/{courseId}")
    public List<Scene> getShareScene(@PathVariable("courseId") int courseId) {
        logger.debug("Enter courseId = ," + courseId);
        return sceneService.getShareScene(courseId);
    }

    /**
     * 根据场景id获取场景信息
     *
     * @param sceneId
     * @return
     */
    @RequestMapping("/getSceneDetail/{sceneId}")
    public Scene getSceneDetail(@PathVariable("sceneId") int sceneId) {
        logger.debug("Enter, sceneId = " + sceneId);
        return sceneService.getSceneDetail(sceneId);
    }

    /**
     * 添加和修改场景
     *
     * @param request
     * @param scene
     * @return
     */
    @RequestMapping("/updateScene")
    public Integer updateScene(HttpServletRequest request, @RequestBody Scene scene) {
        logger.info("Enter, scene = " + scene.toString());
        return sceneService.updateScene(request, scene);
    }

    /**
     * 删除场景
     *
     * @param request
     * @param sceneId
     * @return
     */
    @RequestMapping("/deleteScene/{sceneId}")
    public Integer deleteScene(HttpServletRequest request, @PathVariable("sceneId") int sceneId) {
        logger.debug("Enter, sceneId = " + sceneId);
        return sceneService.deleteScene(request, sceneId);
    }

    /**
     * 复制场景
     *
     * @param sceneId
     * @return
     */
    @RequestMapping("/copyScene/{sceneId}")
    public Integer copyScene(@PathVariable("sceneId") int sceneId) {
        logger.debug("Enter, sceneId = " + sceneId);
        return sceneService.copyScene(sceneId);
    }

    /**
     * 复制场景到我的课程
     *
     * @param sceneId
     * @return
     */
    @RequestMapping("/copySceneToMyCourse/{sceneId}/{courseId}")
    public Integer copySceneToMyCourse(@PathVariable("sceneId") int sceneId, @PathVariable("courseId") int courseId) {
        return sceneService.copySceneToMyCourse(sceneId, courseId);
    }

    /**
     * 测试场景脚本
     *
     * @param scene
     * @return
     */
    @RequestMapping("/testSceneSQLShell")
    public Integer testSceneSQLShell(HttpServletRequest request, @RequestBody Scene scene) {
        logger.debug("Enter, initShell = " + scene.getInitShell());
        return sceneService.testSceneSQLShell(request, scene.getInitShell());
    }

    /**
     * 获取课程封面列表
     *
     * @param request
     * @return
     */
    @RequestMapping("/getCourseCoverImageList")
    public List<String> getCourseCoverImageList(HttpServletRequest request) {
        logger.debug("Enter, ");
        return courseService.getCourseCoverImageList(request);
    }

    /**
     * 课程添加助教
     *
     * @param course
     * @return
     */
    @RequestMapping("/courseAddTeacher")
    public Integer courseAddTeacher(@RequestBody Course course) {
        logger.debug("Enter, ");
        return courseService.courseAddTeacher(course);
    }

    /**
     * 导出习题
     *
     * @param request
     * @param exercisePage
     * @return
     */
    @RequestMapping("/exportExerciseList")
    public String exportExerciseList(HttpServletRequest request, @RequestBody ExercisePage exercisePage) throws Exception {
        logger.info("Enter, exercisePage = " + exercisePage.toString());
        return exerciseService.exportExerciseList(request, exercisePage);
    }

    /**
     * 上传导入习题文件
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping("/uploadExerciseListFile")
    public String uploadExerciseListFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        logger.info("Enter, ");
        return exerciseService.uploadExerciseListFile(request, file);
    }

    /**
     * 导入习题
     *
     * @param request
     * @param importExerciseTO
     * @return
     */
    @RequestMapping("/importExercise")
    public String importExercise(HttpServletRequest request, @RequestBody ImportExerciseTO importExerciseTO) {
        logger.info("Enter, ");
        return exerciseService.importExercise(request, importExerciseTO);
    }

    /**
     * 导出场景
     *
     * @param request
     * @param courseId
     * @return
     */
    @RequestMapping("/exportSceneList/{courseId}")
    public String exportSceneList(HttpServletRequest request, @PathVariable("courseId") int courseId) {
        return sceneService.exportSceneList(request, courseId);
    }

    @RequestMapping("/exportSceneById/{sceneId}")
    public String exportSceneById(HttpServletRequest request, @PathVariable("sceneId") int sceneId) {
        return sceneService.exportSceneById(request, sceneId);
    }

    /**
     * 上传导入场景文件
     *
     * @param request
     * @param file
     * @return
     */
    @RequestMapping("/uploadSceneListFile")
    public String uploadSceneListFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        logger.info("Enter, ");
        return sceneService.uploadSceneListFile(request, file);
    }

    /**
     * 导入场景
     *
     * @param request
     * @param importExerciseTO
     * @return
     */
    @RequestMapping("/importScene")
    public String importScene(HttpServletRequest request, @RequestBody ImportExerciseTO importExerciseTO) {
        logger.info("Enter, course id = " + importExerciseTO.getCourseId());
        return sceneService.importScene(request, importExerciseTO);
    }

    /**
     * @description: 初始化历史课程第一层目录
     * @author:
     * @date: 2022/10/14 13:55
     * @param: []
     * @return: void
     **/
    @ApiOperation(value = "初始化历史课程第一层目录")
    @GetMapping("/addFirstCatalogue")
    public void addFirstCatalogue() {
        logger.info("Enter, 只能用于第一次初始化");
        courseService.addFirstCatalogue();
    }

}
