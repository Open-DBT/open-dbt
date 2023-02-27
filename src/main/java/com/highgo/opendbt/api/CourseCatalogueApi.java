package com.highgo.opendbt.api;

import com.alibaba.fastjson.JSON;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.catalogue.service.TCourseCatalogueService;
import com.highgo.opendbt.contents.domain.entity.TCourseContents;
import com.highgo.opendbt.contents.service.TCourseContentsService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description: 课程目录相关接口
 * @Title: CourseCatalogueApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/19 15:18
 */
@Api(tags = "课程目录相关接口")
@RestController
@CrossOrigin
@RequestMapping("/courseCatalogue")
public class CourseCatalogueApi {

    Logger logger = LoggerFactory.getLogger(getClass());
    private final TCourseCatalogueService TCourseCatalogueService;
    private final TCourseContentsService courseContentsService;

    public CourseCatalogueApi(TCourseCatalogueService TCourseCatalogueService, TCourseContentsService courseContentsService) {
        this.TCourseCatalogueService = TCourseCatalogueService;
        this.courseContentsService = courseContentsService;
    }


    /**
     * @description:根据课程班级查询目录树
     * @author:
     * @date: 2022/8/10 10:20
     * @param: courseId 课程id
     * @param: classId 班级id
     * @return: 目录树
     **/
    @ApiOperation(value = "根据课程id班级id查询目录发布信息")
    @GetMapping("/getCatalogueByClass/{courseId}/{classId}")
    public Map<String, Object> getCatalogueByClass(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "班级id", required = true) @PathVariable("classId") int classId) {
        logger.debug("Enter, courseId ={},classId={}", courseId, classId);
        return TCourseCatalogueService.getCatalogueByClass(request, courseId, classId);
    }

    /**
     * @description:根据目录id查询章节内容 备用
     * @author:
     * @date: 2022/7/14 14:36
     * @param: catalogueId 目录id（叶子节点）
     * @param: courseId  课程id
     * @return: ResultTO<List < TCourseContents>> 内容集合（一个章节有多个tab）
     **/
    @ApiOperation(value = "根据课程id目录id查询内容")
    @GetMapping("/getContents/{courseId}/{catalogueId}")
    public List<TCourseContents> getContents(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId, @ApiParam(value = "目录id", required = true) @PathVariable("catalogueId") int catalogueId) {
        logger.debug("Enter, catalogueId = {} ,courseId = {}", catalogueId, courseId);
        return courseContentsService.getContents(request, courseId, catalogueId);
    }

    /**
     * @description:内容添加备用
     * @author:
     * @date: 2022/7/15 15:36
     * @param: courseContents 内容
     * @return:
     **/
    @ApiOperation(value = "内容保存")
    @PostMapping("/saveContents")
    public TCourseContents saveContents(HttpServletRequest request, @Validated @RequestBody TCourseContents courseContents) {
        logger.debug("Enter, courseContents = {}", courseContents.toString());
        return courseContentsService.saveContents(request, courseContents);
    }

    /**
     * @description:新增目录
     * @author:
     * @date: 2022/7/15 15:42
     * @param: courseCatalogue
     * @return:
     **/
    @ApiOperation(value = "目录保存")
    @PostMapping("/saveCatalogue")
    public CourseCatalogue saveCatalogue(HttpServletRequest request, @Validated @RequestBody CourseCatalogue courseCatalogue) throws Exception {
        logger.debug("Enter, courseContents = {}", courseCatalogue.toString());
        return TCourseCatalogueService.saveCatalogue(request, courseCatalogue);
    }

    /**
     * @description:更新目录
     * @author:
     * @date: 2022/7/15 15:42
     * @param: courseCatalogue
     * @return:
     **/
    @ApiOperation(value = "目录更新")
    @PostMapping("/updateCatalogue")
    public CourseCatalogue updateCatalogue(HttpServletRequest request, @RequestBody CourseCatalogue courseCatalogue) {
        logger.debug("Enter, courseContents = {}", courseCatalogue.toString());
        return TCourseCatalogueService.updateCatalogue(request, courseCatalogue);
    }

    /**
     * @description:删除目录
     * @author:
     * @date: 2022/7/15 15:42
     * @param: courseCatalogue
     * @return:
     **/
    @ApiOperation(value = "目录删除")
    @GetMapping("/delCatalogue/{id}")
    public CourseCatalogue delCatalogue(HttpServletRequest request, @ApiParam(value = "目录id", required = true) @PathVariable("id") int id) {
        logger.debug("Enter, id = {}", id);
        return TCourseCatalogueService.delCatalogue(request, id);
    }

    /**
     * @description:移动目录挂接点 暂时不用
     * @author:
     * @date: 2022/7/15 15:42
     * @param: sid 原始目录id
     * @param: did 目标目录id
     * @return:
     **/
    @ApiOperation(value = "目录移动")
    @GetMapping("/moveCatalogue/{sid}/{did}")
    public CourseCatalogue moveCatalogue(HttpServletRequest request, @ApiParam(value = "原始目录id", required = true) @PathVariable("sid") int sid, @ApiParam(value = "目标目录id", required = true) @PathVariable("did") int did) {
        logger.debug("Enter, sid = {},did={}", sid, did);
        return TCourseCatalogueService.moveCatalogue(request, sid, did);
    }

    /**
     * @description:移动目录排序
     * @author:
     * @date: 2022/7/18 9:32
     * @param: direction 排序方向  up:上移，down下移
     * @return:
     **/
    @ApiOperation(value = "上移下移")
    @GetMapping("/moveSortNum/{catalogueId}/{direction}")
    public CourseCatalogue moveSortNum(HttpServletRequest request, @ApiParam(value = "目录id", required = true) @PathVariable("catalogueId") int catalogueId, @ApiParam(value = "排序方向  up:上移，down下移", required = true) @PathVariable("direction") String direction) {
        logger.debug("Enter, catalogueId={},direction={}", catalogueId, direction);
        return TCourseCatalogueService.moveSortNum(request, catalogueId, direction);
    }


    /**
     * @description:根据课程id，目录id，查询目录的相关信息和目录下的班级
     * @author:
     * @date: 2022/7/20 13:08
     * @param: course_id 课程id
     * @param: catalogue_id 目录id
     * @return: courseCatalogue 目录信息
     **/
    @ApiOperation(value = "目录章节发布信息查询接口（显示开放模式和班级发布情况）")
    @GetMapping("/getCatalogueAuth/{courseId}/{catalogueId}")
    public CourseCatalogue getCatalogueAuth(HttpServletRequest request, @PathVariable("courseId") int courseId, @PathVariable("catalogueId") int catalogueId) {
        logger.debug("Enter, catalogueId = {},courseId = {}", catalogueId, courseId);
        return TCourseCatalogueService.getCatalogueAuth(request, courseId, catalogueId);
    }

    /**
     * @description:设置目录信息及发布班级学生信息
     * @author:
     * @date: 2022/7/20 14:37
     * @param: courseCatalogue 目录信息
     * @return:
     **/
    @ApiOperation(value = "设置目录信息及发布班级学生信息")
    @PostMapping("/saveCatalogueAuthAll")
    public CourseCatalogue saveCatalogueAuthAll(HttpServletRequest request, @RequestBody CourseCatalogue courseCatalogue) {
        logger.debug("Enter, courseCatalogue = {}", JSON.toJSONString(courseCatalogue));
        return TCourseCatalogueService.saveCatalogueAuthAll(request, courseCatalogue);
    }
////////////////////评审到此处////////////////////////////////////////

    /**
     * @description:根据课程id和当前登陆人信息查询所有班级
     * @author:
     * @date: 2022/7/25 12:21
     * @param: courseId 课程id
     * @return:
     **/
    @ApiOperation(value = "班级列表查询")
    @GetMapping("/getClassByLoginUser/{courseId}")
    public List<Sclass> getClassByLoginUser(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) {
        logger.debug("Enter, courseId = {}", courseId);
        return TCourseCatalogueService.getClassByLoginUser(request, courseId);
    }

    /**
     * @description:学生端查询目录树结构及任务完成情况
     * @author:
     * @date: 2022/8/15 17:55
     * @param: courseId 课程id
     * @return: 目录树
     **/
    @ApiOperation(value = "学生端查询目录树结构及任务完成情况")
    @GetMapping("/getCatalogueByStu/{courseId}")
    public Map<String, Object> getCatalogueByStu(HttpServletRequest request, @ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) {
        logger.debug("Enter, courseId = {}", courseId);
        return TCourseCatalogueService.getCatalogueByStu(request, courseId);

    }
}
