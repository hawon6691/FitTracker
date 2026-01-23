package com.example.FitTracker.dto.response.exercise;

import com.example.FitTracker.domain.ExerciseType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ExerciseTypeResponse {
    
    private Long id;
    private String name;
    private String bodyPart;
    private String description;
    
    public static ExerciseTypeResponse from(ExerciseType exerciseType) {
        return ExerciseTypeResponse.builder()
                .id(exerciseType.getId())
                .name(exerciseType.getName())
                .bodyPart(exerciseType.getBodyPart())
                .description(exerciseType.getDescription())
                .build();
    }
}