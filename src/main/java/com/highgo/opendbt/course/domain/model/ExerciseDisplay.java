package com.highgo.opendbt.course.domain.model;

import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.exercise.domain.entity.TExerciseInfo;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString
@Accessors(chain = true)
public class ExerciseDisplay {
	private int exerciseId = -1; // 习题id
	private int courseId; // 课程
	private String courseName; // 课程名
	private int sceneId = -1; // 场景id
	private String sceneName; // 场景名
	private String exerciseName; // 习题名
	private String exerciseDesc; // 习题描述
	private String answer; // 参考答案
	private int[] knowledgeIds = new int[0]; // 关联知识点的id的数组
	private String[] knowledgeNames = new String[0]; // 关联知识点的name,用于习题查看页面，习题知识点显示
	private Scene scene; // 关联场景的信息
	private String stuAnswer; // 学生最新答题的答案
	private String score; // 学生答题成绩
	private String ct; // 学生最新的答题时间
	private int parentId = 0; // 复制的习题的id
	private int creator;
	private String createTime;
	private String updateTime;
	/**
	 * 选择题为prefix，多选逗号隔开。判断题答案只有true false,简答程序题答具体案描
	 */
	private String standardAnswser;
	/**
	 * 试题类型 1：单选2：多选3：判断4：填空5：简答6：数据库题
	 */
	private Integer exerciseType;
	/**
	 * 答案解析
	 */
	private String exerciseAnalysis;

	/**
	 * 选项
	 */
	private List<TExerciseInfo> exerciseInfos;

	/**
	 * 知识点
	 */
	private List<Knowledge> knowledge;

}
