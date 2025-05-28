package com.modive.adminservice.external.dashboard.service;

import com.modive.adminservice.external.dashboard.dto.res.DCDriveListItem;
import com.modive.adminservice.external.dashboard.dto.res.DCDriveListResData;
import com.modive.adminservice.external.dashboard.dto.res.DCMonthlyDriveItem;
import com.modive.adminservice.external.dashboard.dto.res.DCTotalCntAndRateItem;

import java.util.List;
import java.util.Map;

public interface DashboardFetchService {
    Map<Long, Integer> fetchDriveCountMap(List<Long> userIds);
    DCDriveListResData fetchDriveListByUserId(Long userId, int pageSize, String startTime, String driveId);
    DCTotalCntAndRateItem fetchDriveTotalCntAndRate();
    List<DCMonthlyDriveItem> fetchMonthlyDrivesStatistics();
}
