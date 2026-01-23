# FitTracker - 운동 루틴 추적 REST API

> 개인 운동 루틴을 관리하고 운동 기록을 추적하는 백엔드 API 서버
> JWT 인증, JPA 기반 데이터 관리, 통계 분석 기능 제공

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![MySQL](https://img.shields.io/badge/MySQL-8.x-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## 목차
- [프로젝트 개요](#프로젝트-개요)
- [핵심 기술 스택](#핵심-기술-스택)
- [주요 기능](#주요-기능)
- [아키텍처](#아키텍처)
- [ERD](#erd)
- [API 문서](#api-문서)
- [설치 및 실행](#설치-및-실행)
- [개발 포인트](#개발-포인트)
- [성능 및 최적화](#성능-및-최적화)
- [향후 계획](#향후-계획)

---

## 프로젝트 개요

### 프로젝트 목적
운동 애호가들이 자신의 운동 루틴을 체계적으로 관리하고, 운동 기록을 추적하며, 진척도를 분석할 수 있는 백엔드 API 서비스입니다.

### 문제 정의 및 해결
**문제**: 기존 운동 기록 앱들은 데이터 접근성이 낮고, 개인화된 분석 기능이 부족합니다.

**해결**:
- REST API 방식으로 다양한 클라이언트(웹, 모바일)에서 접근 가능
- JWT 기반 인증으로 안전한 개인 데이터 관리
- 통계 및 분석 기능으로 운동 패턴 시각화
- 1RM 계산 등 과학적 지표 제공

### 개발 기간 및 인원
- **기간**: 2025.01 ~ 2025.01 (1개월)
- **인원**: 1명 (백엔드 개발)
- **역할**: 설계, 개발, 테스트

---

## 핵심 기술 스택

### Backend
- **Java 21** - 최신 LTS 버전, Virtual Threads, Record 등 활용
- **Spring Boot 4.0.0** - 최신 스프링 프레임워크
- **Spring Data JPA** - Hibernate 기반 ORM
- **Spring Security 6** - JWT 토큰 기반 인증/인가

### Database
- **MySQL 8.x** - 운영 데이터베이스
- **HikariCP** - 커넥션 풀 관리
- **JPA Auditing** - 생성/수정 시간 자동 관리

### Documentation
- **SpringDoc OpenAPI 3** - Swagger UI 자동 생성
- **실시간 API 문서** - http://localhost:8080/swagger-ui.html

### Security
- **JWT (JSON Web Token)** - Stateless 인증
- **BCrypt** - 비밀번호 암호화
- **Access Token** - 1시간 유효
- **Refresh Token** - 7일 유효

### Build & DevOps
- **Gradle 8.x** - 빌드 자동화
- **Lombok** - 보일러플레이트 코드 제거
- **SLF4J + Logback** - 로깅

---

## 주요 기능

### 1. 인증 및 회원 관리
- [x] JWT 기반 회원가입/로그인
- [x] Access/Refresh Token 발급
- [x] 이메일 중복 검증
- [x] BCrypt 비밀번호 암호화
- [x] 토큰 갱신 (Refresh)
- [x] 로그아웃 (토큰 무효화)

### 2. 운동 종목 관리
- [x] 운동 종목 카탈로그 조회
- [x] 신체 부위별 필터링 (가슴, 등, 하체, 어깨, 팔, 코어)
- [x] 장비 정보 포함
- [x] 운동 종목 상세 정보

### 3. 운동 루틴 관리
- [x] 사용자별 커스텀 루틴 생성
- [x] 루틴에 운동 종목 추가 (순서, 세트/반복 목표)
- [x] 루틴 수정/삭제
- [x] 루틴 목록 조회
- [x] 루틴 상세 조회 (포함된 운동 목록)

### 4. 운동 기록 추적
- [x] 운동 세션 생성 (날짜, 루틴 선택)
- [x] 세트별 기록 (무게, 반복 횟수, 난이도)
- [x] 운동 기록 조회 (날짜별, 기간별)
- [x] 세션 삭제
- [x] 운동 시간 자동 계산

### 5. 통계 및 분석
- [x] **주간 통계** - 운동 횟수, 세트 수, 총 시간, 일평균
- [x] **월간 통계** - 총 운동량, 주당 평균, 실제 운동 일수
- [x] **신체 부위별 통계** - 부위별 운동량 및 비율
- [x] **개인 기록(PR) 추적** - 운동별 최고 무게 및 반복 횟수
- [x] **1RM 계산** - Brzycki 공식을 이용한 추정 최대 중량

### 6. 목표 관리
- [x] 운동 목표 설정 (체중, 운동 횟수 등)
- [x] 목표 진척도 업데이트
- [x] 목표 달성 여부 추적

---

## 아키텍처

### 계층형 아키텍처 (Layered Architecture)
```
┌─────────────────────────────────────────┐
│          Presentation Layer             │
│  (Controllers - REST API Endpoints)     │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Application Layer              │
│      (Services - Business Logic)        │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│          Persistence Layer              │
│   (Repositories - Data Access - JPA)    │
└─────────────────┬───────────────────────┘
                  │
┌─────────────────▼───────────────────────┐
│           Database Layer                │
│            (MySQL 8.x)                  │
└─────────────────────────────────────────┘
```

### 패키지 구조
```
src/main/java/com/example/FitTracker/
├── config/                    # 설정 클래스
│   ├── SecurityConfig.java    # Spring Security 설정
│   └── SwaggerConfig.java     # Swagger 설정
│
├── controller/                # REST API 컨트롤러
│   ├── AuthController.java
│   ├── ExerciseTypeController.java
│   ├── RoutineController.java
│   ├── WorkoutController.java
│   ├── StatsController.java
│   └── GoalController.java
│
├── domain/                    # JPA 엔티티
│   ├── User.java
│   ├── ExerciseType.java
│   ├── Routine.java
│   ├── RoutineExercise.java
│   ├── WorkoutSession.java
│   ├── WorkoutSet.java
│   ├── Goal.java
│   └── RefreshToken.java
│
├── dto/                       # 데이터 전송 객체
│   ├── request/               # 요청 DTO
│   │   ├── SignupRequest.java
│   │   ├── LoginRequest.java
│   │   ├── RoutineRequest.java
│   │   └── WorkoutRequest.java
│   └── response/              # 응답 DTO
│       ├── stats/
│       │   ├── WeeklyStatsResponse.java
│       │   ├── MonthlyStatsResponse.java
│       │   ├── BodyPartStatsResponse.java
│       │   └── PersonalRecordResponse.java
│       └── ...
│
├── repository/                # JPA 리포지토리
│   ├── UserRepository.java
│   ├── ExerciseTypeRepository.java
│   ├── RoutineRepository.java
│   ├── WorkoutSessionRepository.java
│   ├── WorkoutSetRepository.java
│   └── ...
│
├── service/                   # 비즈니스 로직
│   ├── AuthService.java
│   ├── RoutineService.java
│   ├── WorkoutService.java
│   ├── StatsService.java      # 통계 서비스
│   └── GoalService.java
│
├── security/                  # 보안 관련
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── CustomUserDetailsService.java
│
└── exception/                 # 예외 처리
    ├── GlobalExceptionHandler.java
    ├── ResourceNotFoundException.java
    └── UnauthorizedException.java
```

---

## ERD

```
┌──────────────┐         ┌──────────────────┐
│     User     │         │   ExerciseType   │
├──────────────┤         ├──────────────────┤
│ id (PK)      │         │ id (PK)          │
│ email        │         │ name             │
│ password     │         │ bodyPart         │
│ name         │         │ equipment        │
│ created_at   │         │ description      │
└──────┬───────┘         └────────┬─────────┘
       │                          │
       │ 1                        │
       │                          │
       │ N               N        │
┌──────▼───────┐         ┌────────▼─────────┐
│   Routine    │────────▶│ RoutineExercise  │
├──────────────┤  1   N  ├──────────────────┤
│ id (PK)      │         │ id (PK)          │
│ user_id (FK) │         │ routine_id (FK)  │
│ name         │         │ exercise_id (FK) │
│ description  │         │ order_index      │
│ created_at   │         │ target_sets      │
└──────┬───────┘         │ target_reps      │
       │                 └──────────────────┘
       │ 1
       │
       │ N
┌──────▼─────────┐       ┌──────────────────┐
│ WorkoutSession │──────▶│   WorkoutSet     │
├────────────────┤ 1  N  ├──────────────────┤
│ id (PK)        │       │ id (PK)          │
│ user_id (FK)   │       │ session_id (FK)  │
│ routine_id(FK) │       │ exercise_id (FK) │
│ workout_date   │       │ set_number       │
│ duration       │       │ weight           │
│ notes          │       │ reps             │
│ created_at     │       │ difficulty       │
└────────────────┘       └──────────────────┘

┌──────────────┐
│     Goal     │
├──────────────┤
│ id (PK)      │
│ user_id (FK) │─────┐
│ goal_type    │     │
│ target_value │     │ N
│ current_value│     │
│ deadline     │     │
│ achieved     │     ▼
└──────────────┘   User

┌──────────────┐
│ RefreshToken │
├──────────────┤
│ id (PK)      │
│ user_id (FK) │─────┐
│ token        │     │ 1
│ expiry_date  │     │
└──────────────┘     ▼
                   User
```

### 주요 관계
- **User ↔ Routine**: 1:N (사용자는 여러 루틴 소유)
- **Routine ↔ RoutineExercise**: 1:N (루틴은 여러 운동 포함)
- **ExerciseType ↔ RoutineExercise**: 1:N (운동 종목은 여러 루틴에 포함)
- **User ↔ WorkoutSession**: 1:N (사용자는 여러 운동 세션 보유)
- **WorkoutSession ↔ WorkoutSet**: 1:N (세션은 여러 세트 포함)
- **User ↔ Goal**: 1:N (사용자는 여러 목표 설정)
- **User ↔ RefreshToken**: 1:1 (사용자당 하나의 리프레시 토큰)

---

## API 문서

### Swagger UI
**URL**: `http://localhost:8080/swagger-ui.html`

모든 API는 실시간으로 테스트 가능하며, 요청/응답 스키마를 확인할 수 있습니다.

### 주요 API 엔드포인트

#### 1. 인증 (Authentication)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 (JWT 발급) | ❌ |
| POST | `/api/auth/refresh` | 토큰 갱신 | ✅ |
| POST | `/api/auth/logout` | 로그아웃 | ✅ |

**회원가입 요청 예시**:
```json
{
  "email": "user@example.com",
  "password": "securePassword123!",
  "name": "홍길동"
}
```

**로그인 응답 예시**:
```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "refreshToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "홍길동"
  }
}
```

---

#### 2. 운동 종목 (Exercise Types)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/exercises` | 전체 운동 종목 조회 | ✅ |
| GET | `/api/exercises?bodyPart=CHEST` | 신체 부위별 조회 | ✅ |
| GET | `/api/exercises/{id}` | 운동 종목 상세 조회 | ✅ |

**신체 부위 옵션**: `CHEST`, `BACK`, `LEGS`, `SHOULDERS`, `ARMS`, `CORE`

---

#### 3. 운동 루틴 (Routines)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/routines` | 루틴 생성 | ✅ |
| GET | `/api/routines` | 내 루틴 목록 조회 | ✅ |
| GET | `/api/routines/{id}` | 루틴 상세 조회 | ✅ |
| PUT | `/api/routines/{id}` | 루틴 수정 | ✅ |
| DELETE | `/api/routines/{id}` | 루틴 삭제 | ✅ |

**루틴 생성 요청 예시**:
```json
{
  "name": "상체 집중 루틴",
  "description": "가슴과 팔 중심의 운동",
  "exercises": [
    {
      "exerciseTypeId": 1,
      "orderIndex": 1,
      "targetSets": 3,
      "targetReps": 10
    },
    {
      "exerciseTypeId": 5,
      "orderIndex": 2,
      "targetSets": 4,
      "targetReps": 12
    }
  ]
}
```

---

#### 4. 운동 기록 (Workouts)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/workouts` | 운동 세션 생성 | ✅ |
| POST | `/api/workouts/{sessionId}/sets` | 세트 추가 | ✅ |
| GET | `/api/workouts` | 운동 기록 조회 | ✅ |
| GET | `/api/workouts?startDate=2025-01-01&endDate=2025-01-07` | 기간별 조회 | ✅ |
| GET | `/api/workouts/{sessionId}` | 세션 상세 조회 | ✅ |
| DELETE | `/api/workouts/{sessionId}` | 세션 삭제 | ✅ |

**운동 세션 생성 요청 예시**:
```json
{
  "routineId": 1,
  "workoutDate": "2025-01-07",
  "notes": "오늘 컨디션 좋음"
}
```

**세트 추가 요청 예시**:
```json
{
  "exerciseTypeId": 1,
  "setNumber": 1,
  "weight": 60.0,
  "reps": 10,
  "difficulty": "MEDIUM"
}
```

---

#### 5. 통계 및 분석 (Statistics)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/stats/weekly?startDate={date}&endDate={date}` | 주간 통계 | ✅ |
| GET | `/api/stats/monthly?yearMonth=2025-01` | 월간 통계 | ✅ |
| GET | `/api/stats/body-parts?startDate={date}&endDate={date}` | 신체 부위별 통계 | ✅ |
| GET | `/api/stats/personal-records` | 전체 개인 기록 | ✅ |
| GET | `/api/stats/personal-records/{exerciseTypeId}` | 특정 운동 개인 기록 | ✅ |

**주간 통계 응답 예시**:
```json
{
  "totalWorkouts": 5,
  "totalSets": 75,
  "totalDuration": 300,
  "averageWorkoutsPerDay": 0.71
}
```

**개인 기록 응답 예시**:
```json
{
  "exerciseTypeName": "벤치프레스",
  "maxWeight": 100.0,
  "repsAtMaxWeight": 5,
  "achievedDate": "2025-01-05",
  "estimatedOneRepMax": 112.5
}
```

**1RM 계산 공식 (Brzycki)**:
```
1RM = weight × (36 / (37 - reps))

예시: 100kg × 5회 = 100 × (36 / (37 - 5)) = 112.5kg
```

---

#### 6. 목표 관리 (Goals)
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/goals` | 목표 생성 | ✅ |
| GET | `/api/goals` | 내 목표 목록 조회 | ✅ |
| PUT | `/api/goals/{id}/progress` | 진척도 업데이트 | ✅ |

---

### 인증 헤더
모든 인증이 필요한 API는 다음 헤더를 포함해야 합니다:
```
Authorization: Bearer {accessToken}
```

---

## 설치 및 실행

### 사전 요구사항
- **Java 21** 이상
- **MySQL 8.x**
- **Gradle 8.x** (또는 Gradle Wrapper 사용)

### 1. 저장소 클론
```bash
git clone https://github.com/yourusername/FitTracker.git
cd FitTracker
```

### 2. MySQL 데이터베이스 생성
```sql
CREATE DATABASE fittrackerdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 환경 설정
`src/main/resources/application.yml` 파일을 수정합니다:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/fittrackerdb
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver

  jpa:
    hibernate:
      ddl-auto: update  # 최초 실행 시 테이블 자동 생성
    show-sql: true
    properties:
      hibernate:
        format_sql: true
        dialect: org.hibernate.dialect.MySQL8Dialect

jwt:
  secret: your-secret-key-min-256-bits  # 최소 256비트 이상의 시크릿 키
  access-token-validity: 3600000        # 1시간 (밀리초)
  refresh-token-validity: 604800000     # 7일 (밀리초)
```

### 4. 애플리케이션 빌드 및 실행
```bash
# Gradle Wrapper를 사용한 빌드
./gradlew clean build

# 애플리케이션 실행
./gradlew bootRun
```

또는 JAR 파일로 실행:
```bash
./gradlew bootJar
java -jar build/libs/FitTracker-0.0.1-SNAPSHOT.jar
```

### 5. 애플리케이션 확인
- **서버**: http://localhost:8080
- **API 문서**: http://localhost:8080/swagger-ui.html
- **H2 콘솔**: http://localhost:8080/h2-console (개발 환경)

---

## 개발 포인트

### 1. JWT 기반 Stateless 인증 구현
**문제**: 세션 기반 인증은 서버 확장성에 제약이 있음

**해결**:
- JWT를 사용한 무상태(Stateless) 인증 구현
- Access Token (1시간) + Refresh Token (7일) 이중 토큰 전략
- RefreshToken을 DB에 저장하여 무효화 가능

**코드**: [JwtTokenProvider.java](src/main/java/com/example/FitTracker/security/JwtTokenProvider.java:1-150)

```java
public String generateAccessToken(String email) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + accessTokenValidity);

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS512, jwtSecret)
        .compact();
}
```

---

### 2. JPA 연관관계 매핑 및 Lazy Loading 최적화
**문제**: N+1 쿼리 문제로 인한 성능 저하

**해결**:
- `@ManyToOne`, `@OneToMany` 적절한 관계 설정
- 기본 FetchType을 LAZY로 설정
- 필요한 경우 `@EntityGraph` 또는 `JOIN FETCH` 사용

**코드**: [WorkoutSession.java](src/main/java/com/example/FitTracker/domain/WorkoutSession.java:1-50)

```java
@Entity
public class WorkoutSession {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "workoutSession", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WorkoutSet> workoutSets = new ArrayList<>();
}
```

---

### 3. 통계 기능의 복잡한 쿼리 최적화
**문제**: 여러 테이블을 JOIN하는 통계 쿼리의 성능 이슈

**해결**:
- JPA Query Methods의 `@Query` 어노테이션 사용
- 집계 함수(COUNT, SUM, AVG) 활용
- DTO Projection으로 필요한 데이터만 조회

**코드**: [WorkoutSetRepository.java](src/main/java/com/example/FitTracker/repository/WorkoutSetRepository.java:1-100)

```java
@Query("""
    SELECT new com.example.FitTracker.dto.response.stats.PersonalRecordResponse(
        e.name,
        MAX(ws.weight),
        ws.reps,
        wss.workoutDate
    )
    FROM WorkoutSet ws
    JOIN ws.exerciseType e
    JOIN ws.workoutSession wss
    WHERE wss.user.id = :userId
    GROUP BY e.id, ws.reps
    ORDER BY MAX(ws.weight) DESC
    """)
List<PersonalRecordResponse> findPersonalRecords(@Param("userId") Long userId);
```

---

### 4. 1RM 계산 알고리즘 구현
**문제**: 사용자의 실제 최대 중량을 추정하는 과학적 방법 필요

**해결**:
- Brzycki 공식 적용: `1RM = weight × (36 / (37 - reps))`
- Service 계층에서 비즈니스 로직으로 구현
- 반복 횟수가 1일 경우 자체 무게를 1RM으로 간주

**코드**: [StatsService.java](src/main/java/com/example/FitTracker/service/StatsService.java:150-170)

```java
private double calculateOneRepMax(double weight, int reps) {
    if (reps == 1) {
        return weight;
    }
    // Brzycki 공식
    return weight * (36.0 / (37.0 - reps));
}
```

---

### 5. Exception Handling 및 일관된 API 응답
**문제**: 다양한 예외 상황에서 일관성 있는 에러 응답 필요

**해결**:
- `@ControllerAdvice`를 사용한 전역 예외 처리
- Custom Exception 클래스 정의
- 표준 HTTP 상태 코드 사용

**코드**: [GlobalExceptionHandler.java](src/main/java/com/example/FitTracker/exception/GlobalExceptionHandler.java:1-80)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            ex.getMessage(),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}
```

---

### 6. OpenAPI(Swagger) 문서 자동화
**문제**: API 문서의 수동 관리는 유지보수 비용이 높음

**해결**:
- SpringDoc OpenAPI 3 라이브러리 사용
- 어노테이션 기반 API 문서 자동 생성
- Swagger UI를 통한 실시간 테스트 가능

**코드**: [SwaggerConfig.java](src/main/java/com/example/FitTracker/config/SwaggerConfig.java:1-50)

```java
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("FitTracker API")
                .version("1.0")
                .description("운동 루틴 추적 REST API"));
    }
}
```

---

## 성능 및 최적화

### 1. 데이터베이스 최적화
- **인덱스 생성**: 자주 조회되는 컬럼에 인덱스 추가
  - `user_id`, `workout_date`, `exercise_type_id`
- **Connection Pooling**: HikariCP 사용 (최대 10개 커넥션)
- **Batch Insert**: JPA Batch Size 설정 (100)

```yaml
spring:
  jpa:
    properties:
      hibernate:
        jdbc:
          batch_size: 100
  datasource:
    hikari:
      maximum-pool-size: 10
      minimum-idle: 5
```

### 2. 쿼리 최적화
- **N+1 문제 해결**: `@EntityGraph`, `JOIN FETCH` 사용
- **Projection 활용**: 필요한 필드만 조회
- **페이징 처리**: 대량 데이터 조회 시 Pageable 사용

### 3. 캐싱 전략 (향후 적용 예정)
- **Redis** 도입 고려
- 운동 종목 데이터는 변경이 적으므로 캐싱 적합
- 통계 데이터도 일정 시간 캐싱 가능

---

## 향후 계획

### 기능 확장
- [ ] 소셜 기능 (친구 추가, 운동 기록 공유)
- [ ] 운동 추천 알고리즘 (AI 기반)
- [ ] 식단 관리 기능
- [ ] 체중 및 신체 사진 추적
- [ ] 푸시 알림 (운동 리마인더)

### 기술 개선
- [ ] Redis 캐싱 도입
- [ ] 테스트 커버리지 80% 이상
- [ ] CI/CD 파이프라인 구축 (GitHub Actions)
- [ ] Docker 컨테이너화
- [ ] AWS 배포 (EC2 + RDS)
- [ ] 모니터링 (Prometheus + Grafana)

### 아키텍처 개선
- [ ] CQRS 패턴 적용 (조회/명령 분리)
- [ ] Event-Driven Architecture (비동기 처리)
- [ ] Microservices 전환 고려

---

## 라이센스
This project is licensed under the MIT License.

---

## 문의
프로젝트 관련 문의사항이나 버그 리포트는 [Issues](https://github.com/yourusername/FitTracker/issues)에 등록해주세요.

---

**개발자**: [Your Name]
**이메일**: your.email@example.com
**포트폴리오**: https://yourportfolio.com
**GitHub**: https://github.com/yourusername
