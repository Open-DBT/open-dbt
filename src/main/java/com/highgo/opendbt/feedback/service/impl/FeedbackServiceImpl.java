package com.highgo.opendbt.feedback.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.feedback.mapper.FeedbackMapper;
import com.highgo.opendbt.feedback.model.Feedback;
import com.highgo.opendbt.feedback.service.FeedbackService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    Logger logger = LoggerFactory.getLogger(getClass());


    private final FeedbackMapper feedbackMapper;

    public FeedbackServiceImpl(FeedbackMapper feedbackMapper) {
        this.feedbackMapper = feedbackMapper;
    }

    /**
     * @description:新增反馈
     * @author:
     * @date: 2023/1/9 13:12
     * @param: [request, feedback 反馈信息]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer add(HttpServletRequest request, Feedback feedback) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        feedback.setCreator(loginUser.getUserId());
        feedback.setCreateTime(TimeUtil.getDateTime());
        return feedbackMapper.add(feedback);

    }

    /**
     * @description:查询反馈
     * @author:
     * @date: 2023/1/9 13:12
     * @param: [pageTO 分页查询条件]
     * @return: com.github.pagehelper.PageInfo<com.highgo.opendbt.feedback.model.Feedback>
     **/
    @Override
    public PageInfo<Feedback> getFeedbackList(PageTO pageTO) {
        // 分页查询配置
        PageMethod.startPage(pageTO.getCurrent(), pageTO.getPageSize());
        List<Feedback> list = feedbackMapper.getFeedbackList();
        return new PageInfo<Feedback>(list);
    }
}
