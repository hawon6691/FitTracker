package com.example.FitTracker.integration;

import com.example.FitTracker.dto.request.auth.LoginRequest;
import com.example.FitTracker.dto.request.auth.SignupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 인증 API 통합 테스트
 */
@DisplayName("인증 API 통합 테스트")
class AuthIntegrationTest extends IntegrationTestBase {
    
    @Test
    @DisplayName("회원가입 성공")
    void signup_success() throws Exception {
        // given
        String uniqueEmail = "new_user_" + System.currentTimeMillis() + "@test.com";
        SignupRequest request = new SignupRequest(
            uniqueEmail,
            "password123",
            "신규유저"
        );
        
        // when & then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("회원가입 성공"))
                .andExpect(jsonPath("$.data.email").value(uniqueEmail))
                .andExpect(jsonPath("$.data.name").value("신규유저"))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.userId").exists());
    }
    
    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signup_fail_duplicateEmail() throws Exception {
        // given
        String duplicateEmail = "duplicate_" + System.currentTimeMillis() + "@test.com";
        SignupRequest request1 = new SignupRequest(
            duplicateEmail,
            "password123",
            "유저1"
        );
        
        // 첫 번째 회원가입
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
                .andExpect(status().isCreated());
        
        // 같은 이메일로 두 번째 회원가입 시도
        SignupRequest request2 = new SignupRequest(
            duplicateEmail,
            "password456",
            "유저2"
        );
        
        // when & then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("이미 사용 중인 이메일입니다: " + duplicateEmail));
    }
    
    @Test
    @DisplayName("회원가입 실패 - 유효성 검증 (짧은 비밀번호)")
    void signup_fail_shortPassword() throws Exception {
        // given
        SignupRequest request = new SignupRequest(
            "short_" + System.currentTimeMillis() + "@test.com",
            "short",  // 8자 미만
            "유저"
        );
        
        // when & then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("입력값 검증 실패"))
                .andExpect(jsonPath("$.data.password").exists());
    }
    
    @Test
    @DisplayName("회원가입 실패 - 유효성 검증 (잘못된 이메일 형식)")
    void signup_fail_invalidEmail() throws Exception {
        // given
        SignupRequest request = new SignupRequest(
            "invalid-email",  // @ 없는 이메일
            "password123",
            "유저"
        );
        
        // when & then
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.email").exists());
    }
    
    @Test
    @DisplayName("로그인 성공")
    void login_success() throws Exception {
        // given
        String email = "login_test_" + System.currentTimeMillis() + "@test.com";
        String password = "password123";
        
        // 회원가입 먼저
        SignupRequest signupRequest = new SignupRequest(email, password, "로그인테스트");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());
        
        // 로그인
        LoginRequest loginRequest = new LoginRequest(email, password);
        
        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andExpect(jsonPath("$.data.email").value(email))
                .andExpect(jsonPath("$.data.token").exists())
                .andExpect(jsonPath("$.data.type").value("Bearer"));
    }
    
    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 이메일")
    void login_fail_userNotFound() throws Exception {
        // given
        LoginRequest request = new LoginRequest(
            "nonexistent_" + System.currentTimeMillis() + "@test.com",
            "password123"
        );

        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isUnauthorized());  // 인증 실패는 401로 처리됨
    }
    
    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrongPassword() throws Exception {
        // given
        String email = "wrong_pw_" + System.currentTimeMillis() + "@test.com";

        // 회원가입
        SignupRequest signupRequest = new SignupRequest(email, "correctPassword", "유저");
        mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated());

        // 잘못된 비밀번호로 로그인 시도
        LoginRequest loginRequest = new LoginRequest(email, "wrongPassword");

        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());  // 인증 실패는 401로 처리됨
    }
    
    @Test
    @DisplayName("로그인 실패 - 필수 필드 누락")
    void login_fail_missingFields() throws Exception {
        // given - 비밀번호 없음
        String requestJson = "{\"email\":\"test@test.com\"}";
        
        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data.password").exists());
    }
}
