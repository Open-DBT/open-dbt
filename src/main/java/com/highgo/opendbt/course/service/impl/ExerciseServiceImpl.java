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
        // ??????????????????????????????????????????????????????
        for (Exercise exercise : exerciseList) {
            // ????????????id??????????????????????????????????????????id????????????
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
        // ??????????????????
        PageHelper.startPage(exercisePage.getCurrent(), exercisePage.getPageSize());
        List<Exercise> exerciseList = exerciseMapper.getExercise(exercisePage);
        PageInfo<Exercise> exercisePageInfo = new PageInfo<Exercise>(exerciseList);
        // ????????????????????????????????????????????????????????????
        List<Exercise> exerciseListPageInfo = exercisePageInfo.getList();
        for (Exercise exercise : exerciseListPageInfo) {
            // ????????????id??????????????????????????????????????????id????????????
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
     * @description: ??????????????????
     * @author:
     * @date: 2023/1/6 9:57
     * @param: [request, exercise]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer updateExercise(HttpServletRequest request, Exercise exercise) {
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        Course course = courseMapper.getCourseByCourseId(exercise.getCourseId());
        if (null == course) {
            throw new APIException(Message.get("GetExerciseCourseFile"));
        }
        Integer updateExercise;
        if (exercise.getExerciseId() == -1) {
            exercise.setCreateTime(TimeUtil.getDateTime());
            exercise.setCreator(loginUser.getUserId());
            // ????????????
            updateExercise = exerciseMapper.addExercise(exercise);
            // ???????????????????????????????????????
            if (exercise.getKnowledgeIds().length > 0) {
                exerciseKnowledgeMapper.addExerciseKnowledgeArray(exercise.getCourseId(), exercise.getExerciseId(), exercise.getKnowledgeIds());
            }
        } else {
            exercise.setUpdateTime(TimeUtil.getDateTime());
            // ????????????
            updateExercise = exerciseMapper.updateExercise(exercise);
            // ???????????????????????????????????????
            exerciseKnowledgeMapper.deleteByExerciseId(exercise.getExerciseId());
            // ?????????????????????????????????????????????
            if (exercise.getKnowledgeIds().length > 0) {
                exerciseKnowledgeMapper.addExerciseKnowledgeArray(exercise.getCourseId(), exercise.getExerciseId(), exercise.getKnowledgeIds());
            }
        }
        return updateExercise;

    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2023/1/6 9:58
     * @param: [request, exerciseId]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer deleteExercise(HttpServletRequest request, int exerciseId) {
        // ????????????
        return exerciseMapper.deleteExercise(exerciseId);
    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2023/1/6 9:59
     * @param: [request, exerciseId]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copyExercise(HttpServletRequest request, int exerciseId) {
        // ?????????????????????????????????
        Exercise exercise = exerciseMapper.getExerciseById(exerciseId);
        if (null == exercise) {
            throw new APIException(Message.get("GetCopyExerciseFail"));
        }
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ???????????????????????????????????????
        List<ExerciseKnowledge> list = exerciseKnowledgeMapper.getExerciseKnowledgeByExerciseId(exerciseId);
        exercise.setCreateTime(TimeUtil.getDateTime());
        exercise.setCreator(loginUser.getUserId());
        // ????????????
        Integer addExercise = exerciseMapper.addExercise(exercise);
        // ????????????id???????????????id??????????????????????????????????????????
        if (list != null && !list.isEmpty()) {
            for (ExerciseKnowledge item : list) {
                item.setExerciseId(exercise.getExerciseId());
            }
            exerciseKnowledgeMapper.addExerciseKnowledgeList(list);
        }
        return addExercise;
    }

    /**
     * @description: ????????????
     * @author:
     * @date: 2023/1/6 10:01
     * @param: [request, exerciseId, courseId]
     * @return: java.lang.Integer
     **/
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer copyExerciseToMyCourse(HttpServletRequest request, int exerciseId, int courseId) {
        // ?????????????????????????????????
        Exercise exercise = exerciseMapper.getExerciseById(exerciseId);
        if (null == exercise) {
            throw new APIException(Message.get("GetCopyExerciseFail"));
        }
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //???????????????????????????
        exercise.setSceneId(-1);
        exercise.setCourseId(courseId);
        exercise.setCreateTime(TimeUtil.getDateTime());
        exercise.setCreator(loginUser.getUserId());
        // ????????????
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
            // ??????????????????
            UserInfo loginUser = Authentication.getCurrentUser(request);
            // ??????????????????????????????schema?????????
            runAnswerService.getSchemaConnection(loginUser, exercise.getSceneId(), 0, schemaConnection, 0);
            if (null != schemaConnection.getConnection()) {
                connection = schemaConnection.getConnection();
                statement = connection.createStatement();
                resultMap = new LinkedHashMap<String, Object>();
                // ??????????????????????????????????????????????????????????????????????????????
                if (exercise.getAnswer().toLowerCase().startsWith("select")) {
                    resultMap.put("isSelect", true);
                    List<String> columnList = new ArrayList<String>();
                    List<DataTypeAndImg> dataTypeAndImgList = new ArrayList<DataTypeAndImg>();
                    List<Map<Object, Object>> resultSetList = new ArrayList<Map<Object, Object>>();
                    resultSet = statement.executeQuery(exercise.getAnswer());
                    // ???ResultSet??????????????????list??????????????????
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
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        List<Exercise> exerciseList = exerciseMapper.getExerciseInfoList(loginUser.getUserId(), sclassId, courseId, knowledgeId);
        for (Exercise exercise : exerciseList) {
            // ????????????id??????????????????????????????????????????id????????????
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
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        //??????????????????????????????????????????????????????????????????
        ExerciseDisplay exercise = exerciseMapper.getExerciseInfo(loginUser.getUserId(), sclassId, courseId, exerciseId);
        //??????
        BusinessResponseEnum.UNEXERCISE.assertNotNull(exercise);
        //?????????????????????
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
        // ????????????id??????????????????????????????????????????id????????????
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
        // ??????????????????
        UserInfo loginUser = Authentication.getCurrentUser(request);
        // ???????????????
        String fileName = "exercise_export_" + loginUser.getCode() + "_" + TimeUtil.getDateTimeStr() + ".xls";
        // ???????????????
        String[] columnNameArray = new String[]{"??????ID", "????????????", "????????????", "????????????", "????????????"};
        List<Map<Integer, Object>> mapList = new ArrayList<Map<Integer, Object>>();
        List<Exercise> exerciseList = exerciseMapper.getExerciseList(exercisePage);
        // ?????????map???list???????????????????????????????????????????????????????????????????????????
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
        // ????????????????????????
        ExcelUtil.writeXLS(fileName, columnNameArray, mapList);
        return fileName;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadExerciseListFile(HttpServletRequest request, MultipartFile file) {
        FileOutputStream fos = null;
        try {
            // ??????????????????
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
                // ????????????????????????????????????excel??????
                ExcelUtil.checkFile(filePath);
                workbook = ExcelUtil.getWorbook(filePath);
                // ??????excel??????????????????sheet
                Sheet sheet = workbook.getSheetAt(0);
                // ????????????????????????
                int sheetLastRowIndex = sheet.getLastRowNum();
                logger.info("sheet sheetLastRowIndex = " + sheetLastRowIndex);
                // ?????????????????????????????????
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
                            // ????????????
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
        // ????????????
        return exerciseMapper.batchDeleteExercise(exerciseIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer batchBuildScene(int sceneId, int[] exerciseIds) {
        // ????????????
        return exerciseMapper.batchBuildScene(sceneId, exerciseIds);
    }

}
