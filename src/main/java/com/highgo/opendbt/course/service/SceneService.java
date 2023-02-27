package com.highgo.opendbt.course.service;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.course.domain.model.ImportExerciseTO;
import com.highgo.opendbt.course.domain.model.Scene;
import com.highgo.opendbt.course.domain.model.ScenePage;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface SceneService {

	public PageInfo<Scene> getScene(ScenePage scenePage);

	public List<Scene> getShareScene(int courseId);

	public Scene getSceneDetail(int sceneId);

	public Integer updateScene(HttpServletRequest request, Scene scene);

	public Integer deleteScene(HttpServletRequest request, int sceneId);

	public Integer copyScene(int sceneId);

	public Integer copySceneToMyCourse(int sceneId, int courseId);

	public Integer testSceneSQLShell(HttpServletRequest request, String initShell);

	/**
	 * 根据课程id，导出全部场景
	 * @param request
	 * @param courseId
	 * @return
	 */
	public String exportSceneList(HttpServletRequest request, int courseId);

	/**
	 * 根据场景id，导出场景
	 * @param request
	 * @param sceneId
	 * @return
	 */
	public String exportSceneById(HttpServletRequest request, int sceneId);

	public String uploadSceneListFile(HttpServletRequest request, MultipartFile file);

	public String importScene(HttpServletRequest request, ImportExerciseTO importExerciseTO);


}
