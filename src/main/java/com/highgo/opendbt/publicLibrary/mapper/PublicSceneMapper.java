package com.highgo.opendbt.publicLibrary.mapper;

import com.highgo.opendbt.publicLibrary.model.PublicScene;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicSceneMapper {

	//获取课程的场景
	public List<PublicScene> getScene() ;

	public List<PublicScene> getSceneNameList();

	/**
	 * 根据场景id获取场景明细
	 *
	 * @param sceneId
	 * @return
	 * @
	 */
	public PublicScene getSceneDetail(@Param("sceneId") int sceneId) ;

	/**
	 * 新增场景
	 *
	 * @param scene
	 * @return
	 * @
	 */
	public Integer addScene(PublicScene scene) ;

	/**
	 * 修改场景
	 *
	 * @param scene
	 * @return
	 * @
	 */
	public Integer updateScene(PublicScene scene) ;

	/**
	 * 删除场景
	 *
	 * @param sceneId
	 * @return
	 * @
	 */
	public Integer deleteScene(@Param("sceneId") int sceneId) ;

}
