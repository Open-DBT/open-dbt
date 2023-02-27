package com.highgo.opendbt.publicLibrary.service.impl;

import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import com.highgo.opendbt.common.bean.DataTypeAndImg;
import com.highgo.opendbt.common.bean.SchemaConnection;
import com.highgo.opendbt.common.constant.Constant;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.service.RunAnswerService;
import com.highgo.opendbt.common.utils.Authentication;
import com.highgo.opendbt.common.utils.CloseUtil;
import com.highgo.opendbt.common.utils.ExcelUtil;
import com.highgo.opendbt.common.utils.TimeUtil;
import com.highgo.opendbt.publicLibrary.mapper.PublicExerciseMapper;
import com.highgo.opendbt.publicLibrary.model.ImportPublicExerciseTO;
import com.highgo.opendbt.publicLibrary.model.PublicExercise;
import com.highgo.opendbt.publicLibrary.model.PublicExercisePage;
import com.highgo.opendbt.publicLibrary.service.PublicExerciseService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PublicExerciseServiceImpl implements PublicExerciseService {

    Logger logger = LoggerFactory.getLogger(getClass());

    final PublicExerciseMapper publicExerciseMapper;

    final RunAnswerService runAnswerService;

    @Override
    public PageInfo<PublicExercise> getPublicExerciseList(PublicExercisePage publicExercisePage) {
        // 分页查询配置
        PageMethod.startPage(publicExercisePage.getCurrent(), publicExercisePage.getPageSize());
        return new PageInfo<>(publicExerciseMapper.getPublicExerciseList(publicExercisePage));

    }

    @Override
    public PublicExercise getPublicExerciseInfo(int exerciseId) {
        return publicExerciseMapper.getPublicExerciseInfo(exerciseId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateExercise(HttpServletRequest request, PublicExercise publicExercise) {
        UserInfo loginUser = Authentication.getCurrentUser(request);
        if (publicExercise.getExerciseId() == -1) {
            publicExercise.setCreator(loginUser.getUserId());
            publicExercise.setCreateTime(TimeUtil.getDateTime());
            return publicExerciseMapper.addExercise(publicExercise);
        } else {
            return publicExerciseMapper.updateExercise(publicExercise);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExercise(int exerciseId) {
        return publicExerciseMapper.deleteExercise(exerciseId);
    }

    @Override
    public Map<String, Object> testRunAnswer(HttpServletRequest request, PublicExercise publicExercise) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        SchemaConnection schemaConnection = new SchemaConnection();
        Map<String, Object> resultMap = null;
        try {
            // 获取用户信息
            UserInfo loginUser = Authentication.getCurrentUser(request);
            // 初始化脚本并获取指定schema的连接
            runAnswerService.getSchemaConnection(loginUser, publicExercise.getSceneId(), 0, schemaConnection, 1);
            if (null != schemaConnection.getConnection()) {
                resultMap = new LinkedHashMap<>();
                connection = schemaConnection.getConnection();
                statement = connection.createStatement();


                // 区分是查询还是其他，查询返回结果集，其他返回影响行数
                if (publicExercise.getAnswer().toLowerCase().startsWith("select")) {
                    resultMap.put("isSelect", true);
                    List<String> columnList = new ArrayList<>();
                    List<DataTypeAndImg> dataTypeAndImgList = new ArrayList<>();
                    List<Map<Object, Object>> resultSetList = new ArrayList<>();
                    resultSet = statement.executeQuery(publicExercise.getAnswer());
                    // 把ResultSet结果集转换成list，并加唯一列
                    runAnswerService.getResultSetColumnAndData(resultSet, columnList, dataTypeAndImgList, resultSetList);
                    resultMap.put(Constant.TEST_RUN_DATATYPE, dataTypeAndImgList);
                    resultMap.put(Constant.TEST_RUN_COLUMN, columnList);
                    resultMap.put(Constant.TEST_RUN_RESULT, resultSetList);
                } else {
                    resultMap.put("isSelect", false);
                    int updateRow = statement.executeUpdate(publicExercise.getAnswer());
                    resultMap.put(Constant.TEST_RUN_RESULT, updateRow);
                }
            }
            return resultMap;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new APIException(e.getMessage());
        } finally {
            runAnswerService.dropSchema(schemaConnection.getSchemaName());
            CloseUtil.close(resultSet);
            CloseUtil.close(statement);
            CloseUtil.close(connection);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importPublicExercise(HttpServletRequest request, ImportPublicExerciseTO importPublicExerciseTO) {
        Workbook workbook = null;
        try {
            UserInfo loginUser = Authentication.getCurrentUser(request);

            for (String filePath : importPublicExerciseTO.getFilePathList()) {
                // 检查文件是否存在，是否是excel文案
                ExcelUtil.checkFile(filePath);

                workbook = ExcelUtil.getWorbook(filePath);

                // 获取excel表格的第一个sheet
                Sheet sheet = workbook.getSheetAt(0);

                // 获取最后一行下标
                int sheetLastRowIndex = sheet.getLastRowNum();
                logger.info("sheet sheetLastRowIndex = " + sheetLastRowIndex);

                // 读取前两列数据进行导入
                for (int i = 1; i <= sheetLastRowIndex; i++) {
                    try {
                        Row row = sheet.getRow(i);
                        if (null != row) {

                            if (null == row.getCell(2)
                                    || null == row.getCell(4)
                                    || row.getCell(2).toString().trim().equals("")
                                    || row.getCell(4).toString().trim().equals("")) {
                                continue;
                            }

                            PublicExercise publicExercise = new PublicExercise();

                            publicExercise.setExerciseName(row.getCell(2).toString().trim());
                            publicExercise.setExerciseDesc(row.getCell(3).toString().trim());
                            publicExercise.setAnswer(row.getCell(4).toString().trim());

                            publicExercise.setSceneId(-1);
                            publicExercise.setCreator(loginUser.getUserId());
                            publicExercise.setCreateTime(TimeUtil.getDateTime());

                            // 添加习题
                            publicExerciseMapper.addExercise(publicExercise);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return "";
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new APIException(e.getMessage());
        } finally {
            CloseUtil.close(workbook);
        }
    }

    @Override
    public String exportPublicExercise(HttpServletRequest request) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);

        // 导出文件名
        String fileName = "public_exercise_export_" + loginUser.getCode() + "_" + TimeUtil.getDateTimeStr() + ".xls";
        // 表头字段名
        String[] columnNameArray = new String[]{"习题ID", "场景名称", "习题名称", "习题描述", "正确答案"};
        List<Map<Integer, Object>> mapList = new ArrayList<>();

        List<PublicExercise> exerciseList = publicExerciseMapper.getPublicExerciseDetail();

        // 转换为map的list，方便写入文件时每个字段的循环以及公共方法参数一致
		for (PublicExercise exercise : exerciseList) {
			Map<Integer, Object> dataMap = new HashMap<>();
			dataMap.put(0, exercise.getExerciseId());
			dataMap.put(1, exercise.getSceneName());
			dataMap.put(2, exercise.getExerciseName());
			dataMap.put(3, exercise.getExerciseDesc());
			dataMap.put(4, exercise.getAnswer());
			mapList.add(dataMap);
		}
        // 调用写入文件方法
        ExcelUtil.writeXLS(fileName, columnNameArray, mapList);
        return fileName;

    }

}
