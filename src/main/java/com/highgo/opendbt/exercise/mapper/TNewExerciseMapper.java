package com.highgo.opendbt.exercise.mapper;

import com.highgo.opendbt.common.batchOperation.RootMapper;
import com.highgo.opendbt.exercise.domain.entity.TNewExercise;
import com.highgo.opendbt.exercise.domain.model.TNewExerciseDTO;
import com.highgo.opendbt.exercise.domain.model.TNewExerciseExcel;
import com.highgo.opendbt.exercise.domain.model.ExerciseModel;
import com.highgo.opendbt.homeworkmodel.domain.model.NewExerciseDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import javax.validation.Valid;
import java.util.List;

/**
 * @Entity com.highgo.opendbt.exercise.domain.entity.TNewExercise
 */
@Repository
public interface TNewExerciseMapper extends RootMapper<TNewExercise> {
    //根据知识点等参数查询习题列表
    List<TNewExercise> listExercises(@Param("param") @Valid TNewExerciseDTO param);

    //查询每个文件夹下题目数量
    int selectExerciseCount(@Param("param") @Valid TNewExerciseDTO param, @Param("exercise") TNewExercise exercise);

    //统计题目个数
    int countExercises(@Param("param") TNewExercise param);

    //不根据知识点查询
    List<TNewExercise> listExercisesNoKnowledge(@Param("param") TNewExercise param);

    //目录树结构查询
    List<TNewExercise> getExerciseCatalogueTree(@Param("param") ExerciseModel param);

    //目录树结构子查询
    List<TNewExercise> getExerciseCatalogueTreeChildren();

    //根据习题id查询习题
    TNewExercise getExercise(int exerciseId);

    //根据习题ids 模板id查询习题
    List<TNewExercise> getExercises(@Param("param") List<Integer> param, @Param("modelId") int modelId);

    //详情列表查询
    List<TNewExercise> getExercisesDetail(@Param("param") List<Integer> param, @Param("modelId") int modelId);

    //根据习题id查询习题--用于作业查询
    NewExerciseDTO getExerciseById(@Param("exerciseId") int exerciseId, @Param("modelId") int modelId);

    //根据习题id查询习题--用于作业查询 不包含答案
    NewExerciseDTO getExerciseByIdUnAnswer(@Param("exerciseId") int exerciseId, @Param("modelId") int modelId);

    List<TNewExerciseExcel> getExerciseByCourse(@Param("courseId") int courseId);

    //练习重置
    List<Integer> findExerciseScoreInfos(@Param("userId") Integer userId, @Param("courseId") Integer courseId);
}




