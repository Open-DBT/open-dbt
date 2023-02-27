package com.highgo.opendbt.sources.service.impl;


import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.FileConvertUtil;
import com.highgo.opendbt.common.utils.SnowflakeIdWorker;
import com.highgo.opendbt.common.utils.VideoTimeUtil;
import com.highgo.opendbt.sources.domain.entity.TResources;
import com.highgo.opendbt.sources.mapper.TResourcesMapper;
import com.highgo.opendbt.sources.service.TResourcesService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.apache.commons.lang3.StringUtils;
import org.bytedeco.javacv.FrameGrabber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 上传资源服务类
 */
@Service
public class TResourcesServiceImpl extends ServiceImpl<TResourcesMapper, TResources> implements TResourcesService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private TResourcesMapper resourcesMapper;

    /**
     * @description: 上传资源
     * @author:
     * @date: 2022/7/26 10:26
     * @param: file 上传文件
     * @return: TResources
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public TResources uploadResources(HttpServletRequest request, MultipartFile file) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //资源类型参数
        String resourcesType = request.getParameter("resourcesType");
        String resourcesSize = request.getParameter("resourcesSize");
        String resourcesAdditional = request.getParameter("resourcesAdditional");//其他资源
        //文件名
        String fileName = file.getOriginalFilename();
        //判断非空
        assert fileName != null;
        //生成存储路径
        String folderPath = packUrl(fileName, resourcesType);
        //文件重命名
        String name = getFileRename(fileName);
        //存储文件路径地址
        String filePath = folderPath + File.separator + name;
        //上传
        uploadFile(folderPath, filePath, file);
        //保存到资源表
        return saveResources(fileName, name, filePath, loginUser, resourcesType, resourcesSize, folderPath, resourcesAdditional);

    }

    /**
     * @description: 读取资源
     * @author:
     * @date: 2022/7/26 10:24
     * @param: id 资源id
     **/
    @Override
    public void readResourse(HttpServletRequest request, HttpServletResponse response, int id, String resourcesType) {
        TResources resources = this.getById(id);
        BusinessResponseEnum.NOTFOUNDRESOURCES.assertNotNull(resources, id);
        //资源路径转换
        String filePath = switchFilePath(resources, resourcesType);
        //流方式读取资源
        readFile(filePath, resources, response, resourcesType);
    }


    /**
     * @description: 资源树查询
     * @author:
     * @date: 2022/8/25 17:37
     * @param: [request]
     * @return: java.util.List<com.highgo.opendbt.resources.model.TResources>
     **/
    @Override
    public List<TResources> listResourcesTree(HttpServletRequest request, TResources resources) {
        // 查询所有资源
        List<TResources> resourcesList = this.list(new QueryWrapper<TResources>().ne("resources_additional", 1)
                .eq("delete_flag", 0)
                .eq("resources_retype", resources.getResourcesRetype())
                .like(StringUtils.isNoneBlank(resources.getResourcesName()), "resources_name", resources.getResourcesName()));
        // 循环过滤形成树形结构
        //存储过滤后的资源
        List<TResources> collect = null;
        if (resourcesList != null && !resourcesList.isEmpty()) {
            collect = resourcesList.stream().filter(item -> item.getParentId() == 0)
                    .sorted(Comparator.comparing(TResources::getSortNum))
                    .peek(item -> {
                        List<TResources> children = getChildren(item, resourcesList);
                        if (children != null && !children.isEmpty()) {
                            //子资源
                            item.setChildrens(children);
                            item.setIsleaf(false);
                        } else {
                            //叶子节点
                            item.setIsleaf(true);
                        }
                    }).collect(Collectors.toList());
        }
        return collect;
    }


    /**
     * @description: 文件上传
     * @author:
     * @date: 2022/8/25 17:27
     * @param: [folderPath, filePath, file]
     * @return: void
     **/

    private void uploadFile(String folderPath, String filePath, MultipartFile file) {
        File folderPathFile = new File(folderPath);
        //文件目录不存在循环创建目录结构
        if (!folderPathFile.exists()) {
            folderPathFile.mkdirs();
        }
        try (OutputStream fos = new FileOutputStream(filePath)) {
            fos.write(file.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
    }


    /*迭代查询子类资源*/
    private List<TResources> getChildren(TResources resources, List<TResources> resourcesList) {
        return resourcesList.stream().filter(item -> item.getParentId().equals(resources.getId()))
                .sorted(Comparator.comparing(TResources::getSortNum)).peek(item -> {
                    List<TResources> children = getChildren(item, resourcesList);
                    if (children != null && !children.isEmpty()) {
                        item.setChildrens(children);
                        item.setIsleaf(false);
                    } else {
                        item.setIsleaf(true);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * @description: 资源类型名称转换
     * @author:
     * @date: 2022/8/15 14:18
     * @param: resources_type 资源类型
     * @return: 资源类型名称
     **/
    private Map<String, Object> switchResourcesType(String resourcesType) {
        String resTypeName;
        int resType = 0;
        switch (resourcesType) {
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            case "application/vnd.ms-excel":
                resTypeName = "表格";
                resType = 1;
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            case "application/msword":
                resTypeName = "文档";
                resType = 2;
                break;
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                resTypeName = "幻灯片";
                resType = 3;
                break;
            case "application/x-zip-compressed":
                resTypeName = "压缩包";
                resType = 4;
                break;
            case "text/plain":
                resTypeName = "文本文档";
                resType = 5;
                break;
            case "text/html":
                resTypeName = "html";
                resType = 6;
                break;
            case "text/xml":
                resTypeName = "xml";
                resType = 7;
                break;
            case "application/x-javascript":
                resTypeName = "js";
                resType = 8;
                break;
            case "video/mp4":
                resTypeName = "视频";
                resType = 9;
                break;
            case "application/pdf":
                resTypeName = "pdf";
                resType = 10;
                break;
            default:
                resTypeName = "";
        }
        if (resourcesType.startsWith("image")) {
            resTypeName = "图片";
            resType = 11;
        }
        if (resourcesType.startsWith("video")
                || resourcesType.startsWith("audio")
                || resourcesType.equals("application/vnd.rn-realmedia-vbr")) {
            resTypeName = "视频";
            resType = 9;
        }
        Map<String, Object> res = new HashMap<>();
        res.put("resType", resType);
        res.put("resTypeName", resTypeName);
        return res;
    }

    /**
     * @description: 用于生成存储资源文件目录
     * @author:
     * @date: 2022/8/24 9:14
     * @param: [resources_type 资源类型]
     * @return: java.lang.String
     **/
    public static String switchResourcesTypeToCatalogue(String resourcesType) {
        String resType;
        switch (resourcesType) {
            case "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet":
            case "application/vnd.ms-excel":
                resType = "excel";
                break;
            case "application/vnd.openxmlformats-officedocument.wordprocessingml.document":
            case "application/msword":
            case "text/plain":
                resType = "word";
                break;
            case "application/vnd.ms-powerpoint":
            case "application/vnd.openxmlformats-officedocument.presentationml.presentation":
                resType = "ppt";
                break;
            case "application/x-zip-compressed":
                resType = "zip";
                break;
            case "text/html":
                resType = "html";
                break;
            case "text/xml":
                resType = "xml";
                break;
            case "application/x-javascript":
                resType = "js";
                break;
            case "video/mp4":
                resType = "video";
                break;
            case "application/pdf":
                resType = "pdf";
                break;
            default:
                resType = "other";
        }
        if (resourcesType.startsWith("image")) {
            resType = "image";
        }
        if (resourcesType.startsWith("video")
                || resourcesType.startsWith("audio")
                || resourcesType.equals("application/vnd.rn-realmedia-vbr")) {
            resType = "video";
        }


        return resType;
    }

    /**
     * @description: 路径拼装
     * @author:
     * @date: 2022/8/25 17:16
     * @param: [fileName 文件名称]
     * @return: java.lang.String
     **/
    private String packUrl(String fileName, String resourcesType) {
        //存储根路径
        String path;
        try {
            path = new File(ResourceUtils.getURL("classpath:")
                    .getPath()).getParentFile().getParentFile().getParentFile().getParentFile().getParent();
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
        //存储分类路径
        String catalogueName = "flv".equalsIgnoreCase(fileName.substring(fileName.lastIndexOf(".") + 1)) ? "video" : switchResourcesTypeToCatalogue(resourcesType);
        //路径拼装后最终路径
        String folderPath;
        try {
            folderPath = URLDecoder.decode(path, "UTF-8") + File.separator + "resourcesStore" + File.separator + catalogueName;
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        }
        return folderPath;
    }

    /**
     * @description: 文件重命名
     * @author:
     * @date: 2022/8/25 17:18
     * @param: [fileName 文件名]
     * @return: java.lang.String 重名名
     **/
    private String getFileRename(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf("."))
                + "_"
                + new SnowflakeIdWorker(0, 0).nextId()
                + fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * @description: 文档表格类型转pdf
     * @author:
     * @date: 2022/8/25 17:26
     * @param: [resources, filePath文件路径, pdfPathpdf存储路径]
     * @return: void
     **/
    private void switchToPDF(TResources resources, String filePath, String pdfPath) {
        //文档ddd类型转pdf
        if ("doc".equalsIgnoreCase(resources.getResourcesSuffix()) || "docx".equalsIgnoreCase(resources.getResourcesSuffix())) {
            FileConvertUtil.wordBytes2PdfFile(FileUtil.readBytes(filePath),
                    pdfPath);
            resources.setResourcesPdfUrl(pdfPath);
        }
        //表格转pdf
        if ("xls".equalsIgnoreCase(resources.getResourcesSuffix()) || "xlsx".equalsIgnoreCase(resources.getResourcesSuffix())) {
            FileConvertUtil.excelBytes2PdfFile(FileUtil.readBytes(filePath),
                    pdfPath);
            resources.setResourcesPdfUrl(pdfPath);
        }
    }

    /**
     * @description: 保存到资源表
     * @author:
     * @date: 2022/8/25 17:34
     * @param: [fileName 文件名, name 重命名, filePath 文件地址, loginUser 登录人信息, resourcesType, resourcesSize, folderPath存储路径]
     * @return: com.highgo.opendbt.resources.model.TResources
     **/
    private TResources saveResources(String fileName, String name, String filePath, UserInfo loginUser, String resourcesType, String resourcesSize, String folderPath, String resourcesAdditional) {
        TResources resources = new TResources();
        resources.setCreateUser(loginUser.getUserId());
        resources.setCreateTime(new Date());
        //资源名称
        resources.setResourcesName(fileName);
        //资源存储名称
        resources.setResourcesRename(name);
        //资源后缀
        resources.setResourcesSuffix(fileName.substring(fileName.lastIndexOf(".") + 1));
        resources.setDeleteFlag(0);
        //资源路径
        resources.setResourcesUrl(filePath);
        //资源大小
        resources.setResourcesSize(StringUtils.isEmpty(resourcesSize) ? 0 : Integer.parseInt(resourcesSize));
        //资源类型
        resources.setResourcesType(resourcesType);
        //资源类型名称
        resources.setResourcesTypeName("flv".equalsIgnoreCase(resources.getResourcesSuffix()) ? "视频"
                : switchResourcesType(resourcesType).get("resTypeName").toString());
        //转换后的资源类型名称
        resources.setResourcesRetype("flv".equalsIgnoreCase(resources.getResourcesSuffix()) ? 9
                : Integer.parseInt(switchResourcesType(resourcesType).get("resType").toString()));
        //若为视频，设置视频时长，和缩略图
        if (resources.getResourcesRetype() == 9) {
            resources.setResourcesTime(Integer.parseInt(VideoTimeUtil.parseDuration(filePath)[0]));
            //截取缩略图
            String shotUrl;
            try {
                shotUrl = VideoTimeUtil.videoImage(filePath, folderPath + File.separator);
            } catch (FrameGrabber.Exception e) {
                logger.error(e.getMessage(), e);
                throw new APIException(e.getMessage());
            }
            resources.setScreenshot(shotUrl);
        }
        //设置是否为其他资源
        if ("1".equals(resourcesAdditional)) {
            resources.setResourcesAdditional(1);
        }
        String pdfPath = filePath.substring(0, filePath.lastIndexOf(".")) + ".pdf";
        //文档表格类型转pdf
        switchToPDF(resources, filePath, pdfPath);
        logger.info("上传参数【{}】", resources.toString());
        int insert = resourcesMapper.insert(resources);
        BusinessResponseEnum.FAILUPLOAD.assertIsTrue(insert > 0);
        resources.setSortNum(resources.getId());
        //设置排序序号
        BusinessResponseEnum.FAILUPLOAD.assertIsTrue(this.updateById(resources));
        return resources;
    }

    /**
     * @description: 文件路径转换（word excel文件转为pdf文件方便前端显示）
     * @author:
     * @date: 2022/8/25 17:40
     * @param: [resources 资源信息, resourcesType[screenshot:缩略图 original:原始文件 transfer:转换文件（word、excel转pdf） down: 下载 ]]
     * @return: java.lang.String
     **/
    private String switchFilePath(TResources resources, String resourcesType) {
        String filePath;
        switch (resourcesType) {
            case "screenshot":
                filePath = resources.getScreenshot();
                break;
            case "original":
                filePath = resources.getResourcesUrl();
                break;
            case "transfer":
                if ("文档".equalsIgnoreCase(resources.getResourcesTypeName())
                        || "表格".equalsIgnoreCase(resources.getResourcesTypeName())) {
                    filePath = resources.getResourcesPdfUrl();
                } else {
                    filePath = resources.getResourcesUrl();
                }
                break;
            case "down":
                filePath = resources.getResourcesUrl();
                break;
            default:
                filePath = resources.getResourcesUrl();

        }
        return filePath;
    }

    /**
     * @description: 流方式读取资源
     * @author:
     * @date: 2022/8/25 17:45
     * @param: [filePath 文件地址,  resources 资源信息, response 响应, resourcesType 资源类型]
     * @return: void
     **/
    private void readFile(String filePath, TResources resources, HttpServletResponse response, String resourcesType) {
        ServletOutputStream out = null;
        File file = new File(filePath);
        //文件不存在返回
        if (!file.exists()) {
            return;
        }
        try (InputStream ips = new FileInputStream(file)) {
            //获取资源存放路径
            //文件类型
            response.addHeader("Content-Type", resources.getResourcesType() + ";charset=utf-8");
            if ("down".equalsIgnoreCase(resourcesType)) {
                //前端响应下载
                response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
            }
            out = response.getOutputStream();
            //读取文件流
            int len;
            byte[] buffer = new byte[1024 * 10];
            while ((len = ips.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            out.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                    logger.error("流方式读取资源失败", e);
                }
            }
        }
    }
}
