package com.highgo.opendbt.statistics.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.catalogue.mapper.TCatalogueResourcesMapper;
import com.highgo.opendbt.catalogue.mapper.TCourseCatalogueMapper;
import com.highgo.opendbt.catalogue.service.TCatalogueResourcesService;
import com.highgo.opendbt.catalogue.service.TCourseCatalogueService;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.SerialNumCount;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.process.mapper.TCatalogueProcessMapper;
import com.highgo.opendbt.process.service.TCatalogueProcessService;
import com.highgo.opendbt.sources.domain.entity.TResources;
import com.highgo.opendbt.sources.service.TResourcesService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.service.SclassService;
import com.highgo.opendbt.statistics.domain.model.*;
import com.highgo.opendbt.statistics.service.StatisticsService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {
    final
    TCourseCatalogueMapper courseCatalogueMapper;
    final
    TCatalogueResourcesMapper catalogueResourcesMapper;
    final
    TCatalogueProcessMapper catalogueProcessMapper;
    final
    UserInfoService userInfoService;
    final
    SclassService sclassService;
    final
    TResourcesService resourcesService;
    final
    TCourseCatalogueService courseCatalogueService;
    final
    TCatalogueResourcesService catalogueResourcesService;
    final
    TCatalogueProcessService catalogueProcessService;

    /**
     * @description: 章节进度查询
     * @author:
     * @date: 2022/8/10 15:26
     * @param: null
     * @return:
     **/
    @Override
    public List<CourseCatalogue> catalogueStatistics(HttpServletRequest request, int courseId, int catalogueId, int classId, String serialNum) {
        //根据班级id查询班级下学生
        List<UserInfo> studentList = sclassService.getStudentByClassId(classId);
        // 根据目录id查询目录树
        List<CourseCatalogue> courseCataloguesList = findCatalogueList(courseId, classId);
        List<CourseCatalogue> collect = null;
        if (courseCataloguesList != null && !courseCataloguesList.isEmpty()) {
            //设置排序的序号
            SerialNumCount serialNumCount = new SerialNumCount();
            serialNumCount.setNum(serialNum);
            collect = courseCataloguesList.stream().filter(item -> item.getId().equals(catalogueId)).peek(item -> {
                //设置子目录
                setChild(item, classId, studentList, serialNumCount, courseCataloguesList);
            }).sorted(Comparator.comparing(CourseCatalogue::getSortNum)).collect(Collectors.toList());
        }
        return collect;
    }

    /**
     * @description: 章节查看
     * @author:
     * @date: 2022/8/11 14:55
     * @param: null
     * @return:
     **/
    @Override
    public List<TCatalogueProcess> statisticsLook(HttpServletRequest request, StatisticsParam param) {
        return catalogueProcessMapper.statisticslook(param);
    }

    /**
     * @description: 章节统计查看页面的任务相关信息头
     * @author:
     * @date: 2022/8/24 12:26
     * @param: [request, param]
     * @return: java.util.List<com.highgo.opendbt.process.model.TCatalogueProcess>
     **/
    @Override
    public Map<String, Object> getCatalogueInfoTitle(HttpServletRequest request, StatisticsParam param) {
        TResources resources = resourcesService.getById(param.getResourcesId());
        CourseCatalogue catalogue = courseCatalogueService.getById(param.getCatalogueId());
        Map<String, Object> resMap = new HashMap<>();
        resMap.put("resources_name", resources.getResourcesName());
        resMap.put("resources_time", resources.getResourcesTime());
        resMap.put("catalogue_name", catalogue.getCatalogueName());
        return resMap;
    }

    /**
     * @description: 用于学生端目录学习进度保存后，资源完成情况查询
     * @author:
     * @date: 2022/10/13 13:47
     * @param: [request, course_id, catalogue_id, class_id, user_id]
     * @return: java.util.Map<java.lang.String, java.lang.Object>
     **/
    @Override
    public Map<String, Integer> getCatalogueStatisticsInfo(HttpServletRequest request, int courseId, int catalogueId, int classId, int userId) {
        int completeNum = 0;
        int totalNum = 0;
        List<TCatalogueProcess> list = catalogueProcessService.list(new QueryWrapper<TCatalogueProcess>()
                .eq("course_id", courseId)
                .eq("catalogue_id", catalogueId)
                .eq("class_id", classId)
                .eq("user_id", userId)
                .eq("delete_flag", 0));
        if (list != null && !list.isEmpty()) {
            totalNum = list.size();
            List<TCatalogueProcess> collect = list.stream().filter(item -> item.getStudyStatus() == 2).collect(Collectors.toList());
            completeNum = Math.max(collect.size(), 0);
        }
        Map<String, Integer> res = new HashMap<>();
        res.put("total_num", totalNum);
        res.put("complete_num", completeNum);
        return res;
    }

    /**
     * @description: 学生学习进度查询
     * @author:
     * @date: 2022/8/11 11:03
     * @param: statistics
     * @return:
     **/
    @Override
    public List<StudentStatistics> statisticsStudent(HttpServletRequest request, StudentProgressStatistics statistics) {
        //用于存储目录和子目录集合
        List<Integer> list = new ArrayList<>();
        //根目录
        List<Integer> ids = new ArrayList<>();
        ids.add(statistics.getCatalogueId());
        List<Integer> catalogues = new ArrayList<>(ids);
        // 是否为一级目录若为一级目录查询目录下所有子目录，若不为一级目录不查询
        if (statistics.getIsFirstLevel() == 1) {
            // 查询该目录及其下所有有子目录
            catalogues = getCatalogues(ids, list);
        }
        return catalogueProcessMapper.statisticsStudent(statistics.getCourseId(), catalogues, statistics.getClassId(), statistics.getUserName(), statistics.getCode());
    }

    /**
     * @description: 查询该目录及其下所有有子目录
     * @author:
     * @date: 2022/9/7 16:12
     * @param: [catalogue_ids, list 用于存储目录和子目录集合]
     * @return: java.util.List<java.lang.Integer>
     **/
    private List<Integer> getCatalogues(List<Integer> catalogueIds, List<Integer> list) {

        if (catalogueIds != null && !catalogueIds.isEmpty()) {
            list.addAll(catalogueIds);
            catalogueIds.forEach(courseId -> {
                //查询子目录
                List<CourseCatalogue> courses = courseCatalogueService.list(new QueryWrapper<CourseCatalogue>()
                        .eq("parent_id", courseId));
                if (courses != null && !courses.isEmpty()) {
                    //筛选目录id
                    List<Integer> collect = courses.stream().map(CourseCatalogue::getId).collect(Collectors.toList());
                    //迭代子目录
                    getCatalogues(collect, list);
                }
            });
        }
        return list;
    }

    /**
     * @description: 学生学习进度查看
     * @author:
     * @date: 2022/8/11 14:54
     * @param: null
     * @return:
     **/
    @Override
    public Map<String, Object> statisticsStudentLook(HttpServletRequest request, int courseId, int catalogueId, int classId, int userId, String serialNum) {
        // 查询班级名称  人员信息
        UserInfo user = userInfoService.getById(userId);
        BusinessResponseEnum.UNUSERINFO.assertNotNull(user, userId);
        Sclass classes = sclassService.getById(classId);
        BusinessResponseEnum.UNCLASS.assertNotNull(classes, classId);
        // 根据目录id查询目录树
        List<CourseCatalogue> courseCataloguesList = findCatalogueList(courseId, classId);
        List<CourseCatalogue> collect = null;
        AtomicInteger countNum = new AtomicInteger();
        AtomicInteger completeNum = new AtomicInteger();
        //设置排序的序号
        SerialNumCount serialNumCount = new SerialNumCount();
        serialNumCount.setNum(serialNum);
        if (courseCataloguesList != null && !courseCataloguesList.isEmpty()) {
            collect = courseCataloguesList.stream()
                    .filter(item -> catalogueId == -1 ? item.getParentId() == 0 : item.getId().equals(catalogueId))
                    .sorted(Comparator.comparing(CourseCatalogue::getSortNum))
                    .peek(item -> {
                        //设置学习进度和任务数
                        getProcessAndTaskInfo(item, countNum, completeNum, serialNumCount, classId, userId);
                        //一级目录不包含. ,查询子目录
                        if (!serialNum.contains(".")) {
                            //子目录查询
                            List<CourseCatalogue> children = getChildrenStu(item, courseCataloguesList, classId, userId, countNum, completeNum, serialNumCount);
                            if (children != null && !children.isEmpty()) {
                                item.setChildrens(children);
                                item.setIsleaf(false);
                            } else {
                                item.setIsleaf(true);
                            }
                        }
                        //序号增加
                        serialNumCount.addSerralNum(item.getSerialNum());
                    }).collect(Collectors.toList());
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("courseCatalogueTree", collect);
        resp.put("userName", user.getUserName());
        resp.put("code", user.getCode());
        resp.put("className", classes.getClassName());
        resp.put("countNum", countNum.get());
        resp.put("completeNum", completeNum.get());
        return resp;
    }

    /**
     * @description: 设置学习进度和任务数
     * @author:
     * @date: 2023/1/18 14:18
     * @param: [item, countNum, completeNum, serialNumCount, classId, userId]
     * @return: void
     **/
    private void getProcessAndTaskInfo(CourseCatalogue item, AtomicInteger countNum, AtomicInteger completeNum, SerialNumCount serialNumCount, int classId, int userId) {
        //序号
        item.setSerialNum(serialNumCount.getNum());
        //学习进度查询
        List<StudentCatalogueStatistics> studentCatalogueStatistics = catalogueProcessMapper
                .findResourcesProcessByUser(item, classId, userId);
        item.setStudentCatalogueStatistics(studentCatalogueStatistics);
        //统计总任务数和已完成的任务数
        if (studentCatalogueStatistics != null && !studentCatalogueStatistics.isEmpty()) {
            countNum.addAndGet(studentCatalogueStatistics.size());
            long count = studentCatalogueStatistics.stream().filter(stu -> stu.getStudyStatus() == 2).count();
            completeNum.addAndGet((int) count);
        }
    }

    //章节子目录查询
    private List<CourseCatalogue> getChildren(CourseCatalogue courseCatalogue, List<CourseCatalogue> courseCataloguesList, int classId, SerialNumCount serialNumCount, List<UserInfo> studentList) {
        serialNumCount.countlevel(serialNumCount.getNum());
        return courseCataloguesList.stream().filter(item -> item.getParentId().equals(courseCatalogue.getId()))
                .sorted(Comparator.comparing(CourseCatalogue::getSortNum))
                .peek(item -> {
                    //设置子目录
                    setChild(item, classId, studentList, serialNumCount, courseCataloguesList);
                }).collect(Collectors.toList());
    }

    //设置子目录
    private void setChild(CourseCatalogue item, int classId, List<UserInfo> studentList, SerialNumCount serialNumCount, List<CourseCatalogue> courseCataloguesList) {
        List<CatalogueStatistics> catalogueStatistics = catalogueResourcesMapper.findResourcesAndComplateInfo(item, classId);
        item.setCatalogueStatistics(catalogueStatistics);
        item.setSerialNum(serialNumCount.getNum());
        List<CourseCatalogue> children = getChildren(item, courseCataloguesList, classId, serialNumCount, studentList);
        if (children != null && !children.isEmpty()) {
            item.setChildrens(children);
            item.setIsleaf(false);
        } else {
            item.setIsleaf(true);
        }
        serialNumCount.addSerralNum(item.getSerialNum());
    }

    //学生统计子目录查询
    private List<CourseCatalogue> getChildrenStu(CourseCatalogue courseCatalogue, List<CourseCatalogue> courseCataloguesList, int classId, int userId, AtomicInteger countNum, AtomicInteger completeNum, SerialNumCount serialNumCount) {
        serialNumCount.countlevel(serialNumCount.getNum());
        return courseCataloguesList.stream().filter(item -> item.getParentId().equals(courseCatalogue.getId()))
                .sorted(Comparator.comparing(CourseCatalogue::getSortNum))
                .peek(item -> {
                    //设置学习进度和任务数
                    getProcessAndTaskInfo(item, countNum, completeNum, serialNumCount, classId, userId);
                    List<CourseCatalogue> children = getChildrenStu(item, courseCataloguesList, classId, userId, countNum, completeNum, serialNumCount);
                    if (children != null && !children.isEmpty()) {
                        item.setChildrens(children);
                        item.setIsleaf(false);
                    } else {
                        item.setIsleaf(true);
                    }
                    serialNumCount.addSerralNum(item.getSerialNum());
                }).collect(Collectors.toList());
    }

    /**
     * @description: 根据课程id班级id查询目录树
     * @author:
     * @date: 2022/8/10 13:33
     * @param: null
     * @return:
     **/
    private List<CourseCatalogue> findCatalogueList(int courseId, int classId) {
        Map<String, Object> param = new HashMap<>();
        param.put("courseId", courseId);
        param.put("deleteFlag", 0);
        param.put("authType", "0");
        param.put("classId", classId);
        return courseCatalogueMapper.selectCataloguePublish(param);
    }
}
