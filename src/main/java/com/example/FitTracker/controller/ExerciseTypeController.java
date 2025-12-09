package com.example.FitTracker.controller;

import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.exercise.ExerciseTypeResponse;
import com.example.FitTracker.service.ExerciseTypeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Tag(name = "운동 종목", description = "운동 종목 조회 API")
public class ExerciseTypeController {
     private final ExerciseTypeService exerciseTypeService;
    
    @GetMapping
    @Operation(summary = "운동 종목 전체 조회", description = "모든 운동 종목 또는 신체 부위별 조회")  // 추가
    public ResponseEntity<ApiResponse<List<ExerciseTypeResponse>>> getAllExercises(
        @Parameter(description = "신체 부위 (가슴, 등, 다리, 어깨, 팔, 복근)")  // 추가
        @RequestParam(required = false) String bodyPart) {
        List<ExerciseTypeResponse> exercises;
        
        if (bodyPart != null && !bodyPart.isEmpty()) {
            exercises = exerciseTypeService.getExercisesByBodyPart(bodyPart);
        } else {
            exercises = exerciseTypeService.getAllExercises();
        }
        
        return ResponseEntity.ok(ApiResponse.success(exercises));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ExerciseTypeResponse>> getExerciseById(@PathVariable Long id) {
        ExerciseTypeResponse exercise = exerciseTypeService.getExerciseById(id);
        return ResponseEntity.ok(ApiResponse.success(exercise));
    }
}