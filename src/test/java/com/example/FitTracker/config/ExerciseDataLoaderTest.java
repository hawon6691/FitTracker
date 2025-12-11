package com.example.FitTracker.config;

import com.example.FitTracker.repository.ExerciseTypeRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 운동 종목 데이터 초기화 테스트
 */
@SpringBootTest
@Transactional
class ExerciseDataLoaderTest {
    
    @Autowired
    private ExerciseTypeRepository exerciseTypeRepository;
    
    @Test
    void 운동종목_데이터_초기화_확인() {
        // when
        long count = exerciseTypeRepository.count();
        
        // then
        assertThat(count).isGreaterThan(0);
        System.out.println("총 " + count + "개의 운동 종목이 초기화되었습니다.");
    }
    
    @Test
    void 신체부위별_운동종목_확인() {
        // given
        String[] bodyParts = {"가슴", "등", "다리", "어깨", "팔", "복근"};
        
        // when & then
        for (String bodyPart : bodyParts) {
            long count = exerciseTypeRepository.findByBodyPart(bodyPart).size();
            assertThat(count).isGreaterThan(0);
            System.out.println(bodyPart + " 운동: " + count + "개");
        }
    }
    
    @Test
    void 각_신체부위별_운동종목_상세_출력() {
        // given
        String[] bodyParts = {"가슴", "등", "다리", "어깨", "팔", "복근"};
        
        // when & then
        for (String bodyPart : bodyParts) {
            var exercises = exerciseTypeRepository.findByBodyPart(bodyPart);
            System.out.println("\n========== " + bodyPart + " 운동 (" + exercises.size() + "개) ==========");
            exercises.forEach(exercise -> {
                System.out.println("- " + exercise.getName() + ": " + exercise.getDescription());
            });
        }
    }
}
