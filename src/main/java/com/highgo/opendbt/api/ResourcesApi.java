package com.highgo.opendbt.api;

import com.highgo.opendbt.sources.domain.entity.TResources;
import com.highgo.opendbt.sources.service.TResourcesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 资源相关接口
 * @Title: ResourcesApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@Controller
@CrossOrigin
@RequestMapping("/resources")
public class ResourcesApi {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TResourcesService resourcesService;


    /**
     * 上传资源
     *
     * @param request request
     * @param file    文件
     * @return TResources
     */
    @ResponseBody
    @RequestMapping("/uploadResources")
    public TResources uploadResources(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        logger.info("Enter, ");
        return resourcesService.uploadResources(request, file);
    }

    /**
     * @description: 读取资源
     * @author:
     * @date: 2022/8/19 10:09
     * @param: [request, response, id: 资源id, resourcesType: 类型：[screenshot:缩略图 original:原始文件 transfer:转换文件（word、excel转pdf） down: 下载 ]]
     * @return: void
     **/
    @RequestMapping("/readResourse/{id}/{resourcesType}")
    public void readResourse(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") int id, @PathVariable("resourcesType") String resourcesType) {
        logger.info("Enter, id={},resourcesType={}", id, resourcesType);
        resourcesService.readResourse(request, response, id, resourcesType);
    }

    /**
     * @description: 资源树列表查询
     * @author:
     * @date: 2022/8/19 13:42
     * @param: [request]
     * @return: java.util.List<com.highgo.opendbt.resources.model.TResources>
     **/
    @RequestMapping("/listResourcesTree")
    @ResponseBody
    public List<TResources> listResourcesTree(HttpServletRequest request, @RequestBody TResources resources) {
        logger.info("Enter, resources={}", resources.toString());
        return resourcesService.listResourcesTree(request, resources);
    }

}
