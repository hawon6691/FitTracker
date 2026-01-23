package com.example.FitTracker.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 목표 상태
 */
@Getter
@AllArgsConstructor
public enum GoalStatus {
    IN_PROGRESS("진행 중"),
    COMPLETED("완료"),
    FAILED("실패");

    private final String description;
}
