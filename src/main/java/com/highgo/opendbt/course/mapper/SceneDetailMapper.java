package com.highgo.opendbt.course.mapper;

import com.highgo.opendbt.course.domain.model.SceneDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SceneDetailMapper {

    /**
     * 通过场景id获取场景明细
     *
     * @param sceneId
     * @return
     * @
     */
    public List<SceneDetail> getSceneDetailById(@Param("sceneId") int sceneId);

    public List<SceneDetail> getPublicSceneDetailById(@Param("sceneId") int sceneId);

    /**
     * 新增场景明细
     *
     * @param sceneId
     * @param sceneDetailList 场景相关表的list
     * @return
     * @
     */
    public Integer addSceneDetail(@Param("sceneId") int sceneId, @Param("sceneDetailList") List<SceneDetail> sceneDetailList);

    /**
     * 删除场景明细
     *
     * @param sceneId
     * @return
     * @
     */
    public Integer deleteSceneDetail(@Param("sceneId") int sceneId);

    /**
     * 通过课程id删除场景明细表
     *
     * @param courseId
     * @return
     * @
     */
    public Integer deleteSceneDetailByCourseId(@Param("courseId") int courseId);

}
