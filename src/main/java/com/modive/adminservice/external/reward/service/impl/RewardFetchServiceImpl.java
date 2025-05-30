package com.modive.adminservice.external.reward.service.impl;

import com.modive.adminservice.api.reward.dto.*;
import com.modive.adminservice.external.reward.client.RewardClient;
import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.dto.res.*;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class RewardFetchServiceImpl implements RewardFetchService {
    private final RewardClient rewardClient;

    //<editor-folder desc="REWARD FOR USER REQUEST">
    /**
     * 리워드 서비스에서 씨앗 발급 내역 필터링
     *
     * @param req 유저 ID, 발급 사유 등의 필터링 요소 및 페이지네이션에 사용될 데이터
     * @return 필터링 결과
     */
    @Override
    public List<RCRewardFilterItem> fetchRewardFilter(String userId, RCRewardFilterReq req) {
        CommonRes<RCRewardFilterResData> res = rewardClient.filterReward(userId, req);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.filterReward - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData().getSearchResult();
    }

    /**
     * 리워드 서비스에서 Drive ID별 리워드 적립 내용 조회
     *
     * @param req drive ID 리스트
     * @return drive ID별 리워드 조회 결과
     */
    @Override
    public Map<String, Integer> fetchRewardMapByDrive(String userId, RCRewardByDriveReq req) {
        CommonRes<RCRewardByDriveResData> res = rewardClient.getRewardByDrive(userId, req);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getRewardByDrive(req = {}) - response or data is null", req.toString());
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        Map<String, Integer> rewardMap = new HashMap<>();
        for (RCRewardByDriveItem item : res.data.getRewardsByDrive()) {
            rewardMap.put(item.getDriveId(), item.getReward());
        }

        return rewardMap;
    }

    /**
     * 리워드 서비스에서 발급된 리워드 합계 및 증감률 조회
     */
    @Override
    public RCRewardTotalCntAndRateItem fetchTotalIssuedRewards(String userId) {
        CommonRes<RCTotalRewardResData> res = rewardClient.getTotalIssuedRewards(userId);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }

        return res.getData().getTotalIssued();
    }

    //</editor-folder desc="REWARD FOR USER REQUEST">


    //<editor-folder desc="REWARD FOR REWARD REQUEST">
    @Override
    public RewardsSummaryDto fetchRewardSummary(String userId) {
        CommonRes<RewardsSummaryDto> total = rewardClient.fetchRewardSummaryTotal(userId);
        CommonRes<RewardsSummaryDto> monthly = rewardClient.fetchRewardSummaryMonthly(userId);
        CommonRes<RewardsSummaryDto> daily = rewardClient.fetchRewardSummaryDaily(userId);
        CommonRes<RewardsSummaryDto> perUser = rewardClient.fetchRewardSummaryPerUser(userId);

        CommonRes<RewardsSummaryDto> res = new CommonRes<RewardsSummaryDto>();
        RewardsSummaryDto merged = new RewardsSummaryDto();

        if (total.getData() != null) {
            merged.setTotalIssued(total.getData().getTotalIssued());
        }
        if (monthly.getData() != null) {
            merged.setMonthlyIssued(monthly.getData().getMonthlyIssued());
        }
        if (daily.getData() != null) {
            merged.setDailyAverageIssued(daily.getData().getDailyAverageIssued());
        }
        if (perUser.getData() != null) {
            merged.setPerUserAverageIssued(perUser.getData().getPerUserAverageIssued());
        }
        res.setData(merged);

        if (res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData();
    }

    @Override
    public RewardByReasonTotalDto fetchRewardByReasonTotal(String userId) {
        CommonRes<RewardByReasonTotalDto> res = rewardClient.fetchRewardByReasonTotal(userId);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData();
    }

    @Override
    public RewardByReasonMonthDto fetchRewardByReasonMonth(String userId, int year, int month) {
        String req = String.format("%d-%02d", year, month);
        CommonRes<RewardByReasonMonthDto> res = rewardClient.fetchRewardByReasonMonth(userId, req);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData();
    }

    @Override
    public RewardMonthDto fetchRewardMonth(String userId) {
        CommonRes<RewardMonthDto> res = rewardClient.fetchRewardMonth(userId);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData();
    }

    @Override
    public RewardHistoryDto fetchRewardHistory(String userId, int page, int size) {
        CommonRes<RewardHistoryDto> res = rewardClient.fetchRewardHistory(userId, page, size);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData();
    }

    @Override
    public RewardFilterDto fetchRewardFilter(String userId, String email, String description, LocalDate startDate, LocalDate endDate, int page, int size) {
        CommonRes<RewardFilterDto> res = rewardClient.fetchRewardFilter(userId, email, description, startDate, endDate, page, size);
        if (res == null || res.getData() == null) {
            log.warn("RewardClient.getTotalIssuedRewards() - response or data is null");
            throw new RestApiException(ErrorCode.FEIGN_DATA_MISSING);
        }
        return res.getData();
    }
    //</editor-folder desc="REWARD FOR REWARD REQUEST">

}
