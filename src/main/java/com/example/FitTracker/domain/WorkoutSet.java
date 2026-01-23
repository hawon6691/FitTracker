package com.example.FitTracker.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "workout_sets", indexes = {
    @Index(name = "idx_session_exercise", columnList = "workout_session_id, exercise_type_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class WorkoutSet {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workout_session_id", nullable = false)
    private WorkoutSession workoutSession;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exercise_type_id", nullable = false)
    private ExerciseType exerciseType;
    
    @Column(name = "set_number", nullable = false)
    private Integer setNumber;
    
    @Column(nullable = false)
    private Integer reps;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal weight;
    
    @Column(nullable = false)
    private Boolean completed;
    
    // 비즈니스 메서드
    public void updateSet(Integer reps, BigDecimal weight, Boolean completed) {
        this.reps = reps;
        this.weight = weight;
        this.completed = completed;
    }
}