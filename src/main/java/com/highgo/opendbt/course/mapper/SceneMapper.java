package com.highgo.opendbt.course.mapper;

import com.highgo.opendbt.course.domain.model.Scene;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SceneMapper {

    /**
     * 获取课程的场景
     *
     * @param courseId
     * @return
     * @
     */
    public List<Scene> getScene(@Param("courseId") int courseId);

    /**
     * 正序获取课程的场景
     *
     * @param courseId
     * @return
     * @
     */
    public List<Scene> getSceneASC(@Param("courseId") int courseId);

    /**
     * 根据场景id获取场景明细
     *
     * @param sceneId
     * @return
     * @
     */
    public Scene getSceneDetail(@Param("sceneId") int sceneId);

    /**
     * 新增场景
     *
     * @param scene
     * @return
     * @
     */
    public Integer addScene(Scene scene);

    /**
     * 复制场景新增
     *
     * @param scene
     * @return
     * @
     */
    public Integer addCopyScene(Scene scene);

    /**
     * 修改场景
     *
     * @param scene
     * @return
     * @
     */
    public Integer updateScene(Scene scene);

    /**
     * 删除场景
     *
     * @param sceneId
     * @return
     * @
     */
    public Integer deleteScene(@Param("sceneId") int sceneId);

    /**
     * 通过课程id删除场景
     *
     * @param courseId
     * @return
     * @
     */
    public Integer deleteSceneByCourseId(@Param("courseId") int courseId);

}
