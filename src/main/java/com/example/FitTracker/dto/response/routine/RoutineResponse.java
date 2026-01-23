package com.example.FitTracker.dto.response.routine;

import com.example.FitTracker.domain.Routine;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class RoutineResponse {
    
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<RoutineExerciseResponse> exercises;
    
    public static RoutineResponse from(Routine routine) {
        return RoutineResponse.builder()
                .id(routine.getId())
                .name(routine.getName())
                .description(routine.getDescription())
                .createdAt(routine.getCreatedAt())
                .exercises(routine.getRoutineExercises().stream()
                        .map(RoutineExerciseResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}