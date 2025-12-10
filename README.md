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

### 1. 인증 (Auth)
- ✅ 회원가입/로그인 (JWT 인증)
- ✅ 이메일 중복 검증
- ✅ 비밀번호 암호화

### 2. 운동 종목 (Exercise)
- ✅ 운동 종목 조회
- ✅ 신체 부위별 운동 조회

### 3. 운동 루틴 (Routine)
- ✅ 루틴 생성/수정/삭제
- ✅ 루틴 목록 조회
- ✅ 루틴 상세 조회

### 4. 운동 기록 (Workout)
- ✅ 운동 세션 생성
- ✅ 세트 추가
- ✅ 운동 기록 조회 (기간별)
- ✅ 세션 삭제

### 5. 통계 및 분석 (Stats) ⭐ NEW
- ✅ 주간 통계 (운동 횟수, 세트 수, 시간, 평균)
- ✅ 월간 통계 (총 운동량, 주당 평균)
- ✅ 신체 부위별 운동량 통계
- ✅ 개인 기록 (PR) 조회
- ✅ 1RM 계산 (Brzycki 공식)

## 🚀 실행 방법

### 1. MySQL 데이터베이스 설정
```sql
CREATE DATABASE fittrackerdb;
```

### 2. application.yml 설정
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3309/fittrackerdb
    username: your_username
    password: your_password
```

### 3. 애플리케이션 실행
```bash
./gradlew bootRun
```

## 📚 API 문서
http://localhost:8080/swagger-ui.html

## 🔍 API 엔드포인트

### 인증
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인

### 운동 종목
- `GET /api/exercises` - 운동 종목 전체 조회
- `GET /api/exercises?bodyPart={부위}` - 신체 부위별 조회
- `GET /api/exercises/{id}` - 운동 종목 상세 조회

### 루틴
- `POST /api/routines` - 루틴 생성
- `GET /api/routines` - 루틴 목록 조회
- `GET /api/routines/{id}` - 루틴 상세 조회
- `PUT /api/routines/{id}` - 루틴 수정
- `DELETE /api/routines/{id}` - 루틴 삭제

### 운동 기록
- `POST /api/workouts` - 운동 세션 생성
- `POST /api/workouts/{sessionId}/sets` - 세트 추가
- `GET /api/workouts` - 운동 기록 조회
- `GET /api/workouts?startDate={날짜}&endDate={날짜}` - 기간별 조회
- `GET /api/workouts/{sessionId}` - 세션 상세 조회
- `DELETE /api/workouts/{sessionId}` - 세션 삭제

### 통계 ⭐ NEW
- `GET /api/stats/weekly?startDate={날짜}&endDate={날짜}` - 주간 통계
- `GET /api/stats/monthly?yearMonth={년월}` - 월간 통계
- `GET /api/stats/body-parts?startDate={날짜}&endDate={날짜}` - 신체 부위별 통계
- `GET /api/stats/personal-records` - 전체 개인 기록
- `GET /api/stats/personal-records/{exerciseTypeId}` - 특정 운동 개인 기록

## 📁 프로젝트 구조
```
src/main/java/com/example/FitTracker/
├── config/              # 설정 (Security, Swagger)
├── controller/          # REST API 컨트롤러
│   ├── AuthController
│   ├── ExerciseTypeController
│   ├── RoutineController
│   ├── WorkoutController
│   └── StatsController     ⭐ NEW
├── domain/              # Entity
│   ├── User
│   ├── ExerciseType
│   ├── Routine
│   ├── RoutineExercise
│   ├── WorkoutSession
│   └── WorkoutSet
├── dto/                 # 요청/응답 DTO
│   ├── request/
│   └── response/
│       └── stats/          ⭐ NEW
│           ├── WeeklyStatsResponse
│           ├── MonthlyStatsResponse
│           ├── BodyPartStatsResponse
│           └── PersonalRecordResponse
├── repository/          # JPA Repository
├── security/            # JWT 인증
├── service/             # 비즈니스 로직
│   └── StatsService        ⭐ NEW
└── exception/           # 예외 처리
```

## 🔐 인증
모든 API는 JWT 토큰 인증이 필요합니다 (회원가입/로그인 제외)

**Authorization 헤더:**
```
Authorization: Bearer {your_jwt_token}
```

## 📊 통계 기능 상세

### 주간 통계
- 기간 내 총 운동 횟수
- 총 세트 수
- 총 운동 시간
- 일평균 운동 횟수

### 월간 통계
- 월간 총 운동 횟수
- 총 세트 수
- 총 운동 시간
- 주평균 운동 횟수
- 실제 운동한 날 수

### 신체 부위별 통계
- 부위별 세트 수
- 전체 대비 비율

### 개인 기록 (PR)
- 운동별 최고 무게
- 해당 무게에서의 반복 횟수
- 기록 달성 날짜
- 1RM (추정 최대 중량)

## 📈 1RM 계산 공식
Brzycki 공식 사용:
```
1RM = weight × (36 / (37 - reps))
```

## 🧪 테스트
```bash
./gradlew test
```

## 📝 개발 진행 상황
- [x] 프로젝트 초기 설정
- [x] 도메인 모델링
- [x] 인증 기능
- [x] 운동 종목 관리
- [x] 루틴 관리
- [x] 운동 기록 관리
- [x] 통계 및 분석 기능 ⭐ NEW
- [ ] 데이터 초기화 (운동 종목 데이터)
- [ ] 단위 테스트 추가
- [ ] 통합 테스트
- [ ] API 문서 상세화

## 📮 문의
프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.
