package com.highgo.opendbt.sclass.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 * @TableName t_class_stu
 */
@TableName(value ="t_class_stu")
@Data
@NoArgsConstructor
public class TClassStu implements Serializable {
    /**
     * 主键
     */
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    /**
     * 班级id
     */
    @TableField(value = "sclass_id")
    private Integer sclassId;

    /**
     * 用户id
     */
    @TableField(value = "user_id")
    private Integer userId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public TClassStu(Integer sclassId, Integer userId) {
        this.sclassId = sclassId;
        this.userId = userId;
    }
}
