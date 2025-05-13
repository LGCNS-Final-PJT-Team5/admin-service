package com.modive.adminservice.api.user.service;

import com.modive.adminservice.external.client.dashboard.dto.res.DCDriveListItem;

import java.util.List;
import java.util.Map;

public interface DashboardFetchService {
    Map<Long, Integer> fetchDriveCountMap(List<Long> userIds);
    List<DCDriveListItem> fetchDCDriveListByUserId(Long userId);
}
