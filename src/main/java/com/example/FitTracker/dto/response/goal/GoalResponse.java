package com.example.FitTracker.dto.response.goal;

import com.example.FitTracker.domain.Goal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
@Builder
public class GoalResponse {

    private Long id;
    private String title;
    private String description;
    private String type;
    private Integer targetValue;
    private Integer currentValue;
    private LocalDate targetDate;
    private String status;
    private Integer progressPercentage;

    public static GoalResponse from(Goal goal) {
        int progress = 0;
        if (goal.getTargetValue() != null && goal.getTargetValue() > 0) {
            progress = (int) ((goal.getCurrentValue() * 100.0) / goal.getTargetValue());
        }

        return GoalResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .description(goal.getDescription())
                .type(goal.getType().name())
                .targetValue(goal.getTargetValue())
                .currentValue(goal.getCurrentValue())
                .targetDate(goal.getTargetDate())
                .status(goal.getStatus().name())
                .progressPercentage(progress)
                .build();
    }
}
