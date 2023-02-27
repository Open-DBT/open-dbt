package com.highgo.opendbt.login.mapper;

import org.springframework.stereotype.Repository;

import com.highgo.opendbt.login.model.CourseNotice;

@Repository
public interface CourseNoticeMapper {

	/**
	 * 新增课程通知
	 *
	 * @param courseNotice
	 * @return
	 * @throws Exception
	 */
	public Integer addCourseNotice(CourseNotice courseNotice) throws Exception;

}
