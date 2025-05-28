package com.modive.adminservice.api.reward.service;

import java.util.Map;

/**
 * 관리자 리워드 서비스 인터페이스
 */
public interface AdminRewardService {

    /**
     * 리워드 요약 통계 조회
     */
    Map<String, Object> getRewardSummary();

    /**
     * 발급 사유별 총 통계 조회
     */
    Map<String, Object> getRewardsByReasonTotal();

    /**
     * 리워드 사유별 월별 통계 조회
     */
    Map<String, Object> getRewardsByReasonMonthlyStats();

    /**
     * 월별 리워드 통계 조회
     */
    Map<String, Object> getMonthlyRewardStats();

    /**
     * 최근 리워드 발급 내역 조회
     */
    Map<String, Object> getRewardHistory();

    /**
     * 리워드 목록 조회 (필터링)
     */
    Map<String, Object> getRewards();
}