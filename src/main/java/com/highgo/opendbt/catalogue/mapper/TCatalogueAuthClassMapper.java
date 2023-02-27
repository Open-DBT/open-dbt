package com.highgo.opendbt.catalogue.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.catalogue.domain.entity.TCatalogueAuthClass;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.highgo.opendbt.catalogue.model.TCatalogueAuthClass
 */
@Repository
public interface TCatalogueAuthClassMapper extends BaseMapper<TCatalogueAuthClass> {
    /**
     * @description: 查询章节目录的可见性
     * @author:
     * @date: 2022/7/21 14:58
     * @param: userId 用户id
     * @param: courseId 课程id
     * @param: catalogueId 目录id
     * @return:
     **/
    List<Integer> queryVisibility(@Param("userId") int userId, @Param("courseId") int courseId, @Param("catalogueId") int catalogueId);
}




