package com.highgo.opendbt.login.mapper;

import com.highgo.opendbt.login.model.Notice;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticesMapper {

    /**
     * 获取所有通知
     *
     * @return
     * @
     */
    public List<Notice> getNotice(@Param("userId") int userId);

    /**
     * 获取用户未读通知，查询通知和用户关联表中没有存的通知id
     *
     * @param userId
     * @return
     * @
     */
    public List<Notice> getNoticeNotRead(@Param("userId") int userId);

    /**
     * 新增通知
     *
     * @param notice
     * @return
     * @
     */
    public Integer addNotice(Notice notice);

    /**
     * 改变通知为已读，给通知和用户的关联表添加数据
     *
     * @param userId
     * @param noticeId
     * @return
     * @
     */
    public Integer changeNoticeRead(@Param("userId") int userId, @Param("noticeId") int noticeId);

    /**
     * 清空未读通知，给通知和用户的关联表添加数据
     *
     * @param userId
     * @param noticeList
     * @return
     * @
     */
    public Integer clearNotReadNotice(@Param("userId") int userId, @Param("list") List<Notice> noticeList);

}
