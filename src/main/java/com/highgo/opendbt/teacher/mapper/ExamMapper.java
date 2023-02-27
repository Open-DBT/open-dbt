package com.highgo.opendbt.teacher.mapper;

import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.student.domain.model.StudentReportCard;
import com.highgo.opendbt.teacher.domain.model.Exam;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamMapper {

	 List<Exam> getExamList(@Param("userId") int userId, @Param("courseId") int courseId) ;

	 Exam getExamById(@Param("id") int id) ;

	 Integer addExam(Exam exam) ;

	 Integer updateExam(Exam exam) ;

	/**
	 * 根据id删除
	 * @param examId
	 * @return
	 * @
	 */
	 Integer deleteExam(@Param("examId") int examId) ;

	/**
	 * 课程作业指定班级
	 * @param examId
	 * @param courseId
	 * @param classIds
	 */
	 void addExamClass(@Param("examId")int examId, @Param("courseId")int courseId, @Param("classIds")int[] classIds);

	/**
	 * 根据作业id，查询指定班级
	 * @param examId
	 */
	 List<Sclass> getExamClassByExamId(@Param("examId")int examId);

	/**
	 * 删除历史关联记录
	 * @param examId
	 */
	 void deleteExamClassByExamId(@Param("examId")int examId);

	 List<Exam> getExamListBySclass(@Param("userId")int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId) ;

	 List<StudentReportCard> getExamStudentReportCard(@Param("sclassId") int sclassId, @Param("examId") int examId, @Param("examClassId") int examClassId, @Param("exerciseSource") int exerciseSource) ;

	 StudentReportCard getExamDetailByExamClassId(@Param("examClassId") int examClassId, @Param("exerciseSource") int exerciseSource) ;

}
