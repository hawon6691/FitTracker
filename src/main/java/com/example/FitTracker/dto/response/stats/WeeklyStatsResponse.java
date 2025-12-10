package com.example.FitTracker.dto.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklyStatsResponse {
    
    private LocalDate startDate;
    private LocalDate endDate;
    private Long totalWorkouts;
    private Long totalSets;
    private Integer totalMinutes;
    private Double avgWorkoutsPerDay;
}
