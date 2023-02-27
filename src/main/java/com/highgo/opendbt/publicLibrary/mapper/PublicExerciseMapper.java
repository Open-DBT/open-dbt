package com.highgo.opendbt.publicLibrary.mapper;

import com.highgo.opendbt.publicLibrary.model.PublicExercise;
import com.highgo.opendbt.publicLibrary.model.PublicExercisePage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PublicExerciseMapper {

    List<PublicExercise> getPublicExerciseList(PublicExercisePage publicExercisePage);

    List<PublicExercise> getPublicExerciseDetail();

    PublicExercise getPublicExerciseInfo(@Param("exerciseId") int exerciseId);

    Integer addExercise(PublicExercise publicExercise);

    Integer updateExercise(PublicExercise publicExercise);

    Integer deleteExercise(@Param("exerciseId") int exerciseId);

}
