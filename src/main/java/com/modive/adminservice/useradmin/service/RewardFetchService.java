package com.modive.adminservice.useradmin.service;

import com.modive.adminservice.external.client.reward.dto.req.RewardByDriveReq;
import com.modive.adminservice.external.client.reward.dto.req.RewardFilterReq;
import com.modive.adminservice.external.client.reward.dto.res.RewardByDriveItem;
import com.modive.adminservice.external.client.reward.dto.res.RewardFilterItem;

import java.util.List;
import java.util.Map;

public interface RewardFetchService {
    List<RewardFilterItem> fetchRewardFilter(RewardFilterReq req);
    Map<Long, Integer> fetchRewardMapByDrive(RewardByDriveReq req);
}
