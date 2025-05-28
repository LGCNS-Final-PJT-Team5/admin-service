package com.modive.adminservice.api.reward.controller;

import com.modive.adminservice.api.reward.service.RewardFetchService;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.dto.ErrorRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@Tag(name = "리워드 관리", description = "리워드 관리와 관련된 API입니다.")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/rewards")
public class AdminRewardController {

    private final RewardFetchService rewardFetchService;

    @GetMapping("/summary")
    @Operation(summary = "리워드 요약 통계", description = "리워드 관련 요약 통계 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> getRewardSummary() {
        try {
            log.info("리워드 요약 통계 조회 요청");

            Object totalIssued = rewardFetchService.fetchTotalIssuedRewards();
            Object monthlyIssued = rewardFetchService.fetchMonthlyIssuedRewards();
            Object dailyAverage = rewardFetchService.fetchDailyAverageIssued();
            Object perUserAverage = rewardFetchService.fetchPerUserAverageIssued();

            Map<String, Object> rewardSummary = new HashMap<>();
            rewardSummary.put("totalIssued", totalIssued);
            rewardSummary.put("monthlyIssued", monthlyIssued);
            rewardSummary.put("dailyAverage", dailyAverage);
            rewardSummary.put("perUserAverage", perUserAverage);

            Map<String, Object> data = new HashMap<>();
            data.put("rewardSummary", rewardSummary);

            return ResponseEntity.ok(
                    CommonRes.success(data, "리워드 요약 통계 조회에 성공하였습니다.")
            );
        } catch (Exception e) {
            log.error("리워드 요약 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    CommonRes.success(null, "리워드 조회 중 오류가 발생했습니다: " + e.getMessage())
            );
        }
    }

    @GetMapping("/by-reason/total")
    @Operation(summary = "리워드 사유별 총 통계", description = "리워드 발급 사유별 통계 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> getRewardsByReasonTotal() {
        try {
            log.info("리워드 사유별 총 통계 조회 요청");

            Object reasonStats = rewardFetchService.fetchRewardsByReasonTotal();

            Map<String, Object> data = new HashMap<>();
            data.put("rewardsByReason", reasonStats);

            return ResponseEntity.ok(
                    CommonRes.success(data, "리워드 사유별 통계 조회에 성공하였습니다.")
            );
        } catch (Exception e) {
            log.error("리워드 사유별 총 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    CommonRes.success(null, "리워드 조회 중 오류가 발생했습니다: " + e.getMessage())
            );
        }
    }

    @GetMapping("/monthly-stats")
    @Operation(summary = "월별 리워드 통계", description = "월별 리워드 발급 통계 데이터를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(schema = @Schema(implementation = CommonRes.class))}),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = {@Content(schema = @Schema(implementation = ErrorRes.class))})
    })
    public ResponseEntity<CommonRes> getMonthlyRewardStats() {
        try {
            log.info("월별 리워드 통계 조회 요청");

            Object monthlyStats = rewardFetchService.fetchMonthlyStats();

            Map<String, Object> data = new HashMap<>();
            data.put("monthlyRewardStats", monthlyStats);

            return ResponseEntity.ok(
                    CommonRes.success(data, "월별 리워드 통계 조회에 성공하였습니다.")
            );
        } catch (Exception e) {
            log.error("월별 리워드 통계 조회 실패", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    CommonRes.success(null, "리워드 조회 중 오류가 발생했습니다: " + e.getMessage())
            );
        }
    }

    // 간단한 버전들 (에러 처리 최소화)
    @GetMapping("/by-reason/monthly-stats")
    public ResponseEntity<CommonRes> getRewardsByReasonMonthlyStats() {
        Object monthlyStats = rewardFetchService.fetchMonthlyStats();
        Map<String, Object> data = new HashMap<>();
        data.put("monthlyStatsByReason", monthlyStats);
        return ResponseEntity.ok(CommonRes.success(data, "리워드 사유별 월별 통계 조회에 성공하였습니다."));
    }

    @GetMapping("/history")
    public ResponseEntity<CommonRes> getRewardHistory() {
        Object rewardHistory = rewardFetchService.fetchFilteredRewards(null);
        Map<String, Object> data = new HashMap<>();
        data.put("rewardHistory", rewardHistory);
        return ResponseEntity.ok(CommonRes.success(data, "리워드 발급 내역 조회에 성공하였습니다."));
    }

    @GetMapping("")
    public ResponseEntity<CommonRes> getRewards() {
        Object rewardList = rewardFetchService.fetchFilteredRewards(null);
        Map<String, Object> data = new HashMap<>();
        data.put("rewardList", rewardList);
        return ResponseEntity.ok(CommonRes.success(data, "리워드 목록 조회에 성공하였습니다."));
    }
}