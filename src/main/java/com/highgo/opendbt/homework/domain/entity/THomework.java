package com.highgo.opendbt.homework.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.highgo.opendbt.common.entity.BaseEntity;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


/**
 * @TableName t_homework
 * 作业表
 */
@TableName(value = "t_homework")
@Data
@ToString
@Accessors(chain = true)
public class THomework extends BaseEntity {
    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 课程id
     */
    @TableField(value = "course_id")
    private Integer courseId;

    /**
     * 作业名称
     */
    @TableField(value = "homework_name")
    private String homeworkName;

    /**
     * 作业模板id
     */
    @TableField(value = "model_id")
    private Integer modelId;

    /**
     * 作业模板名称
     */
    @TableField(value = "model_name")
    private String modelName;

    /**
     * 开始时间
     */
    @NotNull(message = "发放时间不允许为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @NotNull(message = "发放时间不允许为空")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField(value = "end_time")
    private Date endTime;

    /**
     * 1:允许补交2：不允许补交
     */
    @TableField(value = "allow_after")
    private Integer allowAfter;

    /**
     * 及格分数
     */
    @TableField(value = "pass_mark")
    private Short passMark;

    /**
     * 总分数
     */
    @TableField(value = "score")
    private Double score;

    /**
     * 重做次数
     */
    @TableField(value = "redo_times")
    private Integer redoTimes;

    /**
     * 是否取最高成绩1：是2：否
     */
    @TableField(value = "max_score")
    private Integer maxScore;

    /**
     * 答案查看时间1：批阅后2：提交后3：作业结束后4：不允许
     */
    @NotNull(message = "防作弊设置不允许为空")
    @TableField(value = "view_time")
    private Integer viewTime;
    /**
     * 是否填空题不区分大小写1：是2：否
     */
    @NotNull(message = "评分设置不允许为空")
    @TableField(value = "ignore_case")
    private Integer ignoreCase;

    /**
     * 多选题未选全给一半分1：是2：否
     */
    @NotNull(message = "评分设置不允许为空")
    @TableField(value = "unselected_given")
    private Integer unselectedGiven;


    /**
     * 共有多少个人
     */
    @TableField(exist = false)
    private int totalNum;
    /**
     * 完成多少个人
     */
    @TableField(exist = false)
    private int completeNum;
    /**
     * 未完成多少个人
     */
    @TableField(exist = false)
    private int uncompleteNum;

    /**
     * 作业id
     */
    @TableField(exist = false)
    private int classId;
    /**
     * 作业名称
     */
    @TableField(exist = false)
    private String className;

    /**
     * 发放范围
     */
    @TableField(exist = false)
    private List<THomeworkDistribution> distributions;

    public THomework() {
    }

    public THomework(Integer courseId, String homeworkName, Integer modelId, String modelName, @NotNull(message = "发放时间不允许为空") Date startTime, @NotNull(message = "发放时间不允许为空") Date endTime, Integer allowAfter, @NotNull(message = "评分设置不允许为空") Integer ignoreCase, @NotNull(message = "评分设置不允许为空") Integer unselectedGiven, Date createTime, Integer createUser) {
        this.courseId = courseId;
        this.homeworkName = homeworkName;
        this.modelId = modelId;
        this.modelName = modelName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.allowAfter = allowAfter;
        this.ignoreCase = ignoreCase;
        this.unselectedGiven = unselectedGiven;
        this.createTime = createTime;
        this.createUser = createUser;
    }
}

