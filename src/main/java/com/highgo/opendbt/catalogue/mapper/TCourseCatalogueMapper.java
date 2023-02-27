package com.highgo.opendbt.catalogue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.catalogue.domain.entity.CourseCatalogue;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.highgo.opendbt.catalogue.model.CourseCatalogue
 */
@Repository
public interface TCourseCatalogueMapper extends BaseMapper<CourseCatalogue> {
    //最大目录id
    Integer maxSortNum();

    //根据班级查询目录树发布信息
    List<CourseCatalogue> selectCataloguePublish(Map<String, Object> param);
}




