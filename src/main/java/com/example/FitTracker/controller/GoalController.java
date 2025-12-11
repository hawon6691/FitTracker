package com.example.FitTracker.controller;

import com.example.FitTracker.dto.request.goal.CreateGoalRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.goal.GoalResponse;
import com.example.FitTracker.security.SecurityUtil;
import com.example.FitTracker.service.GoalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Tag(name = "목표", description = "운동 목표 관리 API")
public class GoalController {
    
    private final GoalService goalService;
    private final SecurityUtil securityUtil;
    
    @PostMapping
    @Operation(summary = "목표 생성")
    public ResponseEntity<ApiResponse<GoalResponse>> createGoal(
            @Valid @RequestBody CreateGoalRequest request) {
        GoalResponse response = goalService.createGoal(
                securityUtil.getCurrentUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("목표 생성 완료", response));
    }
    
    @GetMapping
    @Operation(summary = "내 목표 목록 조회")
    public ResponseEntity<ApiResponse<List<GoalResponse>>> getMyGoals() {
        List<GoalResponse> goals = goalService.getUserGoals(
                securityUtil.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(goals));
    }
    
    @PutMapping("/{id}/progress")
    @Operation(summary = "목표 진행도 업데이트")
    public ResponseEntity<ApiResponse<GoalResponse>> updateProgress(
            @PathVariable Long id,
            @RequestParam Integer currentValue) {
        GoalResponse response = goalService.updateProgress(
                securityUtil.getCurrentUserId(), id, currentValue);
        return ResponseEntity.ok(ApiResponse.success("진행도 업데이트 완료", response));
    }
}