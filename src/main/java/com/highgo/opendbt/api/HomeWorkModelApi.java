package com.highgo.opendbt.api;

import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.common.utils.ValidationUtil;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.TNewExerciseDTO;
import com.highgo.opendbt.homework.domain.model.PublishHomeWork;
import com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel;
import com.highgo.opendbt.homeworkmodel.domain.model.*;
import com.highgo.opendbt.homeworkmodel.service.THomeworkModelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Description: 作业模板相关接口
 * @Title: HomeWorkModel
 * @Package com.highgo.opendbt.api
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@Api(tags = "作业模板相关接口")
@RestController
@RequestMapping("/homeWorkModel")
@CrossOrigin
@RequiredArgsConstructor
public class HomeWorkModelApi {
    Logger logger = LoggerFactory.getLogger(getClass());
    final
    THomeworkModelService homeworkModelService;


    /**
     * @description: 作业模板列表
     * @author:
     * @date: 2022/9/6 10:38
     * @param: [request, param, result]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    @ApiOperation(value = "获取作业模板列表", notes = "根据课程id查询作业模板")
    @PostMapping("/getHomeWorkModel")
    public Map<String, Object> getHomeWorkModel(HttpServletRequest request, @RequestBody @Valid PageParam<ListHomeWorkModel> param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return homeworkModelService.getHomeWorkModel(request, param);
    }

    /**
     * @description: 保存文件夹
     * @author:
     * @date: 2022/9/6 11:07
     * @param: [request, param, result]
     * @return: boolean
     **/
    @ApiOperation(value = "保存文件夹")
    @PostMapping("/saveHomeWorkModelFolder")
    public boolean saveHomeWorkModelFolder(HttpServletRequest request, @RequestBody @Valid SaveHomeWorkModelFolder param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return homeworkModelService.saveHomeWorkModelFolder(request, param);
    }

    /**
     * @description: 删除
     * @author:
     * @date: 2022/9/6 13:42
     * @param: [request, id]
     * @return: boolean
     **/
    @ApiOperation(value = "删除文件夹/作业模板")
    @GetMapping("/delHomeWorkModel/{id}")
    public boolean delHomeWorkModel(HttpServletRequest request, @ApiParam(value = "文件夹/作业模板id", required = true) @PathVariable("id") int id) {
        logger.debug("Enter,id={}", id);
        return homeworkModelService.delHomeWorkModel(request, id);
    }

    /**
     * @description: 发布
     * @author:
     * @date: 2022/9/7 19:15
     * @param: [request, param, result]
     * @return: boolean
     **/
    @ApiOperation(value = "发布")
    @PostMapping("/publishHomeWork")
    public boolean publishHomeWork(HttpServletRequest request, @RequestBody @Valid PublishHomeWork param, BindingResult result) {
        logger.debug("Enter,param=" + param.toString());
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return homeworkModelService.publishHomeWork(request, param);
    }


    /**
     * @description: 复制作业模板
     * @author:
     * @date: 2022/9/8 17:41
     * @param: [request, id]
     * @return: com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel
     **/
    @ApiOperation(value = "复制作业模板")
    @GetMapping("/copyHomeWorkModel/{id}")
    public THomeworkModel copyHomeWorkModel(HttpServletRequest request, @ApiParam(value = "作业模板id", required = true) @PathVariable("id") int id) {
        logger.debug("Enter,id={}", id);
        return homeworkModelService.copyHomeWorkModel(request, id);
    }

