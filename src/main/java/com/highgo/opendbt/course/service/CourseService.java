package com.highgo.opendbt.course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.course.domain.entity.Course;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CourseService extends IService<Course> {
    //查询课程列表
    public List<Course> getCourseList(HttpServletRequest request, int type, int number);

    //查询其他人公开的课程基本信息列表
    public List<Course> getOtherPublishCourse(HttpServletRequest request);

    //查询用户自己拥有的课程(分页)
    public List<Course> getOwnCourse(HttpServletRequest request, PageTO pageTO);

    //查询所有人发布的课程(分页)
    public List<Course> getCoursePublish(HttpServletRequest request, PageTO pageTO);

    //根据课程id获取课程信息
    public Course getCourseDetail(int courseId);

    //新增和修改课程
    public Course updateCourse(HttpServletRequest request, Course course);

    //删除课程
    public Integer deleteCourse(int courseId, HttpServletRequest request);

    //修改课程是否发布
    public Integer updateIsOpen(HttpServletRequest request, Course course);

    //复制课程"
    public Integer copyCourse(HttpServletRequest request, int courseId) throws InterruptedException;

    //	public void verifyCourseIsFinish(int courseId, int type) throws Exception;
    //获取课程封面列表
    public List<String> getCourseCoverImageList(HttpServletRequest request);

    //课程添加助教
    public Integer courseAddTeacher(Course course);

    //用于历史课程第一次初始化新增第一层目录
    void addFirstCatalogue();
}
