package com.example.FitTracker.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "routine_exercises")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class RoutineExercise {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routine_id", nullable = false)
    private Routine routine;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_type_id", nullable = false)
    private ExerciseType exerciseType;
    
    @Column(name = "target_sets", nullable = false)
    private Integer targetSets;
    
    @Column(name = "target_reps", nullable = false)
    private Integer targetReps;
    
    @Column(name = "target_weight", precision = 5, scale = 2)
    private BigDecimal targetWeight;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
    
    // 비즈니스 메서드
    public void updateTargets(Integer sets, Integer reps, BigDecimal weight) {
        this.targetSets = sets;
        this.targetReps = reps;
        this.targetWeight = weight;
    }
}