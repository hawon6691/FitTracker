package com.example.FitTracker.dto.request.goal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateGoalRequest {

    @NotBlank(message = "목표 제목은 필수입니다")
    private String title;

    private String description;

    @NotNull(message = "목표 유형은 필수입니다")
    private String type;  // WEIGHT, REPS, FREQUENCY

    @NotNull(message = "목표값은 필수입니다")
    private Integer targetValue;

    private LocalDate targetDate;
}
