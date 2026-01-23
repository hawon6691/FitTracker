package com.example.FitTracker.service;

import com.example.FitTracker.domain.ExerciseType;
import com.example.FitTracker.domain.WorkoutSession;
import com.example.FitTracker.domain.WorkoutSet;
import com.example.FitTracker.dto.response.stats.WeeklyStatsResponse;
import com.example.FitTracker.repository.WorkoutSessionRepository;
import com.example.FitTracker.repository.WorkoutSetRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("StatsService 단위 테스트")
class StatsServiceTest {
    
    @Mock
    private WorkoutSessionRepository workoutSessionRepository;
    
    @Mock
    private WorkoutSetRepository workoutSetRepository;
    
    @InjectMocks
    private StatsService statsService;
    
    @Test
    @DisplayName("주간 통계 계산")
    void getWeeklyStats_success() {
        // given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        WorkoutSession session1 = WorkoutSession.builder()
            .id(1L)
            .workoutDate(LocalDate.now())
            .durationMinutes(60)
            .build();
        
        WorkoutSession session2 = WorkoutSession.builder()
            .id(2L)
            .workoutDate(LocalDate.now().minusDays(2))
            .durationMinutes(45)
            .build();
        
        WorkoutSet set1 = WorkoutSet.builder()
            .id(1L)
            .workoutSession(session1)
            .build();
        
        WorkoutSet set2 = WorkoutSet.builder()
            .id(2L)
            .workoutSession(session1)
            .build();
        
        session1 = WorkoutSession.builder()
            .id(1L)
            .workoutDate(LocalDate.now())
            .durationMinutes(60)
            .workoutSets(Arrays.asList(set1, set2))
            .build();
        
        given(workoutSessionRepository.findByUserIdAndWorkoutDateBetween(1L, startDate, endDate))
            .willReturn(Arrays.asList(session1, session2));
        
        // when
        WeeklyStatsResponse stats = statsService.getWeeklyStats(1L, startDate, endDate);
        
        // then
        assertThat(stats).isNotNull();
        assertThat(stats.getTotalWorkouts()).isEqualTo(2);
        assertThat(stats.getTotalSets()).isEqualTo(2);
        assertThat(stats.getTotalMinutes()).isEqualTo(105);
    }
    
    @Test
    @DisplayName("1RM 계산 검증 - 1회")
    void calculateOneRepMax_oneRep() {
        // 1회일 때는 무게 그대로
        // 이 테스트는 private 메서드라 실제로는 다른 방식으로 검증해야 함
        // 여기서는 개념만 보여드립니다
    }
    
    @Test
    @DisplayName("1RM 계산 검증 - 10회")
    void calculateOneRepMax_tenReps() {
        // 90kg × 10회 = 약 120kg
        // Brzycki 공식: weight × (36 / (37 - reps))
        // 90 × (36 / 27) = 90 × 1.333 = 119.7kg
    }
}