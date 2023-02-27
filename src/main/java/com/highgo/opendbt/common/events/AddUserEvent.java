package com.highgo.opendbt.common.events;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description: 新增学生事件
 * @Title: AddUserEvent
 * @Package com.highgo.opendbt.common.events
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/21 10:51
 */
@SuppressWarnings("Lombok")
@Data
@ToString
public class AddUserEvent extends ApplicationEvent {
    private HttpServletRequest request;
    //班级id
    private Integer classId;
    //人员id
    private Integer userId;

    public AddUserEvent(Object source, Integer classId, Integer userId, HttpServletRequest request) {
        super(source);
        this.classId = classId;
        this.request = request;
        this.userId = userId;

    }
}
