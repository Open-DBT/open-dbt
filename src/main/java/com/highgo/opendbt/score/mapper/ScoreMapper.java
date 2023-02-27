package com.highgo.opendbt.score.mapper;

import com.highgo.opendbt.score.domain.model.Score;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 public interface ScoreMapper {

	/**
	 * 添加学生做题成绩
	 *
	 * @param score
	 * @
	 */
	 void add(Score score) ;

	/**
	 * 获取学生学习课程的开始时间
	 *
	 * @param courseId
	 * @param userId
	 * @return
	 */
	 Score getStuStartTime(@Param("courseId") int courseId, @Param("userId") int userId);

	 List<Score> getStuAnswerSituation(@Param("sclassId") int sclassId, @Param("userId") int userId) ;

	 Score getStuExamAnswerSituation(@Param("sclassId") int sclassId, @Param("examId") int examId, @Param("examClassId") int examClassId, @Param("exerciseId") int exerciseId, @Param("userId") int userId) ;

	 Score getStuScoreById(@Param("scoreId") int scoreId) ;

	 Integer addExamScore(Score score) ;

	 Score getStuExamScoreById(@Param("scoreId") int scoreId) ;

}
