package com.highgo.opendbt.catalogue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueResources;
import com.highgo.opendbt.statistics.domain.model.CatalogueStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.highgo.opendbt.catalogue.model.TCatalogueResources
 */
@Repository
public interface TCatalogueResourcesMapper extends BaseMapper<TCatalogueResources> {
    //教师端查看内容
    List<TCatalogueResources> findTCatalogueResources(Map<String, Object> param);

    //学习进度查询
    List<CatalogueStatistics> findResourcesAndComplateInfo(@Param("item") CourseCatalogue item, @Param("classId") int classId);

    //学生端查看内容
    List<TCatalogueResources> findTCatalogueResourcesbyStudent(@Param("param") Map<String, Object> param);
}




