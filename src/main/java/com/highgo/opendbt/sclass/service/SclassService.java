package com.highgo.opendbt.sclass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.domain.model.*;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface SclassService extends IService<Sclass> {
    //查询教师创建的班级
    List<Sclass> getClassListByTeacherId(HttpServletRequest request, int type);

    //根据当前登录人查询班级
    List<Sclass> getClass(HttpServletRequest request);

    //根据开课和结课时间分别分页获取未开始班级、进行中班级、已结束班级
    PageInfo<Sclass> getClassListPage(HttpServletRequest request, SclassPageTO sclassPageTO);

    //新增和修改班级
    Sclass updateClass(HttpServletRequest request, Sclass sclass);

    //根据班级id获取班级信息，不会获取班级课程信息
    Sclass getSclassById(int sclassId);

    //根据班级id获取班级明细，会获取班级课程信息
    Sclass getSclassInfoById(int sclassId);

    //根据班级的id获取班级的学生
    PageInfo<UserInfo> getSclassStu(SclassUserPage sclassUserPage);

    //根据班级的id获取班级的学生
    List<UserInfo> getSclassStudentList(SclassUserPage sclassUserPage);

    //根据学生id查询所有班级信息
    List<Sclass> getAllSclassByStuId(HttpServletRequest request);

    //根据学生id查询所有班级信息，以及班级课程该学生学习进度
    List<Sclass> getAllSclassProgressByStuId(HttpServletRequest request);

    //新增和修改班级的学生
    ResultTO<Void> updateSclassStu(HttpServletRequest request, SclassStu sclassStu);

    //根据学生查询班级信息
    PageInfo<Sclass> getSclassByStuPage(HttpServletRequest request, SclassPageTO pageTO);

    //上传班级导入学生的文件
    String uploadSclassStuFile(HttpServletRequest request, MultipartFile file) throws IOException;

    //读取文件导入学生
    String importSclassStu(HttpServletRequest request, ImportSclassStuTO importSclassStuTO);

    //删除班级和学生的关联关系
    Integer deleteSclassStu(SclassStu sclassStu, HttpServletRequest request);

    //批量删除学生
    Integer batchDelSclassStu(BatchDelClass batchDelClass, HttpServletRequest request);

    //删除班级，并级联删除班级和学生的关联关系以及该班级学生的成绩记录
    Integer deleteSclassById(int id);

    //根据学生id查询班级
    List<Sclass> getSclassByStuId(HttpServletRequest request);

    //修改班级是否开放
    Integer updateSclassIsOpen(int sclassId, int classIsOpen);

    //根据课程，查询全部班级
    List<Sclass> getSclassByCourseId(int courseId);

    //根据学生id查询所有班级信息修改后剔除相同课程
    ArrayList<Sclass> getAllSclassByStuIdExpectCourse(HttpServletRequest request);

    //根据班级id查询学生
    List<UserInfo> getStudentByClassId(int class_id);

}
