package com.example.FitTracker.dto.request.workout;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddWorkoutSetRequest {
    
    @NotNull(message = "운동 종목 ID는 필수입니다")
    private Long exerciseTypeId;
    
    @NotNull(message = "세트 번호는 필수입니다")
    @Min(value = 1, message = "세트 번호는 최소 1 이상이어야 합니다")
    private Integer setNumber;
    
    @NotNull(message = "반복 횟수는 필수입니다")
    @Min(value = 0, message = "반복 횟수는 0 이상이어야 합니다")
    private Integer reps;
    
    @Min(value = 0, message = "무게는 0 이상이어야 합니다")
    private BigDecimal weight;
    
    @NotNull(message = "완료 여부는 필수입니다")
    private Boolean completed;
}