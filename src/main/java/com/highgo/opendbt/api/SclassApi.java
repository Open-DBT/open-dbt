package com.highgo.opendbt.api;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.domain.model.*;
import com.highgo.opendbt.sclass.service.SclassService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 班级相关接口
 * @Title: SclassApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */

@Api(tags = "班级相关接口")
@RequiredArgsConstructor
@RestController
@CrossOrigin
@RequestMapping("/sclass")
public class SclassApi {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final SclassService sclassService;



    /**
     * 查询教师创建的班级
     *
     * @param request request
     * @param type    查询类型，0为查询进行中和未开始的班级，1为查询已结束的班级
     * @return List<Sclass>
     */
    @ApiOperation(value = "查询教师创建的班级")
    @GetMapping("/getClassListByTeacherId/{type}")
    public List<Sclass> getClassListByTeacherId(HttpServletRequest request, @ApiParam(value = "查询类型，0为查询进行中和未开始的班级，1为查询已结束的班级", required = true) @PathVariable("type") int type) {
        logger.info("Enter, type = {}", type);
        return sclassService.getClassListByTeacherId(request, type);
    }

    /**
     * 根据当前登录人查询班级
     *
     * @param request request
     * @return List<Sclass>
     */
    @ApiOperation(value = "根据当前登录人查询班级")
    @GetMapping("/getClass")
    public List<Sclass> getClass(HttpServletRequest request) {
        logger.debug("Enter, ");
        return sclassService.getClass(request);
    }

    /**
     * 根据开课和结课时间分别分页获取未开始班级、进行中班级、已结束班级
     *
     * @param request request
     * @param sclassPageTO 班级信息
     * @return PageInfo<Sclass>
     */
    @ApiOperation(value = "根据开课和结课时间分别分页获取未开始班级、进行中班级、已结束班级")
    @PostMapping("/getClassList")
    public PageInfo<Sclass> getClassList(HttpServletRequest request, @RequestBody SclassPageTO sclassPageTO) {
        logger.debug("Enter, sclassPageTO = {}", sclassPageTO.toString());
        return sclassService.getClassListPage(request, sclassPageTO);
    }

    /**
     * 根据学生id查询班级
     *
     * @param request request
     * @return List<Sclass>
     */
    @ApiOperation(value = "根据学生id查询班级")
    @GetMapping("/getSclassByStuId")
    public List<Sclass> getSclassByStuId(HttpServletRequest request) {
        return sclassService.getSclassByStuId(request);
    }

    /**
     * 新增和修改班级
     *
     * @param request request
     * @param sclass 班级信息
     * @return Sclass
     */
    @ApiOperation(value = "新增和修改班级")
    @PostMapping("/updateClass")
    public Sclass updateClass(HttpServletRequest request, @RequestBody Sclass sclass) {
        logger.debug("Enter, sclass = {}", sclass.toString());
        return sclassService.updateClass(request, sclass);
    }

    /**
     * 根据班级id获取班级信息，不会获取班级课程信息
     * @param sclassId 班级id
     * @return Sclass
     */
    @ApiOperation(value = "根据班级id获取班级信息，不会获取班级课程信息")
    @GetMapping("/getSclass/{sclassId}")
    public Sclass getSclass(@PathVariable("sclassId") int sclassId) {
        logger.debug("Enter, sclassId = {}", sclassId);
        return sclassService.getSclassById(sclassId);
    }

    /**
     * 根据班级id获取班级明细，会获取班级课程信息
     *
     * @param sclassId 班级id
     * @return Sclass
     */
    @ApiOperation(value = "根据班级id获取班级明细，会获取班级课程信息")
    @GetMapping("/getSclassInfoById/{sclassId}")
    public Sclass getSclassInfoById(@PathVariable("sclassId") int sclassId) {
        logger.debug("Enter, sclassId = {}", sclassId);
        return sclassService.getSclassInfoById(sclassId);
    }

    /**
     * 根据班级的id获取班级的学生
     *
     * @param sclassUserPage 班级id信息及学生名称学号信息
     * @return PageInfo<UserInfo>
     */
    @ApiOperation(value = "根据班级的id获取班级的学生")
    @PostMapping("/getSclassStu")
    public PageInfo<UserInfo> getSclassStu(@RequestBody SclassUserPage sclassUserPage) throws Exception {
        logger.debug("Enter, sclassId = {}", sclassUserPage.getSclassId());
        return sclassService.getSclassStu(sclassUserPage);
    }

    /**
     * 根据班级的id获取班级的学生
     *
     * @param sclassUserPage 班级id和学生姓名
     * @return List<UserInfo>
     */
    @ApiOperation(value = "根据班级的id获取班级的学生")
    @PostMapping("/getSclassStudentList")
    public List<UserInfo> getSclassStudentList(@RequestBody SclassUserPage sclassUserPage) {
        logger.debug("Enter, sclassUserPage = {}", sclassUserPage.toString());
        return sclassService.getSclassStudentList(sclassUserPage);
    }

    /**
     * 新增和修改班级的学生
     *
     * @param sclassStu 学生信息
     * @return void
     */
    @ApiOperation(value = "新增和修改班级的学生")
    @PostMapping("/updateSclassStu")
    public ResultTO<Void> updateSclassStu(HttpServletRequest request, @RequestBody SclassStu sclassStu) throws Exception {
        logger.debug("Enter, ");
        return sclassService.updateSclassStu(request, sclassStu);
    }

