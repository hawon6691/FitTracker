package com.example.FitTracker.repository;

import com.example.FitTracker.domain.Routine;
import com.example.FitTracker.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoutineRepository extends JpaRepository<Routine, Long> {

       // 특정 사용자의 모든 루틴 조회
       List<Routine> findByUser(User user);

       // 사용자 ID로 루틴 조회
       List<Routine> findByUserId(Long userId);

       // 루틴 ID와 사용자 ID로 조회 (권한 확인용)
       Optional<Routine> findByIdAndUserId(Long id, Long userId);

       // 루틴과 운동 종목까지 함께 조회 (N+1 문제 해결)
       @Query("SELECT DISTINCT r FROM Routine r " +
                     "LEFT JOIN FETCH r.routineExercises re " +
                     "LEFT JOIN FETCH re.exerciseType " +
                     "WHERE r.user.id = :userId")
       List<Routine> findAllByUserIdWithExercises(@Param("userId") Long userId);
}