package com.highgo.opendbt.exercise.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.common.entity.BaseEntity;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @TableName t_new_exercise  新习题表
 */
@TableName(value = "t_new_exercise")
@Data
@ToString
@Accessors(chain = true)
public class TNewExercise extends BaseEntity {
    /**
     *主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 父类id
     */
    @NotNull(message = "父类习题id不能为空")
    @TableField(value = "parent_id")
    private Integer parentId=0;

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
    @TableField(value = "exercise_name")
    private String exerciseName;

    /**
     * 习题描述 废弃
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
     * 废弃
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
     * 练习题状态 0：是练习题 1：非练习题
     */
        @TableField(value = "exercise_status")
    private Integer exerciseStatus;

    /**
     * 是否显示答案 0：显示答案 1：不显示答案
     */
    @TableField(value = "show_answer")
    private Integer showAnswer;

    /**
     * 知识点名称
     */
    @TableField(exist = false)
    private String knowledge;
    /**
     * 题型集合
     */
    @TableField(exist = false)
    private List<Integer> exerciseTypeList;
    /**
     * 课程id集合
     */
    @NotEmpty(message = "课程id不能为空")
    @TableField(exist = false)
    private List<Integer> courseIdList;
    /**
     * 排序字段
     */
    @TableField(exist = false)
    private String sortField;
    /**
     * 排序类型
     */
    @TableField(exist = false)
    private String sortType;
    /**
     * 子节点
     */
    @TableField(exist = false)
    private List<TNewExercise> childrens;

    /**
     * 选项
     */
    @TableField(exist = false)
    private List<TExerciseInfo> exerciseInfos;

    /**
     * 知识点
     */
    @TableField(exist = false)
    private List<Knowledge> knowledges;

    /**
     * 习题分数
     */
    @TableField(exist = false)
    private Double exerciseScore;

    /**
     * 多少个子习题
     */
    @TableField(exist = false)
    private int childCount;
    /**
     * 模板中的习题排序
     */
    @TableField(exist = false)
    private int exerciseOrder;
    /**
     * 创建人名称
     */
    @TableField(exist = false)
    private String userName;
    /**
     * 是否绑定作业
     */
    @TableField(exist = false)
    private boolean bandingModel;
    /**
     * 模板id 用于作业模板选题时查询
     */
    @TableField(exist = false)
    private Integer modelId;
    /**
     * 该习题是否选中 用于作业模板选题时的选中属性设置
     */
    @TableField(exist = false)
    private int checked=0;
}
