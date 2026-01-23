package com.example.FitTracker.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "goals")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Goal {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Column(nullable = false)
    private String title;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalType type;  // WEIGHT, REPS, FREQUENCY
    
    @Column(name = "target_value")
    private Integer targetValue;
    
    @Column(name = "current_value")
    private Integer currentValue;
    
    @Column(name = "target_date")
    private LocalDate targetDate;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GoalStatus status;  // IN_PROGRESS, COMPLETED, FAILED
    
    public void updateProgress(Integer currentValue) {
        this.currentValue = currentValue;
        if (this.currentValue >= this.targetValue) {
            this.status = GoalStatus.COMPLETED;
        }
    }
}