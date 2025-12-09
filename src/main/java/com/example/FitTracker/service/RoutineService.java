package com.example.FitTracker.service;

import com.example.FitTracker.domain.ExerciseType;
import com.example.FitTracker.domain.Routine;
import com.example.FitTracker.domain.RoutineExercise;
import com.example.FitTracker.domain.User;
import com.example.FitTracker.dto.request.routine.CreateRoutineRequest;
import com.example.FitTracker.dto.request.routine.RoutineExerciseRequest;
import com.example.FitTracker.dto.request.routine.UpdateRoutineRequest;
import com.example.FitTracker.dto.response.routine.RoutineResponse;
import com.example.FitTracker.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoutineService {
    
    private final RoutineRepository routineRepository;
    private final UserService userService;
    private final ExerciseTypeService exerciseTypeService;
    
    @Transactional
    public RoutineResponse createRoutine(Long userId, CreateRoutineRequest request) {
        User user = userService.findById(userId);
        
        // 루틴 생성
        Routine routine = Routine.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .build();
        
        // 운동 종목 추가
        int orderIndex = 0;
        for (RoutineExerciseRequest exerciseRequest : request.getExercises()) {
            ExerciseType exerciseType = exerciseTypeService.findById(exerciseRequest.getExerciseTypeId());
            
            RoutineExercise routineExercise = RoutineExercise.builder()
                    .routine(routine)
                    .exerciseType(exerciseType)
                    .targetSets(exerciseRequest.getTargetSets())
                    .targetReps(exerciseRequest.getTargetReps())
                    .targetWeight(exerciseRequest.getTargetWeight())
                    .orderIndex(orderIndex++)
                    .build();
            
            routine.getRoutineExercises().add(routineExercise);
        }
        
        Routine savedRoutine = routineRepository.save(routine);
        return RoutineResponse.from(savedRoutine);
    }
    
    public List<RoutineResponse> getUserRoutines(Long userId) {
        return routineRepository.findByUserId(userId).stream()
                .map(RoutineResponse::from)
                .collect(Collectors.toList());
    }
    
    public RoutineResponse getRoutineById(Long userId, Long routineId) {
        Routine routine = routineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다: " + routineId));
        return RoutineResponse.from(routine);
    }
    
    @Transactional
    public RoutineResponse updateRoutine(Long userId, Long routineId, UpdateRoutineRequest request) {
        Routine routine = routineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다: " + routineId));
        
        // 루틴 정보 수정
        routine.updateInfo(request.getName(), request.getDescription());
        
        // 기존 운동 종목 삭제
        routine.getRoutineExercises().clear();
        
        // 새로운 운동 종목 추가
        if (request.getExercises() != null) {
            int orderIndex = 0;
            for (RoutineExerciseRequest exerciseRequest : request.getExercises()) {
                ExerciseType exerciseType = exerciseTypeService.findById(exerciseRequest.getExerciseTypeId());
                
                RoutineExercise routineExercise = RoutineExercise.builder()
                        .routine(routine)
                        .exerciseType(exerciseType)
                        .targetSets(exerciseRequest.getTargetSets())
                        .targetReps(exerciseRequest.getTargetReps())
                        .targetWeight(exerciseRequest.getTargetWeight())
                        .orderIndex(orderIndex++)
                        .build();
                
                routine.getRoutineExercises().add(routineExercise);
            }
        }
        
        return RoutineResponse.from(routine);
    }
    
    @Transactional
    public void deleteRoutine(Long userId, Long routineId) {
        Routine routine = routineRepository.findByIdAndUserId(routineId, userId)
                .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다: " + routineId));
        routineRepository.delete(routine);
    }
}