package com.example.FitTracker.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class AuthResponse {
    
    private String token;
    private String type;  // "Bearer"
    private Long userId;
    private String email;
    private String name;
    
    public static AuthResponse of(String token, Long userId, String email, String name) {
        return AuthResponse.builder()
                .token(token)
                .type("Bearer")
                .userId(userId)
                .email(email)
                .name(name)
                .build();
    }
}