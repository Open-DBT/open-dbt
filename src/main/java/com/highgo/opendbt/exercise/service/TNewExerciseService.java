package com.highgo.opendbt.exercise.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.common.bean.PageParam;
import com.highgo.opendbt.exercise.domain.entity.TExerciseType;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.*;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 *
 */
public interface TNewExerciseService extends IService<TNewExercise> {
    //获取习题列表
    Map<String, Object> getExercise(HttpServletRequest request, PageParam<TNewExerciseDTO> param);

    //排序
    boolean sortExercise(HttpServletRequest request, int oid, int tid);

    //习题目录树查询（只有目录结构）
    List<TNewExercise> getExerciseCatalogueTree(HttpServletRequest request, ExerciseModel param);

    //习题移动
    boolean moveExercise(HttpServletRequest request, int oid, int tid);

    //保存文件夹
    boolean saveExerciseCatalogue(HttpServletRequest request, TNewExerciseCatalogueVo param);

    //保存习题
    TNewExercise saveExercise(HttpServletRequest request, TNewExerciseVo param);

    //根据习题id 查询习题内容
    TNewExercise getExerciseInfo(HttpServletRequest request, int exerciseId);

    // 判定是否绑定
    void decideIsBand(TNewExercise exercise, int exerciseId);

    //删除习题
    boolean deleteExercise(HttpServletRequest request, int exerciseId);

    //批量删除习题
    boolean batchDeleteExercise(HttpServletRequest request, TNewExerciseDelVO param);

    //复制习题
    TNewExercise copyExercise(HttpServletRequest request, int exerciseId);

    //根据idsid 作业模板id查询习题及详情
    List<TNewExercise> getExercisesByIds(List<Integer> exerciseIds, int id, int flag);


    //导出
    void exportExercise(HttpServletRequest request, HttpServletResponse response, int courseId);

    //习题类型
    List<TExerciseType> getExerciseType(HttpServletRequest request);

    //导入
    void importExercise(HttpServletRequest request, MultipartFile file);

    //保存更新习题
    TNewExercise saveAndUpdateNewExercise(TNewExerciseVo param, UserInfo loginUser);

    //更新保存习题选项
    void saveAndUpdateExerciseInfo(TNewExerciseVo param, UserInfo loginUser, TNewExercise exercise);

}
