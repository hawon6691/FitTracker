package com.example.FitTracker.controller;

import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.auth.AuthResponse;
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
    
    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = userService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입 성공", response));
    }
    
    @PostMapping("/login")
    @Operation(summary = "로그인", description = "사용자 로그인 (추후 구현)")
    public ResponseEntity<ApiResponse<String>> login() {
        return ResponseEntity.ok(ApiResponse.success("로그인 기능은 추후 구현 예정"));
    }
}