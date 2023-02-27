package com.highgo.opendbt.homework.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.homework.domain.entity.THomework;
import com.highgo.opendbt.homework.domain.model.ListHomeWork;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.highgo.opendbt.homework.domain.entity.THomework
 */
@Repository
public interface THomeworkMapper extends BaseMapper<THomework> {
    //作业列表查询
    List<THomework> getHomeWorkLilst(@Param("param") ListHomeWork param);
}




