package com.highgo.opendbt.statistics.domain.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
@ToString
public class StatisticsParam {
    @NotNull(message="课程id不能为空")
    private Integer courseId;
    @NotNull(message="目录id不能为空")
    private Integer catalogueId;
    @NotNull(message="班级id不能为空")
    private Integer classId;
    @NotNull(message="资源id不能为空")
    private Integer resourcesId;
    private String sortField;
    /*desc asc*/
    private String sortType;
    private String userName;
    private String code;

}
