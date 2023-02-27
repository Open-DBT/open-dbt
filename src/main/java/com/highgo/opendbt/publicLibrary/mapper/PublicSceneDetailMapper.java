package com.highgo.opendbt.publicLibrary.mapper;

import com.highgo.opendbt.publicLibrary.model.PublicSceneDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicSceneDetailMapper {

	/**
	 * 通过场景id获取场景明细
	 *
	 * @param sceneId
	 * @return
	 * @throws Exception
	 */
	public List<PublicSceneDetail> getSceneDetailById(@Param("sceneId") int sceneId) throws Exception;

	/**
	 * 新增场景明细
	 *
	 * @param sceneId
	 * @param sceneDetailList 场景相关表的list
	 * @return
	 * @throws Exception
	 */
	public Integer addSceneDetail(@Param("sceneId") int sceneId, @Param("sceneDetailList") List<PublicSceneDetail> sceneDetailList) throws Exception;

	/**
	 * 删除场景明细
	 *
	 * @param sceneId
	 * @return
	 * @throws Exception
	 */
	public Integer deleteSceneDetail(@Param("sceneId") int sceneId) throws Exception;

}
