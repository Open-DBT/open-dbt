package com.highgo.opendbt.sclass.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.sclass.domain.entity.TClassStu;

import java.util.List;

/**
 *
 */
public interface TClassStuService extends IService<TClassStu> {
     Integer getCurrentClass(int userId, List<Sclass> sclasss);
}
