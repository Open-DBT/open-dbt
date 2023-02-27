package com.highgo.opendbt.publicLibrary.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.PageTO;
import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.bean.SchemaConnection;
import com.highgo.opendbt.common.service.RunAnswerService;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.CloseUtil;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.publicLibrary.mapper.PublicSceneDetailMapper;
import com.highgo.opendbt.publicLibrary.mapper.PublicSceneMapper;
import com.highgo.opendbt.publicLibrary.model.PublicScene;
import com.highgo.opendbt.publicLibrary.model.PublicSceneDetail;
import com.highgo.opendbt.publicLibrary.service.PublicSceneService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class PublicSceneServiceImpl implements PublicSceneService {
    Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PublicSceneMapper publicSceneMapper;
    @Autowired
    private PublicSceneDetailMapper publicSceneDetailMapper;
    @Autowired
    private RunAnswerService runAnswerService;

    @Override
    public PageInfo<PublicScene> getScene(PageTO page) {
        // 分页查询配置
        PageHelper.startPage(page.getCurrent(), page.getPageSize());
        List<PublicScene> sceneList = publicSceneMapper.getScene();
        return new PageInfo<PublicScene>(sceneList);
    }

    @Override
    public List<PublicScene> getSceneNameList() {
        return publicSceneMapper.getSceneNameList();
    }

    @Override
    public PublicScene getSceneDetail(int sceneId) {
        return publicSceneMapper.getSceneDetail(sceneId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultTO<Integer> updateScene(HttpServletRequest request, PublicScene scene) {
        try {
            UserInfo loginUser = Authentication.getCurrentUser(request);

            // 场景id等于-1为新增
            if (scene.getSceneId() == -1) {
                scene.setCreator(loginUser.getUserId());
                scene.setCreateTime(TimeUtil.getDateTime());
                // 新增场景
                publicSceneMapper.addScene(scene);
                // 解析SQL脚本获取表名，并添加到场景明细表
                if (scene.getInitShell().length() > 0) {
                    List<PublicSceneDetail> sceneDetailList = parsSQLGetTableName(scene.getSceneId(), scene.getInitShell());
                    if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
                        publicSceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
                    }
                }
            } else {
                // 删除场景明细
                publicSceneDetailMapper.deleteSceneDetail(scene.getSceneId());

                // 解析SQL脚本获取表名，并添加到场景明细表
                if (scene.getInitShell().length() > 0) {
                    List<PublicSceneDetail> sceneDetailList = parsSQLGetTableName(scene.getSceneId(), scene.getInitShell());
                    if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
                        publicSceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
                    }
                }
                // 更新场景
                publicSceneMapper.updateScene(scene);
            }
            return ResultTO.OK();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    private static List<PublicSceneDetail> parsSQLGetTableName(int sceneId, String initShell) throws Exception {
        List<PublicSceneDetail> sceneDetailList = new ArrayList<PublicSceneDetail>();

        String matchStr = "createtable";

        // 去掉脚本字符串中的所有换行和空格
        String newStr = initShell.replaceAll("[\\t\\n\\r]", "").replace(" ", "");

        // 用分号分隔获取每个SQL
        String[] newStrArray = newStr.split(";");
        for (int i = 0; i < newStrArray.length; i++) {
            String element = newStrArray[i];
            // 转成小写判断是否是"createtable"开头
            if (element.toLowerCase().startsWith(matchStr)) {
                // 截取出表名
                String tableName = element.substring(matchStr.length(), element.indexOf("("));

                // 如果表名是加引号的就原样保存，否则就转成小写保存
                if (tableName.startsWith("\"") && tableName.endsWith("\"")) {
                    sceneDetailList.add(new PublicSceneDetail(sceneId, tableName));
                } else {
                    sceneDetailList.add(new PublicSceneDetail(sceneId, tableName.toLowerCase()));
                }
            }
        }
        return sceneDetailList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResultTO<Integer> deleteScene(int sceneId) {
        try {
            publicSceneMapper.deleteScene(sceneId);
            return ResultTO.OK();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return ResultTO.FAILURE(e.getMessage());
        }
    }

    @Override
    public ResultTO<Integer> testSceneSQLShell(HttpServletRequest request, String initShell) {
        Connection connection = null;
        Statement statement = null;
        SchemaConnection schemaConnection = new SchemaConnection();
        try {
            // 获取用户信息
            UserInfo loginUser = Authentication.getCurrentUser(request);
            // 初始化脚本并获取指定schema的连接
            runAnswerService.getSchemaConnection(loginUser, schemaConnection);
            if (null != schemaConnection.getConnection()) {
                connection = schemaConnection.getConnection();
                statement = connection.createStatement();
                // 执行sql脚本
                statement.executeUpdate(initShell);
            }
            return ResultTO.OK();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultTO.FAILURE(e.getMessage());
        } finally {
            runAnswerService.dropSchema(schemaConnection.getSchemaName());
            CloseUtil.close(statement);
            CloseUtil.close(connection);
        }
    }

}
