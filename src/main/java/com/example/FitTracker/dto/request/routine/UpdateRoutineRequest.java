package com.example.FitTracker.dto.request.routine;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoutineRequest {
    
    @NotBlank(message = "루틴 이름은 필수입니다")
    @Size(max = 100, message = "루틴 이름은 100자 이하여야 합니다")
    private String name;
    
    @Size(max = 500, message = "설명은 500자 이하여야 합니다")
    private String description;
    
    private List<RoutineExerciseRequest> exercises;
}