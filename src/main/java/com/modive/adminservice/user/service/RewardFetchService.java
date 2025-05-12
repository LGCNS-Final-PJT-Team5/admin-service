package com.modive.adminservice.user.service;

import com.modive.adminservice.external.client.reward.dto.req.RewardFilterReq;
import com.modive.adminservice.external.client.reward.dto.res.RewardFilterItem;

import java.util.List;

public interface RewardFetchService {
    List<RewardFilterItem> fetchRewardFilter(RewardFilterReq req);
}
