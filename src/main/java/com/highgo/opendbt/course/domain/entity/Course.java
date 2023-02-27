package com.highgo.opendbt.course.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@TableName(value ="t_course")
@Data
@ToString
@Accessors(chain = true)
public class Course implements Serializable {
	@TableId(value = "id",type= IdType.AUTO)
	private int courseId = -1; // 课程id
	@TableField(value = "course_name")
	private String courseName; // 课程名
	@TableField(value = "course_desc")
	private String courseDesc; // 课程描述
	@TableField(value = "course_outline")
	private String courseOutline; // 课程大纲
	@TableField(value = "knowledge_tree")
	private String knowledgeTree; // 知识树json串
	@TableField(value = "creator")
	private int creator; // 创建人id
	@TableField(exist = false)
	private String creatorName; // 创建人名
	@TableField(value = "create_time")
	private String createTime; // 创建时间
	@TableField(value = "is_open")
	private int isOpen = 0; // 是否发布
	@TableField(value = "cover_image")
	private String coverImage = "/cover/default.jpg"; // 课程封面图片
	@TableField(value = "delete_time")
	private String deleteTime; // 删除时间
	@TableField(value = "delete_flag")
	private int deleteFlag = 0; // 删除标记
	@TableField(value = "parent_id")
	private int parentId = 0; // 复制的课程的id
	@TableField(value = "is_finish")
	private int isFinish = 0; // 是否完成
	@TableField(exist = false)
	private int[] teachers = new int[0]; //有权限老师的id的数组
}
