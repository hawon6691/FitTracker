package com.example.FitTracker.controller;

import com.example.FitTracker.domain.RefreshToken;
import com.example.FitTracker.dto.request.auth.LoginRequest;
import com.example.FitTracker.dto.request.auth.RefreshTokenRequest;
import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.dto.response.auth.TokenRefreshResponse;
import com.example.FitTracker.service.AuthService;
import com.example.FitTracker.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "인증", description = "회원가입 및 로그인 API")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다. 이메일은 고유해야 하며, 비밀번호는 8자 이상이어야 합니다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "회원가입 성공", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"success\":true,\"message\":\"회원가입 성공\","
                    +
                    "\"data\":{\"token\":\"eyJhbGc...\",\"type\":\"Bearer\"," +
                    "\"userId\":1,\"email\":\"test@test.com\",\"name\":\"홍길동\"}}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "이메일 중복", content = @Content(examples = @ExampleObject(value = "{\"success\":false,\"message\":\"이미 사용 중인 이메일입니다: test@test.com\"}"))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "입력값 검증 실패")
    })
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

    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "Refresh token으로 새로운 access token 발급")
    public ResponseEntity<ApiResponse<TokenRefreshResponse>> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {

        RefreshToken refreshToken = refreshTokenService.verifyRefreshToken(request.getRefreshToken());
        User user = refreshToken.getUser();

        // 새로운 access token 생성
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());
        String newAccessToken = jwtTokenProvider.generateToken(authentication);

        // 새로운 refresh token 생성
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        TokenRefreshResponse response = TokenRefreshResponse.of(
                newAccessToken, newRefreshToken.getToken());

        return ResponseEntity.ok(ApiResponse.success("토큰 갱신 완료", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "Refresh token 삭제")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request) {
        refreshTokenService.deleteByToken(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success("로그아웃 완료", null));
    }
}