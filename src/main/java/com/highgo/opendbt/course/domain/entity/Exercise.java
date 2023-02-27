package com.highgo.opendbt.course.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.course.domain.model.Scene;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
@TableName(value = "t_exercise")
@Data
@ToString
@Accessors(chain = true)
public class Exercise {
	@TableId(value = "id", type = IdType.AUTO)
	private int exerciseId = -1; // 习题id
	@TableField(value = "course_id")
	private int courseId; // 课程
	@TableField(exist = false)
	private String courseName; // 课程名
	@TableField(value = "scene_id")
	private int sceneId = -1; // 场景id
	@TableField(exist = false)
	private String sceneName; // 场景名
	@TableField(value = "exercise_name")
	private String exerciseName; // 习题名
	@TableField(value = "exercise_desc")
	private String exerciseDesc; // 习题描述
	@TableField(value = "answer")
	private String answer; // 参考答案
	@TableField(exist = false)
	private int[] knowledgeIds = new int[0]; // 关联知识点的id的数组
	@TableField(exist = false)
	private String[] knowledgeNames = new String[0]; // 关联知识点的name,用于习题查看页面，习题知识点显示
	@TableField(exist = false)
	private Scene scene; // 关联场景的信息
	@TableField(exist = false)
	private String stuAnswer; // 学生最新答题的答案
	@TableField(exist = false)
	private String score; // 学生答题成绩
	@TableField(exist = false)
	private String ct; // 学生最新的答题时间
	@TableField(value = "parent_id")
	private int parentId = 0; // 复制的习题的id
	@TableField(value = "creator")
	private int creator;
	@TableField(value = "create_time")
	private String createTime;
	@TableField(value = "update_time")
	private String updateTime;

}
