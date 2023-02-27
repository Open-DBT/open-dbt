package com.highgo.opendbt.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.model.ExerciseDisplay;
import com.highgo.opendbt.course.domain.model.ExercisePage;
import com.highgo.opendbt.course.domain.model.ImportExerciseTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface ExerciseService extends IService<Exercise> {

	 List<Exercise> getExerciseList(ExercisePage exercisePage);

	 PageInfo<Exercise> getExercise(ExercisePage exercisePage);

	 Integer updateExercise(HttpServletRequest request, Exercise exercise);

	 Integer deleteExercise(HttpServletRequest request, int exerciseId);

	 Integer copyExercise(HttpServletRequest request, int exerciseId);

	 Integer copyExerciseToMyCourse(HttpServletRequest request, int exerciseId, int courseId);

	 Map<String, Object> testRunAnswer(HttpServletRequest request, Exercise exercise);

	 List<Exercise> getExerciseInfoList(HttpServletRequest request, int sclassId, int courseId, int knowledgeId);

	 List<Exercise> getExerciseListByCourseId(int courseId);

	 Exercise getExerciseById(int exerciseId);

	ExerciseDisplay getExerciseInfo(HttpServletRequest request, int sclassId, int courseId, int exerciseId);

	 String exportExerciseList(HttpServletRequest request, ExercisePage exercisePage) throws Exception;

	 String uploadExerciseListFile(HttpServletRequest request, MultipartFile file);

	 String importExercise(HttpServletRequest request, ImportExerciseTO importExerciseTO);

	 Integer batchDeleteExercise(HttpServletRequest request, int[] exerciseIds);

	 Integer batchBuildScene(int sceneId, int[] exerciseIds);

}
