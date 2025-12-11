package com.example.FitTracker.service;

import com.example.FitTracker.domain.ExerciseType;
import com.example.FitTracker.domain.Routine;
import com.example.FitTracker.domain.RoutineExercise;
import com.example.FitTracker.domain.User;
import com.example.FitTracker.dto.request.routine.CreateRoutineRequest;
import com.example.FitTracker.dto.request.routine.RoutineExerciseRequest;
import com.example.FitTracker.dto.request.routine.UpdateRoutineRequest;
import com.example.FitTracker.dto.response.routine.RoutineResponse;
import com.example.FitTracker.repository.RoutineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoutineService 단위 테스트")
class RoutineServiceTest {
    
    @Mock
    private RoutineRepository routineRepository;
    
    @Mock
    private UserService userService;
    
    @Mock
    private ExerciseTypeService exerciseTypeService;
    
    @InjectMocks
    private RoutineService routineService;
    
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
            .description("가슴 운동")
            .build();
    }
    
    @Test
    @DisplayName("루틴 생성 성공")
    void createRoutine_success() {
        // given
        List<RoutineExerciseRequest> exercises = Arrays.asList(
            new RoutineExerciseRequest(1L, 3, 10, new BigDecimal("80.0"))
        );
        
        CreateRoutineRequest request = new CreateRoutineRequest(
            "가슴 루틴",
            "월요일 가슴 운동",
            exercises
        );
        
        Routine savedRoutine = Routine.builder()
            .id(1L)
            .user(testUser)
            .name(request.getName())
            .description(request.getDescription())
            .build();
        
        given(userService.findById(1L)).willReturn(testUser);
        given(exerciseTypeService.findById(1L)).willReturn(benchPress);
        given(routineRepository.save(any(Routine.class))).willReturn(savedRoutine);
        
        // when
        RoutineResponse response = routineService.createRoutine(1L, request);
        
        // then
        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("가슴 루틴");
        assertThat(response.getDescription()).isEqualTo("월요일 가슴 운동");
        
        verify(userService, times(1)).findById(1L);
        verify(exerciseTypeService, times(1)).findById(1L);
        verify(routineRepository, times(1)).save(any(Routine.class));
    }
    
    @Test
    @DisplayName("루틴 목록 조회")
    void getUserRoutines_success() {
        // given
        Routine routine1 = Routine.builder()
            .id(1L)
            .user(testUser)
            .name("루틴1")
            .description("설명1")
            .build();
        
        Routine routine2 = Routine.builder()
            .id(2L)
            .user(testUser)
            .name("루틴2")
            .description("설명2")
            .build();
        
        given(routineRepository.findByUserId(1L))
            .willReturn(Arrays.asList(routine1, routine2));
        
        // when
        List<RoutineResponse> routines = routineService.getUserRoutines(1L);
        
        // then
        assertThat(routines).hasSize(2);
        assertThat(routines.get(0).getName()).isEqualTo("루틴1");
        assertThat(routines.get(1).getName()).isEqualTo("루틴2");
    }
    
    @Test
    @DisplayName("루틴 상세 조회 성공")
    void getRoutineById_success() {
        // given
        Routine routine = Routine.builder()
            .id(1L)
            .user(testUser)
            .name("테스트 루틴")
            .description("테스트 설명")
            .build();
        
        given(routineRepository.findByIdAndUserId(1L, 1L))
            .willReturn(Optional.of(routine));
        
        // when
        RoutineResponse response = routineService.getRoutineById(1L, 1L);
        
        // then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("테스트 루틴");
    }
    
    @Test
    @DisplayName("루틴 조회 실패 - 존재하지 않는 루틴")
    void getRoutineById_fail_notFound() {
        // given
        given(routineRepository.findByIdAndUserId(999L, 1L))
            .willReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> routineService.getRoutineById(1L, 999L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("루틴을 찾을 수 없습니다");
    }
    
    @Test
    @DisplayName("루틴 삭제 성공")
    void deleteRoutine_success() {
        // given
        Routine routine = Routine.builder()
            .id(1L)
            .user(testUser)
            .name("삭제할 루틴")
            .build();
        
        given(routineRepository.findByIdAndUserId(1L, 1L))
            .willReturn(Optional.of(routine));
        
        // when
        routineService.deleteRoutine(1L, 1L);
        
        // then
        verify(routineRepository, times(1)).delete(routine);
    }
}