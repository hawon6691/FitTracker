package com.example.FitTracker.dto.response.stats;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyPartStatsResponse {
    
    private String bodyPart;
    private Long totalSets;
    private Double percentage;
}
