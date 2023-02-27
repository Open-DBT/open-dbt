package com.highgo.opendbt.course.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(value = "t_exercise_knowledge")
@Data
@ToString
@AllArgsConstructor
@Accessors(chain = true)
public class ExerciseKnowledge implements Serializable {
	@TableId(value = "id", type = IdType.AUTO)
	private Integer id = -1; // id
	@TableField(value = "exercise_id")
	private int exerciseId; // 习题id
	@TableField(value = "knowledge_id")
	private int knowledgeId; // 知识点id
	@TableField(value = "course_id")
	private int courseId; // 课程id



}
