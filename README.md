# 🏋️ FitTracker - 운동 루틴 트래커

## 📋 프로젝트 소개
개인 운동 루틴을 관리하고 운동 기록을 추적하는 REST API 서버

## 🛠️ 기술 스택
- Java 21
- Spring Boot 4.0.0
- Spring Data JPA
- MySQL 8.x
- Spring Security + JWT
- Swagger (OpenAPI 3.0)

## 📦 주요 기능
- 회원가입/로그인 (JWT 인증)
- 운동 루틴 CRUD
- 운동 기록 관리
- 통계 및 분석

## 🚀 실행 방법
```bash
./gradlew bootRun
```

## 📚 API 문서
http://localhost:8080/swagger-ui.html

## 📁 프로젝트 구조
```
src/main/java/com/example/FitTracker/
├── config/          # 설정
├── controller/      # REST API
├── domain/          # Entity
├── dto/             # 요청/응답 DTO
├── repository/      # JPA Repository
├── service/         # 비즈니스 로직
└── exception/       # 예외 처리
```