package com.highgo.opendbt.feedback.service;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.feedback.model.Feedback;

import javax.servlet.http.HttpServletRequest;

public interface FeedbackService {
    //新增反馈
    public Integer add(HttpServletRequest request, Feedback feedback);

    //查询反馈
    public PageInfo<Feedback> getFeedbackList(PageTO pageTO);


}