    /**
     * 根据学生查询班级信息
     *
     * @param pageTO 分页信息及班级状态
     * @return PageInfo<Sclass>
     */
    @ApiOperation(value = "根据学生查询班级信息")
    @PostMapping("/getSclassByStu")
    public PageInfo<Sclass> getSclassByStu(HttpServletRequest request, @RequestBody SclassPageTO pageTO) throws Exception {
        logger.debug("Enter, pageTO = {}", pageTO.toString());
        return sclassService.getSclassByStuPage(request, pageTO);
    }

    /**
     * 根据课程，查询全部班级
     * @param courseId 课程id
     * @return List<Sclass>
     */
    @ApiOperation(value = "查询全部班级")
    @GetMapping("/getSclassByCourseId/{courseId}")
    public List<Sclass> getSclassByCourseId(@ApiParam(value = "课程id", required = true) @PathVariable("courseId") int courseId) {
        return sclassService.getSclassByCourseId(courseId);
    }

    /**
     * 根据学生id查询所有班级信息
     * @param request request
     * @return List<Sclass>
     */
    @ApiOperation(value = "根据学生id查询所有班级信息")
    @GetMapping("/getAllSclassByStuId")
    public List<Sclass> getAllSclassByStuId(HttpServletRequest request) {
        logger.info("Enter, ");
        return sclassService.getAllSclassByStuId(request);
    }

    @ApiOperation(value = "根据学生id查询所有班级信息修改后剔除相同课程")
    @GetMapping("/getAllSclassByStuIdExpectCourse")
    public ArrayList<Sclass> getAllSclassByStuIdExpectCourse(HttpServletRequest request) {
        logger.info("Enter, ");
        return sclassService.getAllSclassByStuIdExpectCourse(request);
    }

    /**
     * 根据学生id查询所有班级信息，以及班级课程该学生学习进度
     * @param request request
     * @return List<Sclass>
     */
    @ApiOperation(value = "根据学生id查询所有班级信息，以及班级课程该学生学习进度")
    @GetMapping("/getAllSclassProgressByStuId")
    public List<Sclass> getAllSclassProgressByStuId(HttpServletRequest request) {
        logger.info("Enter, ");
        return sclassService.getAllSclassProgressByStuId(request);
    }

    /**
     * 上传班级导入学生的文件
     * @param request request
     * @param file 文件
     * @return String
     */
    @ApiOperation(value = "上传班级导入学生的文件")
    @PostMapping("/uploadSclassStuFile")
    public String uploadSclassStuFile(HttpServletRequest request, @RequestParam("file") MultipartFile file) throws IOException {
        logger.info("Enter, ");
        return sclassService.uploadSclassStuFile(request, file);
    }

    /**
     * 读取文件导入学生
     * @param importSclassStuTO  班级id 文件路径细信息
     * @return List<String>
     */
    @ApiOperation(value = "读取文件导入学生")
    @PostMapping("/importSclassStu")
    public String importSclassStu(HttpServletRequest request, @RequestBody ImportSclassStuTO importSclassStuTO) {
        logger.info("Enter, sclassId = {}, fileList size = {}", importSclassStuTO.getSclassId(), importSclassStuTO.getFilePathList().size());
        return sclassService.importSclassStu(request, importSclassStuTO);
    }

    /**
     * 删除班级和学生的关联关系
     * @param sclassStu 用户信息
     * @return Integer
     */
    @ApiOperation(value = "删除班级和学生的关联关系")
    @PostMapping("/deleteSclassStu")
    public Integer deleteSclassStu(HttpServletRequest request, @RequestBody SclassStu sclassStu) {
        logger.info("Enter, sclassStu = {}", sclassStu.toString());
        return sclassService.deleteSclassStu(sclassStu, request);
    }

    /**
     * 批量删除学生
     *
     * @param batchDelClass 需要删除的学生id集合及班级id
     * @return Integer
     */
    @ApiOperation(value = "批量删除学生")
    @PostMapping("/batchDelSclassStu")
    public Integer batchDelSclassStu(HttpServletRequest request, @RequestBody BatchDelClass batchDelClass) {
        logger.info("Enter, batchDelSclassStu = {}", batchDelClass);
        return sclassService.batchDelSclassStu(batchDelClass, request);
    }

    /**
     * 删除班级，并级联删除班级和学生的关联关系以及该班级学生的成绩记录
     *
     * @param id 班级id
     * @return Integer
     */
    @ApiOperation(value = "删除班级，并级联删除班级和学生的关联关系以及该班级学生的成绩记录")
    @GetMapping("/deleteSclassById/{id}")
    public Integer deleteSclassById(@PathVariable("id") int id) {
        return sclassService.deleteSclassById(id);
    }

    /**
     * 修改班级是否开放
     * @param sclassId  班级id
     * @param classIsOpen 是否开放
     * @return Integer
     */
    @ApiOperation(value = "修改班级是否开放")
    @GetMapping("/updateSclassIsOpen/{sclassId}/{classIsOpen}")
    public Integer updateSclassIsOpen(@ApiParam(value = "班级id", required = true) @PathVariable("sclassId") int sclassId, @ApiParam(value = "开放标志", required = true) @PathVariable("classIsOpen") int classIsOpen) {
        return sclassService.updateSclassIsOpen(sclassId, classIsOpen);
    }

}
