package com.example.FitTracker.dto.response.routine;

import com.example.FitTracker.domain.RoutineExercise;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class RoutineExerciseResponse {
    
    private Long id;
    private Long exerciseTypeId;
    private String exerciseName;
    private String bodyPart;
    private Integer targetSets;
    private Integer targetReps;
    private BigDecimal targetWeight;
    private Integer orderIndex;
    
    public static RoutineExerciseResponse from(RoutineExercise routineExercise) {
        return RoutineExerciseResponse.builder()
                .id(routineExercise.getId())
                .exerciseTypeId(routineExercise.getExerciseType().getId())
                .exerciseName(routineExercise.getExerciseType().getName())
                .bodyPart(routineExercise.getExerciseType().getBodyPart())
                .targetSets(routineExercise.getTargetSets())
                .targetReps(routineExercise.getTargetReps())
                .targetWeight(routineExercise.getTargetWeight())
                .orderIndex(routineExercise.getOrderIndex())
                .build();
    }
}