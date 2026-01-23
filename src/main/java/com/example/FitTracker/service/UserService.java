package com.example.FitTracker.service;

import com.example.FitTracker.domain.User;
import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.exception.DuplicateEmailException;
import com.example.FitTracker.repository.UserRepository;
import com.example.FitTracker.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Transactional
    public AuthResponse signup(SignupRequest request) {
        // 이메일 중복 확인
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다: " + request.getEmail());
        }
        
        // 사용자 생성
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();
        
        User savedUser = userRepository.save(user);

        // JWT 토큰 생성
        String token = jwtTokenProvider.generateTokenFromEmail(savedUser.getEmail());

        return AuthResponse.of(
                token,
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getName()
        );
    }
    
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + userId));
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다: " + email));
    }
}