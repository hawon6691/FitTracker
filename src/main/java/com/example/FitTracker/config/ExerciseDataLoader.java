package com.example.FitTracker.config;

import com.example.FitTracker.domain.ExerciseType;
import com.example.FitTracker.repository.ExerciseTypeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 운동 종목 마스터 데이터 초기화
 * 애플리케이션 시작 시 자동으로 실행됩니다.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExerciseDataLoader implements CommandLineRunner {
    
    private final ExerciseTypeRepository exerciseTypeRepository;
    
    @Override
    @Transactional
    public void run(String... args) {
        // 이미 데이터가 있으면 실행하지 않음
        if (exerciseTypeRepository.count() > 0) {
            log.info("운동 종목 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }
        
        log.info("운동 종목 데이터 초기화를 시작합니다...");
        
        List<ExerciseType> exercises = createExerciseData();
        exerciseTypeRepository.saveAll(exercises);
        
        log.info("{}개의 운동 종목이 성공적으로 추가되었습니다.", exercises.size());
    }
    
    /**
     * 운동 종목 데이터 생성
     */
    private List<ExerciseType> createExerciseData() {
        return Arrays.asList(
            // ========== 가슴 운동 ==========
            ExerciseType.builder()
                .name("벤치 프레스")
                .bodyPart("가슴")
                .description("바벨을 사용하여 가슴 전체를 발달시키는 대표적인 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("인클라인 벤치 프레스")
                .bodyPart("가슴")
                .description("상부 가슴 발달에 효과적인 벤치 프레스 변형 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("덤벨 프레스")
                .bodyPart("가슴")
                .description("덤벨을 사용하여 가슴 근육의 가동 범위를 넓히는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("덤벨 플라이")
                .bodyPart("가슴")
                .description("가슴 근육의 스트레칭과 수축을 강조하는 고립 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("체스트 프레스 머신")
                .bodyPart("가슴")
                .description("머신을 이용한 안정적인 가슴 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("딥스")
                .bodyPart("가슴")
                .description("자신의 체중을 이용하여 가슴 하부와 삼두근을 발달시키는 운동입니다.")
                .build(),
            
            // ========== 등 운동 ==========
            ExerciseType.builder()
                .name("데드리프트")
                .bodyPart("등")
                .description("등 전체와 하체, 코어를 동시에 발달시키는 복합 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("풀업")
                .bodyPart("등")
                .description("자신의 체중을 이용하여 광배근을 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("랫 풀다운")
                .bodyPart("등")
                .description("광배근의 넓이를 발달시키는 대표적인 등 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("바벨 로우")
                .bodyPart("등")
                .description("등 중앙부와 두께를 발달시키는 복합 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("덤벨 로우")
                .bodyPart("등")
                .description("한쪽씩 집중하여 등 근육을 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("시티드 로우")
                .bodyPart("등")
                .description("케이블 머신을 이용하여 등 중앙부를 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("티바 로우")
                .bodyPart("등")
                .description("상부 등과 승모근을 발달시키는 운동입니다.")
                .build(),
            
            // ========== 하체 운동 ==========
            ExerciseType.builder()
                .name("스쿼트")
                .bodyPart("다리")
                .description("하체 전체를 발달시키는 가장 기본적이고 효과적인 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("레그 프레스")
                .bodyPart("다리")
                .description("머신을 이용하여 하체 근육을 안전하게 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("레그 익스텐션")
                .bodyPart("다리")
                .description("대퇴사두근을 집중적으로 발달시키는 고립 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("레그 컬")
                .bodyPart("다리")
                .description("햄스트링을 집중적으로 발달시키는 고립 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("런지")
                .bodyPart("다리")
                .description("하체 전체와 균형감각을 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("불가리안 스플릿 스쿼트")
                .bodyPart("다리")
                .description("한쪽 다리씩 집중하여 하체를 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("카프 레이즈")
                .bodyPart("다리")
                .description("종아리 근육을 집중적으로 발달시키는 운동입니다.")
                .build(),
            
            // ========== 어깨 운동 ==========
            ExerciseType.builder()
                .name("숄더 프레스")
                .bodyPart("어깨")
                .description("어깨 전체를 발달시키는 대표적인 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("덤벨 숄더 프레스")
                .bodyPart("어깨")
                .description("덤벨을 이용하여 어깨 근육의 가동 범위를 넓히는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("사이드 레터럴 레이즈")
                .bodyPart("어깨")
                .description("측면 삼각근을 집중적으로 발달시키는 고립 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("프론트 레이즈")
                .bodyPart("어깨")
                .description("전면 삼각근을 집중적으로 발달시키는 고립 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("리어 델트 플라이")
                .bodyPart("어깨")
                .description("후면 삼각근을 집중적으로 발달시키는 고립 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("페이스 풀")
                .bodyPart("어깨")
                .description("후면 삼각근과 회전근개를 강화하는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("업라이트 로우")
                .bodyPart("어깨")
                .description("측면 삼각근과 승모근을 발달시키는 운동입니다.")
                .build(),
            
            // ========== 팔 운동 ==========
            ExerciseType.builder()
                .name("바벨 컬")
                .bodyPart("팔")
                .description("이두근을 발달시키는 가장 기본적인 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("덤벨 컬")
                .bodyPart("팔")
                .description("덤벨을 이용하여 이두근을 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("해머 컬")
                .bodyPart("팔")
                .description("이두근과 상완근을 동시에 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("프리처 컬")
                .bodyPart("팔")
                .description("이두근의 하부를 집중적으로 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("케이블 컬")
                .bodyPart("팔")
                .description("케이블을 이용하여 이두근에 지속적인 긴장을 주는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("트라이셉스 푸시다운")
                .bodyPart("팔")
                .description("케이블을 이용하여 삼두근을 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("오버헤드 트라이셉스 익스텐션")
                .bodyPart("팔")
                .description("삼두근의 장두를 집중적으로 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("스컬 크러셔")
                .bodyPart("팔")
                .description("바벨이나 덤벨을 이용하여 삼두근을 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("클로즈 그립 벤치 프레스")
                .bodyPart("팔")
                .description("삼두근을 집중적으로 발달시키는 복합 운동입니다.")
                .build(),
            
            // ========== 복근 운동 ==========
            ExerciseType.builder()
                .name("크런치")
                .bodyPart("복근")
                .description("복직근 상부를 발달시키는 기본적인 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("레그 레이즈")
                .bodyPart("복근")
                .description("복직근 하부를 집중적으로 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("플랭크")
                .bodyPart("복근")
                .description("코어 전체를 안정화하고 강화하는 정적 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("러시안 트위스트")
                .bodyPart("복근")
                .description("복사근을 발달시키고 회전력을 강화하는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("행잉 레그 레이즈")
                .bodyPart("복근")
                .description("복직근 전체를 강하게 자극하는 고급 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("사이드 플랭크")
                .bodyPart("복근")
                .description("복사근과 코어 측면을 강화하는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("마운틴 클라이머")
                .bodyPart("복근")
                .description("복근과 심폐 지구력을 동시에 발달시키는 운동입니다.")
                .build(),
            
            ExerciseType.builder()
                .name("바이시클 크런치")
                .bodyPart("복근")
                .description("복직근과 복사근을 동시에 자극하는 운동입니다.")
                .build()
        );
    }
}
