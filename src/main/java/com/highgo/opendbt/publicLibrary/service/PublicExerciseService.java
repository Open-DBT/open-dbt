package com.highgo.opendbt.publicLibrary.service;

import com.github.pagehelper.PageInfo;
import com.highgo.opendbt.publicLibrary.model.ImportPublicExerciseTO;
import com.highgo.opendbt.publicLibrary.model.PublicExercise;
import com.highgo.opendbt.publicLibrary.model.PublicExercisePage;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface PublicExerciseService {
    //分页获取公共习题
    PageInfo<PublicExercise> getPublicExerciseList(PublicExercisePage publicExercisePage);

    //获取习题信息
    PublicExercise getPublicExerciseInfo(int exerciseId);

    //修改和新增习题
    Integer updateExercise(HttpServletRequest request, PublicExercise publicExercise);

    //删除习题
    Integer deleteExercise(int exerciseId);

    //测试运行习题正确答案
    Map<String, Object> testRunAnswer(HttpServletRequest request, PublicExercise publicExercise);

    //导入公共习题
    String importPublicExercise(HttpServletRequest request, ImportPublicExerciseTO importPublicExerciseTO);

    //导出公共习题
    String exportPublicExercise(HttpServletRequest request);

}
