package com.highgo.opendbt.teacher.service.impl;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.utils.Message;
import com.highgo.opendbt.score.mapper.ScoreMapper;
import com.highgo.opendbt.score.domain.model.Score;
import com.highgo.opendbt.student.mapper.StudentExamMapper;
import com.highgo.opendbt.student.domain.model.ExerciseReportCard;
import com.highgo.opendbt.student.domain.model.StudentExamExercise;
import com.highgo.opendbt.teacher.mapper.ExamClassMapper;
import com.highgo.opendbt.teacher.mapper.ExamMapper;
import com.highgo.opendbt.teacher.mapper.ExamExerciseMapper;
import com.highgo.opendbt.teacher.domain.model.*;
import com.highgo.opendbt.teacher.service.ExamExerciseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExamExerciseServiceImpl implements ExamExerciseService {

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ExamExerciseMapper examExerciseMapper;

	@Autowired
	private StudentExamMapper studentExamMapper;

	@Autowired
	private ExamMapper examMapper;

	@Autowired
	private ExamClassMapper examClassMapper;

	@Autowired
	private ScoreMapper scoreMapper;

	public ResultTO<List<ExamExercise>> getExamExerciseByExamId(int examId) {
		try {
			Exam exam = examMapper.getExamById(examId);
			if (null == exam) {
				throw new Exception(Message.get("ExamInfoGetFile"));
			}

			return ResultTO.OK(examExerciseMapper.getExamExerciseByExamId(examId, exam.getExerciseSource()));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultTO<Void> saveExamExercise(int examId, ExamExerciseForm examExerciseForm) {
		try {
			Exam exam = examMapper.getExamById(examId);
			if (null == exam) {
				throw new Exception(Message.get("ExamInfoGetFile"));
			}

			List<ExamExercise> list = examExerciseMapper.getExamExerciseByExamId(examId, exam.getExerciseSource());
			/**
			 * ????????????????????? list?????????examExerciseForm?????????exerciseIds
			 */
			int[] exerciseIds = examExerciseForm.getExerciseIds();
			// ????????????????????????
			List<Integer> exercRes = new ArrayList<Integer>();

			for (int i = 0; i < exerciseIds.length; i++) {
				boolean exist = false;
				for (ExamExercise obj : list) {
					if (exerciseIds[i] == obj.getExerciseId()) {
						// ???????????????ID???????????????????????????
						exist = true;
						break;
					}
				}
				if (!exist) {
					exercRes.add(exerciseIds[i]);
				}
			}
			if (exercRes!=null&&!exercRes.isEmpty()) {
				int val = 0;
				if (list!=null&&!list.isEmpty()) {
					// ????????????????????????????????????????????????Mapper??????+1
					val = list.get(list.size() - 1).getOrdinal();
				}
				examExerciseMapper.addExamExerciseArray(examId, exercRes, val);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ResultTO.FAILURE(e.getMessage());
		}
		return ResultTO.OK();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultTO<Void> batchDelExamExercise(ExamExerciseForm examExerciseForm) {
		try {
			examExerciseMapper.batchDelExamExerciseById(examExerciseForm.getExerciseIds());
			return ResultTO.OK();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultTO<Void> sortExercise(int id, int newIndex) {
		try {
			ExamExercise src = examExerciseMapper.getExamExerciseById(id);
			if (src.getOrdinal() < newIndex) {
				/**
				 * ??????????????????????????????1)src?????????????????????????????????-1 2)src???=newIndex
				 * ??????row1?????????row3???newIndex???row3???orderIndex
				 */
				examExerciseMapper.updateSortSub(src.getExamId(), src.getOrdinal(), newIndex);
			} else {
				/**
				 * ????????????????????????????????????+1
				 */
				examExerciseMapper.updateSortAdd(src.getExamId(), src.getOrdinal(), newIndex);
			}
			examExerciseMapper.updateOrderById(id, newIndex);
			return ResultTO.OK();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	public ResultTO<List<ExerciseReportCard>> getStudentExerciseReportCard(int examClassId, int userId) {
		try {
			// ???????????????????????????
			ExamClass examClass = examClassMapper.getExamClassById(examClassId);
			if (null == examClass) {
				throw new Exception(Message.get("ExamInfoGetFile"));
			}

			// ??????????????????
			Exam exam = examMapper.getExamById(examClass.getExamId());
			if (null == exam) {
				throw new Exception(Message.get("ExamInfoGetFile"));
			}

			List<ExerciseReportCard> exerciseReportCardList = new ArrayList<ExerciseReportCard>();

			// ??????????????????????????????
			List<StudentExamExercise> studentExamExerciseList = studentExamMapper.getExamExerciseList(userId, examClass.getClassId(), examClass.getExamId(), examClassId, exam.getExerciseSource());
			for (StudentExamExercise itme : studentExamExerciseList) {
				ExerciseReportCard exerciseReportCard = new ExerciseReportCard();
				exerciseReportCard.setExerciseId(itme.getExerciseId());
				exerciseReportCard.setExerciseName(itme.getExerciseName());
				exerciseReportCard.setExerciseScore(itme.getExerciseScore());// ??????????????????
				exerciseReportCard.setExerciseSituation(itme.getExerciseSituation());// ??????????????????

				if (itme.getExerciseSituation() == 100) {
					exerciseReportCard.setExerciseGoal(itme.getExerciseScore());// ??????????????????
				}

				// ??????????????????????????????
				Score score = scoreMapper.getStuExamAnswerSituation(examClass.getClassId(), examClass.getExamId(), examClassId, itme.getExerciseId(), userId);
				if (null != score) {
					exerciseReportCard.setScoreId(score.getScoreId());
					exerciseReportCard.setAnswerExecuteTime(score.getAnswerExecuteTime());
					exerciseReportCard.setAnswerLength(score.getAnswerLength());
					exerciseReportCard.setAnswerTime(score.getCreateTime());
				}
				exerciseReportCardList.add(exerciseReportCard);
			}
			return ResultTO.OK(exerciseReportCardList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ResultTO.FAILURE(e.getMessage());
		}
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public ResultTO<Void> saveExamExerciseScore(int examId, ExamForm examForm) {
		try {
			for (int i = 0; i < examForm.getIds().length; i++) {
				examExerciseMapper.updateScore(examId, examForm.getIds()[i], examForm.getValues()[i]);
			}
			return ResultTO.OK();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return ResultTO.FAILURE(e.getMessage());
		}
	}

}
