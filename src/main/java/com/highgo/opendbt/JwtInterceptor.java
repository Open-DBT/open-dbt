package com.highgo.opendbt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.utils.JwtUtil;
import com.highgo.opendbt.common.utils.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * @Description: jwt相关配置
 * @Title: JwtInterceptor
 * @Package com.highgo.opendbt
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/9/6 15:37
 */
@CrossOrigin
public class JwtInterceptor implements HandlerInterceptor {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if ("OPTIONS".equals(request.getMethod())) {
			response.setStatus(HttpServletResponse.SC_OK);
			return true;
		}

		if (request.getRequestURI().contains("/avatar/")
				|| request.getRequestURI().contains("/files/")
				|| request.getRequestURI().contains("/cover/")
				|| request.getRequestURI().contains("/dataTypeImg/")
				|| request.getRequestURI().contains("/temp/")
				|| request.getRequestURI().contains("/readResourse/")
				|| request.getRequestURI().contains("/swagger")
				|| request.getRequestURI().contains("/webjars")
				|| request.getRequestURI().contains("/migration/migrationExercise")
		) {
			return true;
		}

		String requestTokenHeader = request.getHeader(JwtUtil.HEADER_STRING);
		logger.info("Request URL = " + request.getRequestURI() + ", preHandle authorization = " + requestTokenHeader);

		String json = null;
		try {
			if (requestTokenHeader != null && requestTokenHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
				JwtUtil.verify(requestTokenHeader.replace(JwtUtil.TOKEN_PREFIX, ""));
				return true;
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		ResultTO<Void> result = ResultTO.FAILURE(Message.get("InvalidToken"), 401);
		json = new ObjectMapper().writeValueAsString(result);
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().println(json); // 返回前台数据

		return false;
	}


}
