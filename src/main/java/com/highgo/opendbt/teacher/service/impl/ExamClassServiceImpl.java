package com.highgo.opendbt.teacher.service.impl;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.service.UserInfoService;
import com.highgo.opendbt.teacher.mapper.ExamClassMapper;
import com.highgo.opendbt.teacher.domain.model.ExamClass;
import com.highgo.opendbt.teacher.service.ExamClassService;
import com.highgo.opendbt.common.utils.Authentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class ExamClassServiceImpl implements ExamClassService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExamClassMapper examClassMapper;

	@Autowired
	private UserInfoService userInfoService;

	@Override
	public ResultTO<List<ExamClass>> getExamClassListByCourseId(HttpServletRequest request, int courseId) {
		try {
			// 获取用户信息
			UserInfo loginUser = Authentication.getCurrentUser(request);

			return ResultTO.OK(examClassMapper.getExamClassListByCourseId(loginUser.getUserId(), courseId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	public ResultTO<List<ExamClass>> getExamClassListByClassId(HttpServletRequest request, int classId) {
		try {
			// 获取用户信息
			UserInfo loginUser =Authentication.getCurrentUser(request);

			return ResultTO.OK(examClassMapper.getExamClassListByClassId(loginUser.getUserId(), classId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultTO<Integer> deleteExamClassById(int examClassId) {
		try {
			return ResultTO.OK(examClassMapper.deleteExamClassById(examClassId));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultTO<Void> saveExamClass(HttpServletRequest request, ExamClass examClass) {
		try {
			UserInfo loginUser =Authentication.getCurrentUser(request);

			if (examClass.getId() == -1) {
				examClass.setCreator(loginUser.getUserId());
				examClassMapper.addExamClass(examClass);
				return ResultTO.OK();
			} else {
				examClassMapper.updateExamClass(examClass);
				return ResultTO.OK();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultTO.FAILURE(e.getMessage());
		}
	}

}
