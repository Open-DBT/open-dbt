package com.highgo.opendbt.api;

import com.highgo.opendbt.common.bean.ResultTO;
import com.highgo.opendbt.common.utils.ValidationUtil;
import com.highgo.opendbt.exercise.domain.model.PublishExercise;
import com.highgo.opendbt.progress.model.*;
import com.highgo.opendbt.progress.service.ProgressService;
import com.highgo.opendbt.sclass.domain.entity.Sclass;
import com.highgo.opendbt.score.domain.model.Score;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Description: 练习相关接口类 暂时不用
 * @Title: ProgressApi
 * @Package com.highgo.opendbt.api
 * @Author: highgo
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/8/25 11:30
 */
@Api(tags = "练习相关接口")
@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/progress")
public class ProgressApi {

    Logger logger = LoggerFactory.getLogger(getClass());

    private final ProgressService progressService;


    /**
     * 学生端习题列表及习题进度
     *
     * @param classId  班级id
     * @param courseId 课程id
     * @return Sclass
     */
    @ApiOperation(value = "学生端习题列表及习题进度")
    @GetMapping("/getCourseProgressByStu/{classId}/{courseId}")
    public Sclass getCourseProgressByStu(HttpServletRequest request, @PathVariable("classId") int classId, @PathVariable("courseId") int courseId) {
        logger.info("Enter, classId = {}, courseId = {}", classId, courseId);
        return progressService.getCourseProgressByStu(request, classId, courseId);
    }

    /**
     * 根据班级课程，查询知识点习题数量和学习进度
     *
     * @param classId  班级id
     * @param courseId 课程id
     * @param number   需要查询的个数，0是查询全部
     * @return List<StuKnowledgeExerciseInfo>
     */
    @ApiOperation(value = "根据班级课程，查询知识点习题数量和学习进度")
    @GetMapping("/getStuKnowledgeExerciseInfo/{classId}/{courseId}/{number}")
    public List<StuKnowledgeExerciseInfo> getStuKnowledgeExerciseInfo(HttpServletRequest request, @PathVariable("classId") int classId, @PathVariable("courseId") int courseId, @PathVariable("number") int number) {
        logger.info("Enter, classId = {}, courseId = {}, number = {}", classId, courseId, number);
        return progressService.getStuKnowledgeExerciseInfo(request, classId, courseId, number);
    }

    /**
     * 根据课程，查询知识点习题数量，用于知识树显示习题数量
     *
     * @param courseId 课程id
     * @return List<KnowledgeExerciseCount>
     */
    @ApiOperation(value = "根据课程，查询知识点习题数量")
    @GetMapping("/getKnowExerciseCountByCourseId/{courseId}")
    public ResultTO<List<KnowledgeExerciseCount>> getKnowExerciseCountByCourseId(@PathVariable("courseId") int courseId) {
        logger.info("Enter, courseId = {}", courseId);
        return progressService.getKnowExerciseCountByCourseId(courseId);
    }

    /**
     * 查询当前学生某课程每个知识点的进度，用于<学生/学习进度>显示习题完成情况，知识点反查询习题
     *
     * @param courseId 课程id
     * @return List<KnowledgeExerciseCount>
     */
    @ApiOperation(value = "询当前学生某课程每个知识点的进度，")
    @GetMapping("/getStuCourseKnowledgeItemProgress/{courseId}")
    public ResultTO<List<KnowledgeExerciseCount>> getStuCourseKnowledgeItemProgress(HttpServletRequest request, @PathVariable("courseId") int courseId) {
        logger.info("Enter, courseId = {}", courseId);
        return progressService.getStuCourseKnowledgeItemProgress(request, courseId);
    }

    /**
     * 班级统计--tab1 正确率 习题列表 | 答对人数、答题人数、全班人数
     *
     * @param classId 班级id
     * @return List<SclassCorrect>
     */
    @ApiOperation(value = "班级统计--tab1")
    @GetMapping("/getSclassCorrect/{classId}")
    public ResultTO<List<SclassCorrect>> getSclassCorrect(HttpServletRequest request, @PathVariable("classId") int classId) {
        logger.info("Enter, classId = {}", classId);
        return progressService.getSclassCorrect(request, classId);
    }

    /**
     * 班级统计--tab2 正确率 习题列表 | 答对人数、答题人数、全班人数
     *
     * @param request request
     * @param classId 班级id
     * @return List<SclassCoverage>
     */
    @ApiOperation(value = "班级统计--tab2")
    @GetMapping("/getSclassCoverage/{classId}/{isFuzzyQuery}/{searchValue}")
    public ResultTO<List<SclassCoverage>> getSclassCoverage(HttpServletRequest request, @PathVariable("classId") int classId, @PathVariable("isFuzzyQuery") int isFuzzyQuery, @PathVariable("searchValue") String searchValue) {
        logger.info("Enter, classId = {}, isFuzzyQuery = {}, searchValue = {}", classId, isFuzzyQuery, searchValue);
        return progressService.getSclassCoverage(request, classId, isFuzzyQuery, searchValue);
    }

