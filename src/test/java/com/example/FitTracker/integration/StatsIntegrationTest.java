package com.example.FitTracker.integration;

import com.example.FitTracker.dto.request.workout.AddWorkoutSetRequest;
import com.example.FitTracker.dto.request.workout.CreateWorkoutSessionRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 통계 API 통합 테스트
 */
@DisplayName("통계 API 통합 테스트")
class StatsIntegrationTest extends IntegrationTestBase {
    
    /**
     * 테스트 데이터 준비
     * 부모 클래스의 setUpMockMvc가 먼저 실행되고 사용자가 생성된 후 이 메서드가 실행됩니다.
     */
    @BeforeEach
    public void prepareTestData() throws Exception {
        // 지난 주 운동 기록 생성 (5일)
        for (int i = 7; i >= 3; i--) {
            Long sessionId = createWorkoutSessionWithDate(LocalDate.now().minusDays(i));
            
            // 가슴 운동 (벤치 프레스)
            addWorkoutSet(sessionId, 1L, 1, 10, new BigDecimal("80.0"));
            addWorkoutSet(sessionId, 1L, 2, 10, new BigDecimal("80.0"));
            addWorkoutSet(sessionId, 1L, 3, 10, new BigDecimal("80.0"));
        }
        
        // 이번 주 운동 기록 생성 (2일)
        for (int i = 2; i >= 1; i--) {
            Long sessionId = createWorkoutSessionWithDate(LocalDate.now().minusDays(i));
            
            // 등 운동 (데드리프트)
            addWorkoutSet(sessionId, 7L, 1, 5, new BigDecimal("100.0"));
            addWorkoutSet(sessionId, 7L, 2, 5, new BigDecimal("100.0"));
            
            // 다리 운동 (스쿼트)
            addWorkoutSet(sessionId, 14L, 1, 8, new BigDecimal("90.0"));
            addWorkoutSet(sessionId, 14L, 2, 8, new BigDecimal("90.0"));
        }
        
        // 오늘 운동 기록
        Long todaySession = createWorkoutSessionWithDate(LocalDate.now());
        addWorkoutSet(todaySession, 1L, 1, 12, new BigDecimal("85.0"));  // 벤치 프레스
        addWorkoutSet(todaySession, 1L, 2, 12, new BigDecimal("85.0"));
        addWorkoutSet(todaySession, 1L, 3, 10, new BigDecimal("90.0"));  // 더 무거운 무게
    }
    
