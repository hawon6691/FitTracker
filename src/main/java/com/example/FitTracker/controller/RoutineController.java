package com.example.FitTracker.controller;

import com.example.FitTracker.dto.request.routine.CreateRoutineRequest;
import com.example.FitTracker.dto.request.routine.UpdateRoutineRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.routine.RoutineResponse;
import com.example.FitTracker.service.RoutineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/routines")
@RequiredArgsConstructor
public class RoutineController {
    
    private final RoutineService routineService;
    
    // 임시로 userId=1 고정 (JWT 구현 후 토큰에서 추출)
    private Long getCurrentUserId() {
        return 1L;
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<RoutineResponse>> createRoutine(
            @Valid @RequestBody CreateRoutineRequest request) {
        RoutineResponse response = routineService.createRoutine(getCurrentUserId(), request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("루틴 생성 완료", response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<RoutineResponse>>> getUserRoutines() {
        List<RoutineResponse> routines = routineService.getUserRoutines(getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(routines));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RoutineResponse>> getRoutineById(@PathVariable Long id) {
        RoutineResponse routine = routineService.getRoutineById(getCurrentUserId(), id);
        return ResponseEntity.ok(ApiResponse.success(routine));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RoutineResponse>> updateRoutine(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRoutineRequest request) {
        RoutineResponse response = routineService.updateRoutine(getCurrentUserId(), id, request);
        return ResponseEntity.ok(ApiResponse.success("루틴 수정 완료", response));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRoutine(@PathVariable Long id) {
        routineService.deleteRoutine(getCurrentUserId(), id);
        return ResponseEntity.ok(ApiResponse.success("루틴 삭제 완료", null));
    }
}