    /**
     * 学生统计--tab1 正确率 习题列表 | 答对次数、答题次数 | 答对人数、答题人数、全班人数
     *
     * @param classId 班级id
     * @param userId  用户id
     * @return List<StudentCorrect>
     */
    @ApiOperation(value = "学生统计--tab1")
    @GetMapping("/getStudentCorrect/{classId}/{userId}")
    public ResultTO<List<StudentCorrect>> getStudentCorrect(HttpServletRequest request, @PathVariable("classId") int classId, @PathVariable("userId") int userId) {
        return progressService.getStudentCorrect(request, classId, userId);
    }

    /**
     * 学生统计--tab2 覆盖率 当前学生 | 答对题数量、答过题数量、总题目数 | 所有学生答对题目平均值(答对人题数/总学生) | 所有学生答过题目平均值(做过人题数/总学生)
     *
     * @param classId 班级id
     * @param userId  用户id
     * @return List<StudentCoverage>
     */
    @ApiOperation(value = "学生统计--tab2")
    @GetMapping("/getStudentCoverage/{classId}/{userId}")
    public ResultTO<List<StudentCoverage>> getStudentCoverage(HttpServletRequest request, @PathVariable("classId") int classId, @PathVariable("userId") int userId) {
        return progressService.getStudentCoverage(request, classId, userId);
    }

    /**
     * 获取学生答题情况
     *
     * @param classId 班级id
     * @param userId  用户id
     * @return List<Score>
     */
    @ApiOperation(value = "获取学生答题情况")
    @GetMapping("/getStuAnswerSituation/{classId}/{userId}")
    public ResultTO<List<Score>> getStuAnswerSituation(@PathVariable("classId") int classId, @PathVariable("userId") int userId) {
        return progressService.getStuAnswerSituation(classId, userId);
    }

    /**
     * 根据成绩id获取学生答题成绩记录
     *
     * @param scoreId 成绩id
     * @return Score
     */
    @ApiOperation(value = "根据成绩id获取学生答题成绩记录")
    @GetMapping("/getStuScoreById/{scoreId}")
    public ResultTO<Score> getStuScoreById(@PathVariable("scoreId") int scoreId) {
        return progressService.getStuScoreById(scoreId);
    }

    /**
     * 导出学生习题做题情况
     *
     * @param request      request
     * @param classId      班级id
     * @param type         类型 type为1是以习题为主统计导出，type为2是以学生为主统计导出
     * @param isFuzzyQuery 是否是模糊查询
     * @param searchValue  模糊查询字符串
     * @return String
     */
    @ApiOperation(value = "导出学生习题做题情况")
    @GetMapping("/exportStatisticsInfo/{classId}/{type}/{isFuzzyQuery}/{searchValue}")
    public ResultTO<String> exportStatisticsInfo(HttpServletRequest request, @PathVariable("classId") int classId, @PathVariable("type") int type, @PathVariable("isFuzzyQuery") int isFuzzyQuery, @PathVariable("searchValue") String searchValue) {
        logger.info("Enter, classId = {}, type = {}, searchValue = {}", classId, type, searchValue);
        return progressService.exportStatisticsInfo(request, classId, type, isFuzzyQuery, searchValue);
    }

    /**
     * @description: 学生练习非sql题提交
     * @author:
     * @date: 2023/2/16 15:42
     * @param: [request, score 题目id，题目答案,题目类型 等信息]
     * @return: com.highgo.opendbt.score.domain.model.SubmitResult
     **/
    @ApiOperation(value = "学生练习非sql题提交")
    @PostMapping("/noSqlSubmitAnswer")
    public boolean noSqlSubmitAnswer(HttpServletRequest request, @RequestBody Score score) {
        logger.debug("Enter, score = " + score.toString());
        return progressService.noSqlSubmitAnswer(request, score);
    }

    @ApiOperation(value = "练习重置")
    @GetMapping("/exerciseReset/{courseId}")
    public boolean exerciseReset(HttpServletRequest request, @PathVariable Integer courseId) {
        logger.debug("Enter,courseId={}", courseId);
        return progressService.exerciseReset(request, courseId);
    }

    @ApiOperation(value = "题库习题设置为练习/取消练习，显示答案/不显示答案")
    @PostMapping("/publishExercise")
    public Integer publishExercise(HttpServletRequest request, @RequestBody @Valid PublishExercise param, BindingResult result) {
        //校验
        ValidationUtil.Validation(result);
        logger.debug("Enter,param={}", param.toString());
        return progressService.publishExercise(request, param);
    }

}
