package com.example.FitTracker.controller;

import com.example.FitTracker.dto.request.workout.AddWorkoutSetRequest;
import com.example.FitTracker.dto.request.workout.CreateWorkoutSessionRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.PageResponse;
import com.example.FitTracker.dto.response.workout.WorkoutSessionResponse;
import com.example.FitTracker.security.SecurityUtil;
import com.example.FitTracker.service.WorkoutService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Tag(name = "운동 기록", description = "운동 기록 관리 API")
public class WorkoutController {

    private final WorkoutService workoutService;
    private final SecurityUtil securityUtil;

    @PostMapping
    @Operation(summary = "운동 세션 생성", description = "새로운 운동 세션을 시작합니다")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> createWorkoutSession(
            @Valid @RequestBody CreateWorkoutSessionRequest request) {
        WorkoutSessionResponse response = workoutService.createWorkoutSession(securityUtil.getCurrentUserId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("운동 세션 생성 완료", response));
    }

    @PostMapping("/{sessionId}/sets")
    @Operation(summary = "세트 추가", description = "운동 세션에 세트를 추가합니다")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> addWorkoutSet(
            @PathVariable Long sessionId,
            @Valid @RequestBody AddWorkoutSetRequest request) {
        WorkoutSessionResponse response = workoutService.addWorkoutSet(securityUtil.getCurrentUserId(), sessionId,
                request);
        return ResponseEntity.ok(ApiResponse.success("세트 추가 완료", response));
    }

    @GetMapping
    @Operation(summary = "운동 기록 조회", description = "운동 기록을 조회합니다")
    public ResponseEntity<ApiResponse<List<WorkoutSessionResponse>>> getWorkouts(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        List<WorkoutSessionResponse> workouts;

        if (startDate != null && endDate != null) {
            workouts = workoutService.getWorkoutsByDateRange(securityUtil.getCurrentUserId(), startDate, endDate);
        } else {
            workouts = workoutService.getUserWorkouts(securityUtil.getCurrentUserId());
        }

        return ResponseEntity.ok(ApiResponse.success(workouts));
    }

    @GetMapping("/{sessionId}")
    @Operation(summary = "운동 세션 상세 조회", description = "특정 운동 세션의 상세 정보를 조회합니다")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> getWorkoutSession(@PathVariable Long sessionId) {
        WorkoutSessionResponse session = workoutService.getWorkoutSession(securityUtil.getCurrentUserId(), sessionId);
        return ResponseEntity.ok(ApiResponse.success(session));
    }

    @DeleteMapping("/{sessionId}")
    @Operation(summary = "운동 세션 삭제", description = "운동 세션을 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutSession(@PathVariable Long sessionId) {
        workoutService.deleteWorkoutSession(securityUtil.getCurrentUserId(), sessionId);
        return ResponseEntity.ok(ApiResponse.success("운동 세션 삭제 완료", null));
    }

    // TODO: 페이징 기능은 향후 구현 예정
    // @GetMapping("/paged")
    // @Operation(summary = "운동 기록 페이징 조회")
    // public ResponseEntity<ApiResponse<PageResponse<WorkoutSessionResponse>>> getWorkoutsPaged(
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "20") int size,
    //         @RequestParam(defaultValue = "workoutDate") String sortBy,
    //         @RequestParam(defaultValue = "DESC") String direction) {
    //
    //     Sort.Direction sortDirection = Sort.Direction.fromString(direction);
    //     Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
    //
    //     Page<WorkoutSessionResponse> workouts = workoutService.getUserWorkoutsPaged(
    //             securityUtil.getCurrentUserId(), pageable);
    //
    //     return ResponseEntity.ok(ApiResponse.success(PageResponse.of(workouts)));
    // }

    @PutMapping("/{sessionId}/sets/{setId}")
    @Operation(summary = "세트 수정", description = "운동 세트 정보를 수정합니다")
    public ResponseEntity<ApiResponse<WorkoutSessionResponse>> updateWorkoutSet(
            @PathVariable Long sessionId,
            @PathVariable Long setId,
            @Valid @RequestBody AddWorkoutSetRequest request) {
        WorkoutSessionResponse response = workoutService.updateWorkoutSet(
                securityUtil.getCurrentUserId(), sessionId, setId, request);
        return ResponseEntity.ok(ApiResponse.success("세트 수정 완료", response));
    }

    @DeleteMapping("/{sessionId}/sets/{setId}")
    @Operation(summary = "세트 삭제", description = "운동 세트를 삭제합니다")
    public ResponseEntity<ApiResponse<Void>> deleteWorkoutSet(
            @PathVariable Long sessionId,
            @PathVariable Long setId) {
        workoutService.deleteWorkoutSet(securityUtil.getCurrentUserId(), sessionId, setId);
        return ResponseEntity.ok(ApiResponse.success("세트 삭제 완료", null));
    }
}