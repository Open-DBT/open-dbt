package com.highgo.opendbt.homework.mapper;

import com.highgo.opendbt.common.batchOperation.RootMapper;
import com.highgo.opendbt.homework.domain.entity.TStuHomework;
import com.highgo.opendbt.homework.domain.model.ApprovalCount;
import com.highgo.opendbt.homework.domain.model.ApprovalList;
import com.highgo.opendbt.homework.domain.model.ListStudentHomeWork;
import com.highgo.opendbt.homework.domain.model.ApprovalCountVO;
import com.highgo.opendbt.homework.domain.model.HomeWrokByStudent;
import com.highgo.opendbt.homework.domain.model.TStuHomeworkVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.highgo.opendbt.homework.domain.entity.TStuHomework
 */
@Repository
public interface TStuHomeworkMapper extends RootMapper<TStuHomework> {
    //批阅列表
    List<TStuHomework> getApprovalList(@Param("param") ApprovalList param);

    //批阅列表数量统计
    ApprovalCountVO getApprovalCount(@Param("param") ApprovalCount param);

    //学生作业详情查看
    TStuHomeworkVO review(@Param("studentId") int studentId, @Param("homeworkId") int homeworkId);

    //学生端作业列表查询
    List<HomeWrokByStudent> getHomeWrokByStudent(@Param("param") ListStudentHomeWork param);
}




