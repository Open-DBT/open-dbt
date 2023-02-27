package com.highgo.opendbt.process.service;

import com.highgo.opendbt.process.domain.entity.TCatalogueProcess;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
public interface TCatalogueProcessService extends IService<TCatalogueProcess> {
    //学生进度更新
    TCatalogueProcess saveProgressByStudent(HttpServletRequest request, TCatalogueProcess progress);
}
