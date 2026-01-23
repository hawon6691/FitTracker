package com.example.FitTracker.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String field, Object value) {
        super(String.format("%s을(를) 찾을 수 없습니다: %s = %s", resource, field, value));
    }
}