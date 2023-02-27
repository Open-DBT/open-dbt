package com.highgo.opendbt.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description: entity基类
 * @Title: BaseEntity
 * @Package com.highgo.opendbt.common.entity
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/1 13:43
 */
@Data
@Accessors(chain = true)
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    public Date createTime;

    /**
     * 创建人员
     */
    @TableField(value = "create_user")
    public Integer createUser;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    public Date updateTime;

    /**
     * 修改人员
     */
    @TableField(value = "update_user")
    public Integer updateUser;

    /**
     * 删除标记
     */
    @TableField(value = "delete_flag")
    public Integer deleteFlag;

    /**
     * 删除时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "delete_time")
    public Date deleteTime;

    /**
     * 删除人员
     */
    @TableField(value = "delete_user")
    public Integer deleteUser;
}
