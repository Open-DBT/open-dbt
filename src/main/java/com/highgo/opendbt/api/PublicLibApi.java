package com.highgo.opendbt.api;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.publicLibrary.model.ImportPublicExerciseTO;
import com.highgo.opendbt.publicLibrary.model.PublicExercise;
import com.highgo.opendbt.publicLibrary.model.PublicExercisePage;
import com.highgo.opendbt.publicLibrary.model.PublicScene;
import com.highgo.opendbt.publicLibrary.service.PublicExerciseService;
import com.highgo.opendbt.publicLibrary.service.PublicSceneService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 公共场景相关接口类 暂时不用
 * @Title: PublicLibApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/public")
public class PublicLibApi {

    Logger logger = LoggerFactory.getLogger(getClass());

    final PublicSceneService publicSceneService;

    final PublicExerciseService publicExerciseService;


    /**
     * @description: 根据课程id分页获取公共场景
     * @author:
     * @date: 2023/1/9 14:08
     * @param: [page]
     * @return: com.github.pagehelper.PageInfo<com.highgo.opendbt.publicLibrary.model.PublicScene>
     **/
    @RequestMapping("/getScene")
    public PageInfo<PublicScene> getScene(@RequestBody PageTO page) {
        return publicSceneService.getScene(page);
    }

    /**
     * 获取所有公共场景的名称
     *
     * @return List<PublicScene>
     */
    @RequestMapping("/getSceneNameList")
    public List<PublicScene> getSceneNameList() {
        return publicSceneService.getSceneNameList();
    }

    /**
     * 根据场景id获取场景信息
     *
     * @param sceneId 场景id
     * @return PublicScene
     */
    @RequestMapping("/getSceneDetail/{sceneId}")
    public PublicScene getSceneDetail(@PathVariable("sceneId") int sceneId) {
        logger.debug("Enter, sceneId = " + sceneId);
        return publicSceneService.getSceneDetail(sceneId);
    }

    /**
     * 添加和修改场景
     *
     * @param request request
     * @param scene 场景
     * @return Integer
     */
    @RequestMapping("/updateScene")
    public ResultTO<Integer> updateScene(HttpServletRequest request, @RequestBody PublicScene scene) {
        logger.info("Enter, scene = " + scene.toString());
        return publicSceneService.updateScene(request, scene);
    }

    /**
     * 删除场景
     *
     * @param sceneId 场景id
     * @return Integer
     */
    @RequestMapping("/deleteScene/{sceneId}")
    public ResultTO<Integer> deleteScene(@PathVariable("sceneId") int sceneId) {
        logger.debug("Enter, sceneId = " + sceneId);
        return publicSceneService.deleteScene(sceneId);
    }

    /**
     * 分页获取公共习题
     *
     * @param publicExercisePage 分页和模糊查询参数
     * @return PageInfo<PublicExercise>
     */
    @RequestMapping("/getPublicExerciseList")
    public PageInfo<PublicExercise> getPublicExerciseList(@RequestBody PublicExercisePage publicExercisePage) {
        logger.info("Enter, publicExercisePage = " + publicExercisePage.toString());
        return publicExerciseService.getPublicExerciseList(publicExercisePage);
    }

    /**
     * 获取习题信息
     *
     * @param exerciseId 习题id
     * @return PublicExercise
     */
    @RequestMapping("/getPublicExerciseInfo/{exerciseId}")
    public PublicExercise getPublicExerciseInfo(@PathVariable("exerciseId") int exerciseId) {
        logger.debug("Enter, exerciseId = " + exerciseId);
        return publicExerciseService.getPublicExerciseInfo(exerciseId);
    }

    /**
     * 修改和新增习题
     *
     * @param request request
     * @param publicExercise 习题信息
     * @return Integer
     */
    @RequestMapping("/updateExercise")
    public Integer updateExercise(HttpServletRequest request, @RequestBody PublicExercise publicExercise) {
        logger.info("Enter, publicExercise = " + publicExercise.toString());
        return publicExerciseService.updateExercise(request, publicExercise);
    }

    /**
     * 删除习题
     *
     * @param exerciseId 习题id
     * @return Integer
     */
    @RequestMapping("/deleteExercise/{exerciseId}")
    public Integer deleteExercise(@PathVariable("exerciseId") int exerciseId) {
        logger.debug("Enter, exerciseId = " + exerciseId);
        return publicExerciseService.deleteExercise(exerciseId);
    }

    /**
     * 测试运行习题正确答案
     *
     * @param request request
     * @param publicExercise 习题
     * @return Map<String, Object>
     */
    @RequestMapping("/testRunAnswer")
    public Map<String, Object> testRunAnswer(HttpServletRequest request, @RequestBody PublicExercise publicExercise) {
        logger.info("Enter, publicExercise = " + publicExercise.toString());
        return publicExerciseService.testRunAnswer(request, publicExercise);
    }

    /**
     * 导入公共习题
     *
     * @param request request
     * @param importPublicExerciseTO 导入习题信息
     * @return String
     */
    @RequestMapping("/importPublicExercise")
    public String importPublicExercise(HttpServletRequest request, @RequestBody ImportPublicExerciseTO importPublicExerciseTO) {
        logger.info("Enter, ");
        return publicExerciseService.importPublicExercise(request, importPublicExerciseTO);
    }

    /**
     * 导出公共习题
     *
     * @param request request
     * @return String
     */
    @RequestMapping("/exportPublicExercise")
    public String exportPublicExercise(HttpServletRequest request) {
        logger.info("Enter, ");
        return publicExerciseService.exportPublicExercise(request);
    }

}
