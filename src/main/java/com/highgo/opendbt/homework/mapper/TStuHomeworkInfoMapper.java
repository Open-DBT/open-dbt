package com.highgo.opendbt.homework.mapper;

import com.highgo.opendbt.common.batchOperation.RootMapper;
import com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo;
import com.highgo.opendbt.homework.domain.model.TStuHomeworkInfoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.highgo.opendbt.homework.domain.entity.TStuHomeworkInfo
 */
@Repository
public interface TStuHomeworkInfoMapper extends RootMapper<TStuHomeworkInfo> {
    //根据作业id，学生id查询学生作业
    List<TStuHomeworkInfoVO> getHomeworkInfos(@Param("studentId") int studentId, @Param("homeworkId") int homeworkId);

    //根据学生端查询该作业该习题的信息
    TStuHomeworkInfo getHomeworkInfoAndExercise(@Param("homeworkId") Integer homeworkId, @Param("exerciseId") Integer exerciseId, @Param("studentId") int studentId);

    //查询作业详情
    List<TStuHomeworkInfoVO> review(@Param("studentId") int studentId, @Param("homeworkId") int homeworkId);

    //学生端查看详情没有答案
    List<TStuHomeworkInfoVO> UnAnswerreview(@Param("studentId") int studentId, @Param("homeworkId") int homeworkId);
}




