package com.highgo.opendbt.api;

import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.common.utils.ValidationUtil;
import com.highgo.opendbt.exercise.domain.entity.TExerciseType;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.*;
import com.highgo.opendbt.exercise.service.TNewExerciseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Description: 习题相关接口类
 * @Title: ExerciseApi
 * @Package com.highgo.opendbt.api
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@Api(tags = "习题相关接口")
@RestController
@RequestMapping("/exercise")
@CrossOrigin
public class ExerciseApi {
    Logger logger = LoggerFactory.getLogger(getClass());
    final
    TNewExerciseService exerciseService;

    public ExerciseApi(TNewExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    /**
     * @description: 获取习题列表
     * @author:
     * @date: 2022/8/26 16:57
     * @param: [request, param TNewExercise 其中课程id必填, result]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    @ApiOperation(value = "习题列表")
    @PostMapping("/getExercise")
    public Map<String, Object> getExercise(HttpServletRequest request, @RequestBody @Valid PageParam<TNewExerciseDTO> param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return exerciseService.getExercise(request, param);
    }


    /**
     * @description: 排序
     * @author:
     * @date: 2022/8/31 10:59
     * @param: [request, oid 要移动的id, tid目标id]
     * @return: int
     **/
    @ApiOperation(value = "排序")
    @GetMapping("/sortExercise/{oid}/{tid}")
    public boolean sortExercise(HttpServletRequest request, @ApiParam(value = "原始id", required = true) @PathVariable("oid") int oid, @ApiParam(value = "目标id", required = true) @PathVariable("tid") int tid) {
        logger.debug("Enter, oid={},tid={}", oid, tid);
        return exerciseService.sortExercise(request, oid, tid);
    }

    /**
     * @description: 习题目录树查询接口
     * @author:
     * @date: 2022/8/31 13:27
     * @param: [request, param, result]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    @ApiOperation(value = "习题目录树查询")
    @PostMapping("/getExerciseCatalogueTree")
    public List<TNewExercise> getExerciseCatalogueTree(HttpServletRequest request, @RequestBody @Valid ExerciseModel param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return exerciseService.getExerciseCatalogueTree(request, param);
    }

    /**
     * @description: 习题移动
     * @author:
     * @date: 2022/8/31 16:39
     * @param: [request, oid 原始id, tid 目标id]
     * @return: boolean
     **/
    @ApiOperation(value = "目录移动")
    @GetMapping("/moveExercise/{oid}/{tid}")
    public boolean moveExercise(HttpServletRequest request, @ApiParam(value = "原始id", required = true) @PathVariable("oid") int oid, @ApiParam(value = "目标id", required = true) @PathVariable("tid") int tid) {
        logger.debug("Enter, oid={},tid={}", oid, tid);
        return exerciseService.moveExercise(request, oid, tid);
    }

    /**
     * @description: 更新保存习题文件夹
     * @author:
     * @date: 2022/8/31 16:57
     * @param: [request, param, result]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @ApiOperation(value = "更新保存习题文件夹")
    @PostMapping("/saveExerciseCatalogue")
    public boolean saveExerciseCatalogue(HttpServletRequest request, @RequestBody @Valid TNewExerciseCatalogueVo param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return exerciseService.saveExerciseCatalogue(request, param);
    }

    /**
     * @description: 习题保存接口
     * @author:
     * @date: 2022/9/1 13:47
     * @param: [request, param, result]
     * @return: boolean
     **/
    @ApiOperation(value = "习题保存")
    @PostMapping("/saveExercise")
    public TNewExercise saveExercise(HttpServletRequest request, @RequestBody TNewExerciseVo param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return exerciseService.saveExercise(request, param);
    }

    /**
     * @description:根据习题id查询具体习题信息
     * @author:
     * @date: 2022/9/1 14:44
     * @param: [request, exercise_id 习题id]
     * @return: boolean
     **/
    @ApiOperation(value = "根据习题id查询具体习题信息")
    @GetMapping("/getExerciseInfo/{exerciseId}")
    public TNewExercise getExerciseInfo(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId) {
        logger.debug("Enter,exerciseId={}", exerciseId);
        TNewExercise exercise = exerciseService.getExerciseInfo(request, exerciseId);
        //设置绑定状态
        exerciseService.decideIsBand(exercise, exerciseId);
        return exercise;
    }

    /**
     * @description: 删除习题
     * @author:
     * @date: 2022/9/2 15:54
     * @param: [request, exerciseId 习题id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @ApiOperation(value = "删除习题")
    @GetMapping("/deleteExercise/{exerciseId}")
    public boolean deleteExercise(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") int exerciseId) {
        logger.debug("Enter,exerciseId={}", exerciseId);
        return exerciseService.deleteExercise(request, exerciseId);
    }

    /**
     * @description:批量删除习题
     * @author:
     * @date: 2022/9/7 13:50
     * @param: [request, param, result]
     * @return: boolean
     **/
    @ApiOperation(value = "批量删除习题")
    @PostMapping("/batchDeleteExercise")
    public boolean batchDeleteExercise(HttpServletRequest request, @RequestBody @Valid TNewExerciseDelVO param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return exerciseService.batchDeleteExercise(request, param);
    }

    /**
     * @description:复制习题
     * @author:
     * @date: 2022/9/7 14:59
     * @param: [request, exerciseId 要复制的习题id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @ApiOperation(value = "复制习题")
    @GetMapping("/copyExercise/{exerciseId}")
    public TNewExercise copyExercise(HttpServletRequest request, @ApiParam(value = "习题id", required = true) @PathVariable("exerciseId") @Valid int exerciseId) {
        logger.debug("Enter,exerciseId={}", exerciseId);
        return exerciseService.copyExercise(request, exerciseId);
    }


    @ApiOperation(value = "习题导出")
    @GetMapping("/exportExercise/{courseId}")
    public void exportExercise(HttpServletRequest request, HttpServletResponse response, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") @Valid int courseId) {
        logger.debug("Enter,courseId={}", courseId);
        exerciseService.exportExercise(request, response, courseId);
    }

    @ApiOperation(value = "习题导入")
    @PostMapping("/importExercise")
    public void importExercise(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        logger.info("Enter, ");
        exerciseService.importExercise(request, file);
    }

    @ApiOperation(value = "题型查询")
    @GetMapping("/getExerciseType")
    public List<TExerciseType> getExerciseType(HttpServletRequest request) {
        logger.debug("Enter,");
        return exerciseService.getExerciseType(request);
    }


}
