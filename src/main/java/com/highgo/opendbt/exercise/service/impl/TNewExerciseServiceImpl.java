package com.highgo.opendbt.exercise.service.impl;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.ExcelUtil;
import com.highgo.opendbt.course.domain.entity.ExerciseKnowledge;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.course.service.TExerciseKnowledgeService;
import com.highgo.opendbt.exercise.domain.entity.TExerciseInfo;
import com.highgo.opendbt.exercise.domain.entity.TExerciseType;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.*;
import com.highgo.opendbt.exercise.mapper.TNewExerciseMapper;
import com.highgo.opendbt.exercise.service.TExerciseInfoService;
import com.highgo.opendbt.exercise.service.TExerciseTypeService;
import com.highgo.opendbt.exercise.service.TNewExerciseService;
import com.highgo.opendbt.homeworkmodel.domain.entity.TModelExercise;
import com.highgo.opendbt.homeworkmodel.service.THomeworkModelService;
import com.highgo.opendbt.homeworkmodel.service.TModelExerciseService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 习题相关服务类
 */
@Service
public class TNewExerciseServiceImpl extends ServiceImpl<TNewExerciseMapper, TNewExercise>
        implements TNewExerciseService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TNewExerciseService exerciseService;
    @Autowired
    private TNewExerciseMapper exerciseMapper;
    @Autowired
    private TExerciseInfoService exerciseInfoService;
    @Autowired
    private TModelExerciseService modelExerciseService;
    @Autowired
    private TExerciseKnowledgeService exerciseKnowledgeService;
    @Autowired
    private THomeworkModelService homeworkModelService;
    @Autowired
    private TExerciseTypeService exerciseTypeService;

    /**
     * @description: 获取习题列表
     * @author:
     * @date: 2022/8/26 16:58
     * @param: [request, param 根据课程、题型、知识点查询 课程是必填项]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    @Override
    public Map<String, Object> getExercise(HttpServletRequest request, @Valid PageParam<TNewExerciseDTO> param) {
        Map<String, Object> res = new HashMap<>();
        if (StringUtils.isBlank(param.getOrderBy())) {
            param.setOrderBy("element_type desc,id desc");
        } else {
            param.setOrderBy("element_type desc," + param.getOrderBy() + ",id desc");
        }
        //题目总数量
        AtomicInteger exerciseCount = new AtomicInteger(0);
        //分页查询
        PageInfo<TNewExercise> objectPageInfo = PageMethod.startPage(param.getPageNum(), param.getPageSize()).setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> list(param.getParam()));
        //查询题目总数
        searchExerciseCount(param.getParam(), exerciseCount);
        res.put("pageList", objectPageInfo);
        res.put("exerciseCount", exerciseCount.get());
        return res;
    }

    /**
     * @description: 查询题目总数
     * @author:
     * @date: 2022/11/9 10:59
     * @param: [param, exerciseCount]
     * @return: void
     **/
    private void searchExerciseCount(TNewExerciseDTO param, AtomicInteger exerciseCount) {
        //设置文件夹下题目数量
        List<TNewExercise> newExercises = exerciseMapper.listExercises(param);
        if (!newExercises.isEmpty()) {
            //设置查询出来的习题的数量
            long count = newExercises.stream().filter(item -> item.getElementType() == 0).count();
            exerciseCount.getAndAdd((int) count);
        }
        setExerciseTotalCount(newExercises, param, exerciseCount);
    }


    /**
     * @description: 排序
     * @author:
     * @date: 2022/8/31 10:59
     * @param: [request, oid 要移动的id, tid目标id]
     * @return: int
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sortExercise(HttpServletRequest request, int oid, int tid) {
        //要移动的习题
        TNewExercise oexercise = exerciseService.getById(oid);
        Integer oSortNum = oexercise.getSortNum();
        //目标习题
        TNewExercise texercise = exerciseService.getById(tid);
        Integer tSortNum = texercise.getSortNum();
        oexercise.setSortNum(tSortNum);
        texercise.setSortNum(oSortNum);
        List<TNewExercise> tNewExercises = new ArrayList<>();
        tNewExercises.add(oexercise);
        tNewExercises.add(texercise);
        boolean res = exerciseService.saveOrUpdateBatch(tNewExercises);
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }


    /**
     * @description: 习题目录树查询接口
     * @author:
     * @date: 2022/8/31 16:29
     * @param: [request, param]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    public List<TNewExercise> getExerciseCatalogueTree(HttpServletRequest request, ExerciseModel param) {
        //查询目录树
        List<TNewExercise> exerciseCatalogueTree = exerciseMapper.getExerciseCatalogueTree(param);
        //添加根节点
        TNewExercise exercise = (TNewExercise) new TNewExercise()
                .setId(0)
                .setExerciseName("根节点")
                .setChildrens(exerciseCatalogueTree)
                .setSortNum(0)
                .setDeleteFlag(0);
        //放入集合中
        List<TNewExercise> tNewExercises = new ArrayList<>();
        tNewExercises.add(exercise);
        return tNewExercises;
    }

    /**
     * @description: 习题移动
     * @author:
     * @date: 2022/8/31 16:39
     * @param: [request, oid 原始id, tid 目标id]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean moveExercise(HttpServletRequest request, int oid, int tid) {

        if (tid != 0) {
            TNewExercise texercise = exerciseService.getById(tid);
            BusinessResponseEnum.NOTFOUNDDIRECTORY.assertNotNull(texercise, tid);
            BusinessResponseEnum.ONLYTODIRECTORY.assertIsTrue(texercise.getElementType() == 1);
        }
        //要移动的习题
        TNewExercise oexercise = exerciseService.getById(oid);
        //相同目录无需移动
        BusinessResponseEnum.EQUALSHOMEWORKMODELPACKAGE.assertIsTrue(oexercise.getParentId() != tid);
        oexercise.setParentId(tid);
        boolean res = exerciseService.saveOrUpdate(oexercise);
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 更新保存习题文件夹
     * @author:
     * @date: 2022/8/31 16:57
     * @param: [request, param, result]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveExerciseCatalogue(HttpServletRequest request, TNewExerciseCatalogueVo param) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        TNewExercise tNewExercise = new TNewExercise();
        BeanUtils.copyProperties(param, tNewExercise);
        AtomicBoolean res = new AtomicBoolean(false);
        if (param.getId() != null) {
            //更新
            res.set(exerciseService.updateById((TNewExercise) tNewExercise.setCreateTime(new Date()).setCreateUser(loginUser.getUserId())));
            BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res.get());
        } else {
            //保存
            tNewExercise.setElementType(1).setAuthType(2).setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
            res.set(exerciseService.saveOrUpdate(tNewExercise));
            BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res.get());
            //设置排序字段
            tNewExercise.setSortNum(tNewExercise.getId());
            res.set(exerciseService.saveOrUpdate(tNewExercise));
            BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res.get());
        }
        return res.get();
    }

    /**
     * @description: 习题保存接口
     * @author:
     * @date: 2022/9/1 13:47
     * @param: [request, param]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TNewExercise saveExercise(HttpServletRequest request, TNewExerciseVo param) {
        //校验获取登录人
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //保存习题表
        TNewExercise exercise = saveAndUpdateNewExercise(param, loginUser);
        //更新知识点
        updateExerciseKnowledge(param, exercise);
        //保存习题选项
        saveAndUpdateExerciseInfo(param, loginUser, exercise);
        return exercise;
    }


    /**
     * @description: 根据习题id查询具体习题信息
     * @author:
     * @date: 2022/9/1 14:44
     * @param: [request, exerciseId 习题id]
     * @return: boolean
     **/
    @Override
    public TNewExercise getExerciseInfo(HttpServletRequest request, int exerciseId) {
        TNewExercise exercise = exerciseMapper.getExercise(exerciseId);
        //习题为空抛异常
        BusinessResponseEnum.UNEXERCISEiNFO.assertNotNull(exercise, exerciseId);
        return exercise;
    }

    // 判定是否绑定
    @Override
    public void decideIsBand(TNewExercise exercise, int exerciseId) {
        // 查询习题是否存绑定在作业中
        List<Integer> isBandingModel = homeworkModelService.getIsBandingModel(exerciseId);
        boolean isBanding = isBandingModel != null && isBandingModel.size() > 1;
        //是否绑定过作业模板
        exercise.setBandingModel(isBanding);
    }

    /**
     * @description: 删除习题
     * @author:
     * @date: 2022/9/2 15:54
     * @param: [request, exerciseId 习题id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExercise(HttpServletRequest request, int exerciseId) {
        List<Integer> ids = new ArrayList<>();
        //查询习题
        TNewExercise exercise = exerciseService.getById(exerciseId);
        //判断是否存在，不存在抛出异常
        BusinessResponseEnum.UNEXERCISE.assertNotNull(exercise, exerciseId);
        //判断习题是否为删除状态，删除状态提示已在删除状态无法删除
        BusinessResponseEnum.DUMPLICATEEXERCISE.assertIsFalse(exercise.getDeleteFlag() == 1, exerciseId);
        //查询子习题
        getChildExercise(ids, exerciseId);
        return exerciseService.removeByIds(ids);
    }

    /**
     * @description: 查询子目录和习题
     * @author:
     * @date: 2022/11/2 10:46
     * @param: [exerciseId]
     * @return: java.util.List<java.lang.Integer>
     **/
    private void getChildExercise(List<Integer> ids, int exerciseId) {
        //判断习题是否在使用中，在使用中无法删除
        int count = modelExerciseService.count(new QueryWrapper<TModelExercise>()
                .eq("exercise_id", exerciseId)
                .eq("delete_flag", 0));
        BusinessResponseEnum.CANNOTDELETEEXERCISE.assertIsFalse(count != 0, exerciseId);
        ids.add(exerciseId);
        List<Integer> childs = exerciseService.list(new QueryWrapper<TNewExercise>().eq("parent_id", exerciseId))
                .stream().map(TNewExercise::getId).collect(Collectors.toList());
        getChild(childs, ids);
    }

    /**
     * @description: 迭代查询子习题
     * @author:
     * @date: 2022/11/2 10:56
     * @param: [integerList, ids]
     * @return: void
     **/
    private void getChild(List<Integer> integerList, List<Integer> ids) {
        if (integerList != null && !integerList.isEmpty()) {
            //校验子习题是否可删
            validaChild(integerList);
            ids.addAll(integerList);
            //查询子目录
            List<Integer> childIds = exerciseService.list(new QueryWrapper<TNewExercise>()
                    .in("parent_id", integerList))
                    .stream()
                    .map(TNewExercise::getId)
                    .collect(Collectors.toList());
            getChild(childIds, ids);
        }
    }

    /**
     * @description: 校验习题是否可删
     * @author:
     * @date: 2023/1/10 16:34
     * @param: [childs 习题集合]
     * @return: void
     **/
    private void validaChild(List<Integer> childs) {
        childs.forEach(item -> {
            //判断习题是否在使用中，在使用中无法删除
            int count = modelExerciseService.count(new QueryWrapper<TModelExercise>()
                    .eq("exercise_id", item)
                    .eq("delete_flag", 0));
            BusinessResponseEnum.CANNOTDELETEEXERCISE.assertIsFalse(count != 0, item);
        });

    }

    /**
     * @description: 批量删除习题
     * @author:
     * @date: 2022/9/7 13:50
     * @param: [request, param, result]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteExercise(HttpServletRequest request, TNewExerciseDelVO param) {
        //身份校验
        UserInfo loginUser = Authentication.getCurrentUser(request);
        List<Integer> exerciseIds = param.getExerciseIds();
        //根据id查询习题
        List<TNewExercise> tNewExercises = exerciseService.listByIds(exerciseIds);
        //习题设置删除
        List<TNewExercise> collect = tNewExercises.stream().peek(item -> item.setDeleteFlag(1)
                .setDeleteTime(new Date())
                .setDeleteUser(loginUser.getUserId())).collect(Collectors.toList());
        //批量更新
        boolean res = exerciseService.saveOrUpdateBatch(collect);
        //判断是否成功
        BusinessResponseEnum.DELFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: 复制习题
     * @author:
     * @date: 2022/9/7 14:59
     * @param: [request, exercise_id 要复制的习题id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TNewExercise copyExercise(HttpServletRequest request, int exerciseId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        TNewExercise exercise = exerciseService.getById(exerciseId);
        //复制习题
        reproduceExercise(exercise, loginUser);
        //复制习题明细
        reproduceExerciseInfos(exercise, exerciseId, loginUser);
        //复制习题知识点
        reproduceKnowledges(exercise, exerciseId);
        return exercise;
    }


    /**
     * @description: 根据ids查询习题及详情
     * @author:
     * @date: 2022/9/9 14:40
     * @param: [ids, id 作业模板id]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    @Override
    public List<TNewExercise> getExercisesByIds(List<Integer> exerciseIds, int id, int flag) {
        if (flag == 0)
            return exerciseMapper.getExercises(exerciseIds, id);
        else
            return exerciseMapper.getExercisesDetail(exerciseIds, id);
    }

    /**
     * @description: 当前课程习题导出
     * @author:
     * @date: 2022/11/4 16:12
     * @param: [request, course_id 课程id]
     **/
    @Override
    public void exportExercise(HttpServletRequest request, HttpServletResponse response, int courseId) {
        // 查询课程下所有的习题
        List<TNewExerciseExcel> exercises = exerciseMapper.getExerciseByCourse(courseId);
        // 导出
        ExcelUtil.exportExcel(request, exercises, TNewExerciseExcel.class, "题库习题导出", "课程下所有习题", response);
    }

    /**
     * @description: 习题类型列表
     * @author:
     * @date: 2022/11/7 15:19
     * @param: [request]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TExerciseType>
     **/
    @Override
    public List<TExerciseType> getExerciseType(HttpServletRequest request) {
        return exerciseTypeService.list();
    }

    /**
     * @description: 习题导入
     * @author:
     * @date: 2022/11/14 16:40
     * @param: [request, file]
     * @return: void
     **/
    @Override
    public void importExercise(HttpServletRequest request, MultipartFile file) {
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerify(true);
        importParams.setHeadRows(2);
        importParams.setTitleRows(1);
        List<TNewExerciseExcel> importExcel;
        try {
            importExcel = ExcelImportUtil.importExcel(file.getInputStream(), TNewExerciseExcel.class, importParams);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
        // 处理导入数据形成树形结构，逐级插入
        processImportDate(importExcel);
    }

    /**
     * @description: 处理导入的数据
     * @author:
     * @date: 2022/11/15 13:22
     * @param: [importExcel]
     * @return: void
     **/
    private void processImportDate(List<TNewExerciseExcel> importExcel) {
        //TODO 待完善 importExcel.
    }

    /**
     * @description: 复制习题知识点
     * @author:
     * @date: 2022/11/9 10:07
     * @param: [exercise, exercise_id, loginUser]
     * @return: void
     **/
    private void reproduceKnowledges(TNewExercise exercise, int exerciseId) {
        //查询原始习题知识点
        List<ExerciseKnowledge> exerciseKnowledges = exerciseKnowledgeService
                .list(new QueryWrapper<ExerciseKnowledge>()
                        .eq("exercise_id", exerciseId));
        //设置新知识点
        List<ExerciseKnowledge> knowledges = exerciseKnowledges.stream().peek(item -> {
            item.setExerciseId(exercise.getId());
            item.setId(null);
        }).collect(Collectors.toList());
        //保存新知识点
        boolean res = exerciseKnowledgeService.saveBatch(knowledges);
        //保存失败抛出异常
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(res);
    }

    /**
     * @description: 复制习题具体步骤
     * @author:
     * @date: 2022/11/9 10:05
     * @param: [exercise, loginUser]
     * @return: void
     **/
    private void reproduceExercise(TNewExercise exercise, UserInfo loginUser) {
        //名字设置为原有习题名称+YYYYMMddHHmmss
        exercise.setExerciseName(exercise.getExerciseName() + "_副本")
                .setId(null)
                .setCreateTime(new Date())
                .setCreateUser(loginUser.getUserId());
        boolean res = exerciseService.save(exercise);
        //保存失败抛出异常
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(res);
    }

    /**
     * @description: 复制习题明细
     * @author:
     * @date: 2022/11/9 10:02
     * @param: [exercise, exercise_id]
     * @return: void
     **/
    private void reproduceExerciseInfos(TNewExercise exercise, int exerciseId, UserInfo loginUser) {
        //查询原始习题明细
        List<TExerciseInfo> infos = exerciseInfoService.list(new QueryWrapper<TExerciseInfo>()
                .eq("exercise_id", exerciseId)
                .eq("delete_flag", 0));
        if ((infos != null) && !infos.isEmpty()) {
            //筛选出新习题的明细
            List<TExerciseInfo> newInfos = infos.stream().peek(item -> item.setId(null)
                    .setExerciseId(exercise.getId())
                    .setCreateTime(new Date())
                    .setCreateUser(loginUser.getUserId())).collect(Collectors.toList());
            //保存新习题的明细
            boolean res = exerciseInfoService.saveBatch(newInfos);
            //保存失败抛出异常
            BusinessResponseEnum.SAVEFAIL.assertIsTrue(res);
            exercise.setExerciseInfos(newInfos);
        }
    }

    /**
     * @description: 更新保存习题选项
     * @author:
     * @date: 2022/9/2 10:50
     * @param: [param, exercise_id习题id]
     * @return: void
     **/
    public void saveAndUpdateExerciseInfo(TNewExerciseVo param, UserInfo loginUser, TNewExercise exercise) {
        Integer exerciseId = exercise.getId();
        //习题选项集合
        List<TExerciseInfo> tExerciseInfo = new ArrayList<>();
        //习题选项请求参数
        List<TExerciseInfoVO> exerciseInfos = param.getExerciseInfos();
        //提取需要删除的选项id
        List<Integer> infoIds = exerciseInfoService.list(new QueryWrapper<TExerciseInfo>()
                .eq("exercise_id", exerciseId))
                .stream().map(TExerciseInfo::getId)
                .collect(Collectors.toList());
        if (!infoIds.isEmpty()) {
            //删除所有选项
            boolean removeByIdsRes = exerciseInfoService.removeByIds(infoIds);
            //删除选项失败
            BusinessResponseEnum.FAILDELETEEXERCISEINFO.assertIsTrue(removeByIdsRes);
        }
        //新增所有题目选项
        addExercise(tExerciseInfo, exerciseInfos, loginUser, exerciseId);
        if (!tExerciseInfo.isEmpty()) {
            boolean res = exerciseInfoService.saveOrUpdateBatch(tExerciseInfo);
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        }
        exercise.setExerciseInfos(tExerciseInfo);
    }




    /**
     * @description: 更新习题
     * @author:
     * @date: 2022/9/28 14:51
     * @param: [tExerciseInfo, exerciseInfos, loginUser, exercise_id]
     * @return: void
     **/
    private void updateExercise(List<TExerciseInfo> tExerciseInfo, List<TExerciseInfoVO> exerciseInfos, UserInfo loginUser, int exerciseId) {
        //更新题目
        List<TExerciseInfo> exerciseInfo = exerciseInfoService.list(new QueryWrapper<TExerciseInfo>()
                .eq("exercise_id", exerciseId)
                .eq("delete_flag", 0));
        if (exerciseInfos == null || exerciseInfos.isEmpty()) {
            return;
        }
        for (TExerciseInfoVO vo : exerciseInfos) {
            if (vo.getDeleteFlag() == 1) {
                continue;
            }
            if (vo.getId() == null) {
                TExerciseInfo info = (TExerciseInfo) new TExerciseInfo()
                        .setExerciseId(exerciseId)
                        .setContent(vo.getContent())
                        .setPrefix(vo.getPrefix())
                        .setCreateTime(new Date())
                        .setCreateUser(loginUser.getUserId());
                tExerciseInfo.add(info);
            } else {
                List<TExerciseInfo> infos = exerciseInfo.stream()
                        .filter(item -> item.getId().equals(vo.getId()))
                        .collect(Collectors.toList());
                //校验
                BusinessResponseEnum.DUMPLICATEINFO.assertIsTrue(infos.size() == 1);
                TExerciseInfo info = (TExerciseInfo) infos.get(0)
                        .setContent(vo.getContent())
                        .setPrefix(vo.getPrefix())
                        .setUpdateTime(new Date())
                        .setUpdateUser(loginUser.getUserId());
                tExerciseInfo.add(info);
            }


        }
    }

    /**
     * @description: 新增习题
     * @author:
     * @date: 2022/9/28 14:50
     * @param: [tExerciseInfo, exerciseInfos, loginUser, exercise_id]
     * @return: void
     **/
    private void addExercise(List<TExerciseInfo> tExerciseInfo, List<TExerciseInfoVO> exerciseInfos, UserInfo loginUser, int exerciseId) {
        if (exerciseInfos == null || exerciseInfos.isEmpty()) {
            return;
        }
        for (TExerciseInfoVO vo : exerciseInfos) {
            TExerciseInfo info = (TExerciseInfo) new TExerciseInfo()
                    .setExerciseId(exerciseId)
                    .setContent(vo.getContent())
                    .setPrefix(vo.getPrefix())
                    .setCreateTime(new Date())
                    .setCreateUser(loginUser.getUserId());
            tExerciseInfo.add(info);
        }
    }

    /**
     * @description: 保存更新习题
     * @author:
     * @date: 2022/9/2 10:48
     * @param: [param]
     * @return: void
     **/
    public TNewExercise saveAndUpdateNewExercise(TNewExerciseVo param, UserInfo loginUser) {
        TNewExercise tNewExercise = new TNewExercise();
        BeanUtils.copyProperties(param, tNewExercise);

        //新增题目
        if (param.getId() == null || param.getId() == -1) {
            tNewExercise.setCreateUser(loginUser.getUserId()).setCreateTime(new Date());
            tNewExercise.setId(null);
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseService.saveOrUpdate(tNewExercise));
            //设置排序并更新
            tNewExercise.setSortNum(tNewExercise.getId());
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseService.saveOrUpdate(tNewExercise));
        } else {
            tNewExercise.setUpdateUser(loginUser.getUserId()).setUpdateTime(new Date());
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseService.saveOrUpdate(tNewExercise));
        }
        return tNewExercise;
    }

    /**
     * @description: 更新知识点
     * @author:
     * @date: 2022/9/2 18:21
     * @param: [param, tNewExercise]
     * @return: void
     **/
    private void updateExerciseKnowledge(TNewExerciseVo param, TNewExercise tNewExercise) {
        List<ExerciseKnowledge> addExerciseKnowledge = new ArrayList<>();
        //获取更新后的知识点
        List<Knowledge> knowledge = param.getKnowledges();
        //删除原来的
        //查询
        List<ExerciseKnowledge> list = exerciseKnowledgeService.list(new QueryWrapper<ExerciseKnowledge>()
                .eq("exercise_id", tNewExercise.getId())
                .eq("course_id", tNewExercise.getCourseId()));
        if ((list != null) && !list.isEmpty()) {
            //筛选出id
            List<Integer> ids = list.stream().map(ExerciseKnowledge::getId).collect(Collectors.toList());
            //删除
            if (!ids.isEmpty()) {
                BusinessResponseEnum.DELFAIL.assertIsTrue(exerciseKnowledgeService.removeByIds(ids));
            }
        }
        if (knowledge == null || knowledge.isEmpty()) {
            return;
        }
        //新增现在的
        knowledge.forEach(item ->
                addExerciseKnowledge.add(
                        new ExerciseKnowledge(null
                                , tNewExercise.getId()
                                , item.getKnowledgeId()
                                , tNewExercise.getCourseId()))
        );
        //保存
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseKnowledgeService.saveBatch(addExerciseKnowledge));
    }

    /**
     * @description: 根据课程题型知识点查询所有习题
     * @author:
     * @date: 2022/8/29 10:31
     * @param: [param]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    private List<TNewExercise> list(@Valid TNewExerciseDTO param) {

        List<TNewExercise> tNewExercises = exerciseMapper.listExercises(param);
        if (tNewExercises != null && !tNewExercises.isEmpty()) {
            //设置文件夹下题目数量
            setExerciseCount(tNewExercises, param);
        }
        return tNewExercises;
    }

    /**
     * @description: 设置文件夹下题目数量
     * @author:
     * @date: 2022/10/21 16:54
     * @param: [tNewExercises 查询出来的列表, param查询条件]
     * @return: void
     **/
    private void setExerciseCount(List<TNewExercise> tNewExercises, @Valid TNewExerciseDTO param) {
        tNewExercises.stream().filter(item -> item.getElementType() == 1).peek(item -> {
            int count = exerciseMapper.selectExerciseCount(param, item);
            item.setChildCount(count);
        }).collect(Collectors.toList());
    }

    private void setExerciseTotalCount(List<TNewExercise> tNewExercises, @Valid TNewExerciseDTO param, AtomicInteger exerciseCount) {
        tNewExercises.stream().filter(item -> item.getElementType() == 1).peek(item -> {
            int count = exerciseMapper.selectExerciseCount(param, item);
            item.setChildCount(count);
            if (item.getChildCount() > 0) {
                //合并文件夹下的题目数量
                exerciseCount.getAndAdd(item.getChildCount());
            }
        }).collect(Collectors.toList());

    }

    /**
     * @description: 查询习题数量
     * @author:
     * @date: 2022/8/30 10:39
     * @param: [knowledgeExercises, allExercise, exerciseNum, param]
     * @return: int
     **/
    private int findChildrenCatalogueItem(List<TNewExercise> knowledgeExercises, List<TNewExercise> allExercise, AtomicInteger exerciseNum, TNewExercise param) {
        //过滤出目录
        List<TNewExercise> catalogues = knowledgeExercises.stream()
                .filter(exercise -> exercise.getElementType().equals(1))
                .collect(Collectors.toList());
        //统计习题数量
        exerciseNum.addAndGet(knowledgeExercises.size() - catalogues.size());
        if (!catalogues.isEmpty()) {
            //根据目录筛选每个目录下习题数量
            for (TNewExercise exercise : catalogues) {
                List<TNewExercise> collect = allExercise.stream()
                        .filter(item -> decide(item, exercise, param))
                        .collect(Collectors.toList());
                findChildrenCatalogueItem(collect, allExercise, exerciseNum, param);
            }

        }
        return exerciseNum.get();
    }

    /**
     * @description: 根据条件判断是否属于需要查询的数据
     * @author:
     * @date: 2022/8/30 10:23
     * @param: [item, exercise, param]
     * @return: boolean
     **/
    private boolean decide(TNewExercise item, TNewExercise exercise, TNewExercise param) {
        //根据id筛选
        boolean ideq = item.getParentId().equals(exercise.getId());
        //根据题型筛选
        boolean typeeq = true;
        //知识点
        boolean knowledgeeq = true;
        if (item.getElementType().equals(0)) {
            //若不包含在该题型内
            if (!param.getExerciseTypeList().contains(item.getExerciseType())) {
                typeeq = false;
            }
            //根据知识点筛选
            if (exerciseKnowledgeService.count(
                    new QueryWrapper<ExerciseKnowledge>()
                            .eq("courseId", param.getCourseId())
                            .eq("exerciseId", item.getId())) == 0) {
                knowledgeeq = false;
            }
        }
        return ideq && typeeq && knowledgeeq;
    }

}



