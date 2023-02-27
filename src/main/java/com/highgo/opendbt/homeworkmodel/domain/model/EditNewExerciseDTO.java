package com.highgo.opendbt.homeworkmodel.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.exercise.domain.model.TExerciseInfoVO;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 模板中习题编辑
 */

@Data
@ToString
@Accessors(chain = true)
public class EditNewExerciseDTO implements Serializable {
    /**
     *
     */
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
    private Integer parentId;

    /**
     * 场景id
     */
    private Integer sceneId;

    /**
     * 场景名称
     */
    private String sceneName;

    /**
     * 0:试题，1:文件夹
     */
    @NotNull(message = "试题/文件夹类型不能为空")
    private Integer elementType;

    /**
     * 习题名称
     */
    @NotBlank(message = "习题名称不能为空")
    private String exerciseName;

    /**
     * 习题描述 备用
     */
    private String exerciseDesc;

    /**
     * 1:私有，2:共享
     */
    private Integer authType;

    /**
     * 试题类型 1：单选2：多选3：判断4：填空5：简答6：数据库题
     */
    private Integer exerciseType;

    /**
     * 试题难度 1：简单 2：一般3：困难
     */
    @NotNull(message = "试题难度不能为空")
    private Integer exerciseLevel;

    /**
     * 题干
     */
    @NotBlank(message = "题干内容不能为空")
    private String stem;

    /**
     * 选择题为prefix，多选逗号隔开。判断题答案只有true false,简答程序题答具体案描
     */
    @NotBlank(message = "答案不能为空")
    private String standardAnswser;

    /**
     * 数据库答案
     */
    private String answer;

    /**
     * 答案解析
     */
    private String exerciseAnalysis;

    /**
     * 排序序号
     */
    private Integer sortNum;
    /**
     * 创建人员
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 修改人员
     */
    private Integer updateUser;

    /**
     * 删除标志0：未删除1：已删除
     */
    private Integer deleteFlag;

    /**
     * 删除时间
     */
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deleteTime;

    /**
     * 删除人员
     */
    private Integer deleteUser;

    private static final long serialVersionUID = 1L;

    /**
     * 知识点ID集合
     */
    private List<Integer> knowledgeIdList;
    /**
     * 题型集合
     */
    private List<Integer> exerciseTypeList;
    /**
     * 课程id集合
     */
    @NotEmpty(message = "课程id不能为空")
    private List<Integer> courseIdList;
    /**
     * 排序字段
     */
    private String sortField;
    /**
     * 排序类型
     */
    private String sortType;
    /**
     * 子节点
     */
    private List<EditNewExerciseDTO> childrens;

    /**
     * 选项集合
     */
    private List<TExerciseInfoVO> exerciseInfos;
    /**
     * 知识点
     */
    private List<Knowledge> knowledges;
    /**
     * 习题分数
     */
    private Double exerciseScore;
    /**
     * 模板id
     */
    private Integer modelId;
}
