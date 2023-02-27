package com.highgo.opendbt.exercise.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.highgo.opendbt.exercise.domain.entity.TExerciseInfo;
import com.highgo.opendbt.exercise.domain.model.TNewExerciseInfoExcel;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.highgo.opendbt.exercise.domain.entity.TExerciseInfo
 */
@Repository
public interface TExerciseInfoMapper extends BaseMapper<TExerciseInfo> {
 List<TExerciseInfo> getExerciseInfo(int exerciseId);
//导出查询
 List<TNewExerciseInfoExcel> getExerciseInfoExcel(int exerciseId);
}




