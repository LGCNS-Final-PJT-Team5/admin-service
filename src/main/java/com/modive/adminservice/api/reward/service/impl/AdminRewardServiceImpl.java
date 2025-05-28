package com.modive.adminservice.api.reward.service.impl;

import com.modive.adminservice.api.reward.service.AdminRewardService;
import com.modive.adminservice.api.reward.service.RewardFetchService;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 관리자 리워드 서비스 구현체
 * 실제 리워드 서비스 API 호출하여 데이터 제공
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminRewardServiceImpl implements AdminRewardService {

    private final RewardFetchService rewardFetchService;

    /**
     * 리워드 요약 통계 조회
     * 실제 리워드 서비스 API들을 조합하여 요약 데이터 생성
     */
    @Override
    public Map<String, Object> getRewardSummary() {
        log.info("리워드 요약 통계 조회 시작");

        try {
            // 실제 API 호출
            Object totalIssued = rewardFetchService.fetchTotalIssuedRewards();
            Object monthlyIssued = rewardFetchService.fetchMonthlyIssuedRewards();
            Object dailyAverage = rewardFetchService.fetchDailyAverageIssued();
            Object perUserAverage = rewardFetchService.fetchPerUserAverageIssued();

            Map<String, Object> summary = new HashMap<>();
            summary.put("totalIssued", totalIssued);
            summary.put("monthlyIssued", monthlyIssued);
            summary.put("dailyAverage", dailyAverage);
            summary.put("perUserAverage", perUserAverage);

            log.info("리워드 요약 통계 조회 완료");
            return summary;

        } catch (Exception e) {
            log.error("리워드 요약 통계 조회 중 오류 발생", e);
            // 오류 발생 시 기본 응답
            Map<String, Object> errorSummary = new HashMap<>();
            errorSummary.put("error", "리워드 서비스 연동 중 오류가 발생했습니다.");
            return errorSummary;
        }
    }

    /**
     * 발급 사유별 총 통계 조회
     * 실제 리워드 서비스 API 호출
     */
    @Override
    public Map<String, Object> getRewardsByReasonTotal() {
        log.info("사유별 총 통계 조회 시작");

        try {
            // 실제 API 호출
            Object reasonStats = rewardFetchService.fetchRewardsByReasonTotal();

            Map<String, Object> result = new HashMap<>();
            result.put("byReasonStats", reasonStats);

            log.info("사유별 총 통계 조회 완료");
            return result;

        } catch (Exception e) {
            log.error("사유별 총 통계 조회 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "사유별 통계 조회 중 오류가 발생했습니다.");
            return errorResult;
        }
    }

    /**
     * 리워드 사유별 월별 통계 조회
     * 현재는 기본 월별 통계 활용
     */
    @Override
    public Map<String, Object> getRewardsByReasonMonthlyStats() {
        log.info("사유별 월별 통계 조회 시작");

        try {
            // 월별 통계 API 활용
            Object monthlyStats = rewardFetchService.fetchMonthlyStats();

            Map<String, Object> result = new HashMap<>();
            result.put("monthlyStatsByReason", monthlyStats);
            result.put("note", "사유별 세분화는 추후 API 추가 예정");

            log.info("사유별 월별 통계 조회 완료");
            return result;

        } catch (Exception e) {
            log.error("사유별 월별 통계 조회 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "월별 통계 조회 중 오류가 발생했습니다.");
            return errorResult;
        }
    }

    /**
     * 월별 리워드 통계 조회
     * 실제 리워드 서비스 API 호출
     */
    @Override
    public Map<String, Object> getMonthlyRewardStats() {
        log.info("월별 통계 조회 시작");

        try {
            // 실제 API 호출
            Object monthlyStats = rewardFetchService.fetchMonthlyStats();
            Object monthlyIssued = rewardFetchService.fetchMonthlyIssuedRewards();

            Map<String, Object> result = new HashMap<>();
            result.put("monthlyStats", monthlyStats);
            result.put("monthlyIssued", monthlyIssued);

            log.info("월별 통계 조회 완료");
            return result;

        } catch (Exception e) {
            log.error("월별 통계 조회 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "월별 통계 조회 중 오류가 발생했습니다.");
            return errorResult;
        }
    }

    /**
     * 최근 리워드 발급 내역 조회
     * 필터링 API 활용
     */
    @Override
    public Map<String, Object> getRewardHistory() {
        log.info("발급 내역 조회 시작");

        try {
            // 필터링 API로 최근 내역 조회 (빈 필터로 전체 조회)
            RCRewardFilterReq params = new RCRewardFilterReq();
            // TODO: 최근 날짜로 필터링 설정 가능
            Object filteredRewards = rewardFetchService.fetchFilteredRewards(params);

            Map<String, Object> result = new HashMap<>();
            result.put("rewardHistory", filteredRewards);

            log.info("발급 내역 조회 완료");
            return result;

        } catch (Exception e) {
            log.error("발급 내역 조회 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "발급 내역 조회 중 오류가 발생했습니다.");
            return errorResult;
        }
    }

    /**
     * 리워드 목록 조회 (필터링)
     * 기존 필터링 API 활용
     */
    @Override
    public Map<String, Object> getRewards() {
        log.info("리워드 목록 조회 시작");

        try {
            // 필터링 API 호출
            RCRewardFilterReq params = new RCRewardFilterReq();
            Object filteredRewards = rewardFetchService.fetchFilteredRewards(params);

            Map<String, Object> result = new HashMap<>();
            result.put("rewardList", filteredRewards);

            log.info("리워드 목록 조회 완료");
            return result;

        } catch (Exception e) {
            log.error("리워드 목록 조회 중 오류 발생", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("error", "리워드 목록 조회 중 오류가 발생했습니다.");
            return errorResult;
        }
    }
}