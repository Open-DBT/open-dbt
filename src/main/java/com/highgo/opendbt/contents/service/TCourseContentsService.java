package com.highgo.opendbt.contents.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import com.highgo.opendbt.contents.domain.entity.TCourseContents;
import com.highgo.opendbt.contents.domain.model.SaveCourseContents;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
public interface TCourseContentsService extends IService<TCourseContents> {
    //查询内容
    List<TCourseContents> getContents(HttpServletRequest request, int courseId, int catalogueId);

    //保存内容
    TCourseContents saveContents(HttpServletRequest request, TCourseContents courseContents);

    //保存富文本内容
    TCourseContents saveRichTXT(HttpServletRequest request, SaveCourseContents direction);

    //查询富文本内容
    TCourseContents findRichTXT(HttpServletRequest request, int courseId, int catalogueId);

    //根据目录资源表id查询目录资源
    TCatalogueResources findCatalogueResources(HttpServletRequest request, long id);

    //生成id
    long getResourcesId(HttpServletRequest request);

    //学生端点击目录查看内容
    TCourseContents findRichTXTByStudent(HttpServletRequest request, int courseId, int catalogueId);
}
