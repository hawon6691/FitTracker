package com.example.FitTracker.repository;

import com.example.FitTracker.domain.RoutineExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoutineExerciseRepository extends JpaRepository<RoutineExercise, Long> {
    
    // 특정 루틴의 모든 운동 종목 조회
    List<RoutineExercise> findByRoutineId(Long routineId);
    
    // 특정 루틴의 운동 종목을 순서대로 조회
    List<RoutineExercise> findByRoutineIdOrderByOrderIndexAsc(Long routineId);
    
    // 특정 루틴의 운동 종목 개수
    long countByRoutineId(Long routineId);
}