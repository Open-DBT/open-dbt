package com.highgo.opendbt.teacher.service;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.teacher.domain.model.ExamClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ExamClassService {

	/**
	 * 根据课程id，查询现有作业班级
	 *
	 * @param request
	 * @param courseId
	 * @return
	 */
	public ResultTO<List<ExamClass>> getExamClassListByCourseId(HttpServletRequest request, int courseId);

	/**
	 * 根据班级id，查询作业
	 * @param request
	 * @param classId
	 * @return
	 */
	public ResultTO<List<ExamClass>> getExamClassListByClassId(HttpServletRequest request, int classId);

	/**
	 * 根据id，删除作业班级表
	 *
	 * @param examClassId
	 * @return
	 */
	public ResultTO<Integer> deleteExamClassById(int examClassId);

	/**
	 * 发放作业到班级
	 *
	 * @param examClass
	 * @return
	 */
	public ResultTO<Void> saveExamClass(HttpServletRequest request, ExamClass examClass);

}
