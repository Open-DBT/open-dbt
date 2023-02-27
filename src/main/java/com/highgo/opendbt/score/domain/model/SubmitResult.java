package com.highgo.opendbt.score.domain.model;

import com.highgo.opendbt.course.domain.entity.Knowledge;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
@ToString
@Accessors(chain = true)
public class SubmitResult {

	private boolean executeRs = false; // 执行是否正确
	private boolean scoreRs = false; // 结果集是否正确
	private String log; // JDBC日志
	private String errorMessage; // 错误提示
	private int usageTime; // 用时
	private List<Knowledge> list; // 所用知识点list
	private int coverage = 0; // 覆盖率

	private int answerExecuteTime = 0;// 答案执行时间

	private Map<String, Object> studentResultMap = new LinkedHashMap<String, Object>(); // 学生答案结果集

	public boolean isExecuteRs() {
		return executeRs;
	}

	public void setExecuteRs(boolean executeRs) {
		this.executeRs = executeRs;
	}

	public boolean isScoreRs() {
		return scoreRs;
	}

	public void setScoreRs(boolean scoreRs) {
		this.scoreRs = scoreRs;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getUsageTime() {
		return usageTime;
	}

	public void setUsageTime(int usageTime) {
		this.usageTime = usageTime;
	}

	public List<Knowledge> getList() {
		return list;
	}

	public void setList(List<Knowledge> list) {
		this.list = list;
	}

	public int getCoverage() {
		return coverage;
	}

	public void setCoverage(int coverage) {
		this.coverage = coverage;
	}

	public int getAnswerExecuteTime() {
		return answerExecuteTime;
	}

	public void setAnswerExecuteTime(int answerExecuteTime) {
		this.answerExecuteTime = answerExecuteTime;
	}

	public Map<String, Object> getStudentResultMap() {
		return studentResultMap;
	}

	public void setStudentResultMap(Map<String, Object> studentResultMap) {
		this.studentResultMap = studentResultMap;
	}

	public SubmitResult() {
		super();
	}

	public SubmitResult(List<Knowledge> list, int usageTime, int coverage) {
		super();
		this.usageTime = usageTime;
		this.coverage = coverage;
		this.list = list;
	}

}
