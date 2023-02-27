package com.highgo.opendbt.login.service;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.login.model.NoticesListTO;

import javax.servlet.http.HttpServletRequest;

public interface NoticesService {
    //获取所有通知消息
    public NoticesListTO getNotices(HttpServletRequest request);

    //取所有未读通知消息，并判断token是否快过期，如果快过期就刷新
    public ResultTO<NoticesListTO> getNoticesNotRead(HttpServletRequest request);

    //修改通知为已读
    public Integer changeRead(HttpServletRequest request, int type, int noticeId);

    //清空未读通知
    public Integer clearNotRead(HttpServletRequest request, int type);

}
