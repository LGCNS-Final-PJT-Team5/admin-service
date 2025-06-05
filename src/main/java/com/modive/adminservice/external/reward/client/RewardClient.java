package com.modive.adminservice.external.reward.client;

import com.modive.adminservice.api.reward.dto.*;
import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.dto.res.RCRewardByDriveResData;
import com.modive.adminservice.external.reward.dto.res.RCRewardFilterResData;
import com.modive.adminservice.external.reward.dto.res.RCTotalRewardResData;
import com.modive.adminservice.global.dto.res.CommonRes;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

/**
 * reward-service와 통신하는 Feign Client.
 */

@Lazy
@FeignClient(name="reward-service",
        url= "${service.reward.url}")
public interface RewardClient {

    //REWARD FOR USER REQUEST
    /**
     * 발급 사유, 유저 ID, 발급 날짜를 기준으로 필터링
     *
     * @param params 필터링 데이터, 페이지네이션에 필요한 데이터
     * @return CommonRes 형태의 필터링 결과 응답
     */
    @GetMapping("/reward/filter")
    CommonRes<RCRewardFilterResData> filterReward(@RequestHeader("X-USER-ID") String userId, @SpringQueryMap RCRewardFilterReq params);

    /**
     * drive ID를 기준으로 리워드 적립 내용 조회
     *
     * @param req drive ID 리스트
     * @return drive ID별 리워드 적립 데이터
     */
    @PostMapping("/reward/by-drive")
    CommonRes<RCRewardByDriveResData> getRewardByDrive(@RequestHeader("X-USER-ID") String userId, @RequestBody RCRewardByDriveReq req);

    /**
     * 총 발급된 리워드 수 및 증감률 조회
     */
    @GetMapping("/reward/stats/total")
    CommonRes<RCTotalRewardResData> getTotalIssuedRewards(@RequestHeader("X-USER-ID") String userId);

    // REWARD FOR REWARD REQUEST
    @GetMapping("/reward/stats/total")
    CommonRes<RewardsSummaryDto> fetchRewardSummaryTotal(@RequestHeader("X-USER-ID") String userId);
    @GetMapping("/reward/stats/monthly")
    CommonRes<RewardsSummaryDto> fetchRewardSummaryMonthly(@RequestHeader("X-USER-ID") String userId);
    @GetMapping("/reward/stats/daily")
    CommonRes<RewardsSummaryDto> fetchRewardSummaryDaily(@RequestHeader("X-USER-ID") String userId);
    @GetMapping("/reward/stats/per-user")
    CommonRes<RewardsSummaryDto> fetchRewardSummaryPerUser(@RequestHeader("X-USER-ID") String userId);

    @GetMapping("/reward/by-reason/total")
    CommonRes<RewardByReasonTotalDto> fetchRewardByReasonTotal(@RequestHeader("X-USER-ID") String userId);
    @GetMapping("/reward/by-reason/monthly")
    CommonRes<RewardByReasonMonthDto> fetchRewardByReasonMonth(
            @RequestHeader("X-USER-ID") String userId,
            @RequestParam String month
    );
    @GetMapping("/reward/monthly-stats")
    CommonRes<RewardMonthDto> fetchRewardMonth(@RequestHeader("X-USER-ID") String userId);
    @GetMapping("/reward/history/all")
    CommonRes<RewardHistoryDto> fetchRewardHistory(
            @RequestHeader("X-USER-ID") String userId,
            @RequestParam int page,
            @RequestParam int size
    );
    @GetMapping("/reward/filter")
    CommonRes<RewardFilterDto> fetchRewardFilter(
            @RequestHeader("X-USER-ID") String userId,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam int page,
            @RequestParam int size
    );
}
