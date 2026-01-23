package com.example.FitTracker.integration;

import com.example.FitTracker.dto.request.routine.CreateRoutineRequest;
import com.example.FitTracker.dto.request.routine.RoutineExerciseRequest;
import com.example.FitTracker.dto.request.routine.UpdateRoutineRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 루틴 API 통합 테스트
 */
@DisplayName("루틴 API 통합 테스트")
class RoutineIntegrationTest extends IntegrationTestBase {
    
    @Test
    @DisplayName("루틴 생성 성공")
    void createRoutine_success() throws Exception {
        // given
        List<RoutineExerciseRequest> exercises = Arrays.asList(
            new RoutineExerciseRequest(1L, 3, 10, new BigDecimal("80.0")),  // 벤치 프레스
            new RoutineExerciseRequest(2L, 3, 10, new BigDecimal("70.0"))   // 인클라인 벤치 프레스
        );
        
        CreateRoutineRequest request = new CreateRoutineRequest(
            "가슴 루틴 A",
            "월요일 가슴 운동 루틴",
            exercises
        );
        
        // when & then
        mockMvc.perform(post("/api/routines")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("루틴 생성 완료"))
                .andExpect(jsonPath("$.data.id").exists())
                .andExpect(jsonPath("$.data.name").value("가슴 루틴 A"))
                .andExpect(jsonPath("$.data.description").value("월요일 가슴 운동 루틴"))
                .andExpect(jsonPath("$.data.exercises").isArray())
                .andExpect(jsonPath("$.data.exercises.length()").value(2))
                .andExpect(jsonPath("$.data.exercises[0].exerciseName").value("벤치 프레스"))
                .andExpect(jsonPath("$.data.exercises[0].targetSets").value(3))
                .andExpect(jsonPath("$.data.exercises[0].targetReps").value(10))
                .andExpect(jsonPath("$.data.exercises[0].targetWeight").value(80.0));
    }
    
    @Test
    @DisplayName("루틴 생성 실패 - 필수 필드 누락")
    void createRoutine_fail_missingFields() throws Exception {
        // given - 이름 없음
        String requestJson = "{\"description\":\"설명만 있음\",\"exercises\":[]}";

        // when & then
        mockMvc.perform(post("/api/routines")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_FAILED"))
                .andExpect(jsonPath("$.validationErrors.name").exists());
    }
    
    @Test
    @DisplayName("루틴 생성 실패 - 존재하지 않는 운동 종목")
    void createRoutine_fail_exerciseNotFound() throws Exception {
        // given
        List<RoutineExerciseRequest> exercises = Arrays.asList(
            new RoutineExerciseRequest(999999L, 3, 10, new BigDecimal("80.0"))  // 존재하지 않는 ID
        );

        CreateRoutineRequest request = new CreateRoutineRequest(
            "테스트 루틴",
            "설명",
            exercises
        );

        // when & then
        mockMvc.perform(post("/api/routines")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").value("운동 종목을 찾을 수 없습니다: 999999"));
    }
    
    @Test
    @DisplayName("사용자의 루틴 목록 조회")
    void getUserRoutines_success() throws Exception {
        // given - 루틴 2개 생성
        createTestRoutine("루틴 1", "첫 번째 루틴");
        createTestRoutine("루틴 2", "두 번째 루틴");
        
        // when & then
        mockMvc.perform(get("/api/routines")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2))
                .andExpect(jsonPath("$.data[0].name").exists())
                .andExpect(jsonPath("$.data[0].exercises").isArray());
    }
    
    @Test
    @DisplayName("루틴 상세 조회 성공")
    void getRoutineById_success() throws Exception {
        // given
        Long routineId = createTestRoutine("상세 조회 테스트", "테스트용 루틴");
        
        // when & then
        mockMvc.perform(get("/api/routines/" + routineId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(routineId))
                .andExpect(jsonPath("$.data.name").value("상세 조회 테스트"))
                .andExpect(jsonPath("$.data.description").value("테스트용 루틴"))
                .andExpect(jsonPath("$.data.exercises").isArray());
    }
    
    @Test
    @DisplayName("루틴 상세 조회 실패 - 존재하지 않는 루틴")
    void getRoutineById_fail_notFound() throws Exception {
        // when & then
        mockMvc.perform(get("/api/routines/999999")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").value("루틴을 찾을 수 없습니다: 999999"));
    }
    
    @Test
    @DisplayName("루틴 수정 성공")
    void updateRoutine_success() throws Exception {
        // given
        Long routineId = createTestRoutine("수정 전 루틴", "수정 전 설명");
        
        List<RoutineExerciseRequest> newExercises = Arrays.asList(
            new RoutineExerciseRequest(7L, 5, 5, new BigDecimal("100.0"))  // 데드리프트
        );
        
        UpdateRoutineRequest updateRequest = new UpdateRoutineRequest(
            "수정 후 루틴",
            "수정 후 설명",
            newExercises
        );
        
        // when & then
        mockMvc.perform(put("/api/routines/" + routineId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("루틴 수정 완료"))
                .andExpect(jsonPath("$.data.name").value("수정 후 루틴"))
                .andExpect(jsonPath("$.data.description").value("수정 후 설명"))
                .andExpect(jsonPath("$.data.exercises.length()").value(1))
                .andExpect(jsonPath("$.data.exercises[0].exerciseName").value("데드리프트"));
    }
    
    @Test
    @DisplayName("루틴 수정 실패 - 존재하지 않는 루틴")
    void updateRoutine_fail_notFound() throws Exception {
        // given
        UpdateRoutineRequest updateRequest = new UpdateRoutineRequest(
            "수정 루틴",
            "설명",
            Arrays.asList()
        );

        // when & then
        mockMvc.perform(put("/api/routines/999999")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
    
    @Test
    @DisplayName("루틴 삭제 성공")
    void deleteRoutine_success() throws Exception {
        // given
        Long routineId = createTestRoutine("삭제할 루틴", "삭제 테스트");
        
        // when & then
        mockMvc.perform(delete("/api/routines/" + routineId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("루틴 삭제 완료"));
        
        // 삭제 확인
        mockMvc.perform(get("/api/routines/" + routineId)
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    @DisplayName("루틴 삭제 실패 - 존재하지 않는 루틴")
    void deleteRoutine_fail_notFound() throws Exception {
        // when & then
        mockMvc.perform(delete("/api/routines/999999")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }
    
    /**
     * 테스트용 루틴 생성 헬퍼 메서드
     */
    private Long createTestRoutine(String name, String description) throws Exception {
        List<RoutineExerciseRequest> exercises = Arrays.asList(
            new RoutineExerciseRequest(1L, 3, 10, new BigDecimal("80.0"))
        );
        
        CreateRoutineRequest request = new CreateRoutineRequest(name, description, exercises);
        
        MvcResult result = mockMvc.perform(post("/api/routines")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        return objectMapper.readTree(response).get("data").get("id").asLong();
    }
}
