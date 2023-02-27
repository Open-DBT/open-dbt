package com.highgo.opendbt.teacher.mapper;

import com.highgo.opendbt.teacher.domain.model.ExamClass;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamClassMapper {

    ExamClass getExamClassById(@Param("examClassId") int examClassId);

    /**
     * 根据课程，查询现有班级作业
     *
     * @param userId
     * @param courseId
     * @return
     * @
     */
    List<ExamClass> getExamClassListByCourseId(@Param("userId") int userId, @Param("courseId") int courseId);

    /**
     * 根据班级id，查询作业列表
     *
     * @param userId
     * @param courseId
     * @return
     * @
     */
    List<ExamClass> getExamClassListByClassId(@Param("userId") int userId, @Param("classId") int courseId);


    /**
     * 根据id删除
     *
     * @param examClassId
     * @return
     * @
     */
    Integer deleteExamClassById(@Param("examClassId") int examClassId);

    /**
     * 保存作业班级
     *
     * @param examClass
     */
    void addExamClass(ExamClass examClass);

    /**
     * 更新作业班级
     *
     * @param examClass
     */
    void updateExamClass(ExamClass examClass);

    ExamClass getExamDetailById(@Param("examClassId") int examClassId);


}
