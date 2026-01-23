package com.example.FitTracker.repository;

import com.example.FitTracker.domain.WorkoutSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutSetRepository extends JpaRepository<WorkoutSet, Long> {
    
    // 특정 세션의 모든 세트 조회
    List<WorkoutSet> findByWorkoutSessionId(Long sessionId);
    
    // 특정 세션의 특정 운동 종목 세트 조회
    List<WorkoutSet> findByWorkoutSessionIdAndExerciseTypeId(
        Long sessionId, 
        Long exerciseTypeId
    );
    
    // 모든 운동 종목의 기록 조회 (개인 기록 추적용)
    @Query("SELECT ws FROM WorkoutSet ws " +
           "JOIN FETCH ws.workoutSession session " +
           "JOIN FETCH ws.exerciseType et " +
           "WHERE session.user.id = :userId " +
           "AND ws.weight IS NOT NULL " +
           "ORDER BY ws.weight DESC, ws.reps DESC")
    List<WorkoutSet> findAllPersonalRecords(@Param("userId") Long userId);
    
    // 특정 운동 종목의 최근 기록 조회 (개인 기록 추적용)
    @Query("SELECT ws FROM WorkoutSet ws " +
           "JOIN FETCH ws.workoutSession session " +
           "WHERE session.user.id = :userId " +
           "AND ws.exerciseType.id = :exerciseTypeId " +
           "AND ws.weight IS NOT NULL " +
           "ORDER BY ws.weight DESC, ws.reps DESC")
    List<WorkoutSet> findPersonalRecords(@Param("userId") Long userId, 
                                        @Param("exerciseTypeId") Long exerciseTypeId);
    
    // 신체 부위별 운동량 통계
    @Query("SELECT et.bodyPart, COUNT(ws) as setCount " +
           "FROM WorkoutSet ws " +
           "JOIN ws.exerciseType et " +
           "JOIN ws.workoutSession session " +
           "WHERE session.user.id = :userId " +
           "AND session.workoutDate BETWEEN :startDate AND :endDate " +
           "GROUP BY et.bodyPart")
    List<Object[]> findBodyPartStats(@Param("userId") Long userId, 
                                     @Param("startDate") LocalDate startDate, 
                                     @Param("endDate") LocalDate endDate);
}
