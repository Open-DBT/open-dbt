package com.highgo.opendbt.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 public interface KnowledgeMapper extends BaseMapper<Knowledge> {
    //获取新习题表相关知识点
     List<Knowledge> getKnowledgeByNewExerciseId(@Param("exerciseId") int exerciseId);

    /**
     * 获取课程的知识点
     *
     * @param courseId
     * @return
     * @
     */
     List<Knowledge> getKnowledge(@Param("courseId") int courseId);

    /**
     * 根据习题id获取知识点
     *
     * @param exerciseId
     * @return
     * @
     */
     List<Knowledge> getKnowledgeByExerciseId(@Param("exerciseId") int exerciseId);

    /**
     * 根据知识点id获取知识点
     *
     * @param knowledgeId
     * @return
     * @
     */
     Knowledge getKnowledgeByKnowledgeId(@Param("knowledgeId") int knowledgeId);

    /**
     * 新增知识点
     *
     * @param knowledge
     * @return
     * @
     */
     Integer addKnowledge(Knowledge knowledge);

    /**
     * 修改知识点
     *
     * @param knowledge
     * @return
     * @
     */
     Integer updateKnowledge(Knowledge knowledge);

    /**
     * 删除知识点
     *
     * @param knowledgeId
     * @return
     * @
     */
     Integer deleteKnowledge(@Param("knowledgeId") int knowledgeId);

    /**
     * 根据课程id删除知识点
     *
     * @param courseId
     * @return
     * @
     */
     Integer deleteKnowledgeByCourseId(@Param("courseId") int courseId);

}
