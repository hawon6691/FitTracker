package com.example.FitTracker.service;

import com.example.FitTracker.domain.User;
import com.example.FitTracker.dto.request.auth.SignupRequest;
import com.example.FitTracker.dto.response.auth.AuthResponse;
import com.example.FitTracker.exception.DuplicateEmailException;
import com.example.FitTracker.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService 단위 테스트")
class UserServiceTest {
    
    @Mock
    private UserRepository userRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private UserService userService;
    
    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // given
        SignupRequest request = new SignupRequest(
            "test@test.com",
            "password123",
            "테스트유저"
        );
        
        User savedUser = User.builder()
            .id(1L)
            .email(request.getEmail())
            .password("encoded_password")
            .name(request.getName())
            .build();
        
        given(userRepository.existsByEmail(request.getEmail())).willReturn(false);
        given(passwordEncoder.encode(request.getPassword())).willReturn("encoded_password");
        given(userRepository.save(any(User.class))).willReturn(savedUser);
        
        // when
        AuthResponse response = userService.signup(request);
        
        // then
        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("test@test.com");
        assertThat(response.getName()).isEqualTo("테스트유저");
        assertThat(response.getUserId()).isEqualTo(1L);
        
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }
    
    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signup_fail_duplicateEmail() {
        // given
        SignupRequest request = new SignupRequest(
            "duplicate@test.com",
            "password123",
            "중복유저"
        );
        
        given(userRepository.existsByEmail(request.getEmail())).willReturn(true);
        
        // when & then
        assertThatThrownBy(() -> userService.signup(request))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining("이미 사용 중인 이메일입니다");
        
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(userRepository, never()).save(any(User.class));
    }
    
    @Test
    @DisplayName("ID로 사용자 조회 성공")
    void findById_success() {
        // given
        Long userId = 1L;
        User user = User.builder()
            .id(userId)
            .email("test@test.com")
            .name("테스트유저")
            .build();
        
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        
        // when
        User foundUser = userService.findById(userId);
        
        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getId()).isEqualTo(userId);
        assertThat(foundUser.getEmail()).isEqualTo("test@test.com");
    }
    
    @Test
    @DisplayName("ID로 사용자 조회 실패 - 존재하지 않는 사용자")
    void findById_fail_notFound() {
        // given
        Long userId = 999L;
        given(userRepository.findById(userId)).willReturn(Optional.empty());
        
        // when & then
        assertThatThrownBy(() -> userService.findById(userId))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("사용자를 찾을 수 없습니다");
    }
    
    @Test
    @DisplayName("이메일로 사용자 조회 성공")
    void findByEmail_success() {
        // given
        String email = "test@test.com";
        User user = User.builder()
            .id(1L)
            .email(email)
            .name("테스트유저")
            .build();
        
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));
        
        // when
        User foundUser = userService.findByEmail(email);
        
        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getEmail()).isEqualTo(email);
    }
}