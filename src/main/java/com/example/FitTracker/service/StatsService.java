package com.example.FitTracker.service;

import com.example.FitTracker.domain.WorkoutSet;
import com.example.FitTracker.domain.WorkoutSession;
import com.example.FitTracker.dto.response.stats.BodyPartStatsResponse;
import com.example.FitTracker.dto.response.stats.MonthlyStatsResponse;
import com.example.FitTracker.dto.response.stats.PersonalRecordResponse;
import com.example.FitTracker.dto.response.stats.WeeklyStatsResponse;
import com.example.FitTracker.repository.WorkoutSessionRepository;
import com.example.FitTracker.repository.WorkoutSetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StatsService {
    
    private final WorkoutSessionRepository workoutSessionRepository;
    private final WorkoutSetRepository workoutSetRepository;
    
    /**
     * 주간 통계 조회
     */
    public WeeklyStatsResponse getWeeklyStats(Long userId, LocalDate startDate, LocalDate endDate) {
        List<WorkoutSession> sessions = workoutSessionRepository
                .findByUserIdAndWorkoutDateBetween(userId, startDate, endDate);
        
        long totalWorkouts = sessions.size();
        long totalSets = sessions.stream()
                .mapToLong(session -> session.getWorkoutSets().size())
                .sum();
        
        int totalMinutes = sessions.stream()
                .filter(session -> session.getDurationMinutes() != null)
                .mapToInt(WorkoutSession::getDurationMinutes)
                .sum();
        
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        double avgWorkoutsPerDay = daysBetween > 0 ? 
                (double) totalWorkouts / daysBetween : 0.0;
        
        return WeeklyStatsResponse.builder()
                .startDate(startDate)
                .endDate(endDate)
                .totalWorkouts(totalWorkouts)
                .totalSets(totalSets)
                .totalMinutes(totalMinutes)
                .avgWorkoutsPerDay(Math.round(avgWorkoutsPerDay * 100.0) / 100.0)
                .build();
    }
    
    /**
     * 월간 통계 조회
     */
    public MonthlyStatsResponse getMonthlyStats(Long userId, YearMonth yearMonth) {
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();
        
        List<WorkoutSession> sessions = workoutSessionRepository
                .findByUserIdAndWorkoutDateBetween(userId, startDate, endDate);
        
        long totalWorkouts = sessions.size();
        long totalSets = sessions.stream()
                .mapToLong(session -> session.getWorkoutSets().size())
                .sum();
        
        int totalMinutes = sessions.stream()
                .filter(session -> session.getDurationMinutes() != null)
                .mapToInt(WorkoutSession::getDurationMinutes)
                .sum();
        
        // 실제 운동한 날짜 수
        long totalDaysWorkedOut = sessions.stream()
                .map(WorkoutSession::getWorkoutDate)
                .distinct()
                .count();
        
        // 주당 평균 운동 횟수 (한 달을 4주로 계산)
        double avgWorkoutsPerWeek = totalWorkouts / 4.0;
        
        return MonthlyStatsResponse.builder()
                .yearMonth(yearMonth)
                .totalWorkouts(totalWorkouts)
                .totalSets(totalSets)
                .totalMinutes(totalMinutes)
                .avgWorkoutsPerWeek(Math.round(avgWorkoutsPerWeek * 100.0) / 100.0)
                .totalDaysWorkedOut((int) totalDaysWorkedOut)
                .build();
    }
    
    /**
     * 신체 부위별 운동량 통계
     */
    public List<BodyPartStatsResponse> getBodyPartStats(Long userId, LocalDate startDate, LocalDate endDate) {
        List<Object[]> results = workoutSetRepository
                .findBodyPartStats(userId, startDate, endDate);
        
        // 전체 세트 수 계산
        long totalSets = results.stream()
                .mapToLong(result -> (Long) result[1])
                .sum();
        
        return results.stream()
                .map(result -> {
                    String bodyPart = (String) result[0];
                    Long setCount = (Long) result[1];
                    Double percentage = totalSets > 0 ? 
                            (setCount * 100.0) / totalSets : 0.0;
                    
                    return BodyPartStatsResponse.builder()
                            .bodyPart(bodyPart)
                            .totalSets(setCount)
                            .percentage(Math.round(percentage * 10.0) / 10.0)
                            .build();
                })
                .sorted(Comparator.comparing(BodyPartStatsResponse::getTotalSets).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 개인 기록 조회 (운동 종목별 최고 기록)
     */
    public List<PersonalRecordResponse> getPersonalRecords(Long userId) {
        List<WorkoutSet> allSets = workoutSetRepository.findAllPersonalRecords(userId);
        
        // 운동 종목별로 그룹화
        Map<Long, List<WorkoutSet>> setsByExercise = allSets.stream()
                .collect(Collectors.groupingBy(set -> set.getExerciseType().getId()));
        
        return setsByExercise.entrySet().stream()
                .map(entry -> {
                    List<WorkoutSet> sets = entry.getValue();
                    
                    // 최고 무게 세트 찾기
                    WorkoutSet maxWeightSet = sets.stream()
                            .max(Comparator.comparing((WorkoutSet set) -> 
                                    set.getWeight() != null ? set.getWeight() : BigDecimal.ZERO)
                                    .thenComparing(WorkoutSet::getReps))
                            .orElse(null);
                    
                    if (maxWeightSet == null || maxWeightSet.getWeight() == null) {
                        return null;
                    }
                    
                    // 1RM 계산 (Brzycki 공식: weight × (36 / (37 - reps)))
                    BigDecimal oneRepMax = calculateOneRepMax(
                            maxWeightSet.getWeight(), 
                            maxWeightSet.getReps()
                    );
                    
                    return PersonalRecordResponse.builder()
                            .exerciseTypeId(maxWeightSet.getExerciseType().getId())
                            .exerciseName(maxWeightSet.getExerciseType().getName())
                            .bodyPart(maxWeightSet.getExerciseType().getBodyPart())
                            .maxWeight(maxWeightSet.getWeight())
                            .repsAtMaxWeight(maxWeightSet.getReps())
                            .achievedDate(maxWeightSet.getWorkoutSession().getWorkoutDate())
                            .oneRepMax(oneRepMax)
                            .build();
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(PersonalRecordResponse::getExerciseName))
                .collect(Collectors.toList());
    }
    
    /**
     * 특정 운동의 개인 기록 조회
     */
    public PersonalRecordResponse getPersonalRecordByExercise(Long userId, Long exerciseTypeId) {
        List<WorkoutSet> sets = workoutSetRepository.findPersonalRecords(userId, exerciseTypeId);
        
        if (sets.isEmpty()) {
            throw new IllegalArgumentException("해당 운동의 기록이 없습니다");
        }
        
        // 최고 무게 세트 찾기
        WorkoutSet maxWeightSet = sets.stream()
                .filter(set -> set.getWeight() != null)
                .max(Comparator.comparing(WorkoutSet::getWeight)
                        .thenComparing(WorkoutSet::getReps))
                .orElseThrow(() -> new IllegalArgumentException("무게 기록이 없습니다"));
        
        // 1RM 계산
        BigDecimal oneRepMax = calculateOneRepMax(
                maxWeightSet.getWeight(), 
                maxWeightSet.getReps()
        );
        
        return PersonalRecordResponse.builder()
                .exerciseTypeId(maxWeightSet.getExerciseType().getId())
                .exerciseName(maxWeightSet.getExerciseType().getName())
                .bodyPart(maxWeightSet.getExerciseType().getBodyPart())
                .maxWeight(maxWeightSet.getWeight())
                .repsAtMaxWeight(maxWeightSet.getReps())
                .achievedDate(maxWeightSet.getWorkoutSession().getWorkoutDate())
                .oneRepMax(oneRepMax)
                .build();
    }
    
    /**
     * 1RM 계산 (Brzycki 공식)
     * 1RM = weight × (36 / (37 - reps))
     */
    private BigDecimal calculateOneRepMax(BigDecimal weight, Integer reps) {
        if (reps == 1) {
            return weight;
        }
        
        if (reps >= 37) {
            // reps가 너무 많으면 공식 적용 불가
            return weight;
        }
        
        BigDecimal divisor = new BigDecimal(37 - reps);
        BigDecimal multiplier = new BigDecimal("36").divide(divisor, 2, RoundingMode.HALF_UP);
        
        return weight.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }
}
