package com.example.FitTracker.repository;

import com.example.FitTracker.domain.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {

       // 특정 사용자의 모든 세션 조회 (최신순)
       List<WorkoutSession> findByUserIdOrderByWorkoutDateDesc(Long userId);

       // 특정 기간의 세션 조회
       List<WorkoutSession> findByUserIdAndWorkoutDateBetween(
                     Long userId,
                     LocalDate startDate,
                     LocalDate endDate);

       // 세션 ID와 사용자 ID로 조회 (권한 확인용)
       Optional<WorkoutSession> findByIdAndUserId(Long id, Long userId);

       // 세션과 세트 정보까지 함께 조회
       @Query("SELECT DISTINCT ws FROM WorkoutSession ws " +
                     "LEFT JOIN FETCH ws.workoutSets wset " +
                     "LEFT JOIN FETCH wset.exerciseType " +
                     "WHERE ws.user.id = :userId " +
                     "ORDER BY ws.workoutDate DESC")
       List<WorkoutSession> findAllByUserIdWithSets(@Param("userId") Long userId);

       // 특정 세션과 세트 정보 함께 조회
       @Query("SELECT ws FROM WorkoutSession ws " +
                     "LEFT JOIN FETCH ws.workoutSets wset " +
                     "LEFT JOIN FETCH wset.exerciseType " +
                     "WHERE ws.id = :sessionId AND ws.user.id = :userId")
       Optional<WorkoutSession> findByIdWithSets(@Param("sessionId") Long sessionId,
                                                 @Param("userId") Long userId);

       // 주간 운동 횟수 조회
       @Query("SELECT COUNT(ws) FROM WorkoutSession ws " +
                     "WHERE ws.user.id = :userId " +
                     "AND ws.workoutDate BETWEEN :startDate AND :endDate")
       long countWeeklyWorkouts(@Param("userId") Long userId,
                     @Param("startDate") LocalDate startDate,
                     @Param("endDate") LocalDate endDate);
}