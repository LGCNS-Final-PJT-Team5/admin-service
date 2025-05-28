package com.modive.adminservice.api.reward.service;

import com.modive.adminservice.external.reward.client.RewardClient;
import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.global.dto.res.CommonRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 리워드 외부 API 호출 서비스
 * RewardClient를 통해 reward-service와 통신
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class RewardFetchService {

    private final RewardClient rewardClient;

    /**
     * 총 발급된 리워드 수 조회
     */
    public Object fetchTotalIssuedRewards() {
        try {
            log.info("총 발급된 리워드 통계 조회 요청");
            CommonRes response = rewardClient.getTotalIssuedRewards();
            return response.getData();
        } catch (Exception e) {
            log.error("총 발급된 리워드 통계 조회 실패", e);
            throw new RuntimeException("리워드 통계 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 월별 발급 리워드 수 조회
     */
    public Object fetchMonthlyIssuedRewards() {
        try {
            log.info("월별 발급 리워드 수 조회 요청");
            CommonRes response = rewardClient.getMonthlyIssuedRewards();
            return response.getData();
        } catch (Exception e) {
            log.error("월별 발급 리워드 수 조회 실패", e);
            throw new RuntimeException("월별 리워드 통계 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 하루 평균 발급 수 조회
     */
    public Object fetchDailyAverageIssued() {
        try {
            log.info("하루 평균 발급 수 조회 요청");
            CommonRes response = rewardClient.getDailyAverageIssued();
            return response.getData();
        } catch (Exception e) {
            log.error("하루 평균 발급 수 조회 실패", e);
            throw new RuntimeException("하루 평균 통계 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 사용자당 평균 발급 수 조회
     */
    public Object fetchPerUserAverageIssued() {
        try {
            log.info("사용자당 평균 발급 수 조회 요청");
            CommonRes response = rewardClient.getPerUserAverageIssued();
            return response.getData();
        } catch (Exception e) {
            log.error("사용자당 평균 발급 수 조회 실패", e);
            throw new RuntimeException("사용자당 평균 통계 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 발급 사유별 총 통계 조회
     */
    public Object fetchRewardsByReasonTotal() {
        try {
            log.info("발급 사유별 총 통계 조회 요청");
            CommonRes response = rewardClient.getRewardsByReasonTotal();
            return response.getData();
        } catch (Exception e) {
            log.error("발급 사유별 총 통계 조회 실패", e);
            throw new RuntimeException("사유별 통계 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 월별 씨간 지급 통계 조회
     */
    public Object fetchMonthlyStats() {
        try {
            log.info("월별 씨간 지급 통계 조회 요청");
            CommonRes response = rewardClient.getMonthlyStats();
            return response.getData();
        } catch (Exception e) {
            log.error("월별 씨간 지급 통계 조회 실패", e);
            throw new RuntimeException("월별 통계 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * 리워드 필터링 조회
     */
    public Object fetchFilteredRewards(RCRewardFilterReq params) {
        try {
            log.info("리워드 필터링 조회 요청");
            CommonRes response = rewardClient.filterReward(params);
            return response.getData();
        } catch (Exception e) {
            log.error("리워드 필터링 조회 실패", e);
            throw new RuntimeException("리워드 목록 조회 중 오류가 발생했습니다.", e);
        }
    }

    /**
     * Drive별 리워드 조회
     */
    public Object fetchRewardByDrive(RCRewardByDriveReq req) {
        try {
            log.info("Drive별 리워드 조회 요청");
            CommonRes response = rewardClient.getRewardByDrive(req);
            return response.getData();
        } catch (Exception e) {
            log.error("Drive별 리워드 조회 실패", e);
            throw new RuntimeException("Drive별 리워드 조회 중 오류가 발생했습니다.", e);
        }
    }
}