package com.example.FitTracker.dto.response.workout;

import com.example.FitTracker.domain.WorkoutSession;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@Builder
public class WorkoutSessionResponse {
    
    private Long id;
    private Long routineId;
    private String routineName;
    private LocalDate workoutDate;
    private Integer durationMinutes;
    private String notes;
    private LocalDateTime createdAt;
    private List<WorkoutSetResponse> sets;
    
    public static WorkoutSessionResponse from(WorkoutSession session) {
        return WorkoutSessionResponse.builder()
                .id(session.getId())
                .routineId(session.getRoutine() != null ? session.getRoutine().getId() : null)
                .routineName(session.getRoutine() != null ? session.getRoutine().getName() : null)
                .workoutDate(session.getWorkoutDate())
                .durationMinutes(session.getDurationMinutes())
                .notes(session.getNotes())
                .createdAt(session.getCreatedAt())
                .sets(session.getWorkoutSets().stream()
                        .map(WorkoutSetResponse::from)
                        .collect(Collectors.toList()))
                .build();
    }
}