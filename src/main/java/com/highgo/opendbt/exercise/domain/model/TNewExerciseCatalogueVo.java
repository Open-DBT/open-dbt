package com.highgo.opendbt.exercise.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@Data
@ToString
@Accessors(chain = true)
public class TNewExerciseCatalogueVo implements Serializable {
    /**
     *
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 课程id
     */
    @NotNull(message = "课程id不能为空")
    private Integer courseId;

    /**
     * 父类id
     */
    @NotNull(message = "父类习题id不能为空")
    @TableField(value = "parent_id")
    private Integer parentId;

    /**
     * 场景id
     */
    @TableField(value = "scene_id")
    private Integer sceneId;

    /**
     * 场景名称
     */
    @TableField(value = "scene_name")
    private String sceneName;

    /**
     * 0:试题，1:文件夹
     */
    @TableField(value = "element_type")
    private Integer elementType;

    /**
     * 习题名称
     */
    @NotNull(message = "文件夹名称不能为空")
    @TableField(value = "exercise_name")
    private String exerciseName;

    /**
     * 习题描述 备用
     */
    @TableField(value = "exercise_desc")
    private String exerciseDesc;

    /**
     * 1:私有，2:共享
     */
    @TableField(value = "auth_type")
    private Integer authType;

    /**
     * 试题类型 1：单选2：多选3：判断4：填空5：简答6：数据库题
     */
    @TableField(value = "exercise_type")
    private Integer exerciseType;

    /**
     * 试题难度 1：简单 2：一般3：困难
     */
    @TableField(value = "exercise_level")
    private Integer exerciseLevel;

    /**
     * 题干
     */
    @TableField(value = "stem")
    private String stem;

    /**
     * 选择题为prefix，多选逗号隔开。判断题答案只有true false,简答程序题答具体案描
     */
    @TableField(value = "standard_answser")
    private String standardAnswser;

    /**
     * 数据库答案
     */
    @TableField(value = "answer")
    private String answer;

    /**
     * 答案解析
     */
    @TableField(value = "exercise_analysis")
    private String exerciseAnalysis;

    /**
     * 排序序号
     */
    @TableField(value = "sort_num")
    private Integer sortNum;
    /**
     * 创建人员
     */
    @TableField(value = "create_user")
    private Integer createUser;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 修改人员
     */
    @TableField(value = "update_user")
    private Integer updateUser;

    /**
     * 删除标志0：未删除1：已删除
     */
    @TableField(value = "delete_flag")
    private Integer deleteFlag;

    /**
     * 删除时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "delete_time")
    private Date deleteTime;

    /**
     * 删除人员
     */
    @TableField(value = "delete_user")
    private Integer deleteUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}
