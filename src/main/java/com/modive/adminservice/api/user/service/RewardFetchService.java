package com.modive.adminservice.api.user.service;

import com.modive.adminservice.external.client.reward.dto.req.RCRewardByDriveReq;
import com.modive.adminservice.external.client.reward.dto.req.RCRewardFilterReq;
import com.modive.adminservice.external.client.reward.dto.res.RCRewardFilterItem;

import java.util.List;
import java.util.Map;

public interface RewardFetchService {
    List<RCRewardFilterItem> fetchRewardFilter(RCRewardFilterReq req);
    Map<Long, Integer> fetchRewardMapByDrive(RCRewardByDriveReq req);
}
