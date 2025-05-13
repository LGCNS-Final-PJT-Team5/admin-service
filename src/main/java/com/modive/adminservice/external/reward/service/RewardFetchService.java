package com.modive.adminservice.external.reward.service;

import com.modive.adminservice.external.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.reward.dto.res.RCRewardFilterItem;
import com.modive.adminservice.external.reward.dto.res.RCRewardTotalCntAndRateItem;

import java.util.List;
import java.util.Map;

public interface RewardFetchService {
    List<RCRewardFilterItem> fetchRewardFilter(RCRewardFilterReq req);
    Map<Long, Integer> fetchRewardMapByDrive(RCRewardByDriveReq req);
    RCRewardTotalCntAndRateItem fetchTotalIssuedRewards();
}
