package com.highgo.opendbt.contents.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import com.highgo.opendbt.catalogue.mapper.TCatalogueResourcesMapper;
import com.highgo.opendbt.catalogue.service.TCatalogueResourcesService;
import com.highgo.opendbt.catalogue.service.TCourseCatalogueService;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.SnowflakeIdWorker;
import com.highgo.opendbt.contents.domain.entity.TCourseContents;
import com.highgo.opendbt.contents.domain.model.SaveCourseContents;
import com.highgo.opendbt.contents.mapper.TCourseContentsMapper;
import com.highgo.opendbt.contents.service.TCourseContentsService;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.process.service.TCatalogueProcessService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.mapper.SclassMapper;
import com.highgo.opendbt.sclass.service.TClassStuService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 课程内容服务类
 */
@Service
@RequiredArgsConstructor
public class TCourseContentsServiceImpl extends ServiceImpl<TCourseContentsMapper, TCourseContents>
        implements TCourseContentsService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TCourseContentsMapper courseContentsMapper;
    @Autowired
    private TCatalogueResourcesService catalogueResourcesService;
    @Autowired
    private TCatalogueResourcesMapper catalogueResourcesMapper;
    @Autowired
    private TCatalogueProcessService catalogueProcessService;
    @Autowired
    private SclassMapper sclassMapper;
    @Autowired
    private TCourseCatalogueService courseCatalogueService;
    @Autowired
    private TClassStuService classStuService;
    SnowflakeIdWorker idWorker = null;


    @Override
    public List<TCourseContents> getContents(HttpServletRequest request, int courseId, int catalogueId) {
        Map<String, Object> param = new HashMap<>();
        param.put("catalogue_id", catalogueId);
        param.put("course_id", courseId);
        return courseContentsMapper.selectByMap(param);
    }

    @Override
    public TCourseContents saveContents(HttpServletRequest request, TCourseContents courseContents) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        Date nowTime = new Date();
        courseContents.setCreateTime(nowTime);
        courseContents.setCreateUser(loginUser.getUserId());
        courseContentsMapper.insert(courseContents);
        return courseContents;

    }


    /**
     * @description: 章节内容保存
     * @author:
     * @date: 2022/8/15 11:29
     * @param: courseContents 内容资源信息表
     * @return:
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TCourseContents saveRichTXT(HttpServletRequest request, SaveCourseContents saveCourseContents) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //内容
        TCourseContents courseContents = new TCourseContents();
        //查询原来的内容信息
        TCourseContents contents = this.getOne(new QueryWrapper<TCourseContents>()
                .eq("course_id", saveCourseContents.getCourseId())
                .eq("catalogue_id", saveCourseContents.getCatalogueId()));
        //内容不变不更新
        if ((contents.getContents() == null && saveCourseContents.getContents().equals("<p></p>"))
                || (saveCourseContents.getContents().equals(contents.getContents()))) {
            return courseContents;
        }
        BeanUtils.copyProperties(saveCourseContents, courseContents);
        //更新内容
        updateContents(request, courseContents, loginUser);
        //更新内容资源相关
        updateAttachments(request, courseContents, loginUser);
        return courseContents;
    }

    /**
     * @description: 更新内容资源相关
     * @author:
     * @date: 2023/1/16 15:39
     * @param: [request, courseContents, loginUser]
     * @return: void
     **/
    private void updateAttachments(HttpServletRequest request, TCourseContents courseContents, UserInfo loginUser) {
        //要删除更新的进度表集合
        List<TCatalogueProcess> processList = new ArrayList<>();
        //目录资源信息
        //存在目录资源信息
        if (courseContents.getAttachments() != null && !courseContents.getAttachments().isEmpty()) {
            //存储目录资源
            List<TCatalogueResources> courseResources = new ArrayList<>(courseContents.getAttachments());
            // 内容里面不能存在相同重复的资源
            validCopyResources(courseResources, courseContents);
            //目录资源设置更新时间
            List<TCatalogueResources> catalogueResources = courseResources.stream()
                    .peek(item -> item.setUpdateTime(new Date()).setUpdateUser(loginUser.getUserId()))
                    .collect(Collectors.toList());
            //更新目录资源表
            logger.info("目录资源保存" + JSON.toJSONString(catalogueResources) + Thread.currentThread().getId());
            boolean resourcesRes = catalogueResourcesService.saveOrUpdateBatch(catalogueResources);
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(resourcesRes);
            // 筛选出需要删除的集合
            List<TCatalogueResources> deleteList = courseResources.stream()
                    .filter(item -> (item.getDeleteFlag() == 1 || item.getIsTask() == 0))
                    .collect(Collectors.toList());
            //筛选要删除更新的进度表
            getCatalogueProgress(processList, deleteList);
            //批量删除更新进度表
            updateCatalogueProgress(processList, loginUser);
            //保存时自动发布
            autoPublish(courseContents, request);
        }
    }

    /**
     * @description: 内容里面不能存在相同的资源
     * @author:
     * @date: 2023/1/16 15:43
     * @param: [courseResources, courseContents]
     * @return: void
     **/
    private void validCopyResources(List<TCatalogueResources> courseResources, TCourseContents courseContents) {
        //保存的目录资源中存在重复资源
        List<Integer> collect = courseResources.stream().map(TCatalogueResources::getResourcesId).distinct().collect(Collectors.toList());
        BusinessResponseEnum.NOTSAMERES.assertIsFalse(collect.size() != courseContents.getAttachments().size());
        //目录资源表中存在和当前相同的资源且id不同
        courseResources.forEach(item -> {
            TCatalogueResources res = catalogueResourcesService.getOne(new QueryWrapper<TCatalogueResources>()
                    .eq("delete_flag", 0)
                    .eq("course_id", item.getCourseId())
                    .eq("catalogue_id", item.getCatalogueId())
                    .eq("resources_id", item.getResourcesId()));
            BusinessResponseEnum.NOTSAMERES.assertIsFalse(res != null && !res.getId().equals(item.getId()));
        });
    }


    /**
     * @description: 获取章节内容及内容中资源信息
     * @author:
     * @date: 2022/8/15 11:12
     * @param: course_id 课程id
     * @param: catalogue_id 目录id
     * @return: TCourseContents
     **/
    @Override
    public TCourseContents findRichTXT(HttpServletRequest request, int courseId, int catalogueId) {
        Map<String, Object> param = new HashMap<>();
        param.put("courseId", courseId);
        param.put("catalogueId", catalogueId);
        //查询内容表
        TCourseContents tCourseContent = this.getOne(new QueryWrapper<TCourseContents>()
                .eq("course_id", courseId)
                .eq("catalogue_id", catalogueId));
        //根据课程id目录id查询相关资源信息（包括资源表和作业表都作为任务点）
        List<TCatalogueResources> tCourseResources = catalogueResourcesMapper.findTCatalogueResources(param);
        if (tCourseResources != null && !tCourseResources.isEmpty()) {
            tCourseContent.setAttachments(tCourseResources);
        }
        return tCourseContent;
    }

    /**
     * @description: 根据目录资源表id查询目录资源表信息
     * @author:
     * @date: 2023/1/16 10:54
     * @param: [request, id 目录资源id]
     * @return: com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources
     **/
    @Override
    public TCatalogueResources findCatalogueResources(HttpServletRequest request, long id) {
        return catalogueResourcesService.getById(id);
    }

    @Override
    public long getResourcesId(HttpServletRequest request) {
        if (idWorker == null) {
            idWorker = new SnowflakeIdWorker(0, 0);
        }
        return idWorker.nextId();
    }

    /**
     * @description: 学生端查看内容
     * @author:
     * @date: 2022/8/30 14:18
     * @param: [request, course_id 课程id, catalogue_id 目录id, class_id 班级id, user_id 用户id]
     * @return: com.highgo.opendbt.contents.domain.entity.TCourseContents
     **/
    @Override
    public TCourseContents findRichTXTByStudent(HttpServletRequest request, int courseId, int catalogueId) {
        //校验并获取登录人信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //当前班级
        Integer classId;
        //获取登录人所在班级
        List<Sclass> clazz = sclassMapper.getActiveClassByStu(loginUser.getUserId(), courseId);
        if (clazz == null || clazz.isEmpty()) {
            throw new APIException("当前登录人下未查询到所在班级");
        }
        BusinessResponseEnum.UNFINDCLASS.assertIsNotEmpty(clazz, loginUser.getUserId(), courseId);
        //存在多个班级，默认显示最后加入的班级
        if (clazz.size() > 1) {
            classId = classStuService.getCurrentClass(loginUser.getUserId(), clazz);
            logger.info("课程{}下,学生{}({})存在多个正在开课的班级中", courseId, loginUser.getUserName(), loginUser.getCode());
            logger.info("此处为教师切换为学生浏览的情况");
        } else {
            classId = clazz.get(0).getId();
        }
        Map<String, Object> param = new HashMap<>();
        param.put("courseId", courseId);
        param.put("catalogueId", catalogueId);
        param.put("classId", classId);
        param.put("userId", loginUser.getUserId());
        //查询内容表
        TCourseContents tCourseContent = this.getOne(new QueryWrapper<TCourseContents>()
                .eq("course_id", courseId)
                .eq("catalogue_id", catalogueId));
        //根据课程id目录id查询相关资源信息（包括资源表和作业表都作为任务点）
        List<TCatalogueResources> tCourseResources = catalogueResourcesMapper.findTCatalogueResourcesbyStudent(param);
        if (tCourseResources != null && !tCourseResources.isEmpty()) {
            tCourseContent.setAttachments(tCourseResources);
        }
        return tCourseContent;
    }

    /**
     * @description: 编辑内容保存时自动发布
     * @author:
     * @date: 2022/8/30 17:39
     * @param: [courseContents, request]
     * @return: void
     **/
    private void autoPublish(TCourseContents courseContents, HttpServletRequest request) {
        //查询到目录
        CourseCatalogue catalogue = courseCatalogueService.getById(courseContents.getCatalogueId());
        // 查询班级发布信息
        CourseCatalogue catalogueAuth = courseCatalogueService.getCatalogueAuth(request, catalogue.getCourseId(), catalogue.getId());
        catalogue.setClasses(catalogueAuth.getClasses());
        // 更新保存班级发布信息
        courseCatalogueService.defaultPublish(request, catalogue);
    }

    /**
     * @description: 删除相关目录资源进度
     * @author:
     * @date: 2022/8/30 19:04
     * @param: [processList, loginUser]
     * @return: void
     **/
    private void updateCatalogueProgress(List<TCatalogueProcess> processList, UserInfo loginUser) {
        if (processList != null && !processList.isEmpty()) {
            // 设置删除标记
            List<TCatalogueProcess> collect = processList.stream()
                    .peek(item -> item.setDeleteUser(loginUser.getUserId()).setDeleteFlag(1).setDeleteTime(new Date()))
                    .collect(Collectors.toList());
            // 批量更细进度表
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(catalogueProcessService.saveOrUpdateBatch(collect));
            logger.info("更新进度表集合{}", JSON.toJSONString(collect));
        }
    }

    /**
     * @description: 筛选进度表
     * @author:
     * @date: 2022/8/30 19:08
     * @param: [processList, deleteList]
     * @return: void
     **/
    private void getCatalogueProgress(List<TCatalogueProcess> processList, List<TCatalogueResources> deleteList) {
        for (TCatalogueResources catalogueRes : deleteList) {
            List<TCatalogueProcess> searchList = catalogueProcessService.list(new QueryWrapper<TCatalogueProcess>()
                    .eq("delete_flag", 0)
                    .eq("course_id", catalogueRes.getCourseId())
                    .eq("catalogue_id", catalogueRes.getCatalogueId())
                    .eq("resources_id", catalogueRes.getResourcesId()));
            if (searchList != null && !searchList.isEmpty()) {
                processList.addAll(searchList);
            }
        }
    }

    /**
     * @description: 更新保存内容
     * @author:
     * @date: 2022/8/30 19:16
     * @param: [request, courseContents, loginUser]
     * @return: void
     **/
    private void updateContents(HttpServletRequest request, TCourseContents courseContents, UserInfo loginUser) {
        //根据目录id和课程id查询是否存在
        TCourseContents oldCourseContents = this
                .findRichTXT(request, courseContents.getCourseId(), courseContents.getCatalogueId());
        if (oldCourseContents != null) {
            //已存在，更新
            courseContents.setId(oldCourseContents.getId());
        }
        //更新
        if (courseContents.getId() != null) {
            courseContents.setUpdateTime(new Date());
            courseContents.setUpdateUser(loginUser.getUserId());
        } else {
            //新增
            courseContents.setCreateTime(new Date());
            courseContents.setCreateUser(loginUser.getUserId());
        }
        //保存内容
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(this.saveOrUpdate(courseContents));
    }
}
