package com.highgo.opendbt.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.model.ExerciseDisplay;
import com.highgo.opendbt.course.domain.model.ExercisePage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseMapper extends BaseMapper<Exercise> {

    /**
     * 获取课程的习题
     *
     * @param exercisePage
     * @return
     * @
     */
     List<Exercise> getExercise(ExercisePage exercisePage);

    /**
     * 获取课程的习题
     *
     * @param exercisePage
     * @return
     * @
     */
     List<Exercise> getExerciseList(ExercisePage exercisePage);

    /**
     * 根据课程获取全部习题
     *
     * @param courseId
     * @param orderBy        0-ASC,1-DESC
     * @param isSelectAnswer 0-不查答案,1-查答案
     * @return
     * @
     */
     List<Exercise> getExerciseByCourseId(@Param("courseId") int courseId, @Param("orderBy") int orderBy, @Param("isSelectAnswer") int isSelectAnswer);

    /**
     * 通过习题id获取习题信息
     *
     * @param exerciseId
     * @return
     * @
     */
     Exercise getExerciseById(@Param("exerciseId") int exerciseId);

     Exercise getPublicExerciseById(@Param("exerciseId") int exerciseId);

    /**
     * 通过场景id获取习题列表
     *
     * @param sceneId
     * @return
     * @
     */
     List<Exercise> getExerciseBySceneId(@Param("sceneId") int sceneId);

    /**
     * 通过习题id获取全部习题
     *
     * @param userId 用户id
     * @param sclassId 班级id
     * @param courseId 课程id
     * @param knowledgeId 知识点id
     * @return
     * @
     */
     List<Exercise> getExerciseInfoList(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId, @Param("knowledgeId") int knowledgeId);


    /**
     * @description: 通过习题id获取习题信息以及学生上次答题的答案
     * @author:
     * @date: 2023/1/9 16:27
     * @param: [userId 用户id, sclassId 班级id, courseId 课程id, exerciseId 习题id]
     * @return: java.util.List<com.highgo.opendbt.course.domain.entity.Exercise>
     **/
    ExerciseDisplay getExerciseInfo(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId, @Param("exerciseId") int exerciseId);

    /**
     * 新增习题
     *
     * @param exercise
     * @return
     * @
     */
     Integer addExercise(Exercise exercise);

    /**
     * 复制习题新增
     *
     * @param exercise
     * @return
     * @
     */
     Integer addCopyExercise(Exercise exercise);

    /**
     * 修改习题
     *
     * @param exercise
     * @return
     * @
     */
     Integer updateExercise(Exercise exercise);

    /**
     * 删除习题
     *
     * @param exerciseId 习题id
     * @return
     * @
     */
     Integer deleteExercise(@Param("exerciseId") int exerciseId);

    /**
     * 删除课程的所有习题
     *
     * @param courseId
     * @return
     * @
     */
     Integer deleteExerciseByCourseId(@Param("courseId") int courseId);

    /**
     * 获取当前学生的习题列表，包含答题结果和答题时间
     *
     * @param courseId
     * @param userId
     * @return
     * @
     */
     List<Exercise> getExerciseByStu(@Param("courseId") int courseId, @Param("userId") int userId);

    /**
     * @description:根据学生查询习题的基本信息以及做题情况
     * @author:
     * @date: 2023/1/9 16:27
     * @param: [userId 用户id, sclassId 班级id, courseId 课程id, knowledgeId 知识点id]
     * @return: java.util.List<com.highgo.opendbt.course.domain.entity.Exercise>
     **/
     List<Exercise> getExerciseInfoByStu(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId, @Param("knowledgeId") int knowledgeId);

    /**
     * 批量删除习题
     *
     * @param exerciseIds 习题id集合
     */
     Integer batchDeleteExercise(@Param("exerciseIds") int[] exerciseIds);


    /**
     * @description:
     * @author:
     * @date: 2023/1/9 16:28
     * @param: [sceneId 场景id, exerciseIds 习题id集合]
     * @return: void
     **/
     Integer batchBuildScene(@Param("sceneId") int sceneId, @Param("exerciseIds") int[] exerciseIds);


}
