package com.example.FitTracker.repository;

import com.example.FitTracker.domain.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseTypeRepository extends JpaRepository<ExerciseType, Long> {
    
    // 신체 부위별 운동 종목 조회
    List<ExerciseType> findByBodyPart(String bodyPart);
    
    // 운동 이름으로 검색
    List<ExerciseType> findByNameContaining(String name);
}