    @Test
    @DisplayName("주간 통계 조회 성공")
    void getWeeklyStats_success() throws Exception {
        // given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        // when & then
        mockMvc.perform(get("/api/stats/weekly")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.startDate").value(startDate.toString()))
                .andExpect(jsonPath("$.data.endDate").value(endDate.toString()))
                .andExpect(jsonPath("$.data.totalWorkouts").exists())
                .andExpect(jsonPath("$.data.totalSets").exists())
                .andExpect(jsonPath("$.data.totalMinutes").exists())
                .andExpect(jsonPath("$.data.avgWorkoutsPerDay").exists());
    }
    
    @Test
    @DisplayName("월간 통계 조회 성공")
    void getMonthlyStats_success() throws Exception {
        // given
        YearMonth yearMonth = YearMonth.now();
        
        // when & then
        mockMvc.perform(get("/api/stats/monthly")
                .param("yearMonth", yearMonth.toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.yearMonth").value(yearMonth.toString()))
                .andExpect(jsonPath("$.data.totalWorkouts").exists())
                .andExpect(jsonPath("$.data.totalSets").exists())
                .andExpect(jsonPath("$.data.totalMinutes").exists())
                .andExpect(jsonPath("$.data.avgWorkoutsPerWeek").exists())
                .andExpect(jsonPath("$.data.totalDaysWorkedOut").exists());
    }
    
    @Test
    @DisplayName("신체 부위별 통계 조회 성공")
    void getBodyPartStats_success() throws Exception {
        // given
        LocalDate startDate = LocalDate.now().minusDays(7);
        LocalDate endDate = LocalDate.now();
        
        // when & then
        mockMvc.perform(get("/api/stats/body-parts")
                .param("startDate", startDate.toString())
                .param("endDate", endDate.toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))  // 가슴, 등, 다리
                .andExpect(jsonPath("$.data[0].bodyPart").exists())
                .andExpect(jsonPath("$.data[0].totalSets").exists())
                .andExpect(jsonPath("$.data[0].percentage").exists());
    }
    
    @Test
    @DisplayName("개인 기록 전체 조회 성공")
    void getPersonalRecords_success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/stats/personal-records")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(3))  // 벤치프레스, 데드리프트, 스쿼트
                .andExpect(jsonPath("$.data[0].exerciseTypeId").exists())
                .andExpect(jsonPath("$.data[0].exerciseName").exists())
                .andExpect(jsonPath("$.data[0].bodyPart").exists())
                .andExpect(jsonPath("$.data[0].maxWeight").exists())
                .andExpect(jsonPath("$.data[0].repsAtMaxWeight").exists())
                .andExpect(jsonPath("$.data[0].achievedDate").exists())
                .andExpect(jsonPath("$.data[0].oneRepMax").exists());
    }
    
    @Test
    @DisplayName("특정 운동의 개인 기록 조회 성공")
    void getPersonalRecordByExercise_success() throws Exception {
        // when & then - 벤치 프레스 개인 기록
        mockMvc.perform(get("/api/stats/personal-records/1")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.exerciseTypeId").value(1))
                .andExpect(jsonPath("$.data.exerciseName").value("벤치 프레스"))
                .andExpect(jsonPath("$.data.maxWeight").value(90.0))  // 오늘 기록한 90kg
                .andExpect(jsonPath("$.data.repsAtMaxWeight").value(10))
                .andExpect(jsonPath("$.data.oneRepMax").exists());
    }
    
    @Test
    @DisplayName("특정 운동의 개인 기록 조회 실패 - 기록 없음")
    void getPersonalRecordByExercise_fail_noRecords() throws Exception {
        // when & then - 기록이 없는 운동
        mockMvc.perform(get("/api/stats/personal-records/2")  // 인클라인 벤치 프레스
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("해당 운동의 기록이 없습니다"));
    }
    
    @Test
    @DisplayName("1RM 계산 검증")
    void oneRepMaxCalculation_verification() throws Exception {
        // 벤치 프레스 90kg × 10회의 1RM은 약 120kg
        // Brzycki 공식: 90 × (36 / (37 - 10)) = 90 × 1.33 = 119.7kg
        
        mockMvc.perform(get("/api/stats/personal-records/1")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.maxWeight").value(90.0))
                .andExpect(jsonPath("$.data.repsAtMaxWeight").value(10))
                .andExpect(jsonPath("$.data.oneRepMax").value(119.7));  // 119.7kg
    }
    
    @Test
    @DisplayName("통계 조회 실패 - 필수 파라미터 누락")
    void getWeeklyStats_fail_missingParams() throws Exception {
        // when & then - endDate 없음
        mockMvc.perform(get("/api/stats/weekly")
                .param("startDate", LocalDate.now().toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("빈 기간의 통계 조회")
    void getWeeklyStats_emptyPeriod() throws Exception {
        // given - 미래 날짜 (데이터 없음)
        LocalDate futureStart = LocalDate.now().plusDays(10);
        LocalDate futureEnd = LocalDate.now().plusDays(17);
        
        // when & then
        mockMvc.perform(get("/api/stats/weekly")
                .param("startDate", futureStart.toString())
                .param("endDate", futureEnd.toString())
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalWorkouts").value(0))
                .andExpect(jsonPath("$.data.totalSets").value(0))
                .andExpect(jsonPath("$.data.totalMinutes").value(0));
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
    
    /**
     * 세트 추가
     */
    private void addWorkoutSet(Long sessionId, Long exerciseTypeId, int setNumber, 
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
}
