package com.highgo.opendbt.course.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;

@TableName(value = "t_exercise_knowledge")
@Data
@ToString
public class Knowledge {

    private int knowledgeId = -1; // 知识点id
    private int parentId = -1; // 知识点父类id
    private int courseId; // 课程id
    private String name; // 知识点名
    private String keyword; // 知识点关键字
    private String knowledgeDesc; // 知识点描述

    public Knowledge() {
    }

    public Knowledge(int knowledgeId, String name, String keyword) {
        this.knowledgeId = knowledgeId;
        this.name = name;
        this.keyword = keyword;
    }

    public Knowledge(int courseId, int parentId, String name, String keyword) {
        this.courseId = courseId;
        this.parentId = parentId;
        this.name = name;
        this.keyword = keyword;
    }

}
