package com.highgo.opendbt.api;

import com.alibaba.fastjson.JSON;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import com.highgo.opendbt.common.utils.ValidationUtil;
import com.highgo.opendbt.contents.domain.entity.TCourseContents;
import com.highgo.opendbt.contents.domain.model.SaveCourseContents;
import com.highgo.opendbt.contents.service.TCourseContentsService;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.process.service.TCatalogueProcessService;
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

/**
 * @Description: 章节内容相关接口
 * @Title: ContentsApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/contents")
@Api(tags = "内容相关接口")
public class ContentsApi {

    Logger logger = LoggerFactory.getLogger(getClass());
    private final TCourseContentsService courseContentsService;
    private final TCatalogueProcessService catalogueProcessService;


    /**
     * @description: 保存富文本内容
     * @author:
     * @date: 2022/7/22 15:45
     * @param: direction 内容及相关资源信息
     * @return:
     **/
    @ApiOperation(value = "保存富文本内容")
    @PostMapping("/saveRichTXT")
    public TCourseContents saveRichTXT(HttpServletRequest request, @RequestBody @Valid SaveCourseContents direction, BindingResult result) {
        logger.debug("Enter, direction={}", JSON.toJSONString(direction));
        //校验
        ValidationUtil.Validation(result);
        return courseContentsService.saveRichTXT(request, direction);
    }

    /**
     * @description: 查询富文本内容
     * @author:
     * @date: 2022/7/22 16:29
     * @param: courseId 课程id
     * @param: catalogueId 目录id
     * @return:
     **/
    @ApiOperation(value = "查询富文本内容")
    @GetMapping("/findRichTXT/{courseId}/{catalogueId}")
    public TCourseContents findRichTXT(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "目录id", required = true) @PathVariable("catalogueId") int catalogueId) {
        logger.debug("Enter, catalogueId = {},courseId = {}", catalogueId, +courseId);
        return courseContentsService.findRichTXT(request, courseId, catalogueId);
    }

    /**
     * @description: 根据目录资源表id查询目录资源表内容
     * @author:
     * @date: 2022/8/18 16:36
     * @param: [request, id t_catalogue_resources表id]
     * @return: com.highgo.opendbt.catalogue.model.TCatalogueResources
     **/
    @ApiOperation(value = "根据目录资源表id查询目录资源表内容")
    @GetMapping("/findCatalogueResources/{id}")
    public TCatalogueResources findCatalogueResources(HttpServletRequest request, @ApiParam(value = "目录资源表id", required = true) @PathVariable("id") long id) {
        logger.info("Enter, id = {}", id);
        return courseContentsService.findCatalogueResources(request, id);
    }

    /**
     * @description: 雪花算法生成目录资源表id
     * @author:
     * @date: 2022/8/18 17:05
     * @param: [request]
     * @return: long
     **/
    @ApiOperation(value = "雪花算法生成目录资源表id")
    @GetMapping("/getCatalogueResourcesId")
    public long getCatalogueResourcesId(HttpServletRequest request) {
        logger.debug("Enter,  = ");
        return courseContentsService.getResourcesId(request);
    }

    /**
     * @description: 学生端查看内容
     * @author:
     * @date: 2022/8/30 14:16
     * @param: [request, courseId 课程id, catalogueId 目录id, userId 学生id, classId 班级id]
     * @return: com.highgo.opendbt.contents.domain.entity.TCourseContents
     **/
    @ApiOperation(value = "学生端查看内容")
    @GetMapping("/findRichTXTByStudent/{courseId}/{catalogueId}")
    public TCourseContents findRichTXTByStudent(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "目录id", required = true) @PathVariable("catalogueId") int catalogueId) {
        logger.debug("Enter, catalogueId = {},courseId = {}", catalogueId, courseId);
        return courseContentsService.findRichTXTByStudent(request, courseId, catalogueId);
    }

    /**
     * @description: 学生学习查看进度保存
     * @author:
     * @date: 2022/8/30 14:51
     * @param: [request, progress 进度信息]
     * @return: int
     **/
    @ApiOperation(value = "学生学习查看进度保存")
    @PostMapping("/saveProgressByStudent")
    public TCatalogueProcess saveProgressByStudent(HttpServletRequest request, @RequestBody TCatalogueProcess progress) {
        logger.debug("Enter,  progress= {}", progress.toString());
        return catalogueProcessService.saveProgressByStudent(request, progress);
    }


}
