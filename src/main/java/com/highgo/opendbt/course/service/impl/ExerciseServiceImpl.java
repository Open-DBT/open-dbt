package com.highgo.opendbt.course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.common.bean.DataTypeAndImg;
import com.highgo.opendbt.common.bean.SchemaConnection;
import com.highgo.opendbt.common.constant.Constant;
import com.highgo.opendbt.common.exception.APIException;
import com.highgo.opendbt.common.exception.enums.BusinessResponseEnum;
import com.highgo.opendbt.common.service.RunAnswerService;
import com.highgo.opendbt.common.utils.*;
import com.highgo.opendbt.course.domain.entity.Course;
import com.highgo.opendbt.course.domain.entity.Exercise;
import com.highgo.opendbt.course.domain.entity.ExerciseKnowledge;
import com.highgo.opendbt.course.domain.entity.Knowledge;
import com.highgo.opendbt.course.domain.model.ExerciseDisplay;
import com.highgo.opendbt.course.domain.model.ExercisePage;
import com.highgo.opendbt.course.domain.model.ImportExerciseTO;
import com.highgo.opendbt.course.mapper.CourseMapper;
import com.highgo.opendbt.course.mapper.ExerciseKnowledgeMapper;
import com.highgo.opendbt.course.mapper.ExerciseMapper;
import com.highgo.opendbt.course.mapper.KnowledgeMapper;
import com.highgo.opendbt.course.service.ExerciseService;
import com.highgo.opendbt.exercise.service.TNewExerciseService;
import com.highgo.opendbt.system.domain.entity.UserInfo;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Service
public class ExerciseServiceImpl extends ServiceImpl<ExerciseMapper, Exercise>
        implements ExerciseService {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final ExerciseMapper exerciseMapper;

    private final ExerciseKnowledgeMapper exerciseKnowledgeMapper;

    private final KnowledgeMapper knowledgeMapper;

    private final RunAnswerService runAnswerService;

    private final CourseMapper courseMapper;

    private final TNewExerciseService exerciseService;

    public ExerciseServiceImpl(ExerciseMapper exerciseMapper, ExerciseKnowledgeMapper exerciseKnowledgeMapper, KnowledgeMapper knowledgeMapper, RunAnswerService runAnswerService, CourseMapper courseMapper, TNewExerciseService exerciseService) {
        this.exerciseMapper = exerciseMapper;
        this.exerciseKnowledgeMapper = exerciseKnowledgeMapper;
        this.knowledgeMapper = knowledgeMapper;
        this.runAnswerService = runAnswerService;
        this.courseMapper = courseMapper;
        this.exerciseService = exerciseService;
    }

    @Override
    public List<Exercise> getExerciseList(ExercisePage exercisePage) {
        List<Exercise> exerciseList = exerciseMapper.getExercise(exercisePage);
        // 遍历查询出的习题查询习题关联的知识点
        for (Exercise exercise : exerciseList) {
            // 通过习题id查询关联的知识点，并把知识点id放到数组
            List<Knowledge> knowledgeList = knowledgeMapper.getKnowledgeByExerciseId(exercise.getExerciseId());
            int[] knowledgeIds = new int[knowledgeList.size()];
            String[] knowledgeNames = new String[knowledgeList.size()];
            if (knowledgeList != null && !knowledgeList.isEmpty()) {
                for (int i = 0; i < knowledgeList.size(); i++) {
                    knowledgeIds[i] = knowledgeList.get(i).getKnowledgeId();
                    knowledgeNames[i] = knowledgeList.get(i).getName();
                }
            }
            exercise.setKnowledgeIds(knowledgeIds);
            exercise.setKnowledgeNames(knowledgeNames);
        }
        return exerciseList;
    }

    @Override
    public PageInfo<Exercise> getExercise(ExercisePage exercisePage) {
        // 分页查询配置
        PageHelper.startPage(exercisePage.getCurrent(), exercisePage.getPageSize());
        List<Exercise> exerciseList = exerciseMapper.getExercise(exercisePage);
        PageInfo<Exercise> exercisePageInfo = new PageInfo<Exercise>(exerciseList);
        // 遍历分页查询出的习题查询习题关联的知识点
        List<Exercise> exerciseListPageInfo = exercisePageInfo.getList();
        for (Exercise exercise : exerciseListPageInfo) {
            // 通过习题id查询关联的知识点，并把知识点id放到数组
            List<Knowledge> knowledgeList = knowledgeMapper.getKnowledgeByExerciseId(exercise.getExerciseId());
            int[] knowledgeIds = new int[knowledgeList.size()];
            String[] knowledgeNames = new String[knowledgeList.size()];
            if (knowledgeList != null && !knowledgeList.isEmpty()) {
                for (int i = 0; i < knowledgeList.size(); i++) {
                    knowledgeIds[i] = knowledgeList.get(i).getKnowledgeId();
                    knowledgeNames[i] = knowledgeList.get(i).getName();
                }
            }
            exercise.setKnowledgeIds(knowledgeIds);
            exercise.setKnowledgeNames(knowledgeNames);
        }
        return exercisePageInfo;
    }

    /**
     * @description: 新增修改习题
     * @author:
     * @date: 2023/1/6 9:57
     * @param: [request, exercise]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateExercise(HttpServletRequest request, Exercise exercise) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        Course course = courseMapper.getCourseByCourseId(exercise.getCourseId());
        if (null == course) {
            throw new APIException(Message.get("GetExerciseCourseFile"));
        }
        Integer updateExercise;
        if (exercise.getExerciseId() == -1) {
            exercise.setCreateTime(TimeUtil.getDateTime());
            exercise.setCreator(loginUser.getUserId());
            // 添加习题
            updateExercise = exerciseMapper.addExercise(exercise);
            // 添加习题和知识点的关联关系
            if (exercise.getKnowledgeIds().length > 0) {
                exerciseKnowledgeMapper.addExerciseKnowledgeArray(exercise.getCourseId(), exercise.getExerciseId(), exercise.getKnowledgeIds());
            }
        } else {
            exercise.setUpdateTime(TimeUtil.getDateTime());
            // 修改习题
            updateExercise = exerciseMapper.updateExercise(exercise);
            // 删除习题和知识点的关联关系
            exerciseKnowledgeMapper.deleteByExerciseId(exercise.getExerciseId());
            // 重新添加习题和知识点的关联关系
            if (exercise.getKnowledgeIds().length > 0) {
                exerciseKnowledgeMapper.addExerciseKnowledgeArray(exercise.getCourseId(), exercise.getExerciseId(), exercise.getKnowledgeIds());
            }
        }
        return updateExercise;

    }

    /**
     * @description: 删除习题
     * @author:
     * @date: 2023/1/6 9:58
     * @param: [request, exerciseId]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExercise(HttpServletRequest request, int exerciseId) {
        // 删除习题
        return exerciseMapper.deleteExercise(exerciseId);
    }

    /**
     * @description: 复制习题
     * @author:
     * @date: 2023/1/6 9:59
     * @param: [request, exerciseId]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copyExercise(HttpServletRequest request, int exerciseId) {
        // 获取要复制的习题的信息
        Exercise exercise = exerciseMapper.getExerciseById(exerciseId);
        if (null == exercise) {
            throw new APIException(Message.get("GetCopyExerciseFail"));
        }
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 获取习题和知识点的关联数据
        List<ExerciseKnowledge> list = exerciseKnowledgeMapper.getExerciseKnowledgeByExerciseId(exerciseId);
        exercise.setCreateTime(TimeUtil.getDateTime());
        exercise.setCreator(loginUser.getUserId());
        // 添加习题
        Integer addExercise = exerciseMapper.addExercise(exercise);
        // 修改习题id为新的习题id，新增习题和知识点的关联数据
        if (list != null && !list.isEmpty()) {
            for (ExerciseKnowledge item : list) {
                item.setExerciseId(exercise.getExerciseId());
            }
            exerciseKnowledgeMapper.addExerciseKnowledgeList(list);
        }
        return addExercise;
    }

    /**
     * @description: 新增习题
     * @author:
     * @date: 2023/1/6 10:01
     * @param: [request, exerciseId, courseId]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copyExerciseToMyCourse(HttpServletRequest request, int exerciseId, int courseId) {
        // 获取要复制的习题的信息
        Exercise exercise = exerciseMapper.getExerciseById(exerciseId);
        if (null == exercise) {
            throw new APIException(Message.get("GetCopyExerciseFail"));
        }
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //清空场景、重置习题
        exercise.setSceneId(-1);
        exercise.setCourseId(courseId);
        exercise.setCreateTime(TimeUtil.getDateTime());
        exercise.setCreator(loginUser.getUserId());
        // 添加习题
        Integer addExercise = exerciseMapper.addExercise(exercise);
        return addExercise;

    }

    @Override
    public Map<String, Object> testRunAnswer(HttpServletRequest request, Exercise exercise) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        Map<String, Object> resultMap = null;
        SchemaConnection schemaConnection = new SchemaConnection();
        try {
            // 获取用户信息
            UserInfo loginUser = Authentication.getCurrentUser(request);
            // 初始化脚本并获取指定schema的连接
            runAnswerService.getSchemaConnection(loginUser, exercise.getSceneId(), 0, schemaConnection, 0);
            if (null != schemaConnection.getConnection()) {
                connection = schemaConnection.getConnection();
                statement = connection.createStatement();
                resultMap = new LinkedHashMap<String, Object>();
                // 区分是查询还是其他，查询返回结果集，其他返回影响行数
                if (exercise.getAnswer().toLowerCase().startsWith("select")) {
                    resultMap.put("isSelect", true);
                    List<String> columnList = new ArrayList<String>();
                    List<DataTypeAndImg> dataTypeAndImgList = new ArrayList<DataTypeAndImg>();
                    List<Map<Object, Object>> resultSetList = new ArrayList<Map<Object, Object>>();
                    resultSet = statement.executeQuery(exercise.getAnswer());
                    // 把ResultSet结果集转换成list，并加唯一列
                    runAnswerService.getResultSetColumnAndData(resultSet, columnList, dataTypeAndImgList, resultSetList);
                    resultMap.put(Constant.TEST_RUN_DATATYPE, dataTypeAndImgList);
                    resultMap.put(Constant.TEST_RUN_COLUMN, columnList);
                    resultMap.put(Constant.TEST_RUN_RESULT, resultSetList);
                } else {
                    resultMap.put("isSelect", false);
                    int updateRow = statement.executeUpdate(exercise.getAnswer());
                    resultMap.put(Constant.TEST_RUN_RESULT, updateRow);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            new APIException(e.getMessage());
        } finally {
            runAnswerService.dropSchema(schemaConnection.getSchemaName());
            CloseUtil.close(resultSet);
            CloseUtil.close(statement);
            CloseUtil.close(connection);
        }
        return resultMap;
    }

    @Override
    public List<Exercise> getExerciseInfoList(HttpServletRequest request, int sclassId, int courseId, int knowledgeId) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        List<Exercise> exerciseList = exerciseMapper.getExerciseInfoList(loginUser.getUserId(), sclassId, courseId, knowledgeId);
        for (Exercise exercise : exerciseList) {
            // 通过习题id查询关联的知识点，并把知识点id放到数组
            List<Knowledge> knowledgeList = knowledgeMapper.getKnowledgeByExerciseId(exercise.getExerciseId());
            String[] knowledgeNames = new String[knowledgeList.size()];
            if (knowledgeList != null && !knowledgeList.isEmpty()) {
                for (int i = 0; i < knowledgeList.size(); i++) {
                    knowledgeNames[i] = knowledgeList.get(i).getName();
                }
            }
            exercise.setKnowledgeNames(knowledgeNames);
        }
        return exerciseList;

    }

    @Override
    public ExerciseDisplay getExerciseInfo(HttpServletRequest request, int sclassId, int courseId, int exerciseId) {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //查询习题详情（题目信息，知识点，选项等信息）
        ExerciseDisplay exercise = exerciseMapper.getExerciseInfo(loginUser.getUserId(), sclassId, courseId, exerciseId);
        //判空
        BusinessResponseEnum.UNEXERCISE.assertNotNull(exercise);
        //设置知识点名称
        if (exercise.getKnowledge() != null && !exercise.getKnowledge().isEmpty()) {
            exercise.setKnowledgeNames(exercise.getKnowledge().stream().map(Knowledge::getName).toArray(String[]::new));
        }
        return exercise;
    }

    @Override
    public List<Exercise> getExerciseListByCourseId(int courseId) {
        List<Exercise> exerciseList = exerciseMapper.getExerciseByCourseId(courseId, 1, 0);
        return exerciseList;
    }

    @Override
    public Exercise getExerciseById(int exerciseId) {
        Exercise ex = exerciseMapper.getExerciseById(exerciseId);
        // 通过习题id查询关联的知识点，并把知识点id放到数组
        List<Knowledge> knowledgeList = knowledgeMapper.getKnowledgeByExerciseId(exerciseId);
        if (knowledgeList.size() > 0) {
            int[] knowledgeIds = new int[knowledgeList.size()];
            String[] knowledgeNames = new String[knowledgeList.size()];
            for (int i = 0; i < knowledgeList.size(); i++) {
                knowledgeIds[i] = knowledgeList.get(i).getKnowledgeId();
                knowledgeNames[i] = knowledgeList.get(i).getName();
            }
            ex.setKnowledgeIds(knowledgeIds);
            ex.setKnowledgeNames(knowledgeNames);
        }
        return ex;
    }

    @Override
    public String exportExerciseList(HttpServletRequest request, ExercisePage exercisePage) throws Exception {
        // 获取用户信息
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // 导出文件名
        String fileName = "exercise_export_" + loginUser.getCode() + "_" + TimeUtil.getDateTimeStr() + ".xls";
        // 表头字段名
        String[] columnNameArray = new String[]{"习题ID", "场景名称", "习题名称", "习题描述", "正确答案"};
        List<Map<Integer, Object>> mapList = new ArrayList<Map<Integer, Object>>();
        List<Exercise> exerciseList = exerciseMapper.getExerciseList(exercisePage);
        // 转换为map的list，方便写入文件时每个字段的循环以及公共方法参数一致
        for (int i = 0; i < exerciseList.size(); i++) {
            Exercise exercise = exerciseList.get(i);
            Map<Integer, Object> dataMap = new HashMap<Integer, Object>();
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadExerciseListFile(HttpServletRequest request, MultipartFile file) {
        FileOutputStream fos = null;
        try {
            // 获取用户信息
            UserInfo loginUser = Authentication.getCurrentUser(request);
            String fileName = file.getOriginalFilename();
            String[] fileNameArray = fileName.split("\\.");
            String newFileName = "exercise_import_" + loginUser.getUserId() + "_" + loginUser.getCode() + "." + fileNameArray[fileNameArray.length - 1];
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
            logger.error(e.getMessage(), e);
            //return ResultTO.FAILURE(e.getMessage());
            throw new APIException(e.getMessage());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importExercise(HttpServletRequest request, ImportExerciseTO importExerciseTO) {
        Workbook workbook = null;
        try {
            UserInfo loginUser = Authentication.getCurrentUser(request);
            for (String filePath : importExerciseTO.getFilePathList()) {
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
                            Exercise exercise = new Exercise();
                            exercise.setExerciseName(row.getCell(2).toString().trim());
                            exercise.setExerciseDesc(row.getCell(3).toString().trim());
                            exercise.setAnswer(row.getCell(4).toString().trim());
                            exercise.setCourseId(importExerciseTO.getCourseId());
                            exercise.setSceneId(-1);
                            exercise.setCreator(loginUser.getUserId());
                            exercise.setCreateTime(TimeUtil.getDateTime());
                            // 添加习题
                            exerciseMapper.addExercise(exercise);
                        }
                    } catch (Exception e) {
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
    @Transactional(rollbackFor = Exception.class)
    public Integer batchDeleteExercise(HttpServletRequest request, int[] exerciseIds) {
        // 删除习题
        return exerciseMapper.batchDeleteExercise(exerciseIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchBuildScene(int sceneId, int[] exerciseIds) {
        // 删除习题
        return exerciseMapper.batchBuildScene(sceneId, exerciseIds);
    }

}
