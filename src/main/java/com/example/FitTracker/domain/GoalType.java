package com.example.FitTracker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 목표 유형
 */
@Getter
@AllArgsConstructor
public enum GoalType {
    WEIGHT("무게 목표"),
    REPS("횟수 목표"),
    FREQUENCY("운동 빈도 목표");

    private final String description;
}
