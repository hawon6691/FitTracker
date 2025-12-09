package com.example.FitTracker.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercise_types")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExerciseType {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(name = "body_part", nullable = false, length = 50)
    private String bodyPart;  // 가슴, 등, 다리, 어깨, 팔, 복근
    
    @Column(columnDefinition = "TEXT")
    private String description;
}