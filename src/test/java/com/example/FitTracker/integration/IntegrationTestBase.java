package com.example.FitTracker.integration;

import com.example.FitTracker.config.TestSecurityConfig;
import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
public abstract class IntegrationTestBase {

    @Autowired
    protected WebApplicationContext context;

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String accessToken;
    protected Long userId;

    @BeforeEach
    public void setUpMockMvc() throws Exception {
        // MockMvc 설정
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        // AuthIntegrationTest가 아닌 경우에만 자동으로 테스트 사용자 생성
        if (!this.getClass().getSimpleName().equals("AuthIntegrationTest")) {
            createTestUser();
        }
    }

    /**
     * 테스트용 사용자를 생성하고 인증 토큰을 받습니다.
     * 인증이 필요한 테스트에서 호출하세요.
     */
    protected void createTestUser() throws Exception {
        String uniqueEmail = "test_" + System.currentTimeMillis() + "@test.com";

        SignupRequest signupRequest = new SignupRequest(
            uniqueEmail,
            "Password123!",
            "테스트유저"
        );

        MvcResult signupResult = mockMvc.perform(post("/api/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String signupResponse = signupResult.getResponse().getContentAsString();
        // JSON 파싱 using ObjectMapper
        try {
            var jsonNode = objectMapper.readTree(signupResponse);
            var dataNode = jsonNode.get("data");

            this.accessToken = dataNode.get("token").asText();
            this.userId = dataNode.get("userId").asLong();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse signup response: " + signupResponse, e);
        }
    }
    
    protected String getAuthorizationHeader() {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalStateException("Access token is null or empty!");
        }
        return "Bearer " + accessToken;
    }
}