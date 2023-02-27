package com.highgo.opendbt.score.service.impl;

import com.highgo.opendbt.ApplicationContextRegister;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.score.domain.model.SubmitResult;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

public class VerifyAnswerCallable implements Callable<SubmitResult> {

	Logger logger = LoggerFactory.getLogger(getClass());

	private UserInfo loginUser;
	private Score score;
	private int exerciseSource;
	private boolean isSaveSubmitData;
	private int entranceType;

	public VerifyAnswerCallable(UserInfo loginUser, Score score, int exerciseSource, boolean isSaveSubmitData, int entranceType) {
		this.loginUser = loginUser;
		this.score = score;
		this.exerciseSource = exerciseSource;
		this.isSaveSubmitData = isSaveSubmitData;
		this.entranceType = entranceType;
	}

	@Override
	public SubmitResult call() {
		logger.info("VerifyAnswerCallable start");

		// 调用验证答案方法
		ScoreServiceImpl scoreServiceImpl = ApplicationContextRegister.getBean(ScoreServiceImpl.class);
		SubmitResult result = scoreServiceImpl.submitAnswer(loginUser, score, exerciseSource, isSaveSubmitData, entranceType);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("VerifyAnswerCallable end");
		return result;
	}

}