    /**
     * @description: 根据作业模板id查询习题列表
     * @author:
     * @date: 2022/9/9 11:07
     * @param: [request, id 作业模板id,flag 0:左侧列表 1：详情列表]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    @ApiOperation(value = "根据作业模板id查询习题列表")
    @GetMapping("/getExercisesByModelId/{id}/{flag}")
    public SaveHomeWorkModel getExercisesByModelId(HttpServletRequest request, @ApiParam(value = "作业模板id", required = true) @PathVariable("id") int id, @ApiParam(value = "标记0左侧列表1：详情列表", required = true) @PathVariable("flag") int flag) {
        logger.debug("Enter,id={},flag={}", id, flag);
        return homeworkModelService.getExercisesByModelId(request, id, flag);
    }

    @ApiOperation(value = "根据模板id习题id查询习题详情")
    @GetMapping("/getExerciseInfoByModel/{exerciseId}/{modelId}")
    public TNewExercise getExerciseInfo(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId, @ApiParam(value = "模板id", required = true) @PathVariable("modelId") int modelId) {
        logger.debug("Enter,exerciseId={}，modelId={}", exerciseId, modelId);
        return homeworkModelService.getExerciseInfo(request, exerciseId, modelId);
    }

    @ApiOperation(value = "选题列表查询")
    @PostMapping("/getHomeWorkModelExercises")
    public Map<String, Object> getHomeWorkModelExercises(HttpServletRequest request, @RequestBody @Valid PageParam<TNewExerciseDTO> param, BindingResult result) {
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return homeworkModelService.getHomeWorkModelExercises(request, param);
    }

    @ApiOperation(value = "选题完成")
    @PostMapping("/completedSelectedExercises")
    public THomeworkModel completedSelectedExercises(HttpServletRequest request, @RequestBody @Valid SelectedExercisesDTO param, BindingResult result) {
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return homeworkModelService.completedSelectedExercises(request, param);
    }

    @ApiOperation(value = "删除选题")
    @GetMapping("/delSelectedExercises/{modelId}/{exerciseId}")
    public boolean delSelectedExercises(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId, @ApiParam(value = "模板id", required = true) @PathVariable("modelId") int modelId) {
        logger.debug("Enter,modelId={},exerciseId={}", modelId, exerciseId);
        return homeworkModelService.delSelectedExercises(request, modelId, exerciseId);
    }

    @ApiOperation(value = "模板中习题保存")
    @PostMapping("/saveExerciseInfoByModel")
    public TNewExercise saveExerciseInfoByModel(HttpServletRequest request, @RequestBody EditNewExerciseDTO param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return homeworkModelService.saveExercise(request, param);
    }

    @ApiOperation(value = "模板中习题分数修改")
    @GetMapping("/updateScoreByModel/{exerciseId}/{modelId}/{exerciseScore}")
    public boolean updateScoreByModel(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId, @ApiParam(value = "模板id", required = true) @PathVariable("modelId") int modelId, @ApiParam(value = "习题分数", required = true) @PathVariable("exerciseScore") Double exerciseScore) {
        logger.debug("Enter,exerciseId={}，modelId={}，exerciseScore={}", exerciseId, modelId, exerciseScore);
        return homeworkModelService.updateScoreByModel(request, exerciseId, modelId, exerciseScore);
    }

    /**
     * @description: 作业库目录树查询
     * @author:
     * @date: 2022/11/23 13:28
     * @param: [request, course_id 课程id]
     * @return: java.util.List<com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel>
     **/
    @ApiOperation(value = "作业库目录树查询")
    @GetMapping("/getHomeWorkModelCatalogueTree/{courseId}")
    public List<THomeworkModel> getHomeWorkModelCatalogueTree(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("courseId") Integer courseId) {
        //校验
        logger.debug("Enter,courseId={}", courseId);
        return homeworkModelService.getHomeWorkModelCatalogueTree(request, courseId);
    }

    /**
     * @description: 作业模板移动
     * @author:
     * @date: 2022/8/31 16:39
     * @param: [request, oid 原始id, tid 目标id]
     * @return: boolean
     **/
    @ApiOperation(value = "作业模板移动")
    @GetMapping("/moveHomeWorkModel/{oid}/{tid}")
    public boolean moveHomeWorkModel(HttpServletRequest request, @ApiParam(value = "原始id", required = true) @PathVariable("oid") int oid, @ApiParam(value = "目标id", required = true) @PathVariable("tid") int tid) {
        logger.debug("Enter, oid={},tid={}", oid, tid);
        return homeworkModelService.moveHomeWorkModel(request, oid, tid);
    }

    @ApiOperation(value = "发布班级学生列表")
    @GetMapping("/publishList/{courseId}")
    public List<PublishClass> publishList(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) {
        logger.debug("Enter, courseId={}", courseId);
        return homeworkModelService.publishList(request, courseId);
    }
}
