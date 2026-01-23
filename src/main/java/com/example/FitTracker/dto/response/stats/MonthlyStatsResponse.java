package com.example.FitTracker.dto.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyStatsResponse {
    
    private YearMonth yearMonth;
    private Long totalWorkouts;
    private Long totalSets;
    private Integer totalMinutes;
    private Double avgWorkoutsPerWeek;
    private Integer totalDaysWorkedOut;
}
