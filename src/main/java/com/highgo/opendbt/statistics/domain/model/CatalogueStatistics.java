package com.highgo.opendbt.statistics.domain.model;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@ToString
@Accessors(chain = true)
public class CatalogueStatistics implements Serializable {
    /**
     * 资源id
     */
    private Integer resourcesId;
    /**
     * 资源名称
     */
    private String resourcesName;
    /**
     * 资源类型
     */
    private String resourcesType;
    /**
     * 视频时长
     */
    private Integer resourcesTime;
    /**
     * 总学生数
     */
    private Integer totalNum;
    /**
     * 已完成学生数
     */
    private Integer completeNum;
}
