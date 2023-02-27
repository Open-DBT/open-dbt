package com.highgo.opendbt.homework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.homework.domain.entity.THomeworkDistribution;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.highgo.opendbt.homework.domain.entity.THomeworkDistribution
 */
@Repository
public interface THomeworkDistributionMapper extends BaseMapper<THomeworkDistribution> {
    //查询作业发放班级
    List<Sclass> getHomeWorkClazz(@Param("param") int id);
}




