package com.example.FitTracker.controller;

import com.example.FitTracker.dto.response.ApiResponse;
import com.example.FitTracker.dto.response.stats.*;
import com.example.FitTracker.security.SecurityUtil;
import com.example.FitTracker.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
@Tag(name = "통계", description = "운동 통계 및 분석 API")
public class StatsController {
    
    private final StatsService statsService;
    private final SecurityUtil securityUtil;
    
    @GetMapping("/weekly")
    @Operation(summary = "주간 통계 조회", description = "지정한 기간의 주간 운동 통계를 조회합니다")
    public ResponseEntity<ApiResponse<WeeklyStatsResponse>> getWeeklyStats(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        WeeklyStatsResponse stats = statsService.getWeeklyStats(
                securityUtil.getCurrentUserId(), startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
    
    @GetMapping("/monthly")
    @Operation(summary = "월간 통계 조회", description = "특정 월의 운동 통계를 조회합니다")
    public ResponseEntity<ApiResponse<MonthlyStatsResponse>> getMonthlyStats(
            @Parameter(description = "년-월 (yyyy-MM)", example = "2024-12")
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth yearMonth) {
        
        MonthlyStatsResponse stats = statsService.getMonthlyStats(
                securityUtil.getCurrentUserId(), yearMonth);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
    
    @GetMapping("/body-parts")
    @Operation(summary = "신체 부위별 통계", description = "지정한 기간의 신체 부위별 운동량을 조회합니다")
    public ResponseEntity<ApiResponse<List<BodyPartStatsResponse>>> getBodyPartStats(
            @Parameter(description = "시작 날짜 (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "종료 날짜 (yyyy-MM-dd)")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<BodyPartStatsResponse> stats = statsService.getBodyPartStats(
                securityUtil.getCurrentUserId(), startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(stats));
    }
    
    @GetMapping("/personal-records")
    @Operation(summary = "개인 기록 조회", description = "모든 운동 종목의 개인 최고 기록을 조회합니다")
    public ResponseEntity<ApiResponse<List<PersonalRecordResponse>>> getPersonalRecords() {
        List<PersonalRecordResponse> records = statsService.getPersonalRecords(
                securityUtil.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success(records));
    }
    
    @GetMapping("/personal-records/{exerciseTypeId}")
    @Operation(summary = "특정 운동의 개인 기록", description = "특정 운동 종목의 개인 최고 기록을 조회합니다")
    public ResponseEntity<ApiResponse<PersonalRecordResponse>> getPersonalRecordByExercise(
            @Parameter(description = "운동 종목 ID")
            @PathVariable Long exerciseTypeId) {
        
        PersonalRecordResponse record = statsService.getPersonalRecordByExercise(
                securityUtil.getCurrentUserId(), exerciseTypeId);
        return ResponseEntity.ok(ApiResponse.success(record));
    }
}
