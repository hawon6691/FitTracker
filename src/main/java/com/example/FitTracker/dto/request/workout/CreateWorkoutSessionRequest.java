package com.example.FitTracker.dto.request.workout;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkoutSessionRequest {
    
    private Long routineId;  // nullable (루틴 없이도 운동 가능)
    
    @NotNull(message = "운동 날짜는 필수입니다")
    private LocalDate workoutDate;
    
    private Integer durationMinutes;
    
    private String notes;
}