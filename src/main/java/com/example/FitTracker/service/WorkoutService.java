package com.example.FitTracker.service;

import com.example.FitTracker.domain.*;
import com.example.FitTracker.dto.request.workout.AddWorkoutSetRequest;
import com.example.FitTracker.dto.request.workout.CreateWorkoutSessionRequest;
import com.example.FitTracker.dto.response.workout.WorkoutSessionResponse;
import com.example.FitTracker.exception.ResourceNotFoundException;
import com.example.FitTracker.repository.RoutineRepository;
import com.example.FitTracker.repository.WorkoutSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkoutService {
    
    private final WorkoutSessionRepository workoutSessionRepository;
    private final RoutineRepository routineRepository;
    private final UserService userService;
    private final ExerciseTypeService exerciseTypeService;
    
    @Transactional
    public WorkoutSessionResponse createWorkoutSession(Long userId, CreateWorkoutSessionRequest request) {
        User user = userService.findById(userId);
        
        Routine routine = null;
        if (request.getRoutineId() != null) {
            routine = routineRepository.findByIdAndUserId(request.getRoutineId(), userId)
                    .orElseThrow(() -> new IllegalArgumentException("루틴을 찾을 수 없습니다: " + request.getRoutineId()));
        }
        
        WorkoutSession session = WorkoutSession.builder()
                .user(user)
                .routine(routine)
                .workoutDate(request.getWorkoutDate())
                .durationMinutes(request.getDurationMinutes())
                .notes(request.getNotes())
                .build();
        
        WorkoutSession savedSession = workoutSessionRepository.save(session);
        return WorkoutSessionResponse.from(savedSession);
    }
    
    @Transactional
    public WorkoutSessionResponse addWorkoutSet(Long userId, Long sessionId, AddWorkoutSetRequest request) {
        WorkoutSession session = workoutSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("운동 세션을 찾을 수 없습니다: " + sessionId));
        
        ExerciseType exerciseType = exerciseTypeService.findById(request.getExerciseTypeId());
        
        WorkoutSet workoutSet = WorkoutSet.builder()
                .workoutSession(session)
                .exerciseType(exerciseType)
                .setNumber(request.getSetNumber())
                .reps(request.getReps())
                .weight(request.getWeight())
                .completed(request.getCompleted())
                .build();
        
        session.getWorkoutSets().add(workoutSet);
        
        return WorkoutSessionResponse.from(session);
    }
    
    public List<WorkoutSessionResponse> getUserWorkouts(Long userId) {
        return workoutSessionRepository.findByUserIdOrderByWorkoutDateDesc(userId).stream()
                .map(WorkoutSessionResponse::from)
                .collect(Collectors.toList());
    }
    
    public List<WorkoutSessionResponse> getWorkoutsByDateRange(Long userId, LocalDate startDate, LocalDate endDate) {
        return workoutSessionRepository.findByUserIdAndWorkoutDateBetween(userId, startDate, endDate).stream()
                .map(WorkoutSessionResponse::from)
                .collect(Collectors.toList());
    }
    
    public WorkoutSessionResponse getWorkoutSession(Long userId, Long sessionId) {
        WorkoutSession session = workoutSessionRepository.findByIdWithSets(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("운동 세션을 찾을 수 없습니다: " + sessionId));
        return WorkoutSessionResponse.from(session);
    }
    
    @Transactional
    public void deleteWorkoutSession(Long userId, Long sessionId) {
        WorkoutSession session = workoutSessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new IllegalArgumentException("운동 세션을 찾을 수 없습니다: " + sessionId));
        workoutSessionRepository.delete(session);
    }

    @Transactional
public WorkoutSessionResponse updateWorkoutSet(Long userId, Long sessionId, 
                                                Long setId, AddWorkoutSetRequest request) {
    WorkoutSession session = workoutSessionRepository.findByIdAndUserId(sessionId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("운동 세션", "id", sessionId));
    
    WorkoutSet workoutSet = session.getWorkoutSets().stream()
            .filter(set -> set.getId().equals(setId))
            .findFirst()
            .orElseThrow(() -> new ResourceNotFoundException("운동 세트", "id", setId));
    
    workoutSet.updateSet(request.getReps(), request.getWeight(), request.getCompleted());
    
    return WorkoutSessionResponse.from(session);
}

@Transactional
public void deleteWorkoutSet(Long userId, Long sessionId, Long setId) {
    WorkoutSession session = workoutSessionRepository.findByIdAndUserId(sessionId, userId)
            .orElseThrow(() -> new ResourceNotFoundException("운동 세션", "id", sessionId));
    
    session.getWorkoutSets().removeIf(set -> set.getId().equals(setId));
}
}