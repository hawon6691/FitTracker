package com.example.FitTracker.controller;

import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserService userService;
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(@Valid @RequestBody SignupRequest request) {
        AuthResponse response = userService.signup(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입 성공", response));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login() {
        // JWT 구현 전까지 임시 응답
        return ResponseEntity.ok(ApiResponse.success("로그인 기능은 추후 구현 예정"));
    }
}