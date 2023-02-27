package com.highgo.opendbt.student.mapper;

import com.highgo.opendbt.student.domain.model.StudentExamExercise;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentExamMapper {

    List<StudentExamExercise> getExamExerciseList(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("examId") int examId, @Param("examClassId") int examClassId, @Param("exerciseSource") int exerciseSource);

    StudentExamExercise getExamExerciseById(@Param("userId") int userId, @Param("sclassId") int sclassId, @Param("examId") int examId, @Param("examClassId") int examClassId, @Param("exerciseId") int exerciseId, @Param("exerciseSource") int exerciseSource);

}
