package com.example.FitTracker.service;

import com.example.FitTracker.domain.*;
import com.example.FitTracker.dto.request.workout.AddWorkoutSetRequest;
import com.example.FitTracker.dto.request.workout.CreateWorkoutSessionRequest;
import com.example.FitTracker.dto.response.workout.WorkoutSessionResponse;
import com.example.FitTracker.repository.WorkoutSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("WorkoutService 단위 테스트")
class WorkoutServiceTest {
    
    @Mock
    private WorkoutSessionRepository workoutSessionRepository;
    
    @Mock
    private UserService userService;
    
    @Mock
    private ExerciseTypeService exerciseTypeService;
    
    @InjectMocks
    private WorkoutService workoutService;
    
    private User testUser;
    private ExerciseType benchPress;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .email("test@test.com")
            .name("테스트유저")
            .build();
        
        benchPress = ExerciseType.builder()
            .id(1L)
            .name("벤치 프레스")
            .bodyPart("가슴")
            .build();
    }
    
    @Test
    @DisplayName("운동 세션 생성 성공")
    void createWorkoutSession_success() {
        // given
        CreateWorkoutSessionRequest request = new CreateWorkoutSessionRequest(
            null,
            LocalDate.now(),
            60,
            "오늘 운동"
        );
        
        WorkoutSession savedSession = WorkoutSession.builder()
            .id(1L)
            .user(testUser)
            .workoutDate(request.getWorkoutDate())
            .durationMinutes(request.getDurationMinutes())
            .notes(request.getNotes())
            .build();
        
        given(userService.findById(1L)).willReturn(testUser);
        given(workoutSessionRepository.save(any(WorkoutSession.class)))
            .willReturn(savedSession);
        
        // when
        WorkoutSessionResponse response = workoutService.createWorkoutSession(1L, request);
        
        // then
        assertThat(response).isNotNull();
        assertThat(response.getWorkoutDate()).isEqualTo(LocalDate.now());
        assertThat(response.getDurationMinutes()).isEqualTo(60);
        assertThat(response.getNotes()).isEqualTo("오늘 운동");
        
        verify(userService, times(1)).findById(1L);
        verify(workoutSessionRepository, times(1)).save(any(WorkoutSession.class));
    }
    
    @Test
    @DisplayName("세트 추가 성공")
    void addWorkoutSet_success() {
        // given
        WorkoutSession session = WorkoutSession.builder()
            .id(1L)
            .user(testUser)
            .workoutDate(LocalDate.now())
            .build();
        
        AddWorkoutSetRequest request = new AddWorkoutSetRequest(
            1L, 1, 10, new BigDecimal("80.0"), true
        );
        
        given(workoutSessionRepository.findByIdAndUserId(1L, 1L))
            .willReturn(Optional.of(session));
        given(exerciseTypeService.findById(1L)).willReturn(benchPress);
        
        // when
        WorkoutSessionResponse response = workoutService.addWorkoutSet(1L, 1L, request);
        
        // then
        assertThat(response).isNotNull();
        verify(exerciseTypeService, times(1)).findById(1L);
    }
    
    @Test
    @DisplayName("사용자 운동 기록 조회")
    void getUserWorkouts_success() {
        // given
        WorkoutSession session1 = WorkoutSession.builder()
            .id(1L)
            .user(testUser)
            .workoutDate(LocalDate.now())
            .build();
        
        WorkoutSession session2 = WorkoutSession.builder()
            .id(2L)
            .user(testUser)
            .workoutDate(LocalDate.now().minusDays(1))
            .build();
        
        given(workoutSessionRepository.findByUserIdOrderByWorkoutDateDesc(1L))
            .willReturn(Arrays.asList(session1, session2));
        
        // when
        List<WorkoutSessionResponse> workouts = workoutService.getUserWorkouts(1L);
        
        // then
        assertThat(workouts).hasSize(2);
    }
    
    @Test
    @DisplayName("세션 삭제 성공")
    void deleteWorkoutSession_success() {
        // given
        WorkoutSession session = WorkoutSession.builder()
            .id(1L)
            .user(testUser)
            .workoutDate(LocalDate.now())
            .build();
        
        given(workoutSessionRepository.findByIdAndUserId(1L, 1L))
            .willReturn(Optional.of(session));
        
        // when
        workoutService.deleteWorkoutSession(1L, 1L);
        
        // then
        verify(workoutSessionRepository, times(1)).delete(session);
    }
}