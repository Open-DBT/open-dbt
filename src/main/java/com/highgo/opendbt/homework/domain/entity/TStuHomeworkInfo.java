package com.highgo.opendbt.homework.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.highgo.opendbt.common.entity.BaseEntity;
import com.highgo.opendbt.exercise.domain.entity.TExerciseInfo;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @TableName t_stu_homework_info
 * 学生作业明细表
 */
@TableName(value = "t_stu_homework_info")
@Data
@ToString
@Accessors(chain = true)
public class TStuHomeworkInfo extends BaseEntity {
    /**
     *
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 作业id
     */
    @TableField(value = "homework_id")
    private Integer homeworkId;

    /**
     * 作业名称
     */
    @TableField(value = "homework_name")
    private String homeworkName;

    /**
     * 模板id
     */
    @TableField(value = "model_id")
    private Integer modelId;

    /**
     * 模板名称
     */
    @TableField(value = "model_name")
    private String modelName;

    /**
     * 学生ID
     */
    @TableField(value = "student_id")
    private Integer studentId;

    /**
     * 学生的名字
     */
    @TableField(value = "student_name")
    private String studentName;

    /**
     * 学号
     */
    @TableField(value = "student_code")
    private String studentCode;

    /**
     * 班级id
     */
    @TableField(value = "class_id")
    private Integer classId;

    /**
     * 班级名称
     */
    @TableField(value = "class_name")
    private String className;

    /**
     * 习题id
     */
    @TableField(value = "exercise_id")
    private Integer exerciseId;

    /**
     * 习题分数
     */
    @TableField(value = "exercise_score")
    private Double exerciseScore;

    /**
     * 习题答案
     */
    @TableField(value = "exercise_result")
    private String exerciseResult;

    /**
     * 是否正确1：对 2：错 3:半对
     */
    @TableField(value = "is_correct")
    private Integer isCorrect;


    /**
     * 习题满分分数
     */
    @TableField(exist = false)
    private Double exerciseActualScore;

    /**
     * 试题类型 1：主观 2：客观
     */
    @TableField(exist = false)
    private Integer exerciseStyle;
    /**
     * 习题答案
     */
    @TableField(exist = false)
    private String standardAnswser;
    /**
     * 习题类型 1：单选2：多选3：判断4：填空5：简答
     */
    @TableField(exist = false)
    private Integer exerciseType;
    /**
     * 是否填空题不区分大小写1：是2：否
     */
    @TableField(exist = false)
    private Integer ignoreCase;
    /**
     * 多选题未选全给一半分1：是2：否
     */
    @TableField(exist = false)
    private Integer unselectedGiven;

    /**
     * 该题目下的选项或答案
     */
    @TableField(exist = false)
    private List<TExerciseInfo> exerciseInfoList;

    public TStuHomeworkInfo() {
    }

    public TStuHomeworkInfo(Integer courseId, Integer homeworkId, String homeworkName, Integer modelId, String modelName, Integer studentId, String studentName, String studentCode, Integer classId, Integer exerciseId, Double exerciseScore, String exerciseResult, Integer isCorrect, Date createTime, Integer deleteFlag) {
        this.courseId = courseId;
        this.homeworkId = homeworkId;
        this.homeworkName = homeworkName;
        this.modelId = modelId;
        this.modelName = modelName;
        this.studentId = studentId;
        this.studentName = studentName;
        this.studentCode = studentCode;
        this.classId = classId;
        this.exerciseId = exerciseId;
        this.exerciseScore = exerciseScore;
        this.exerciseResult = exerciseResult;
        this.isCorrect = isCorrect;
        this.createTime = createTime;
        this.deleteFlag = deleteFlag;
    }
}
