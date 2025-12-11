package com.example.FitTracker.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 운동 종목 API 통합 테스트
 */
@DisplayName("운동 종목 API 통합 테스트")
class ExerciseTypeIntegrationTest extends IntegrationTestBase {
    
    @Test
    @DisplayName("전체 운동 종목 조회 성공")
    void getAllExercises_success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(48))  // 48개의 운동 종목
                .andExpect(jsonPath("$.data[0].id").exists())
                .andExpect(jsonPath("$.data[0].name").exists())
                .andExpect(jsonPath("$.data[0].bodyPart").exists())
                .andExpect(jsonPath("$.data[0].description").exists());
    }
    
    @Test
    @DisplayName("신체 부위별 운동 종목 조회 - 가슴")
    void getExercisesByBodyPart_chest() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .param("bodyPart", "가슴")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(6))  // 가슴 운동 6개
                .andExpect(jsonPath("$.data[0].bodyPart").value("가슴"))
                .andExpect(jsonPath("$.data[0].name").exists());
    }
    
    @Test
    @DisplayName("신체 부위별 운동 종목 조회 - 등")
    void getExercisesByBodyPart_back() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .param("bodyPart", "등")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(7));  // 등 운동 7개
    }
    
    @Test
    @DisplayName("신체 부위별 운동 종목 조회 - 다리")
    void getExercisesByBodyPart_legs() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .param("bodyPart", "다리")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(7));  // 다리 운동 7개
    }
    
    @Test
    @DisplayName("신체 부위별 운동 종목 조회 - 어깨")
    void getExercisesByBodyPart_shoulders() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .param("bodyPart", "어깨")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(7));  // 어깨 운동 7개
    }
    
    @Test
    @DisplayName("신체 부위별 운동 종목 조회 - 팔")
    void getExercisesByBodyPart_arms() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .param("bodyPart", "팔")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(9));  // 팔 운동 9개
    }
    
    @Test
    @DisplayName("신체 부위별 운동 종목 조회 - 복근")
    void getExercisesByBodyPart_abs() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .param("bodyPart", "복근")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.length()").value(8));  // 복근 운동 8개
    }
    
    @Test
    @DisplayName("존재하지 않는 신체 부위 조회")
    void getExercisesByBodyPart_notFound() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .param("bodyPart", "존재하지않음")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(0));  // 빈 배열
    }
    
    @Test
    @DisplayName("특정 운동 종목 상세 조회 성공")
    void getExerciseById_success() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises/1")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.name").value("벤치 프레스"))
                .andExpect(jsonPath("$.data.bodyPart").value("가슴"))
                .andExpect(jsonPath("$.data.description").exists());
    }
    
    @Test
    @DisplayName("존재하지 않는 운동 종목 조회")
    void getExerciseById_notFound() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises/999999")
                .header("Authorization", getAuthorizationHeader())
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("운동 종목을 찾을 수 없습니다: 999999"));
    }
    
    @Test
    @DisplayName("인증 없이 운동 종목 조회 실패")
    void getAllExercises_unauthorized() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());  // 403 Forbidden
    }
    
    @Test
    @DisplayName("잘못된 토큰으로 운동 종목 조회 실패")
    void getAllExercises_invalidToken() throws Exception {
        // when & then
        mockMvc.perform(get("/api/exercises")
                .header("Authorization", "Bearer invalid-token")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isForbidden());  // 403 Forbidden
    }
}
