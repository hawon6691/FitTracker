package com.example.FitTracker.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI openAPI() {
        Server localServer = new Server();
        localServer.setUrl("http://localhost:8080");
        localServer.setDescription("로컬 개발 서버");
        
        Server prodServer = new Server();
        prodServer.setUrl("https://api.fittracker.com");
        prodServer.setDescription("프로덕션 서버");
        
        return new OpenAPI()
                .servers(List.of(localServer, prodServer))
                .info(new Info()
                        .title("FitTracker API")
                        .description("운동 루틴 트래커 REST API 문서\n\n" +
                                "## 인증\n" +
                                "대부분의 API는 JWT 토큰 인증이 필요합니다.\n" +
                                "1. `/api/auth/signup` 또는 `/api/auth/login`으로 토큰 발급\n" +
                                "2. 우측 상단 'Authorize' 버튼 클릭\n" +
                                "3. Bearer {token} 형식으로 입력\n\n" +
                                "## 주요 기능\n" +
                                "- 회원가입 및 로그인\n" +
                                "- 운동 루틴 관리\n" +
                                "- 운동 기록 추적\n" +
                                "- 통계 및 개인 기록 조회")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("FitTracker Team")
                                .email("support@fittracker.com")
                                .url("https://fittracker.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT 토큰을 입력하세요 (Bearer 제외)")));
    }
}