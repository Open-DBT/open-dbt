package com.highgo.opendbt.course.mapper;

import com.highgo.opendbt.common.batchOperation.RootMapper;
import com.highgo.opendbt.course.domain.entity.ExerciseKnowledge;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseKnowledgeMapper extends RootMapper<ExerciseKnowledge> {

    /**
     * 通过课程id获取习题和知识点的关联关系
     *
     * @param courseId 课程id
     * @return
     * @
     */
    public List<ExerciseKnowledge> getExerciseKnowledgeByCourseId(@Param("courseId") int courseId);

    /**
     * 通过习题id获取习题和知识点的关联关系
     *
     * @param exerciseId 习题id
     * @return
     * @
     */
    public List<ExerciseKnowledge> getExerciseKnowledgeByExerciseId(@Param("exerciseId") int exerciseId);

    /**
     * 添加习题和知识点的关联关系（数组）
     *
     * @param courseId 课程id
     * @param exerciseId 习题id
     * @param knowledgeIds 知识点id集合
     * @return
     * @
     */
    public Integer addExerciseKnowledgeArray(@Param("courseId") int courseId, @Param("exerciseId") int exerciseId, @Param("knowledgeIds") int[] knowledgeIds);

    /**
     * 添加习题和知识点的关联关系（list）
     *
     * @param list
     * @return
     * @
     */
    public Integer addExerciseKnowledgeList(@Param("list") List<ExerciseKnowledge> list);

    /**
     * 通过习题id删除习题和知识点的关联关系
     *
     * @param exerciseId 习题id
     * @return
     * @
     */
    public Integer deleteByExerciseId(@Param("exerciseId") int exerciseId);

    /**
     * 通过知识点id删除习题和知识点的关联关系
     *
     * @param knowledgeId 知识点id
     * @return
     * @
     */
    public Integer deleteByKnowledgeId(@Param("knowledgeId") int knowledgeId);

    /**
     * 通过课程id删除习题和知识点的关联关系
     *
     * @param courseId 课程id
     * @return
     * @
     */
    public Integer deleteByCourseId(@Param("courseId") int courseId);

}
