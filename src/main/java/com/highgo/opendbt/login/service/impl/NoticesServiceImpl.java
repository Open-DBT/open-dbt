package com.highgo.opendbt.login.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.JwtUtil;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.login.mapper.NoticesMapper;
import com.highgo.opendbt.login.model.Notice;
import com.highgo.opendbt.login.model.NoticesListTO;
import com.highgo.opendbt.login.model.Upcom;
import com.highgo.opendbt.login.service.NoticesService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticesServiceImpl implements NoticesService {

	Logger logger = LoggerFactory.getLogger(getClass());

	private final NoticesMapper noticesMapper;



	@Override
	public NoticesListTO getNotices(HttpServletRequest request) {
			// 获取用户信息
			UserInfo loginUser = Authentication.getCurrentUser(request);
			NoticesListTO noticesList = new NoticesListTO();
			// 通知
			List<Notice> noticeList = noticesMapper.getNotice(loginUser.getUserId());
			// 待办
			List<Upcom> upcomList = new ArrayList<>();
			noticesList.setNoticeList(noticeList);
			noticesList.setUpcomList(upcomList);
			noticesList.setCount(noticeList.size() + upcomList.size());
			return noticesList;
	}

	@Override
	public ResultTO<NoticesListTO> getNoticesNotRead(HttpServletRequest request) {
		try {
			// 获取用户信息
			UserInfo loginUser =Authentication.getCurrentUser(request);

			NoticesListTO noticesList = new NoticesListTO();

			// 通知
			List<Notice> noticeList = noticesMapper.getNoticeNotRead(loginUser.getUserId());

			// 待办
			List<Upcom> upcomList = new ArrayList<>();

			noticesList.setNoticeList(noticeList);
			noticesList.setUpcomList(upcomList);
			noticesList.setCount(noticeList.size() + upcomList.size());
			// 刷新token
			String newToken = refreshToken(request);
			return ResultTO.OK(noticesList, newToken);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	private String refreshToken(HttpServletRequest request) {
		// 获取token
		String requestTokenHeader = request.getHeader(JwtUtil.HEADER_STRING);
		logger.info("preHandle authorization = " + requestTokenHeader);

		if (requestTokenHeader != null && requestTokenHeader.startsWith(JwtUtil.TOKEN_PREFIX)) {
			String token = requestTokenHeader.replace(JwtUtil.TOKEN_PREFIX, "");
			// 解析token
			DecodedJWT jwt = JWT.decode(token);
			// 从token中获取过期时间，时间为毫秒数
			long expiresTime = TimeUtil.dateConvertMS(jwt.getExpiresAt());
			// 获取当前时间毫秒数
			long currentTime = System.currentTimeMillis();
			// 如果token过期时间小于五分钟就获取新token
			if (expiresTime - currentTime <= 300000) {
				UserInfo user = new UserInfo();
				// 获取token中的学号
				user.setCode(jwt.getClaim(JwtUtil.USER_CODE).asString());
				// 生成新token
				return JwtUtil.generateToken(user);
			}
		}
		return "";
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer changeRead(HttpServletRequest request, int type, int noticeId) {
			// 获取用户信息
			UserInfo loginUser =Authentication.getCurrentUser(request);

			// 1=>通知 2=>待办
			if (type == 2) {
				return 2;
			} else {
				return noticesMapper.changeNoticeRead(loginUser.getUserId(), noticeId);
			}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public Integer clearNotRead(HttpServletRequest request, int type) {
			// 获取用户信息
			UserInfo loginUser =Authentication.getCurrentUser(request);
			// 1=>通知 2=>待办
			if (type == 2) {
				return 2;
			} else {
				List<Notice> noticeList = noticesMapper.getNoticeNotRead(loginUser.getUserId());
				return noticesMapper.clearNotReadNotice(loginUser.getUserId(), noticeList);
			}

	}

}
