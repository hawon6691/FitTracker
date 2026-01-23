package com.example.FitTracker.service;

import com.example.FitTracker.domain.Goal;
import com.example.FitTracker.domain.GoalStatus;
import com.example.FitTracker.domain.GoalType;
import com.example.FitTracker.domain.User;
import com.example.FitTracker.dto.request.goal.CreateGoalRequest;
import com.example.FitTracker.dto.response.goal.GoalResponse;
import com.example.FitTracker.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoalService {

    private final GoalRepository goalRepository;
    private final UserService userService;

    @Transactional
    public GoalResponse createGoal(Long userId, CreateGoalRequest request) {
        User user = userService.findById(userId);

        Goal goal = Goal.builder()
                .user(user)
                .title(request.getTitle())
                .description(request.getDescription())
                .type(GoalType.valueOf(request.getType()))
                .targetValue(request.getTargetValue())
                .currentValue(0)
                .targetDate(request.getTargetDate())
                .status(GoalStatus.IN_PROGRESS)
                .build();

        Goal savedGoal = goalRepository.save(goal);
        return GoalResponse.from(savedGoal);
    }

    public List<GoalResponse> getUserGoals(Long userId) {
        return goalRepository.findByUserIdOrderByIdDesc(userId).stream()
                .map(GoalResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public GoalResponse updateProgress(Long userId, Long goalId, Integer currentValue) {
        Goal goal = goalRepository.findByIdAndUserId(goalId, userId)
                .orElseThrow(() -> new IllegalArgumentException("목표를 찾을 수 없습니다: " + goalId));

        goal.updateProgress(currentValue);

        return GoalResponse.from(goal);
    }
}
