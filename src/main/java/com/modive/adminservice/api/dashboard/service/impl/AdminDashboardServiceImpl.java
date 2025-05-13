package com.modive.adminservice.api.dashboard.service.impl;

import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;
import com.modive.adminservice.api.dashboard.service.AdminDashboardService;
import com.modive.adminservice.external.dashboard.dto.res.DCTotalCntAndRateItem;
import com.modive.adminservice.external.dashboard.service.DashboardFetchService;
import com.modive.adminservice.external.reward.dto.res.RCRewardTotalCntAndRateItem;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import com.modive.adminservice.external.user.dto.res.UCTotalCntAndRateItem;
import com.modive.adminservice.external.user.service.UserFetchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private UserFetchService userFetchService;
    private DashboardFetchService dashboardFetchService;
    private RewardFetchService rewardFetchService;

    @Override
    public Map<String, TotalCntAndRateItem> getDashboardStatistics() {
        UCTotalCntAndRateItem userTotalCountAndChangeRate = userFetchService.fetchUserTotalCountAndChangeRate();
        TotalCntAndRateItem totalUsers = TotalCntAndRateItem.builder()
                .value(userTotalCountAndChangeRate.getValue())
                .changeRate(userTotalCountAndChangeRate.getChangeRate())
                .build();

        UCTotalCntAndRateItem deviceTotalCountAndChangeRate = userFetchService.fetchDevicesTotalCountAndChangeRate();
        TotalCntAndRateItem totalDevices = TotalCntAndRateItem.builder()
                .value(deviceTotalCountAndChangeRate.getValue())
                .changeRate(deviceTotalCountAndChangeRate.getChangeRate())
                .build();

        DCTotalCntAndRateItem driveTotalCountAndChangeRate = dashboardFetchService.fetchDriveTotalCntAndRate();
        TotalCntAndRateItem totalDrives = TotalCntAndRateItem.builder()
                .value(driveTotalCountAndChangeRate.getValue())
                .changeRate(driveTotalCountAndChangeRate.getChangeRate())
                .build();

        RCRewardTotalCntAndRateItem rewardTotalCntAndRateItem = rewardFetchService.fetchTotalIssuedRewards();
        TotalCntAndRateItem totalIssuedRewards = TotalCntAndRateItem.builder()
                .value(rewardTotalCntAndRateItem.getValue())
                .changeRate(rewardTotalCntAndRateItem.getChangeRate())
                .build();

        Map<String, TotalCntAndRateItem> dashboardStatistics = new HashMap<>();
        dashboardStatistics.put("totalUsers", totalUsers);
        dashboardStatistics.put("totalDevices", totalDevices);
        dashboardStatistics.put("totalDrives", totalDrives);
        dashboardStatistics.put("totalIssuedRewards", totalIssuedRewards);

        return dashboardStatistics;
    }

    @Override
    public List<MonthlyDrivesItem> getMonthlyDrivesStatistics() {
        List<MonthlyDrivesItem> monthlyDrivesStatistics = dashboardFetchService.fetchMonthlyDrivesStatistics().stream()
                .map(item -> MonthlyDrivesItem.builder()
                        .year(item.getYear())
                        .month(item.getMonth())
                        .count(item.getCount())
                        .build()
                )
                .collect(Collectors.toList());
        return monthlyDrivesStatistics;
    }
}
