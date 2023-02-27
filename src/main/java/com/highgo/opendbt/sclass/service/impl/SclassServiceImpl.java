package com.highgo.opendbt.sclass.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.events.AddUserEvent;
import com.highgo.opendbt.common.events.DelUserEvent;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.ExcelUtil;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.progress.mapper.ProgressMapper;
import com.highgo.opendbt.progress.model.CourseProgress;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.domain.entity.TClassStu;
import com.highgo.opendbt.sclass.domain.model.*;
import com.highgo.opendbt.sclass.mapper.SclassMapper;
import com.highgo.opendbt.sclass.service.SclassService;
import com.highgo.opendbt.sclass.service.TClassStuService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import com.highgo.opendbt.system.mapper.UserInfoMapper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SclassServiceImpl extends ServiceImpl<SclassMapper, Sclass> implements SclassService {

    Logger logger = LoggerFactory.getLogger(getClass());
    /**
     * 注入ApplicationContext用来发布事件
     */
    private final ApplicationContext applicationContext;
    @Autowired
    private SclassMapper sclassMapper;
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Autowired
    private ProgressMapper progressMapper;
    @Autowired
    private SclassService sclassService;
    @Autowired
    private TClassStuService classStuService;

    public SclassServiceImpl(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public List<Sclass> getClassListByTeacherId(HttpServletRequest request, int type) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // type：0进行中和未毕业，1已毕业
        List<Sclass> classList;
        if (type == 1) {
            classList = sclassMapper.getClassListByTeacherIdEnd(loginUser.getUserId());
        } else {
            classList = sclassMapper.getClassListByTeacherIdNotEnd(loginUser.getUserId());
        }
        return classList;
    }

    @Override
    public PageInfo<Sclass> getClassListPage(HttpServletRequest request, SclassPageTO sclassPageTO) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 分页查询配置
        PageMethod.startPage(sclassPageTO.getCurrent(), sclassPageTO.getPageSize());
        // type：0进行中，-1未开始，1已毕业
        List<Sclass> classList = new ArrayList<Sclass>();
        if (sclassPageTO.getType() == -1) {
            classList = sclassMapper.getNoStartClassList(loginUser.getUserId());
        } else if (sclassPageTO.getType() == 1) {
            classList = sclassMapper.getEndClassList(loginUser.getUserId());
        } else {
            classList = sclassMapper.getStartClassListByCreator(loginUser.getUserId());
        }

        return new PageInfo<>(classList);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Sclass updateClass(HttpServletRequest request, Sclass sclass) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 班级id等于-1为新增，否则就是修改
        if (sclass.getId() == -1) {
            sclass.setCreator(loginUser.getUserId());
            sclassMapper.addClass(sclass);
            // 默认添加创建人到班级
            sclassMapper.addSclassStu(new SclassStu(sclass.getId(), loginUser.getUserId()));
        } else {
            sclassMapper.updateClass(sclass);
        }
        return sclass;
    }

    @Override
    public Sclass getSclassById(int sclassId) {
        return sclassMapper.getSclassById(sclassId);
    }

    @Override
    public Sclass getSclassInfoById(int sclassId) {
        return sclassMapper.getSclassInfoById(sclassId);
    }

    @Override
    public PageInfo<UserInfo> getSclassStu(SclassUserPage sclassUserPage) {
        // 分页查询配置
        PageMethod.startPage(sclassUserPage.getCurrent(), sclassUserPage.getPageSize());
        //去空格
        sclassUserPage.setUserName(sclassUserPage.getUserName() == null ? null : sclassUserPage.getUserName().trim());
        sclassUserPage.setCode(sclassUserPage.getCode() == null ? null : sclassUserPage.getCode().trim());
        List<UserInfo> userList = userInfoMapper
                .getSclassStu(sclassUserPage.getSclassId(), sclassUserPage.getUserName(), sclassUserPage.getCode());
        return new PageInfo<>(userList);
    }

    @Override
    public List<UserInfo> getSclassStudentList(SclassUserPage sclassUserPage) {
        //去空格
        sclassUserPage.setKeyWord(sclassUserPage.getKeyWord() == null ? null : sclassUserPage.getKeyWord().trim());
        return userInfoMapper.getSclassStudentList(sclassUserPage);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultTO<Void> updateSclassStu(HttpServletRequest request, SclassStu sclassStu) {
        addStuToSclass(request, sclassStu, true);
        return ResultTO.OK();
    }

    private void addStuToSclass(HttpServletRequest request, SclassStu sclassStu, boolean isThrowException) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 验证学号是否注册到用户表
        List<UserInfo> userList = userInfoMapper.getUserByCode(sclassStu.getCode());
        Integer userId = null;
        if (userList == null || userList.isEmpty()) {
            // 未注册学生添加到用户表
            UserInfo user = new UserInfo(sclassStu.getUserName(), sclassStu.getCode());
            user.setRoleType(4);
            user.setCreator(loginUser.getUserId());
            user.setCreateTime(TimeUtil.getDateTime());
            userInfoMapper.addUser(user);
            // 默认角色为学生添加到用户和角色关联关系表中
            userInfoMapper.addUserRole(user.getUserId(), new int[]{4});
            // 班级与用户关联
            sclassMapper.addSclassStu(new SclassStu(sclassStu.getSclassId(), user.getUserId()));
            userId = user.getUserId();
        } else {
            // 已注册学生信息，验证是否绑定到当前班级
            SclassStu stu = new SclassStu(sclassStu.getSclassId(), userList.get(0).getUserId());
            List<SclassStu> stuInfo = sclassMapper.getSclassStuByUserId(stu);
            BusinessResponseEnum.STUDENTALREADYCLASS
                    .assertIsTrue(!(isThrowException && (stuInfo != null && !stuInfo.isEmpty())), sclassStu.getCode());
            if (stuInfo == null || stuInfo.isEmpty()) {
                // 没有绑定到当前班级，绑定到当前班级
                TClassStu tClassStu = new TClassStu(sclassStu.getSclassId(), userList.get(0).getUserId());
                //绑定班级
                BusinessResponseEnum.FAILBINDINGCLASS.assertIsTrue(classStuService.save(tClassStu));
                userId = tClassStu.getUserId();
            }
        }
        //新增人员触发监听时间处理课程目录和作业的初始化相关业务
        applicationContext.publishEvent(new AddUserEvent(this, sclassStu.getSclassId(), userId, request));
    }


    @Override
    public PageInfo<Sclass> getSclassByStuPage(HttpServletRequest request, SclassPageTO pageTO) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 分页查询配置
        PageMethod.startPage(pageTO.getCurrent(), pageTO.getPageSize());
        // type：0进行中，-1未开始，1已毕业
        List<Sclass> classList;
        if (pageTO.getType() == 0) {
            classList = sclassMapper.getSclassStartByStuId(loginUser.getUserId());
        } else {
            classList = sclassMapper.getSclassEndByStuId(loginUser.getUserId());
        }
        PageInfo<Sclass> sclassPageInfo = new PageInfo<>(classList);
        List<Sclass> sclassListPageInfo = sclassPageInfo.getList();
        for (Sclass sclass : sclassListPageInfo) {
            // 查询进度
            CourseProgress progress = progressMapper.getCourseProgress(sclass.getCourseId(), loginUser.getUserId(), sclass.getId());
            if (progress.getCount() == 0) {
                sclass.setProgress(100);
            } else {
                BigDecimal bigDecimal = BigDecimal.valueOf(progress.getCorrect() * 1.0 / progress.getCount() * 100);
                sclass.setProgress(bigDecimal.setScale(2, RoundingMode.UP).doubleValue());
            }
        }
        return sclassPageInfo;
    }

    @Override
    public List<Sclass> getAllSclassByStuId(HttpServletRequest request) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return sclassMapper.getAllSclassByStuId(loginUser.getUserId());
    }

    @Override
    public List<Sclass> getAllSclassProgressByStuId(HttpServletRequest request) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 获取学生学习课程进度
        List<Sclass> classList = sclassMapper.getAllSclassProgressByStuId(loginUser.getUserId());
        for (Sclass sclass : classList) {
            // 获取全班学生(除了当前用户之外)的学习进度
            List<Sclass> sclassAllStuProgressList = sclassMapper.getSclassAllStuProgressList(loginUser.getUserId(), sclass.getId(), sclass.getCourseId());
            if (!sclassAllStuProgressList.isEmpty()) {
                // 计算进度超过全班的人数
                int overNumber = 0;
                for (Sclass sclassAllStuProgress : sclassAllStuProgressList) {
                    if (sclass.getProgress() > sclassAllStuProgress.getProgress()) {
                        overNumber++;
                    } else {
                        break;
                    }
                }
                // 超过人数/(全班除了当前用户之外的总人数)
                BigDecimal bigDecimal = BigDecimal.valueOf(overNumber * 1.0 / (sclassAllStuProgressList.size()) * 100);
                double doubleValue = bigDecimal.setScale(2, RoundingMode.UP).doubleValue();
                sclass.setOverPercentage(doubleValue);
            } else {
                sclass.setOverPercentage(0);
            }
        }
        return classList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadSclassStuFile(HttpServletRequest request, MultipartFile file) throws IOException {
        FileOutputStream fos = null;
        try {
            // 获取用户信息
            UserInfo loginUser = Authentication.getCurrentUser(request);
            String newFileName = loginUser.getUserId() + "_" + TimeUtil.getDateTimeStr() + "_" + file.getOriginalFilename();
            String folderPath = ExcelUtil.getProjectPath() + File.separator + "temp";
            File folderPathFile = new File(folderPath);
            if (!folderPathFile.exists()) {
                folderPathFile.mkdir();
            }
            String filePath = folderPath + File.separator + newFileName;
            fos = new FileOutputStream(filePath);
            fos.write(file.getBytes());
            return filePath;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importSclassStu(HttpServletRequest request, ImportSclassStuTO importSclassStuTO) {
        try {
            for (String filePath : importSclassStuTO.getFilePathList()) {
                // 检查文件是否存在，是否是excel文案
                ExcelUtil.checkFile(filePath);
                Workbook workbook = ExcelUtil.getWorbook(filePath);
                // 获取excel表格的第一个sheet
                Sheet sheet = workbook.getSheetAt(0);
                // 获取最后一行下标
                int sheetLastRowIndex = sheet.getLastRowNum();
                logger.info("sheet sheetLastRowIndex = " + sheetLastRowIndex);
                // 读取前两列数据进行导入
                for (int i = 1; i <= sheetLastRowIndex; i++) {
                    Row row = sheet.getRow(i);
                    if (null != row) {
                        String code = "";
                        if (null != row.getCell(0)) {
                            code = row.getCell(0).toString().trim();
                        }
                        String userName = "";
                        if (null != row.getCell(1)) {
                            userName = row.getCell(1).toString().trim();
                        }
                        logger.info("code = " + code + ", userName = " + userName);
                        if (!code.equals("")) {
                            SclassStu sclassStu = new SclassStu(importSclassStuTO.getSclassId(), code, userName);
                            addStuToSclass(request, sclassStu, false);
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteSclassStu(SclassStu sclassStu, HttpServletRequest request) {
        ArrayList<Integer> users = new ArrayList<>();
        users.add(sclassStu.getUserId());
        Integer deleteClassStu = sclassMapper.deleteSclassStu(sclassStu);
        //删除学生触发监听设置
        applicationContext.publishEvent(new DelUserEvent(this, sclassStu.getSclassId(), users, request));
        return deleteClassStu;
    }

    @Override
    public List<Sclass> getClass(HttpServletRequest request) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return sclassMapper.getSclassByTeaId(loginUser.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteSclassById(int id) {
        // 删除班级,修改删除标记
        return sclassMapper.deleteSclassById(id);
    }

    @Override
    public List<Sclass> getSclassByStuId(HttpServletRequest request) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        return sclassMapper.getSclassByStuId(loginUser.getUserId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDelSclassStu(BatchDelClass batchDelClass, HttpServletRequest request) {
        Integer delSclassStuRes = sclassMapper.batchDelSclassStu(batchDelClass);
        //删除人员触发监听时间处理课程目录和作业的初始化相关业务
        applicationContext.publishEvent(new DelUserEvent(this, batchDelClass.getSclassId(),
                Arrays.stream(batchDelClass.getUserId()).boxed().collect(Collectors.toList()), request));
        return delSclassStuRes;

    }

    @Override
    public Integer updateSclassIsOpen(int sclassId, int classIsOpen) {
        return sclassMapper.updateSclassIsOpen(sclassId, classIsOpen);
    }

    @Override
    public List<Sclass> getSclassByCourseId(int courseId) {
        return sclassMapper.getSclassByCourseId(courseId);
    }

    @Override
    public ArrayList<Sclass> getAllSclassByStuIdExpectCourse(HttpServletRequest request) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        List<Sclass> classList = sclassMapper.getAllSclassByStuId(loginUser.getUserId());
        return classList.stream().collect(Collectors.collectingAndThen(Collectors.toCollection(() ->
                new TreeSet<>(Comparator.comparing(Sclass::getCourseId))), ArrayList::new));
    }

    @Override
    public List<UserInfo> getStudentByClassId(int classId) {
        SclassUserPage sclassUserPage = new SclassUserPage();
        sclassUserPage.setSclassId(classId);
        return sclassService.getSclassStudentList(sclassUserPage);
    }

}
