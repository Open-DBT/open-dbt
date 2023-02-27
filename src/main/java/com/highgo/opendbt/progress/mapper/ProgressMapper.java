package com.highgo.opendbt.progress.mapper;

import com.highgo.opendbt.progress.model.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 public interface ProgressMapper {

	/**
	 * 获取学生的课程进度
	 *
	 * @param courseId
	 * @param userId
	 * @param sclassId
	 * @return
	 * @
	 */
	 CourseProgress getCourseProgress(@Param("courseId") int courseId, @Param("userId") int userId, @Param("sclassId") int sclassId) ;

	/**
	 * 查询学生某课程，每个知识点的习题数量，知识点学习进度
	 *
	 * @param userId
	 * @param sclassId
	 * @param courseId
	 * @param number
	 * @return
	 * @
	 */
	 List<StuKnowledgeExerciseInfo> getStuKnowledgeExerciseInfo(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId, @Param("number") int number) ;

	/**
	 * 查询无知识点的习题数量
	 *
	 * @param courseId
	 * @return
	 * @
	 */
	 StuKnowledgeExerciseInfo getNotKnowledgeExerciseCount(@Param("courseId") int courseId) ;

	/**
	 * 查询无知识点的习题信息
	 *
	 * @param userId
	 * @param sclassId
	 * @param courseId
	 * @return
	 * @
	 */
	 StuKnowledgeExerciseInfo getNotKnowledgeExerciseInfo(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId) ;

	/**
	 * 查询学生某课程，每个知识点的习题数量
	 *
	 * @param courseId
	 * @return
	 * @
	 */
	 List<KnowledgeExerciseCount> getKnowExerciseCountByCourseId(@Param("courseId") int courseId) ;

	/**
	 * 查询学生某课程，每个知识点的进度
	 *
	 * @param courseId
	 * @param userId
	 * @return
	 * @
	 */
	 List<KnowledgeExerciseCount> getStuCourseKnowledgeItemProgress(@Param("courseId") int courseId, @Param("userId") int userId) ;

	/**
	 * 班级统计--tab1 正确率 习题列表 | 答对人数、答题人数、全班人数
	 *
	 * @param sclassId
	 * @return
	 * @
	 */
	 List<SclassCorrect> getSclassCorrect(@Param("sclassId") int sclassId) ;

	/**
	 * 班级统计--tab2 覆盖率 学生列表 | 答对题数量、答过题数量、总题目数
	 *
	 * @param sclassId
	 * @return
	 * @
	 */
	 List<SclassCoverage> getSclassCoverage(@Param("sclassId") int sclassId) ;

	 List<SclassCoverage> getSclassCoverageByNameAndCode(@Param("sclassId") int sclassId, @Param("searchValue") String searchValue) ;

	/**
	 * 学生统计--tab1 正确率 习题列表 | 答对次数、答题次数 | 答对人数、答题人数、全班人数
	 *
	 * @param sclassId
	 * @param userId
	 * @return
	 * @
	 */
	 List<StudentCorrect> getStudentCorrect(@Param("sclassId") int sclassId, @Param("userId") int userId) ;

	/**
	 * 学生统计--tab2 覆盖率 当前学生 | 答对题数量、答过题数量、总题目数 | 所有学生答对题目平均值(答对人题数/总学生) |
	 * 所有学生答过题目平均值(做过人题数/总学生)
	 *
	 * @param sclassId
	 * @param userId
	 * @return
	 * @
	 */
	 List<StudentCoverage> getStudentCoverage(@Param("sclassId") int sclassId, @Param("userId") int userId) ;

}
