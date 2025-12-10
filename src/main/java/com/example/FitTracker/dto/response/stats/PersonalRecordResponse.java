package com.example.FitTracker.dto.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalRecordResponse {
    
    private Long exerciseTypeId;
    private String exerciseName;
    private String bodyPart;
    private BigDecimal maxWeight;
    private Integer repsAtMaxWeight;
    private LocalDate achievedDate;
    private BigDecimal oneRepMax;
}
