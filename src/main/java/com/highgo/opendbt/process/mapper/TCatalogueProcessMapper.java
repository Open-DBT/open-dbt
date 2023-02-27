package com.highgo.opendbt.process.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.statistics.domain.model.StudentCatalogueStatistics;
import com.highgo.opendbt.statistics.domain.model.StudentStatistics;
import com.highgo.opendbt.statistics.domain.model.StatisticsParam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.highgo.opendbt.process.model.TCatalogueProcess
 */
@Repository
public interface TCatalogueProcessMapper extends BaseMapper<TCatalogueProcess> {


    CourseCatalogue countProcess(@Param("param") Map<String, Object> param);

    List<TCatalogueProcess> statisticslook(@Param("param") StatisticsParam param);

    List<StudentStatistics> statisticsStudent(@Param("courseId") int courseId, @Param("catalogueIds") List<Integer> catalogueIds, @Param("classId") int classId, @Param("userName") String userName, @Param("code") String code);

    //进度查询
    List<StudentCatalogueStatistics> findResourcesProcessByUser(@Param("item") CourseCatalogue item, @Param("classId") int classId, @Param("userId") int userId);

    //批量新增
    void saveBatch(@Param("param") List<TCatalogueProcess> insertCatalogueProcesses);

    //批量更新
    void updateBatch(@Param("param") List<TCatalogueProcess> updateCatalogueProcesses);
}




