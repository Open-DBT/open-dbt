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
 * ?????????????????????
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
     * @description: ??????????????????
     * @author:
     * @date: 2022/8/26 16:58
     * @param: [request, param ??????????????????????????????????????? ??????????????????]
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
        //???????????????
        AtomicInteger exerciseCount = new AtomicInteger(0);
        //????????????
        PageInfo<TNewExercise> objectPageInfo = PageMethod.startPage(param.getPageNum(), param.getPageSize()).setOrderBy(param.getOrderBy())
                .doSelectPageInfo(() -> list(param.getParam()));
        //??????????????????
        searchExerciseCount(param.getParam(), exerciseCount);
        res.put("pageList", objectPageInfo);
        res.put("exerciseCount", exerciseCount.get());
        return res;
    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/11/9 10:59
     * @param: [param, exerciseCount]
     * @return: void
     **/
    private void searchExerciseCount(TNewExerciseDTO param, AtomicInteger exerciseCount) {
        //??????????????????????????????
        List<TNewExercise> newExercises = exerciseMapper.listExercises(param);
        if (!newExercises.isEmpty()) {
            //????????????????????????????????????
            long count = newExercises.stream().filter(item -> item.getElementType() == 0).count();
            exerciseCount.getAndAdd((int) count);
        }
        setExerciseTotalCount(newExercises, param, exerciseCount);
    }


    /**
     * @description: ??????
     * @author:
     * @date: 2022/8/31 10:59
     * @param: [request, oid ????????????id, tid??????id]
     * @return: int
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sortExercise(HttpServletRequest request, int oid, int tid) {
        //??????????????????
        TNewExercise oexercise = exerciseService.getById(oid);
        Integer oSortNum = oexercise.getSortNum();
        //????????????
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
     * @description: ???????????????????????????
     * @author:
     * @date: 2022/8/31 16:29
     * @param: [request, param]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    public List<TNewExercise> getExerciseCatalogueTree(HttpServletRequest request, ExerciseModel param) {
        //???????????????
        List<TNewExercise> exerciseCatalogueTree = exerciseMapper.getExerciseCatalogueTree(param);
        //???????????????
        TNewExercise exercise = (TNewExercise) new TNewExercise()
                .setId(0)
                .setExerciseName("?????????")
                .setChildrens(exerciseCatalogueTree)
                .setSortNum(0)
                .setDeleteFlag(0);
        //???????????????
        List<TNewExercise> tNewExercises = new ArrayList<>();
        tNewExercises.add(exercise);
        return tNewExercises;
    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2022/8/31 16:39
     * @param: [request, oid ??????id, tid ??????id]
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
        //??????????????????
        TNewExercise oexercise = exerciseService.getById(oid);
        //????????????????????????
        BusinessResponseEnum.EQUALSHOMEWORKMODELPACKAGE.assertIsTrue(oexercise.getParentId() != tid);
        oexercise.setParentId(tid);
        boolean res = exerciseService.saveOrUpdate(oexercise);
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: ???????????????????????????
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
            //??????
            res.set(exerciseService.updateById((TNewExercise) tNewExercise.setCreateTime(new Date()).setCreateUser(loginUser.getUserId())));
            BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res.get());
        } else {
            //??????
            tNewExercise.setElementType(1).setAuthType(2).setCreateTime(new Date()).setCreateUser(loginUser.getUserId());
            res.set(exerciseService.saveOrUpdate(tNewExercise));
            BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res.get());
            //??????????????????
            tNewExercise.setSortNum(tNewExercise.getId());
            res.set(exerciseService.saveOrUpdate(tNewExercise));
            BusinessResponseEnum.UPDATEFAIL.assertIsTrue(res.get());
        }
        return res.get();
    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/1 13:47
     * @param: [request, param]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TNewExercise saveExercise(HttpServletRequest request, TNewExerciseVo param) {
        //?????????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //???????????????
        TNewExercise exercise = saveAndUpdateNewExercise(param, loginUser);
        //???????????????
        updateExerciseKnowledge(param, exercise);
        //??????????????????
        saveAndUpdateExerciseInfo(param, loginUser, exercise);
        return exercise;
    }


    /**
     * @description: ????????????id????????????????????????
     * @author:
     * @date: 2022/9/1 14:44
     * @param: [request, exerciseId ??????id]
     * @return: boolean
     **/
    @Override
    public TNewExercise getExerciseInfo(HttpServletRequest request, int exerciseId) {
        TNewExercise exercise = exerciseMapper.getExercise(exerciseId);
        //?????????????????????
        BusinessResponseEnum.UNEXERCISEiNFO.assertNotNull(exercise, exerciseId);
        return exercise;
    }

    // ??????????????????
    @Override
    public void decideIsBand(TNewExercise exercise, int exerciseId) {
        // ???????????????????????????????????????
        List<Integer> isBandingModel = homeworkModelService.getIsBandingModel(exerciseId);
        boolean isBanding = isBandingModel != null && isBandingModel.size() > 1;
        //???????????????????????????
        exercise.setBandingModel(isBanding);
    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2022/9/2 15:54
     * @param: [request, exerciseId ??????id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExercise(HttpServletRequest request, int exerciseId) {
        List<Integer> ids = new ArrayList<>();
        //????????????
        TNewExercise exercise = exerciseService.getById(exerciseId);
        //??????????????????????????????????????????
        BusinessResponseEnum.UNEXERCISE.assertNotNull(exercise, exerciseId);
        //????????????????????????????????????????????????????????????????????????????????????
        BusinessResponseEnum.DUMPLICATEEXERCISE.assertIsFalse(exercise.getDeleteFlag() == 1, exerciseId);
        //???????????????
        getChildExercise(ids, exerciseId);
        return exerciseService.removeByIds(ids);
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/11/2 10:46
     * @param: [exerciseId]
     * @return: java.util.List<java.lang.Integer>
     **/
    private void getChildExercise(List<Integer> ids, int exerciseId) {
        //?????????????????????????????????????????????????????????
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
     * @description: ?????????????????????
     * @author:
     * @date: 2022/11/2 10:56
     * @param: [integerList, ids]
     * @return: void
     **/
    private void getChild(List<Integer> integerList, List<Integer> ids) {
        if (integerList != null && !integerList.isEmpty()) {
            //???????????????????????????
            validaChild(integerList);
            ids.addAll(integerList);
            //???????????????
            List<Integer> childIds = exerciseService.list(new QueryWrapper<TNewExercise>()
                    .in("parent_id", integerList))
                    .stream()
                    .map(TNewExercise::getId)
                    .collect(Collectors.toList());
            getChild(childIds, ids);
        }
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2023/1/10 16:34
     * @param: [childs ????????????]
     * @return: void
     **/
    private void validaChild(List<Integer> childs) {
        childs.forEach(item -> {
            //?????????????????????????????????????????????????????????
            int count = modelExerciseService.count(new QueryWrapper<TModelExercise>()
                    .eq("exercise_id", item)
                    .eq("delete_flag", 0));
            BusinessResponseEnum.CANNOTDELETEEXERCISE.assertIsFalse(count != 0, item);
        });

    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/7 13:50
     * @param: [request, param, result]
     * @return: boolean
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteExercise(HttpServletRequest request, TNewExerciseDelVO param) {
        //????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        List<Integer> exerciseIds = param.getExerciseIds();
        //??????id????????????
        List<TNewExercise> tNewExercises = exerciseService.listByIds(exerciseIds);
        //??????????????????
        List<TNewExercise> collect = tNewExercises.stream().peek(item -> item.setDeleteFlag(1)
                .setDeleteTime(new Date())
                .setDeleteUser(loginUser.getUserId())).collect(Collectors.toList());
        //????????????
        boolean res = exerciseService.saveOrUpdateBatch(collect);
        //??????????????????
        BusinessResponseEnum.DELFAIL.assertIsTrue(res);
        return res;
    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2022/9/7 14:59
     * @param: [request, exercise_id ??????????????????id]
     * @return: com.highgo.opendbt.exercise.domain.entity.TNewExercise
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TNewExercise copyExercise(HttpServletRequest request, int exerciseId) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        TNewExercise exercise = exerciseService.getById(exerciseId);
        //????????????
        reproduceExercise(exercise, loginUser);
        //??????????????????
        reproduceExerciseInfos(exercise, exerciseId, loginUser);
        //?????????????????????
        reproduceKnowledges(exercise, exerciseId);
        return exercise;
    }


    /**
     * @description: ??????ids?????????????????????
     * @author:
     * @date: 2022/9/9 14:40
     * @param: [ids, id ????????????id]
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
     * @description: ????????????????????????
     * @author:
     * @date: 2022/11/4 16:12
     * @param: [request, course_id ??????id]
     **/
    @Override
    public void exportExercise(HttpServletRequest request, HttpServletResponse response, int courseId) {
        // ??????????????????????????????
        List<TNewExerciseExcel> exercises = exerciseMapper.getExerciseByCourse(courseId);
        // ??????
        ExcelUtil.exportExcel(request, exercises, TNewExerciseExcel.class, "??????????????????", "?????????????????????", response);
    }

    /**
     * @description: ??????????????????
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
     * @description: ????????????
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
        // ???????????????????????????????????????????????????
        processImportDate(importExcel);
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/11/15 13:22
     * @param: [importExcel]
     * @return: void
     **/
    private void processImportDate(List<TNewExerciseExcel> importExcel) {
        //TODO ????????? importExcel.
    }

    /**
     * @description: ?????????????????????
     * @author:
     * @date: 2022/11/9 10:07
     * @param: [exercise, exercise_id, loginUser]
     * @return: void
     **/
    private void reproduceKnowledges(TNewExercise exercise, int exerciseId) {
        //???????????????????????????
        List<ExerciseKnowledge> exerciseKnowledges = exerciseKnowledgeService
                .list(new QueryWrapper<ExerciseKnowledge>()
                        .eq("exercise_id", exerciseId));
        //??????????????????
        List<ExerciseKnowledge> knowledges = exerciseKnowledges.stream().peek(item -> {
            item.setExerciseId(exercise.getId());
            item.setId(null);
        }).collect(Collectors.toList());
        //??????????????????
        boolean res = exerciseKnowledgeService.saveBatch(knowledges);
        //????????????????????????
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(res);
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/11/9 10:05
     * @param: [exercise, loginUser]
     * @return: void
     **/
    private void reproduceExercise(TNewExercise exercise, UserInfo loginUser) {
        //?????????????????????????????????+YYYYMMddHHmmss
        exercise.setExerciseName(exercise.getExerciseName() + "_??????")
                .setId(null)
                .setCreateTime(new Date())
                .setCreateUser(loginUser.getUserId());
        boolean res = exerciseService.save(exercise);
        //????????????????????????
        BusinessResponseEnum.SAVEFAIL.assertIsTrue(res);
    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/11/9 10:02
     * @param: [exercise, exercise_id]
     * @return: void
     **/
    private void reproduceExerciseInfos(TNewExercise exercise, int exerciseId, UserInfo loginUser) {
        //????????????????????????
        List<TExerciseInfo> infos = exerciseInfoService.list(new QueryWrapper<TExerciseInfo>()
                .eq("exercise_id", exerciseId)
                .eq("delete_flag", 0));
        if ((infos != null) && !infos.isEmpty()) {
            //???????????????????????????
            List<TExerciseInfo> newInfos = infos.stream().peek(item -> item.setId(null)
                    .setExerciseId(exercise.getId())
                    .setCreateTime(new Date())
                    .setCreateUser(loginUser.getUserId())).collect(Collectors.toList());
            //????????????????????????
            boolean res = exerciseInfoService.saveBatch(newInfos);
            //????????????????????????
            BusinessResponseEnum.SAVEFAIL.assertIsTrue(res);
            exercise.setExerciseInfos(newInfos);
        }
    }

    /**
     * @description: ????????????????????????
     * @author:
     * @date: 2022/9/2 10:50
     * @param: [param, exercise_id??????id]
     * @return: void
     **/
    public void saveAndUpdateExerciseInfo(TNewExerciseVo param, UserInfo loginUser, TNewExercise exercise) {
        Integer exerciseId = exercise.getId();
        //??????????????????
        List<TExerciseInfo> tExerciseInfo = new ArrayList<>();
        //????????????????????????
        List<TExerciseInfoVO> exerciseInfos = param.getExerciseInfos();
        //???????????????????????????id
        List<Integer> infoIds = exerciseInfoService.list(new QueryWrapper<TExerciseInfo>()
                .eq("exercise_id", exerciseId))
                .stream().map(TExerciseInfo::getId)
                .collect(Collectors.toList());
        if (!infoIds.isEmpty()) {
            //??????????????????
            boolean removeByIdsRes = exerciseInfoService.removeByIds(infoIds);
            //??????????????????
            BusinessResponseEnum.FAILDELETEEXERCISEINFO.assertIsTrue(removeByIdsRes);
        }
        //????????????????????????
        addExercise(tExerciseInfo, exerciseInfos, loginUser, exerciseId);
        if (!tExerciseInfo.isEmpty()) {
            boolean res = exerciseInfoService.saveOrUpdateBatch(tExerciseInfo);
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(res);
        }
        exercise.setExerciseInfos(tExerciseInfo);
    }




    /**
     * @description: ????????????
     * @author:
     * @date: 2022/9/28 14:51
     * @param: [tExerciseInfo, exerciseInfos, loginUser, exercise_id]
     * @return: void
     **/
    private void updateExercise(List<TExerciseInfo> tExerciseInfo, List<TExerciseInfoVO> exerciseInfos, UserInfo loginUser, int exerciseId) {
        //????????????
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
                //??????
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
     * @description: ????????????
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
     * @description: ??????????????????
     * @author:
     * @date: 2022/9/2 10:48
     * @param: [param]
     * @return: void
     **/
    public TNewExercise saveAndUpdateNewExercise(TNewExerciseVo param, UserInfo loginUser) {
        TNewExercise tNewExercise = new TNewExercise();
        BeanUtils.copyProperties(param, tNewExercise);

        //????????????
        if (param.getId() == null || param.getId() == -1) {
            tNewExercise.setCreateUser(loginUser.getUserId()).setCreateTime(new Date());
            tNewExercise.setId(null);
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseService.saveOrUpdate(tNewExercise));
            //?????????????????????
            tNewExercise.setSortNum(tNewExercise.getId());
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseService.saveOrUpdate(tNewExercise));
        } else {
            tNewExercise.setUpdateUser(loginUser.getUserId()).setUpdateTime(new Date());
            BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseService.saveOrUpdate(tNewExercise));
        }
        return tNewExercise;
    }

    /**
     * @description: ???????????????
     * @author:
     * @date: 2022/9/2 18:21
     * @param: [param, tNewExercise]
     * @return: void
     **/
    private void updateExerciseKnowledge(TNewExerciseVo param, TNewExercise tNewExercise) {
        List<ExerciseKnowledge> addExerciseKnowledge = new ArrayList<>();
        //???????????????????????????
        List<Knowledge> knowledge = param.getKnowledges();
        //???????????????
        //??????
        List<ExerciseKnowledge> list = exerciseKnowledgeService.list(new QueryWrapper<ExerciseKnowledge>()
                .eq("exercise_id", tNewExercise.getId())
                .eq("course_id", tNewExercise.getCourseId()));
        if ((list != null) && !list.isEmpty()) {
            //?????????id
            List<Integer> ids = list.stream().map(ExerciseKnowledge::getId).collect(Collectors.toList());
            //??????
            if (!ids.isEmpty()) {
                BusinessResponseEnum.DELFAIL.assertIsTrue(exerciseKnowledgeService.removeByIds(ids));
            }
        }
        if (knowledge == null || knowledge.isEmpty()) {
            return;
        }
        //???????????????
        knowledge.forEach(item ->
                addExerciseKnowledge.add(
                        new ExerciseKnowledge(null
                                , tNewExercise.getId()
                                , item.getKnowledgeId()
                                , tNewExercise.getCourseId()))
        );
        //??????
        BusinessResponseEnum.SAVEORUPDATEFAIL.assertIsTrue(exerciseKnowledgeService.saveBatch(addExerciseKnowledge));
    }

    /**
     * @description: ?????????????????????????????????????????????
     * @author:
     * @date: 2022/8/29 10:31
     * @param: [param]
     * @return: java.util.List<com.highgo.opendbt.exercise.domain.entity.TNewExercise>
     **/
    private List<TNewExercise> list(@Valid TNewExerciseDTO param) {

        List<TNewExercise> tNewExercises = exerciseMapper.listExercises(param);
        if (tNewExercises != null && !tNewExercises.isEmpty()) {
            //??????????????????????????????
            setExerciseCount(tNewExercises, param);
        }
        return tNewExercises;
    }

    /**
     * @description: ??????????????????????????????
     * @author:
     * @date: 2022/10/21 16:54
     * @param: [tNewExercises ?????????????????????, param????????????]
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
                //?????????????????????????????????
                exerciseCount.getAndAdd(item.getChildCount());
            }
        }).collect(Collectors.toList());

    }

    /**
     * @description: ??????????????????
     * @author:
     * @date: 2022/8/30 10:39
     * @param: [knowledgeExercises, allExercise, exerciseNum, param]
     * @return: int
     **/
    private int findChildrenCatalogueItem(List<TNewExercise> knowledgeExercises, List<TNewExercise> allExercise, AtomicInteger exerciseNum, TNewExercise param) {
        //???????????????
        List<TNewExercise> catalogues = knowledgeExercises.stream()
                .filter(exercise -> exercise.getElementType().equals(1))
                .collect(Collectors.toList());
        //??????????????????
        exerciseNum.addAndGet(knowledgeExercises.size() - catalogues.size());
        if (!catalogues.isEmpty()) {
            //?????????????????????????????????????????????
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
     * @description: ???????????????????????????????????????????????????
     * @author:
     * @date: 2022/8/30 10:23
     * @param: [item, exercise, param]
     * @return: boolean
     **/
    private boolean decide(TNewExercise item, TNewExercise exercise, TNewExercise param) {
        //??????id??????
        boolean ideq = item.getParentId().equals(exercise.getId());
        //??????????????????
        boolean typeeq = true;
        //?????????
        boolean knowledgeeq = true;
        if (item.getElementType().equals(0)) {
            //???????????????????????????
            if (!param.getExerciseTypeList().contains(item.getExerciseType())) {
                typeeq = false;
            }
            //?????????????????????
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



