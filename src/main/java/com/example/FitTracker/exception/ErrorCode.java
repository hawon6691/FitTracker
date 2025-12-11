package com.example.FitTracker.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    // 인증 관련
    INVALID_CREDENTIALS(401, "AUTH001", "이메일 또는 비밀번호가 올바르지 않습니다"),
    INVALID_TOKEN(401, "AUTH002", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN(401, "AUTH003", "만료된 토큰입니다"),
    UNAUTHORIZED(401, "AUTH004", "인증이 필요합니다"),
    
    // 사용자 관련
    DUPLICATE_EMAIL(409, "USER001", "이미 사용 중인 이메일입니다"),
    USER_NOT_FOUND(404, "USER002", "사용자를 찾을 수 없습니다"),
    
    // 루틴 관련
    ROUTINE_NOT_FOUND(404, "ROUTINE001", "루틴을 찾을 수 없습니다"),
    ROUTINE_ACCESS_DENIED(403, "ROUTINE002", "루틴에 대한 접근 권한이 없습니다"),
    
    // 운동 기록 관련
    WORKOUT_SESSION_NOT_FOUND(404, "WORKOUT001", "운동 세션을 찾을 수 없습니다"),
    EXERCISE_TYPE_NOT_FOUND(404, "WORKOUT002", "운동 종목을 찾을 수 없습니다"),
    
    // 일반
    INVALID_INPUT(400, "COMMON001", "입력값이 올바르지 않습니다"),
    INTERNAL_SERVER_ERROR(500, "COMMON002", "서버 오류가 발생했습니다");
    
    private final int status;
    private final String code;
    private final String message;
    
    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}