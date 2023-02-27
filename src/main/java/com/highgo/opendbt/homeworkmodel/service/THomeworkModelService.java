package com.highgo.opendbt.homeworkmodel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.TNewExerciseDTO;
import com.highgo.opendbt.homework.domain.model.PublishHomeWork;
import com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel;
import com.highgo.opendbt.homeworkmodel.domain.model.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface THomeworkModelService extends IService<THomeworkModel> {
    //作业模板列表
    Map<String, Object> getHomeWorkModel(HttpServletRequest request, @Valid PageParam<ListHomeWorkModel> param);

    //保存文件夹
    boolean saveHomeWorkModelFolder(HttpServletRequest request, SaveHomeWorkModelFolder param);

    //删除
    boolean delHomeWorkModel(HttpServletRequest request, int id);

    //发布
    boolean publishHomeWork(HttpServletRequest request, PublishHomeWork param);

    //初始化作业相关表
    void InitHomeWorkTables(HttpServletRequest request, Integer classId, Integer userId);

    //删除作业相关表
    void delInitHomeWorkTables(HttpServletRequest request, Integer classId, List<Integer> userId);


    //复制作业模板
    THomeworkModel copyHomeWorkModel(HttpServletRequest request, int id);

    //根据作业模板id查询习题列表
    SaveHomeWorkModel getExercisesByModelId(HttpServletRequest request, Integer id,int flag);

    //根据习题id查询是否绑定了作业模板
    List<Integer> getIsBandingModel(int exerciseId);

    //根据习题id查询习题详情
    TNewExercise getExerciseInfo(HttpServletRequest request, int exerciseId, int modelId);

    //作业选题，习题列表
    Map<String, Object> getHomeWorkModelExercises(HttpServletRequest request, @Valid PageParam<TNewExerciseDTO> modelId);

    //选题
    THomeworkModel completedSelectedExercises(HttpServletRequest request, SelectedExercisesDTO param);

    //删除选中的习题
    boolean delSelectedExercises(HttpServletRequest request, int modelId, int exerciseId);

    //模板中习题保存
    TNewExercise saveExercise(HttpServletRequest request, EditNewExerciseDTO param);

    //模板中习题分数修改
    boolean updateScoreByModel(HttpServletRequest request, int exerciseId, int modelId, Double exerciseScore);

    //作业库目录树查询
    List<THomeworkModel> getHomeWorkModelCatalogueTree(HttpServletRequest request, Integer courseId);

    //作业模板移动
    boolean moveHomeWorkModel(HttpServletRequest request, int oid, int tid);

    //发布班级列表
    List<PublishClass> publishList(HttpServletRequest request, int courseId);
}
