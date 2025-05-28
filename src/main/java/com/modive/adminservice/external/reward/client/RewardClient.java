package com.modive.adminservice.external.reward.client;

import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.global.dto.res.CommonRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * reward-service와 통신하는 Feign Client.
 */
@FeignClient(name="reward-service", url="http://localhost:8083")
public interface RewardClient {

    /**
     * 발급 사유, 유저 ID, 발급 날짜를 기준으로 필터링
     *
     * @param params 필터링 데이터, 페이지네이션에 필요한 데이터
     * @return CommonRes 형태의 필터링 결과 응답
     */
    @GetMapping("/reward/filter")
    CommonRes filterReward(@SpringQueryMap RCRewardFilterReq params);

    /**
     * drive ID를 기준으로 리워드 적립 내용 조회
     *
     * @param req drive ID 리스트
     * @return drive ID별 리워드 적립 데이터
     */
    @PostMapping("/reward/by-drive")
    CommonRes getRewardByDrive(@RequestBody RCRewardByDriveReq req);

    /**
     * 총 발급된 리워드 수 및 증감률 조회
     */
    @GetMapping("/reward/total-issued")
    CommonRes getTotalIssuedRewards();

    /**
     * 월간 발급된 리워드 수 및 증감률 조회
     */
    @GetMapping("/reward/monthly-issued")
    CommonRes getMonthlyIssuedRewards();

    /**
     * 일평균 발급된 리워드 수 및 증감률 조회
     */
    @GetMapping("/reward/daily-average-issued")
    CommonRes getDailyAverageIssued();

    /**
     * 사용자당 평균 발급된 리워드 수 및 증감률 조회
     */
    @GetMapping("/reward/per-user-average-issued")
    CommonRes getPerUserAverageIssued();

    /**
     * 발급 사유별 총 통계 조회
     */
    @GetMapping("/reward/by-reason/total")
    CommonRes getRewardsByReasonTotal();

    /**
     * 월별 리워드 통계 조회
     */
    @GetMapping("/reward/monthly-stats")
    CommonRes getMonthlyStats();

    /**
     * 전체 씨앗 발급 내역 조회
     */
    @GetMapping("/reward/history/all")
    CommonRes getAllRewardHistory(@SpringQueryMap Pageable pageable);  // 👈 추가!

}