package com.highgo.opendbt.teacher.service;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.student.domain.model.ExerciseReportCard;
import com.highgo.opendbt.teacher.domain.model.ExamExercise;
import com.highgo.opendbt.teacher.domain.model.ExamExerciseForm;
import com.highgo.opendbt.teacher.domain.model.ExamForm;

import java.util.List;

public interface ExamExerciseService {

	public ResultTO<List<ExamExercise>> getExamExerciseByExamId(int examId);

	public ResultTO<Void> saveExamExercise(int examId, ExamExerciseForm examExerciseForm);

	public ResultTO<Void> batchDelExamExercise(ExamExerciseForm examExerciseForm);

	public ResultTO<Void> sortExercise(int id, int newIndex);

	public ResultTO<List<ExerciseReportCard>> getStudentExerciseReportCard(int examClassId, int userId);

	/**
	 * 根据作业id，批量保存习题得分
	 *
	 * @param examId
	 * @param examForm 习题ids，得分values
	 * @return
	 */
	public ResultTO<Void> saveExamExerciseScore(int examId, ExamForm examForm);

}
