package com.example.FitTracker.integration;

import com.example.FitTracker.dto.request.workout.AddWorkoutSetRequest;
import com.example.FitTracker.dto.request.workout.CreateWorkoutSessionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 운동 기록 API 통합 테스트
 */
@DisplayName("운동 기록 API 통합 테스트")
class WorkoutIntegrationTest extends IntegrationTestBase {
    
    @Test
    @DisplayName("운동 세션 생성 성공 - 루틴 없이")
    void createWorkoutSession_withoutRoutine_success() throws Exception {
        // given
        CreateWorkoutSessionRequest request = new CreateWorkoutSessionRequest(
            null,  // 루틴 없이
            LocalDate.now(),
            60,
            "오늘 운동 기록"
        );
        
        // when & then
        mockMvc.perform(post("/api/workouts")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("운동 세션 생성 완료"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.workoutDate").value(LocalDate.now().toString()))
                .andExpect(jsonPath("$.data.durationMinutes").value(60))
                .andExpect(jsonPath("$.data.notes").value("오늘 운동 기록"))
                .andExpect(jsonPath("$.data.sets").isArray())
                .andExpect(jsonPath("$.data.sets.length()").value(0));
    }
    
    @Test
    @DisplayName("운동 세션 생성 실패 - 필수 필드 누락")
    void createWorkoutSession_fail_missingDate() throws Exception {
        // given - 날짜 없음
        String requestJson = "{\"durationMinutes\":60,\"notes\":\"메모\"}";
        
        // when & then
        mockMvc.perform(post("/api/workouts")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.workoutDate").exists());
    }
    
    @Test
    @DisplayName("세트 추가 성공")
    void addWorkoutSet_success() throws Exception {
        // given - 세션 먼저 생성
        Long sessionId = createTestWorkoutSession();
        
        AddWorkoutSetRequest request = new AddWorkoutSetRequest(
            1L,  // 벤치 프레스
            1,   // 1세트
            10,  // 10회
            new BigDecimal("80.0"),  // 80kg
            true
        );
        
        // when & then
        mockMvc.perform(post("/api/workouts/" + sessionId + "/sets")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("세트 추가 완료"))
                .andExpect(jsonPath("$.data.sets").isArray())
                .andExpect(jsonPath("$.data.sets.length()").value(1))
                .andExpect(jsonPath("$.data.sets[0].exerciseName").value("벤치 프레스"))
                .andExpect(jsonPath("$.data.sets[0].setNumber").value(1))
                .andExpect(jsonPath("$.data.sets[0].reps").value(10))
                .andExpect(jsonPath("$.data.sets[0].weight").value(80.0))
                .andExpect(jsonPath("$.data.sets[0].completed").value(true));
    }
    
    @Test
    @DisplayName("세트 추가 실패 - 존재하지 않는 세션")
    void addWorkoutSet_fail_sessionNotFound() throws Exception {
        // given
        AddWorkoutSetRequest request = new AddWorkoutSetRequest(
            1L, 1, 10, new BigDecimal("80.0"), true
        );
        
        // when & then
        mockMvc.perform(post("/api/workouts/999999/sets")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("운동 세션을 찾을 수 없습니다: 999999"));
    }
    
    @Test
    @DisplayName("세트 추가 실패 - 존재하지 않는 운동 종목")
    void addWorkoutSet_fail_exerciseNotFound() throws Exception {
        // given
        Long sessionId = createTestWorkoutSession();
        
        AddWorkoutSetRequest request = new AddWorkoutSetRequest(
            999999L,  // 존재하지 않는 운동 종목
            1, 10, new BigDecimal("80.0"), true
        );
        
        // when & then
        mockMvc.perform(post("/api/workouts/" + sessionId + "/sets")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("운동 종목을 찾을 수 없습니다: 999999"));
    }
    
    @Test
    @DisplayName("운동 기록 전체 조회")
    void getUserWorkouts_success() throws Exception {
        // given - 세션 2개 생성
        createTestWorkoutSession();
        createTestWorkoutSession();
        
        // when & then
        mockMvc.perform(get("/api/workouts")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }
    
    @Test
    @DisplayName("기간별 운동 기록 조회")
    void getWorkoutsByDateRange_success() throws Exception {
        // given - 다른 날짜로 세션 생성
        createWorkoutSessionWithDate(LocalDate.now().minusDays(5));
        createWorkoutSessionWithDate(LocalDate.now().minusDays(3));
        createWorkoutSessionWithDate(LocalDate.now().minusDays(1));
        
        // when & then - 최근 3일 조회
        mockMvc.perform(get("/api/workouts")
                .param("startDate", LocalDate.now().minusDays(3).toString())
                .param("endDate", LocalDate.now().toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));  // 3일과 1일 전 세션
    }
    
    @Test
    @DisplayName("운동 세션 상세 조회")
    void getWorkoutSession_success() throws Exception {
        // given
        Long sessionId = createTestWorkoutSession();
        
        // 세트 추가
        AddWorkoutSetRequest setRequest = new AddWorkoutSetRequest(
            1L, 1, 10, new BigDecimal("80.0"), true
        );
        
        mockMvc.perform(post("/api/workouts/" + sessionId + "/sets")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(setRequest)))
                .andExpect(status().isOk());
        
        // when & then
        mockMvc.perform(get("/api/workouts/" + sessionId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(sessionId))
                .andExpect(jsonPath("$.data.sets").isArray())
                .andExpect(jsonPath("$.data.sets.length()").value(1));
    }
    
    @Test
    @DisplayName("운동 세션 삭제 성공")
    void deleteWorkoutSession_success() throws Exception {
        // given
        Long sessionId = createTestWorkoutSession();
        
        // when & then
        mockMvc.perform(delete("/api/workouts/" + sessionId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("운동 세션 삭제 완료"));
        
        // 삭제 확인
        mockMvc.perform(get("/api/workouts/" + sessionId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("완전한 운동 기록 플로우 테스트")
    void completeWorkoutFlow_success() throws Exception {
        // 1. 세션 생성
        Long sessionId = createTestWorkoutSession();
        
        // 2. 벤치 프레스 3세트 추가
        for (int i = 1; i <= 3; i++) {
            AddWorkoutSetRequest setRequest = new AddWorkoutSetRequest(
                1L, i, 10, new BigDecimal("80.0"), true
            );
            
            mockMvc.perform(post("/api/workouts/" + sessionId + "/sets")
                    .header("Authorization", getAuthorizationHeader())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(setRequest)))
                    .andExpect(status().isOk());
        }
        
        // 3. 최종 세션 확인
        mockMvc.perform(get("/api/workouts/" + sessionId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sets.length()").value(3))
                .andExpect(jsonPath("$.data.sets[0].setNumber").value(1))
                .andExpect(jsonPath("$.data.sets[1].setNumber").value(2))
                .andExpect(jsonPath("$.data.sets[2].setNumber").value(3));
    }
    
    /**
     * 테스트용 운동 세션 생성 헬퍼 메서드
     */
    private Long createTestWorkoutSession() throws Exception {
        return createWorkoutSessionWithDate(LocalDate.now());
    }
    
    /**
     * 특정 날짜로 운동 세션 생성
     */
    private Long createWorkoutSessionWithDate(LocalDate date) throws Exception {
        CreateWorkoutSessionRequest request = new CreateWorkoutSessionRequest(
            null, date, 60, "테스트 세션"
        );
        
        MvcResult result = mockMvc.perform(post("/api/workouts")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("data").get("id").asLong();
    }
}
