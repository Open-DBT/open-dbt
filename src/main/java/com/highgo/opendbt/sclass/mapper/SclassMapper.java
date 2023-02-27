package com.highgo.opendbt.sclass.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.sclass.domain.model.BatchDelClass;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.domain.model.SclassStu;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
 public interface SclassMapper extends BaseMapper<Sclass> {

    /**
     * 查询教师创建的进行中和未开始的班级
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getClassListByTeacherIdNotEnd(@Param("userId") int userId);

    /**
     * 查询教师创建的已结束的班级
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getClassListByTeacherIdEnd(@Param("userId") int userId);

    /**
     * 根据学生查询班级以及班级课程，根据班级结束时间倒序
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getSclassByStuId(@Param("userId") int userId);

    /**
     * 获取未开始班级
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getNoStartClassList(@Param("userId") int userId);

    /**
     * 获取进行中班级
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getStartClassListByCreator(@Param("userId") int userId);

    /**
     * 获取已结束班级
     *
     * @param sceneId
     * @return
     * @
     */
     List<Sclass> getEndClassList(@Param("userId") int sceneId);

    /**
     * 添加班级
     *
     * @param sclass
     * @
     */
     void addClass(Sclass sclass);

    /**
     * 修改班级
     *
     * @param sclass
     * @
     */
     void updateClass(Sclass sclass);

    /**
     * 通过班级id获取班级
     *
     * @param sclassId
     * @return
     * @
     */
     Sclass getSclassById(@Param("sclassId") int sclassId);

    /**
     * 通过班级id获取班级信息
     *
     * @param sclassId
     * @return
     * @
     */
     Sclass getSclassInfoById(@Param("sclassId") int sclassId);

    /**
     * 获取班级的学生
     *
     * @param sclassId
     * @return
     * @
     */
     List<UserInfo> getSclassStu(@Param("sclassId") int sclassId);

    /**
     * 添加学生到班级
     *
     * @param sclassStu
     * @
     */
     void addSclassStu(SclassStu sclassStu);

    /**
     * 通过学生id获取班级学生信息
     *
     * @param sclassStu
     * @return
     * @
     */
     List<SclassStu> getSclassStuByUserId(SclassStu sclassStu);

    /**
     * 根据学生id，查询进行中的课程
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getSclassStartByStuId(@Param("userId") int userId);

    /**
     * 根据学生id，查询结束的课程
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getSclassEndByStuId(@Param("userId") int userId);

    /**
     * 根据学生id，查询进行中和已结束的课程
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getAllSclassByStuId(@Param("userId") int userId);

    /**
     * 根据学生id，查询已学习过的课程的进度
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getAllSclassProgressByStuId(@Param("userId") int userId);

    /**
     * 根据学生id，查询已学习过的课程的进度
     *
     * @param userId
     * @return
     * @
     */
     List<Sclass> getSclassAllStuProgressList(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId);

    /**
     * 删除班级中的学生
     *
     * @param sclassStu
     * @return
     * @
     */
     Integer deleteSclassStu(SclassStu sclassStu);

    /**
     * 批量删除班级中的学生
     *
     * @param sclassStu
     * @return
     * @
     */
     Integer batchDelSclassStu(BatchDelClass batchDelClass);

    /**
     * 删除班级中所有的学生
     *
     * @param sclassStu
     * @return
     * @
     */
     Integer deleteSclassAllStu(@Param("sclassId") int sclassId);

    /**
     * 删除班级中所有学生的答题记录
     *
     * @param sclassStu
     * @return
     * @
     */
     Integer deleteSclassAllStuScore(@Param("sclassId") int sclassId);

    /**
     * 通过班级id，删除班级
     *
     * @param id 班级id
     * @return
     * @
     */
     Integer deleteSclassById(@Param("id") int id);

    /**
     * 通过老师id获取班级
     *
     * @param sclassStu
     * @return
     * @
     */
     List<Sclass> getSclassByTeaId(@Param("userId") int userId);

    /**
     * 获取学生的课程进度
     *
     * @param courseId
     * @param userId
     * @return
     * @
     */
     Sclass getCourseProgressByStu(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("courseId") int courseId);

    /**
     * 根据课程，查询开始的班级，返回id
     *
     * @param courseId
     * @return
     * @
     */
     List<Sclass> getStartSclassByCourseId(@Param("courseId") int courseId);

    /**
     * 根据课程，查询全部的班级，返回list
     *
     * @param courseId
     * @return
     * @
     */
     List<Sclass> getSclassByCourseId(@Param("courseId") int courseId);

    /**
     * 修改班级是否开放给学生
     *
     * @param sclassStu
     * @return
     * @
     */
     Integer updateSclassIsOpen(@Param("sclassId") int sclassId, @Param("classIsOpen") int classIsOpen);

    //查询当前登录人名下该课程的班级
    List<Sclass> getClassByLoginUserAndCourse(@Param("userId") int userId, @Param("course_id") int course_id);


    /**
     * @description:根据学生课程id查询所在班级，同一个课程同一个学生只会有一个在发布状态且进行中的班级（新建班级时注意权限控制）
     * @author:
     * @date: 2022/8/17 10:07
     * @param: [course_id 课程id, user_id 用户id]
     * @return: com.highgo.opendbt.sclass.domain.entity.Sclass 班级信息
     **/
    List<Sclass> findClassByStuAndCourse(@Param("course_id") int course_id, @Param("user_id") int user_id);

    //根据学生id和课程id查询学生所在班级
    List<Sclass> getActiveClassByStu(@Param("user_id") int user_id, @Param("course_id") int course_id);
}
