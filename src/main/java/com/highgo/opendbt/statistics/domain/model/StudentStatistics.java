package com.highgo.opendbt.statistics.domain.model;

import lombok.Data;

@Data
public class StudentStatistics {
    /**
     * 学生名称
     */
    private String userName;
    /**
     * 学生id
     */
    private Integer userId;
    /**
     * 学号
     */
    private String code;

    /**
     * 总学生数
     */
    private Integer countNum;
    /**
     * 已完成学生数
     */
    private Integer completeNum;
}
