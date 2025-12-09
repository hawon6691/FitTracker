package com.example.FitTracker.controller;

import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.exercise.ExerciseTypeResponse;
import com.example.FitTracker.service.ExerciseTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
public class ExerciseTypeController {
    
    private final ExerciseTypeService exerciseTypeService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<ExerciseTypeResponse>>> getAllExercises(
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