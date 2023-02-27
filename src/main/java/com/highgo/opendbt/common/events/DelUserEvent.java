package com.highgo.opendbt.common.events;

import lombok.Data;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Description: 删除学生事件
 * @Title: DelUserEvent
 * @Package com.highgo.opendbt.common.events
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/11/21 11:20
 */
@SuppressWarnings("Lombok")
@Data
@ToString
public class DelUserEvent extends ApplicationEvent {
    private HttpServletRequest request;
    //班级id
    private Integer classId;
    //人员id
    private List<Integer> users;

    public DelUserEvent(Object source, Integer classId, List<Integer> users, HttpServletRequest request) {
        super(source);
        this.request = request;
        this.classId = classId;
        this.users = users;
    }
}
