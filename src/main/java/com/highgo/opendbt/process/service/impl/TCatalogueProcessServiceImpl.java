package com.highgo.opendbt.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.highgo.opendbt.process.mapper.TCatalogueProcessMapper;
import com.highgo.opendbt.process.service.TCatalogueProcessService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.mapper.SclassMapper;
import com.highgo.opendbt.sclass.service.TClassStuService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 章节进度服务
 */
@Service
@RequiredArgsConstructor
public class TCatalogueProcessServiceImpl extends ServiceImpl<TCatalogueProcessMapper, TCatalogueProcess>
        implements TCatalogueProcessService {
    Logger logger = LoggerFactory.getLogger(getClass());
    private final SclassMapper sclassMapper;
    final TClassStuService classStuService;
    /**
     * @description: 学生视频进度更新保存
     * @author:
     * @date: 2022/8/30 14:53
     * @param: [request, progress]
     * @return: int
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TCatalogueProcess saveProgressByStudent(HttpServletRequest request, TCatalogueProcess progress) {
        //校验并获取登录人信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //获取登录人所在班级
        List<Sclass> clazz = sclassMapper.getActiveClassByStu(loginUser.getUserId(), progress.getCourseId());
        Integer classId;
        BusinessResponseEnum.UNFINDCLASS.assertIsNotEmpty(clazz, loginUser.getUserId(), progress.getCourseId());
        //存在多个班级，默认显示最后加入的班级
        if (clazz.size() > 1) {
            logger.info("课程" + progress.getCourseId() + "下,学生" + loginUser.getUserName() + "(" + loginUser.getCode() + ")存在多个正在开课的班级中");
            classId = classStuService.getCurrentClass(loginUser.getUserId(), clazz);
        } else {
            classId = clazz.get(0).getId();
        }
        //根据条件查询该进度内容
        TCatalogueProcess process = this.getOne(new QueryWrapper<TCatalogueProcess>()
                .eq("course_id", progress.getCourseId())
                .eq("catalogue_id", progress.getCatalogueId())
                .eq("resources_id", progress.getResourcesId())
                .eq("class_id", classId)
                .eq("user_id", loginUser.getUserId()));
        BusinessResponseEnum.FAILSEARCHSTUDYPROCESS.assertNotNull(process);
        //变更进度内容的观看时长等信息
        //观看时长
        process.setDuration(process.getDuration() != null ? (process.getDuration() + progress.getDuration()) : progress.getDuration())
                //观看进度
                .setProgress(progress.getProgress())
                //是否完成
                .setStudyStatus((short) ((process.getDuration() >= progress.getResourcesTime()) ? 2 : 1))
                .setUpdateTime(new Date())
                .setUpdateUser(loginUser.getUserId());
        BusinessResponseEnum.UPDATEFAIL.assertIsTrue(this.updateById(process));
        return progress;
    }
}




