package com.example.FitTracker.controller;

import com.example.FitTracker.dto.request.auth.LoginRequest;
import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.service.AuthService;
import com.example.FitTracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입 및 로그인 API")
public class AuthController {
    
    private final UserService userService;
    private final AuthService authService;
    
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = userService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입 성공", response));
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하고 JWT 토큰을 받습니다")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("로그인 성공", response));
    }
}