package com.modive.adminservice.external.dashboard.service;

import com.modive.adminservice.external.dashboard.dto.res.DCDriveListItem;
import com.modive.adminservice.external.dashboard.dto.res.DCMonthlyDriveItem;
import com.modive.adminservice.external.dashboard.dto.res.DCTotalCntAndRateItem;

import java.util.List;
import java.util.Map;

public interface DashboardFetchService {
    Map<Long, Integer> fetchDriveCountMap(List<Long> userIds);
    List<DCDriveListItem> fetchDriveListByUserId(Long userId);
    DCTotalCntAndRateItem fetchDriveTotalCntAndRate();
    List<DCMonthlyDriveItem> fetchMonthlyDrivesStatistics();
}
