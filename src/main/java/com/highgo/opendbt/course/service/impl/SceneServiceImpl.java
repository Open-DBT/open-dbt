package com.highgo.opendbt.course.service.impl;

import au.com.bytecode.opencsv.CSVReader;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.SchemaConnection;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.service.RunAnswerService;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.CloseUtil;
import com.highgo.opendbt.common.utils.ExcelUtil;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.model.ImportExerciseTO;
import com.highgo.opendbt.course.domain.model.Scene;
import com.highgo.opendbt.course.domain.model.SceneDetail;
import com.highgo.opendbt.course.domain.model.ScenePage;
import com.highgo.opendbt.course.mapper.ExerciseMapper;
import com.highgo.opendbt.course.mapper.SceneDetailMapper;
import com.highgo.opendbt.course.mapper.SceneMapper;
import com.highgo.opendbt.course.service.SceneService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SceneServiceImpl implements SceneService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final SceneMapper sceneMapper;

    private final SceneDetailMapper sceneDetailMapper;

    private final RunAnswerService runAnswerService;

    private final ExerciseMapper exerciseMapper;

    public SceneServiceImpl(SceneMapper sceneMapper, SceneDetailMapper sceneDetailMapper, RunAnswerService runAnswerService, ExerciseMapper exerciseMapper) {
        this.sceneMapper = sceneMapper;
        this.sceneDetailMapper = sceneDetailMapper;
        this.runAnswerService = runAnswerService;
        this.exerciseMapper = exerciseMapper;
    }

    @Override
    public PageInfo<Scene> getScene(ScenePage scenePage) {
            // ??????????????????
            PageHelper.startPage(scenePage.getCurrent(), scenePage.getPageSize());
            List<Scene> sceneList = sceneMapper.getScene(scenePage.getCourseId());
            return new PageInfo<Scene>(sceneList);
    }

    @Override
    public List<Scene> getShareScene(int courseId) {
            return sceneMapper.getScene(courseId);
    }

    @Override
    public Scene getSceneDetail(int sceneId) {
            // ?????????????????????
            Scene scene = sceneMapper.getSceneDetail(sceneId);
            // ???????????????????????????
            List<SceneDetail> sceneDetailList = sceneDetailMapper.getSceneDetailById(sceneId);
            scene.setSceneDetailList(sceneDetailList);
            return scene;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateScene(HttpServletRequest request, Scene scene) {
        // ??????id??????-1?????????
        if (scene.getSceneId() == -1) {
            // ????????????
            Integer addScene = sceneMapper.addScene(scene);
            // ??????SQL????????????????????????????????????????????????
            if (scene.getInitShell().length() > 0) {
                List<SceneDetail> sceneDetailList = parsSQLGetTableName(scene.getSceneId(), scene.getInitShell());
                if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
                    sceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
                }
            }
            return addScene;
        } else {
            // ??????????????????
            sceneDetailMapper.deleteSceneDetail(scene.getSceneId());
            // ??????SQL????????????????????????????????????????????????
            if (scene.getInitShell().length() > 0) {
                List<SceneDetail> sceneDetailList = parsSQLGetTableName(scene.getSceneId(), scene.getInitShell());
                if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
                    sceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
                }
            }
            // ????????????
            return  sceneMapper.updateScene(scene);
        }
    }

    private static List<SceneDetail> parsSQLGetTableName(int sceneId, String initShell){
        List<SceneDetail> sceneDetailList = new ArrayList<SceneDetail>();

        String matchStr = "createtable";

        // ????????????????????????????????????????????????
        String newStr = initShell.replaceAll("[\\t\\n\\r]", "").replace(" ", "");

        // ???????????????????????????SQL
        String[] newStrArray = newStr.split(";");
        for (int i = 0; i < newStrArray.length; i++) {
            String element = newStrArray[i];
            // ???????????????????????????"createtable"??????
            if (element.toLowerCase().startsWith(matchStr)) {
                // ???????????????
                String tableName = element.substring(matchStr.length(), element.indexOf("("));

                // ????????????????????????????????????????????????????????????????????????
                if (tableName.startsWith("\"") && tableName.endsWith("\"")) {
                    sceneDetailList.add(new SceneDetail(sceneId, tableName));
                } else {
                    sceneDetailList.add(new SceneDetail(sceneId, tableName.toLowerCase()));
                }
            }
        }
        return sceneDetailList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteScene(HttpServletRequest request, int sceneId) {
        List<Exercise> exerciseList = exerciseMapper.getExerciseBySceneId(sceneId);
        BusinessResponseEnum.SCENEISUSENOTDELETE.assertIsEmpty(exerciseList);
        Integer deleteScene = sceneMapper.deleteScene(sceneId);
        return deleteScene;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copyScene(int sceneId) {
        // ?????????????????????????????????
        Scene scene = sceneMapper.getSceneDetail(sceneId);
        BusinessResponseEnum.GETCOPYSCENEFAIL.assertNotNull(scene);
        // ?????????????????????????????????
        List<SceneDetail> sceneDetailList = sceneDetailMapper.getSceneDetailById(sceneId);
        Integer addScene = sceneMapper.addScene(scene);
        if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
            sceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
        }
        return addScene;
    }

    @Override
    public Integer testSceneSQLShell(HttpServletRequest request, String initShell) {
        Connection connection = null;
        Statement statement = null;
        Integer executeUpdate = null;
        SchemaConnection schemaConnection = new SchemaConnection();
        try {
            // ??????????????????
            UserInfo loginUser = Authentication.getCurrentUser(request);
            // ??????????????????????????????schema?????????
            runAnswerService.getSchemaConnection(loginUser, schemaConnection);
            if (null != schemaConnection.getConnection()) {
                connection = schemaConnection.getConnection();
                statement = connection.createStatement();
                // ??????sql??????
                executeUpdate = statement.executeUpdate(initShell);
            }
            return executeUpdate;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        } finally {
            runAnswerService.dropSchema(schemaConnection.getSchemaName());
            CloseUtil.close(statement);
            CloseUtil.close(connection);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copySceneToMyCourse(int sceneId, int courseId) {
        // ?????????????????????????????????
        Scene scene = sceneMapper.getSceneDetail(sceneId);
        BusinessResponseEnum.GETCOPYSCENEFAIL.assertNotNull(scene);
        // ?????????????????????????????????
        List<SceneDetail> sceneDetailList = sceneDetailMapper.getSceneDetailById(sceneId);
        scene.setCourseId(courseId);
        Integer addScene = sceneMapper.addScene(scene);
        if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
            sceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
        }
        return addScene;
    }

    @Override
    public String exportSceneList(HttpServletRequest request, int courseId) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ???????????????
        String fileName = "scene_export_" + loginUser.getCode() + "_" + TimeUtil.getDateTimeStr() + ".csv";
        // ???????????????
        String[] columnNameArray = new String[]{"??????ID", "????????????", "????????????", "??????SQL??????"};
        List<Map<Integer, Object>> mapList = new ArrayList<Map<Integer, Object>>();
        List<Scene> sceneList = sceneMapper.getScene(courseId);
        // ?????????map???list???????????????????????????????????????????????????????????????????????????
        for (int i = 0; i < sceneList.size(); i++) {
            Scene scene = sceneList.get(i);

            Map<Integer, Object> dataMap = new HashMap<Integer, Object>();
            dataMap.put(0, scene.getSceneId());
            dataMap.put(1, scene.getSceneName());
            dataMap.put(2, scene.getSceneDesc());
            dataMap.put(3, scene.getInitShell());
            mapList.add(dataMap);
        }
        // ????????????????????????
        ExcelUtil.writeCSV(fileName, columnNameArray, mapList);
        return fileName;
    }

    @Override
    public String exportSceneById(HttpServletRequest request, int sceneId) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ???????????????
        String[] columnNameArray = new String[]{"??????ID", "????????????", "????????????", "??????SQL??????"};
        List<Map<Integer, Object>> mapList = new ArrayList<Map<Integer, Object>>();
        Scene scene = sceneMapper.getSceneDetail(sceneId);
        Map<Integer, Object> dataMap = new HashMap<Integer, Object>();
        dataMap.put(0, scene.getSceneId());
        dataMap.put(1, scene.getSceneName());
        dataMap.put(2, scene.getSceneDesc());
        dataMap.put(3, scene.getInitShell());
        mapList.add(dataMap);
        // ???????????????
        String fileName = loginUser.getCode() + "_" + scene.getSceneName() + ".csv";
        // ????????????????????????
        ExcelUtil.writeCSV(fileName, columnNameArray, mapList);
        return fileName;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadSceneListFile(HttpServletRequest request, MultipartFile file) {
        FileOutputStream fos = null;
        try {
            // ??????????????????
            UserInfo loginUser = Authentication.getCurrentUser(request);

            String fileName = file.getOriginalFilename();
            String[] fileNameArray = fileName.split("\\.");
            String newFileName = "scene_import_" + loginUser.getUserId() + "_" + loginUser.getCode() + "." + fileNameArray[fileNameArray.length - 1];

            String folderPath = ExcelUtil.getProjectPath() + "/temp";

            File folderPathFile = new File(folderPath);
            if (!folderPathFile.exists()) {
                folderPathFile.mkdir();
            }
            String filePath = folderPath + "/" + newFileName;
            fos = new FileOutputStream(filePath);
            fos.write(file.getBytes());
            return filePath;
        } catch (Exception e) {
            throw new APIException(e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importScene(HttpServletRequest request, ImportExerciseTO importExerciseTO) {
        CSVReader csvReader = null;
        try {
            for (String filePath : importExerciseTO.getFilePathList()) {
                // ????????????????????????????????????excel??????
                ExcelUtil.checkFileExists(filePath);
                csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), Charset.forName("UTF-8")));
                List<String[]> dataList = csvReader.readAll();
                logger.info("data count = " + dataList.size());
                if (dataList.size() > 1) {
                    // ????????????????????????
                    for (int i = 1; i < dataList.size(); i++) {
                        try {
                            String[] data = dataList.get(i);
                            Scene scene = new Scene();

                            scene.setSceneName(data[1]);
                            scene.setSceneDesc(data[2]);
                            scene.setInitShell(data[3]);
                            scene.setCourseId(importExerciseTO.getCourseId());
                            sceneMapper.addScene(scene);
                            // ??????SQL????????????????????????????????????????????????
                            if (scene.getInitShell().length() > 0) {
                                List<SceneDetail> sceneDetailList = parsSQLGetTableName(scene.getSceneId(), scene.getInitShell());
                                if (sceneDetailList != null && !sceneDetailList.isEmpty()) {
                                    sceneDetailMapper.addSceneDetail(scene.getSceneId(), sceneDetailList);
                                }
                            }
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
            return "";
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new APIException(e.getMessage());
        } finally {
            CloseUtil.close(csvReader);
        }
    }

}
