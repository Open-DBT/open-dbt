package com.highgo.opendbt.catalogue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.sclass.domain.entity.Sclass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 目录相关接口
 */
public interface TCourseCatalogueService extends IService<CourseCatalogue> {

    //保存目录
    CourseCatalogue saveCatalogue(HttpServletRequest request, CourseCatalogue courseCatalogue);

    //更新目录
    CourseCatalogue updateCatalogue(HttpServletRequest request, CourseCatalogue courseCatalogue);

    //删除目录
    CourseCatalogue delCatalogue(HttpServletRequest request, int id);

    //移动目录
    CourseCatalogue moveCatalogue(HttpServletRequest request, int sid, int did);

    //修改排序
    CourseCatalogue moveSortNum(HttpServletRequest request, int catalogueId, String direction);

    //查询章节开放情况
    CourseCatalogue getCatalogueAuth(HttpServletRequest request, int courseId, int catalogueId);


    //整体保存章节开放情况
    CourseCatalogue saveCatalogueAuthAll(HttpServletRequest request, CourseCatalogue courseCatalogue);


    //获取该课程的所有班级，用于目录发布授权
    List<Sclass> getClassByLoginUser(HttpServletRequest request, int courseId);

    //根据班级获取目录树中每个目录的发布信息
    Map<String, Object> getCatalogueByClass(HttpServletRequest request, int courseId, int classId);

    //学生端查询目录树
    Map<String, Object> getCatalogueByStu(HttpServletRequest request, int courseId);

    //默认发布
    void defaultPublish(HttpServletRequest request, CourseCatalogue catalogue);

    //添加学生时重新发布
    void addStudentPublish(HttpServletRequest request, Integer classId, Integer userId);

    //删除学生时重新发布
    void delStudentPublish(HttpServletRequest request, Integer classId, List<Integer> userId);
}
