package com.highgo.opendbt.api;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.feedback.model.Feedback;
import com.highgo.opendbt.feedback.service.FeedbackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
/**
 * @Description: 意见反馈相关接口类
 * @Title: FeedbackApi
 * @Package com.highgo.opendbt.api
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@RestController
@CrossOrigin
@RequestMapping("/feedback")
public class FeedbackApi {

	Logger logger = LoggerFactory.getLogger(getClass());

	private final FeedbackService feedbackService;

	public FeedbackApi(FeedbackService feedbackService) {
		this.feedbackService = feedbackService;
	}

	/**
	 * 新增反馈
	 *
	 * @param request
	 * @param feedback
	 * @return
	 */
	@RequestMapping("/add")
	public Integer add(HttpServletRequest request, @RequestBody Feedback feedback) {
		logger.info("Enter, feedback = " + feedback.toString());
		return feedbackService.add(request, feedback);
	}

	/**
	 * 分页获取反馈
	 *
	 * @param pageTO
	 * @return
	 */
	@RequestMapping("/getFeedbackList")
	public PageInfo<Feedback> getFeedbackList(@RequestBody PageTO pageTO) {
		logger.debug("Enter, pageTO = " + pageTO.toString());
		return feedbackService.getFeedbackList(pageTO);
	}

}
