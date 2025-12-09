package com.example.FitTracker.controller;

import com.example.FitTracker.dto.request.workout.AddWorkoutSetRequest;
import com.example.FitTracker.dto.request.workout.CreateWorkoutSessionRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.workout.WorkoutSessionResponse;
import com.example.FitTracker.service.WorkoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
public class WorkoutController {
    
    private final WorkoutService workoutService;
    
    // 임시로 userId=1 고정 (JWT 구현 후 토큰에서 추출)
    private Long getCurrentUserId() {
        return 1L;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> createWorkoutSession(
            @Valid @RequestBody CreateWorkoutSessionRequest request) {
        WorkoutSessionResponse response = workoutService.createWorkoutSession(getCurrentUserId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("운동 세션 생성 완료", response));
    }
    
    @PostMapping("/{sessionId}/sets")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> addWorkoutSet(
            @PathVariable Long sessionId,
            @Valid @RequestBody AddWorkoutSetRequest request) {
        WorkoutSessionResponse response = workoutService.addWorkoutSet(getCurrentUserId(), sessionId, request);
        return ResponseEntity.ok(ApiResponse.success("세트 추가 완료", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkoutSessionResponse>>> getWorkouts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<WorkoutSessionResponse> workouts;
        
        if (startDate != null && endDate != null) {
            workouts = workoutService.getWorkoutsByDateRange(getCurrentUserId(), startDate, endDate);
        } else {
            workouts = workoutService.getUserWorkouts(getCurrentUserId());
        }
        
        return ResponseEntity.ok(ApiResponse.success(workouts));
    }
    
    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> getWorkoutSession(@PathVariable Long sessionId) {
        WorkoutSessionResponse session = workoutService.getWorkoutSession(getCurrentUserId(), sessionId);
        return ResponseEntity.ok(ApiResponse.success(session));
    }
    
    @DeleteMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutSession(@PathVariable Long sessionId) {
        workoutService.deleteWorkoutSession(getCurrentUserId(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("운동 세션 삭제 완료", null));
    }
}