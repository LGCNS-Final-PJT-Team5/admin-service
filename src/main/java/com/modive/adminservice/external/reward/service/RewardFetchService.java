package com.modive.adminservice.external.reward.service;

import com.modive.adminservice.api.reward.dto.*;
import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.dto.res.RCRewardFilterItem;
import com.modive.adminservice.external.reward.dto.res.RCRewardTotalCntAndRateItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface RewardFetchService {

    //REWARD FOR USER REQUEST
    List<RCRewardFilterItem> fetchRewardFilter(Long userId, RCRewardFilterReq req);
    Map<Long, Integer> fetchRewardMapByDrive(Long userId, RCRewardByDriveReq req);
    RCRewardTotalCntAndRateItem fetchTotalIssuedRewards(Long userId);


    // REWARD FOR REWARD REQUEST
    RewardsSummaryDto fetchRewardSummary(String userId);
    RewardByReasonTotalDto fetchRewardByReasonTotal(String userId);
    RewardByReasonMonthDto fetchRewardByReasonMonth(String userId, int year, int month);
    RewardMonthDto fetchRewardMonth(String userId);
    RewardHistoryDto fetchRewardHistory(String userId, int page, int pageSize);
    RewardFilterDto fetchRewardFilter(String userId, String email, String description, LocalDate startDate, LocalDate endDate, int page, int size);
}
