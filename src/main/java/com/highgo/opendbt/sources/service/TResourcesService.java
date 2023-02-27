package com.highgo.opendbt.sources.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.highgo.opendbt.sources.domain.entity.TResources;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 */
public interface TResourcesService extends IService<TResources> {
    //上传资源
    TResources uploadResources(HttpServletRequest request, MultipartFile file);

    //读取资源
    void readResourse(HttpServletRequest request, HttpServletResponse response, int id, String resourcesType);

    //资源树
    List<TResources> listResourcesTree(HttpServletRequest request, TResources resources);
}
