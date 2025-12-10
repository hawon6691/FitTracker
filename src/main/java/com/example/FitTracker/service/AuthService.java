package com.example.FitTracker.service;

import com.example.FitTracker.dto.request.auth.LoginRequest;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserService userService;
    
    public AuthResponse login(LoginRequest request) {
        // 1. 인증
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        
        // 2. SecurityContext에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. JWT 토큰 생성
        String jwt = tokenProvider.generateToken(authentication);
        
        // 4. 사용자 정보 조회
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        var user = userService.findByEmail(userDetails.getUsername());
        
        // 5. 응답 생성
        return AuthResponse.of(jwt, user.getId(), user.getEmail(), user.getName());
    }
}