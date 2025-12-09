package com.example.FitTracker.service;

import com.example.FitTracker.domain.ExerciseType;
import com.example.FitTracker.dto.response.exercise.ExerciseTypeResponse;
import com.example.FitTracker.repository.ExerciseTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExerciseTypeService {
    
    private final ExerciseTypeRepository exerciseTypeRepository;
    
    public List<ExerciseTypeResponse> getAllExercises() {
        return exerciseTypeRepository.findAll().stream()
                .map(ExerciseTypeResponse::from)
                .collect(Collectors.toList());
    }
    
    public List<ExerciseTypeResponse> getExercisesByBodyPart(String bodyPart) {
        return exerciseTypeRepository.findByBodyPart(bodyPart).stream()
                .map(ExerciseTypeResponse::from)
                .collect(Collectors.toList());
    }
    
    public ExerciseTypeResponse getExerciseById(Long id) {
        ExerciseType exerciseType = exerciseTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("운동 종목을 찾을 수 없습니다: " + id));
        return ExerciseTypeResponse.from(exerciseType);
    }
    
    public ExerciseType findById(Long id) {
        return exerciseTypeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("운동 종목을 찾을 수 없습니다: " + id));
    }
}