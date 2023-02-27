package com.highgo.opendbt.homeworkmodel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel;
import com.highgo.opendbt.homeworkmodel.domain.model.ListHomeWorkModel;
import com.highgo.opendbt.homeworkmodel.domain.model.PublishClass;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.highgo.opendbt.homeworkmodel.domain.entity.THomeworkModel
 */
@Repository
public interface THomeworkModelMapper extends BaseMapper<THomeworkModel> {
    //根据习题id判断是否绑定了作业模板
    List<Integer> getIsBandingModel(int exerciseId);

    //查询作业模板
    List<THomeworkModel> listHomeworkModel(@Param("param") ListHomeWorkModel param);

    //作业库目录查询
    List<THomeworkModel> getHomeWorkModelCatalogueTree(Integer courseId);

    //目录树结构子查询
    List<THomeworkModel> getHomeWorkModelTreeChildren();

    //发布班级查询
    List<PublishClass> getPublishList(@Param("courseId") Integer courseId,@Param("userId") Integer userId);

    //发布班级学生
    List<PublishClass> getPublishStudentList();
}




