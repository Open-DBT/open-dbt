package com.highgo.opendbt.homeworkmodel.domain.model;

import lombok.Data;

import java.util.List;

/**
 * @Description: 发布班级列表
 * @Title: PublishClass
 * @Package com.highgo.opendbt.homeworkmodel.domain.model.param
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/29 11:07
 */
@Data
public class PublishClass {
    //id
    private Integer key;
    //名称
    private String title;
    //学号
    private String code;
    private List<PublishClass> children;
}
