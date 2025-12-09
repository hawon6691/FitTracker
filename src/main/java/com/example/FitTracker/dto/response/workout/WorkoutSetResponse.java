package com.example.FitTracker.dto.response.workout;

import com.example.FitTracker.domain.WorkoutSet;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@Builder
public class WorkoutSetResponse {
    
    private Long id;
    private Long exerciseTypeId;
    private String exerciseName;
    private String bodyPart;
    private Integer setNumber;
    private Integer reps;
    private BigDecimal weight;
    private Boolean completed;
    
    public static WorkoutSetResponse from(WorkoutSet workoutSet) {
        return WorkoutSetResponse.builder()
                .id(workoutSet.getId())
                .exerciseTypeId(workoutSet.getExerciseType().getId())
                .exerciseName(workoutSet.getExerciseType().getName())
                .bodyPart(workoutSet.getExerciseType().getBodyPart())
                .setNumber(workoutSet.getSetNumber())
                .reps(workoutSet.getReps())
                .weight(workoutSet.getWeight())
                .completed(workoutSet.getCompleted())
                .build();
    }
}