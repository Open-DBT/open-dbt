package com.highgo.opendbt.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.common.bean.CourseKnowledgeTO;
import com.highgo.opendbt.course.domain.entity.Knowledge;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface KnowledgeService extends IService<Knowledge> {

	public String getKnowledge(int courseId);

	public List<Knowledge> getKnowledgeNotTree(int courseId);

	public Integer updateKnowledge(HttpServletRequest request, CourseKnowledgeTO courseKnowledge);
}
