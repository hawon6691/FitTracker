package com.example.FitTracker;

import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.exception.DuplicateEmailException;
import com.example.FitTracker.repository.UserRepository;
import com.example.FitTracker.service.UserService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private UserRepository userRepository;
    
    @Test
    void 회원가입_성공() {
        // given
        SignupRequest request = new SignupRequest(
            "test@test.com",
            "password123",
            "테스트유저"
        );
        
        // when
        AuthResponse response = userService.signup(request);
        
        // then
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getName()).isEqualTo("테스트유저");
    }
    
    @Test
    void 회원가입_이메일중복_실패() {
        // given
        SignupRequest request1 = new SignupRequest(
            "test@test.com",
            "password123",
            "테스트유저1"
        );
        userService.signup(request1);
        
        SignupRequest request2 = new SignupRequest(
            "test@test.com",
            "password456",
            "테스트유저2"
        );
        
        // when & then
        assertThatThrownBy(() -> userService.signup(request2))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining("이미 사용 중인 이메일입니다");
    }
}