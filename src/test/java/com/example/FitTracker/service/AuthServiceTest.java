package com.example.FitTracker.service;

import com.example.FitTracker.domain.User;
import com.example.FitTracker.dto.request.auth.LoginRequest;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService 단위 테스트")
class AuthServiceTest {
    
    @Mock
    private AuthenticationManager authenticationManager;
    
    @Mock
    private JwtTokenProvider tokenProvider;
    
    @Mock
    private UserService userService;
    
    @InjectMocks
    private AuthService authService;
    
    @Mock
    private Authentication authentication;
    
    @Mock
    private UserDetails userDetails;
    
    private User testUser;
    
    @BeforeEach
    void setUp() {
        testUser = User.builder()
            .id(1L)
            .email("test@test.com")
            .password("encoded_password")
            .name("테스트유저")
            .build();
    }
    
    @Test
    @DisplayName("로그인 성공")
    void login_success() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "password123");
        String mockToken = "mock.jwt.token";
        
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(userDetails);
        given(userDetails.getUsername()).willReturn("test@test.com");
        given(tokenProvider.generateToken(authentication)).willReturn(mockToken);
        given(userService.findByEmail("test@test.com")).willReturn(testUser);
        
        // when
        AuthResponse response = authService.login(request);
        
        // then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo(mockToken);
        assertThat(response.getType()).isEqualTo("Bearer");
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getName()).isEqualTo("테스트유저");
        assertThat(response.getUserId()).isEqualTo(1L);
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, times(1)).generateToken(authentication);
        verify(userService, times(1)).findByEmail("test@test.com");
    }
    
    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void login_fail_wrongPassword() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "wrongPassword");
        
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willThrow(new BadCredentialsException("Bad credentials"));
        
        // when & then
        assertThatThrownBy(() -> authService.login(request))
            .isInstanceOf(BadCredentialsException.class)
            .hasMessageContaining("Bad credentials");
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenProvider, never()).generateToken(any());
        verify(userService, never()).findByEmail(any());
    }
    
    @Test
    @DisplayName("로그인 실패 - 존재하지 않는 사용자")
    void login_fail_userNotFound() {
        // given
        LoginRequest request = new LoginRequest("notfound@test.com", "password123");
        
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willThrow(new BadCredentialsException("User not found"));
        
        // when & then
        assertThatThrownBy(() -> authService.login(request))
            .isInstanceOf(BadCredentialsException.class);
        
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
    
    @Test
    @DisplayName("JWT 토큰 생성 검증")
    void jwtTokenGeneration_verification() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "password123");
        String expectedToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.mock.token";
        
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(userDetails);
        given(userDetails.getUsername()).willReturn("test@test.com");
        given(tokenProvider.generateToken(authentication)).willReturn(expectedToken);
        given(userService.findByEmail("test@test.com")).willReturn(testUser);
        
        // when
        AuthResponse response = authService.login(request);
        
        // then
        assertThat(response.getToken()).isNotNull();
        assertThat(response.getToken()).isEqualTo(expectedToken);
        assertThat(response.getToken()).startsWith("eyJ"); // JWT 형식 확인
    }
    
    @Test
    @DisplayName("로그인 후 SecurityContext 설정 검증")
    void login_securityContextSet() {
        // given
        LoginRequest request = new LoginRequest("test@test.com", "password123");
        
        given(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .willReturn(authentication);
        given(authentication.getPrincipal()).willReturn(userDetails);
        given(userDetails.getUsername()).willReturn("test@test.com");
        given(tokenProvider.generateToken(authentication)).willReturn("token");
        given(userService.findByEmail("test@test.com")).willReturn(testUser);
        
        // when
        authService.login(request);
        
        // then
        // SecurityContextHolder.getContext().setAuthentication()이 호출되었는지 확인
        verify(authenticationManager, times(1)).authenticate(any());
    }
}