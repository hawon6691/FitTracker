package com.example.FitTracker.dto.request.routine;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoutineExerciseRequest {
    
    @NotNull(message = "운동 종목 ID는 필수입니다")
    private Long exerciseTypeId;
    
    @NotNull(message = "목표 세트 수는 필수입니다")
    @Min(value = 1, message = "세트 수는 최소 1 이상이어야 합니다")
    private Integer targetSets;
    
    @NotNull(message = "목표 반복 횟수는 필수입니다")
    @Min(value = 1, message = "반복 횟수는 최소 1 이상이어야 합니다")
    private Integer targetReps;
    
    @Min(value = 0, message = "무게는 0 이상이어야 합니다")
    private BigDecimal targetWeight;
}