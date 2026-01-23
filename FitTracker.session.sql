
-- 1. 테스트 사용자 추가 (비밀번호: password123, BCrypt 암호화됨)
INSERT INTO users (email, password, name, created_at) VALUES
('test@example.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', '테스트유저', NOW());

-- 2. 운동 종목 추가
INSERT INTO exercise_types (name, body_part, description) VALUES
-- 가슴
('벤치프레스', '가슴', '가슴 운동의 기본'),
('인클라인 벤치프레스', '가슴', '상부 가슴 발달'),
('덤벨 플라이', '가슴', '가슴 스트레칭'),
('푸시업', '가슴', '자중 운동'),

-- 등
('데드리프트', '등', '전신 운동'),
('풀업', '등', '광배근 운동'),
('바벨 로우', '등', '등 두께 증가'),
('랫 풀다운', '등', '광배근 발달'),

-- 다리
('스쿼트', '다리', '하체 운동의 왕'),
('레그프레스', '다리', '대퇴사두근'),
('레그 컬', '다리', '햄스트링'),
('레그 익스텐션', '다리', '대퇴사두근 고립'),

-- 어깨
('밀리터리 프레스', '어깨', '전면 어깨'),
('사이드 레터럴 레이즈', '어깨', '측면 삼각근'),
('리어 델트 플라이', '어깨', '후면 삼각근'),

-- 팔
('바벨 컬', '팔', '이두근'),
('트라이셉 익스텐션', '팔', '삼두근'),
('해머컬', '팔', '상완근'),

-- 복근
('크런치', '복근', '복직근'),
('플랭크', '복근', '코어 강화'),
('레그레이즈', '복근', '하복부');

-- 확인
SELECT * FROM users;
SELECT * FROM exercise_types;
SELECT * FROM routine_exercises;

SHOW TABLES;

SELECT DATABASE();

USE fittrackerdb;