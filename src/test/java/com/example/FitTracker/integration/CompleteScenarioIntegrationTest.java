package com.example.FitTracker.integration;

import com.example.FitTracker.dto.request.routine.CreateRoutineRequest;
import com.example.FitTracker.dto.request.routine.RoutineExerciseRequest;
import com.example.FitTracker.dto.request.workout.AddWorkoutSetRequest;
import com.example.FitTracker.dto.request.workout.CreateWorkoutSessionRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 전체 사용자 시나리오 통합 테스트
 * 실제 사용자 플로우를 테스트합니다
 */
@DisplayName("전체 시나리오 통합 테스트")
class CompleteScenarioIntegrationTest extends IntegrationTestBase {
    
    @Test
    @DisplayName("완전한 운동 루틴 관리 및 기록 시나리오")
    void completeWorkoutScenario() throws Exception {
        // ==================== 1단계: 루틴 생성 ====================
        System.out.println("\n===== 1단계: 가슴 운동 루틴 생성 =====");
        
        CreateRoutineRequest routineRequest = new CreateRoutineRequest(
            "월요일 가슴 루틴",
            "가슴과 삼두근 집중 운동",
            Arrays.asList(
                new RoutineExerciseRequest(1L, 4, 10, new BigDecimal("80.0")),   // 벤치 프레스
                new RoutineExerciseRequest(2L, 3, 12, new BigDecimal("60.0")),   // 인클라인 벤치
                new RoutineExerciseRequest(3L, 3, 12, new BigDecimal("30.0")),   // 덤벨 프레스
                new RoutineExerciseRequest(6L, 3, 12, null)                      // 딥스 (자중)
            )
        );
        
        MvcResult routineResult = mockMvc.perform(post("/api/routines")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(routineRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.name").value("월요일 가슴 루틴"))
                .andExpect(jsonPath("$.data.exercises.length()").value(4))
                .andReturn();
        
        Long routineId = objectMapper.readTree(routineResult.getResponse().getContentAsString())
                .get("data").get("id").asLong();
        
        System.out.println("✅ 루틴 생성 완료: ID = " + routineId);
        
        // ==================== 2단계: 루틴 조회 ====================
        System.out.println("\n===== 2단계: 생성한 루틴 조회 =====");
        
        mockMvc.perform(get("/api/routines/" + routineId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.exercises[0].exerciseName").value("벤치 프레스"))
                .andExpect(jsonPath("$.data.exercises[0].targetSets").value(4))
                .andExpect(jsonPath("$.data.exercises[0].targetWeight").value(80.0));
        
        System.out.println("✅ 루틴 조회 완료");
        
        // ==================== 3단계: 운동 세션 생성 ====================
        System.out.println("\n===== 3단계: 오늘의 운동 세션 시작 =====");
        
        CreateWorkoutSessionRequest sessionRequest = new CreateWorkoutSessionRequest(
            routineId,
            LocalDate.now(),
            90,
            "오늘은 컨디션이 좋았다!"
        );
        
        MvcResult sessionResult = mockMvc.perform(post("/api/workouts")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.routineName").value("월요일 가슴 루틴"))
                .andReturn();
        
        Long sessionId = objectMapper.readTree(sessionResult.getResponse().getContentAsString())
                .get("data").get("id").asLong();
        
        System.out.println("✅ 운동 세션 생성 완료: ID = " + sessionId);
        
        // ==================== 4단계: 벤치 프레스 4세트 기록 ====================
        System.out.println("\n===== 4단계: 벤치 프레스 기록 입력 =====");
        
        // 1세트: 워밍업
        addSet(sessionId, 1L, 1, 12, new BigDecimal("60.0"));
        System.out.println("1세트 완료: 60kg × 12회");
        
        // 2세트: 메인
        addSet(sessionId, 1L, 2, 10, new BigDecimal("80.0"));
        System.out.println("2세트 완료: 80kg × 10회");
        
        // 3세트: 메인
        addSet(sessionId, 1L, 3, 10, new BigDecimal("80.0"));
        System.out.println("3세트 완료: 80kg × 10회");
        
        // 4세트: 드롭셋
        addSet(sessionId, 1L, 4, 12, new BigDecimal("70.0"));
        System.out.println("4세트 완료: 70kg × 12회");
        
        System.out.println("✅ 벤치 프레스 완료");
        
        // ==================== 5단계: 인클라인 벤치 3세트 기록 ====================
        System.out.println("\n===== 5단계: 인클라인 벤치 프레스 기록 입력 =====");
        
        addSet(sessionId, 2L, 1, 12, new BigDecimal("60.0"));
        addSet(sessionId, 2L, 2, 12, new BigDecimal("60.0"));
        addSet(sessionId, 2L, 3, 10, new BigDecimal("65.0"));
        
        System.out.println("✅ 인클라인 벤치 프레스 완료");
        
        // ==================== 6단계: 덤벨 프레스와 딥스 기록 ====================
        System.out.println("\n===== 6단계: 나머지 운동 기록 =====");
        
        // 덤벨 프레스
        addSet(sessionId, 3L, 1, 12, new BigDecimal("30.0"));
        addSet(sessionId, 3L, 2, 12, new BigDecimal("30.0"));
        addSet(sessionId, 3L, 3, 10, new BigDecimal("32.5"));
        
        // 딥스 (자중)
        addSet(sessionId, 6L, 1, 15, null);
        addSet(sessionId, 6L, 2, 12, null);
        addSet(sessionId, 6L, 3, 10, null);
        
        System.out.println("✅ 모든 운동 완료");
        
        // ==================== 7단계: 세션 최종 확인 ====================
        System.out.println("\n===== 7단계: 오늘 운동 기록 최종 확인 =====");
        
        mockMvc.perform(get("/api/workouts/" + sessionId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.sets.length()").value(13))  // 총 13세트
                .andExpect(jsonPath("$.data.durationMinutes").value(90))
                .andExpect(jsonPath("$.data.notes").value("오늘은 컨디션이 좋았다!"));
        
        System.out.println("✅ 운동 세션 확인 완료 - 총 13세트");
        
        // ==================== 8단계: 개인 기록 조회 ====================
        System.out.println("\n===== 8단계: 벤치 프레스 개인 기록 확인 =====");
        
        mockMvc.perform(get("/api/stats/personal-records/1")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.exerciseName").value("벤치 프레스"))
                .andExpect(jsonPath("$.data.maxWeight").value(80.0))
                .andExpect(jsonPath("$.data.repsAtMaxWeight").value(10))
                .andExpect(jsonPath("$.data.oneRepMax").exists());
        
        System.out.println("✅ 개인 기록 조회 완료");
        
        // ==================== 9단계: 주간 통계 확인 ====================
        System.out.println("\n===== 9단계: 이번 주 통계 확인 =====");
        
        mockMvc.perform(get("/api/stats/weekly")
                .param("startDate", LocalDate.now().minusDays(7).toString())
                .param("endDate", LocalDate.now().toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalWorkouts").exists())
                .andExpect(jsonPath("$.data.totalSets").exists());
        
        System.out.println("✅ 주간 통계 확인 완료");
        
        // ==================== 10단계: 신체 부위별 통계 ====================
        System.out.println("\n===== 10단계: 신체 부위별 운동량 확인 =====");
        
        mockMvc.perform(get("/api/stats/body-parts")
                .param("startDate", LocalDate.now().minusDays(30).toString())
                .param("endDate", LocalDate.now().toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isArray());
        
        System.out.println("✅ 신체 부위별 통계 확인 완료");
        
        System.out.println("\n========================================");
        System.out.println("🎉 전체 시나리오 테스트 성공!");
        System.out.println("========================================");
    }
    
    @Test
    @DisplayName("여러 날 운동 기록 및 주간 통계 시나리오")
    void multiDayWorkoutScenario() throws Exception {
        System.out.println("\n===== 일주일 운동 기록 시나리오 =====");
        
        // 월요일: 가슴
        System.out.println("\n[월요일] 가슴 운동");
        Long mondaySession = createWorkoutSession(LocalDate.now().minusDays(6));
        addSet(mondaySession, 1L, 1, 10, new BigDecimal("80.0"));  // 벤치 프레스
        addSet(mondaySession, 1L, 2, 10, new BigDecimal("80.0"));
        addSet(mondaySession, 1L, 3, 10, new BigDecimal("80.0"));
        
        // 수요일: 등
        System.out.println("\n[수요일] 등 운동");
        Long wednesdaySession = createWorkoutSession(LocalDate.now().minusDays(4));
        addSet(wednesdaySession, 7L, 1, 5, new BigDecimal("100.0"));  // 데드리프트
        addSet(wednesdaySession, 7L, 2, 5, new BigDecimal("100.0"));
        addSet(wednesdaySession, 8L, 1, 10, null);  // 풀업
        
        // 금요일: 다리
        System.out.println("\n[금요일] 다리 운동");
        Long fridaySession = createWorkoutSession(LocalDate.now().minusDays(2));
        addSet(fridaySession, 14L, 1, 8, new BigDecimal("90.0"));  // 스쿼트
        addSet(fridaySession, 14L, 2, 8, new BigDecimal("90.0"));
        addSet(fridaySession, 14L, 3, 8, new BigDecimal("90.0"));
        
        // 주간 통계 확인
        System.out.println("\n===== 주간 통계 조회 =====");
        mockMvc.perform(get("/api/stats/weekly")
                .param("startDate", LocalDate.now().minusDays(7).toString())
                .param("endDate", LocalDate.now().toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalWorkouts").value(3))
                .andExpect(jsonPath("$.data.totalSets").value(9));
        
        // 신체 부위별 통계 확인
        System.out.println("\n===== 신체 부위별 통계 =====");
        mockMvc.perform(get("/api/stats/body-parts")
                .param("startDate", LocalDate.now().minusDays(7).toString())
                .param("endDate", LocalDate.now().toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3))  // 가슴, 등, 다리
                .andExpect(jsonPath("$.data[0].bodyPart").exists())
                .andExpect(jsonPath("$.data[0].totalSets").exists())
                .andExpect(jsonPath("$.data[0].percentage").exists());
        
        System.out.println("\n✅ 일주일 운동 기록 시나리오 완료");
    }
    
    /**
     * 헬퍼 메서드: 세트 추가
     */
    private void addSet(Long sessionId, Long exerciseTypeId, int setNumber, 
                        int reps, BigDecimal weight) throws Exception {
        AddWorkoutSetRequest request = new AddWorkoutSetRequest(
            exerciseTypeId, setNumber, reps, weight, true
        );
        
        mockMvc.perform(post("/api/workouts/" + sessionId + "/sets")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
    
    /**
     * 헬퍼 메서드: 운동 세션 생성
     */
    private Long createWorkoutSession(LocalDate date) throws Exception {
        CreateWorkoutSessionRequest request = new CreateWorkoutSessionRequest(
            null, date, 60, "운동 기록"
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
