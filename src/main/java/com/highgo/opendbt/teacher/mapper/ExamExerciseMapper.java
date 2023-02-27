package com.highgo.opendbt.teacher.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.highgo.opendbt.teacher.domain.model.ExamExercise;

@Repository
public interface ExamExerciseMapper {

	public List<ExamExercise> getExamExerciseByExamId(@Param("examId") int examId, @Param("exerciseSource") int exerciseSource) throws Exception;

	public ExamExercise getExamExerciseById(@Param("id") int id) throws Exception;

	public Integer addExamExerciseArray(@Param("examId") int examId, @Param("exerciseIds") List<Integer> exerciseIds, @Param("orderIndex")int orderIndex) throws Exception;

	public Integer batchDelExamExerciseById(@Param("exerciseIds") int[] exerciseIds);

	/**
	 * 级联-1
	 * @param examId
	 * @param srcIndex 开始位置，不包含拖拽行
	 * @param tarIndex 结束位置
	 * @return
	 */
	public Integer updateSortSub(@Param("examId") int examId,@Param("srcIndex") int srcIndex,@Param("tarIndex") int tarIndex);

	/**
	 * 级联 +1
	 * @param examId
	 * @param srcIndex 开始位置，不包含拖拽行
	 * @param tarIndex 结束位置
	 * @return
	 */
	public Integer updateSortAdd(@Param("examId") int examId,@Param("srcIndex") int srcIndex,@Param("tarIndex") int tarIndex);

	/**
	 * 修改拖拽行为目标行
	 * @param id  关联表id
	 * @param tarIndex  目标行
	 * @return
	 */
	public Integer updateOrderById(@Param("id") int id,@Param("tarIndex") int tarIndex);


	public Integer updateScore(@Param("examId") int id,@Param("exerciseId") int exerciseId,@Param("score") int score);

}
