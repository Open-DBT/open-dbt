package com.highgo.opendbt.course.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.course.domain.entity.Course;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseMapper extends BaseMapper<Course> {

    /**
     * 查询课程基本信息列表
     *
     * @param userId 用户id
     * @param type   查询类型，0为查询自己的课程，1为查询发布的课程
     * @param number 查询个数，0为查询全部
     * @return
     * @
     */
    public List<Course> getCourseList(@Param("userId") int userId, @Param("type") int type, @Param("number") int number);

    /**
     * 查询其他人公开的课程基本信息列表
     *
     * @param userId 用户id
     * @return
     * @
     */
    public List<Course> getOtherPublishCourse(@Param("userId") int userId);

    public List<Course> getCourseByParentId(@Param("courseId") int courseId);

    /**
     * 获取当前用户拥有的课程
     *
     * @param userId
     * @return
     * @
     */
    public List<Course> getOwnCourse(@Param("userId") int userId);

    /**
     * 获取所有用户发布的课程
     *
     * @return
     * @
     */
    public List<Course> getCoursePublish();

    /**
     * 通过课程id获取课程信息
     *
     * @param courseId
     * @return
     * @
     */
    public Course getCourseByCourseId(@Param("courseId") int courseId);

    /**
     * 通过课程名获取课程信息
     *
     * @param courseName 课程名称
     * @return
     * @
     */
    public List<Course> getCourseByCourseName(@Param("courseName") String courseName);

    /**
     * 新增课程
     *
     * @param course 课程信息
     * @return
     * @
     */
    public Integer addCourse(Course course);

    /**
     * 复制课程新增
     *
     * @param course 课程信息
     * @return
     * @
     */
    public Integer addCopyCourse(Course course);

    /**
     * 修改课程
     *
     * @param course 课程信息
     * @return
     * @
     */
    public Integer updateCourse(Course course);

    /**
     * 删除课程
     *
     * @param courseId 课程id
     * @return
     * @
     */
    public Integer deleteCourse(@Param("courseId") int courseId);

    /**
     * 修改课程是否发布
     *
     * @param course 课程信息
     * @return
     * @
     */
    public Integer updateIsOpen(Course course);

    /**
     * 修改课程是否编辑完成状态
     *
     * @param courseId 课程id
     * @return
     * @
     */
    public Integer updateIsFinish(@Param("courseId") int courseId);

    public List<Integer> getCourseTeachers(@Param("courseId") int courseId);

    public Integer deleteUserCourse(@Param("courseId") int courseId);

    public Integer addUserCourseArray(@Param("courseId") int courseId, @Param("teachers") int[] teachers);

}
