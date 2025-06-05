package com.modive.adminservice.api.dashboard.service.impl;

import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;
import com.modive.adminservice.api.dashboard.dto.res.UserMonthlyItem;
import com.modive.adminservice.api.dashboard.dto.res.UserStatisticsSummaryItem;
import com.modive.adminservice.api.dashboard.service.AdminDashboardService;
import com.modive.adminservice.external.dashboard.dto.res.DCTotalCntAndRateItem;
import com.modive.adminservice.external.dashboard.service.DashboardFetchService;
import com.modive.adminservice.external.reward.dto.res.RCRewardTotalCntAndRateItem;
import com.modive.adminservice.external.reward.service.RewardFetchService;
import com.modive.adminservice.external.user.dto.res.UCTotalCntAndRateItem;
import com.modive.adminservice.external.user.dto.res.UCUserMonthlyItem;
import com.modive.adminservice.external.user.dto.res.UCUserStatisticsSummaryItem;
import com.modive.adminservice.external.user.service.UserFetchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {
    private final UserFetchService userFetchService;
    private final DashboardFetchService dashboardFetchService;
    private final RewardFetchService rewardFetchService;

    /**
     * 대시보드 통계 조회
     */
    @Override
    public Map<String, TotalCntAndRateItem> getDashboardStatistics(String userId) {
        Map<String, TotalCntAndRateItem> dashboardStatistics = new HashMap<>();

        // 사용자 수 통계
        try {
            UCTotalCntAndRateItem userTotalCountAndChangeRate = userFetchService.fetchUserTotalCountAndChangeRate();
            TotalCntAndRateItem totalUsers = TotalCntAndRateItem.builder()
                    .value(userTotalCountAndChangeRate.getValue())
                    .changeRate(userTotalCountAndChangeRate.getChangeRate())
                    .build();
            dashboardStatistics.put("totalUsers", totalUsers);
        } catch (Exception e) {
            log.error("Failed to fetch user statistics: {}", e.getMessage(), e);
            // 기본값으로 대체
            dashboardStatistics.put("totalUsers", TotalCntAndRateItem.builder()
                    .value(0L)
                    .changeRate(0.0)
                    .build());
        }

        // 디바이스 수 통계
        try {
            UCTotalCntAndRateItem deviceTotalCountAndChangeRate = userFetchService.fetchDevicesTotalCountAndChangeRate();
            TotalCntAndRateItem totalDevices = TotalCntAndRateItem.builder()
                    .value(deviceTotalCountAndChangeRate.getValue())
                    .changeRate(deviceTotalCountAndChangeRate.getChangeRate())
                    .build();
            dashboardStatistics.put("totalDevices", totalDevices);
        } catch (Exception e) {
            log.error("Failed to fetch device statistics: {}", e.getMessage(), e);
            dashboardStatistics.put("totalDevices", TotalCntAndRateItem.builder()
                    .value(0L)
                    .changeRate(0.0)
                    .build());
        }

        // 주행 수 통계
        try {
            DCTotalCntAndRateItem driveTotalCountAndChangeRate = dashboardFetchService.fetchDriveTotalCntAndRate();
            TotalCntAndRateItem totalDrives = TotalCntAndRateItem.builder()
                    .value(driveTotalCountAndChangeRate.getValue())
                    .changeRate(driveTotalCountAndChangeRate.getChangeRate())
                    .build();
            dashboardStatistics.put("totalDrives", totalDrives);
        } catch (Exception e) {
            log.error("Failed to fetch drive statistics: {}", e.getMessage(), e);
            dashboardStatistics.put("totalDrives", TotalCntAndRateItem.builder()
                    .value(0L)
                    .changeRate(0.0)
                    .build());
        }

        // 리워드 통계
        try {
            RCRewardTotalCntAndRateItem rewardTotalCntAndRateItem = rewardFetchService.fetchTotalIssuedRewards(userId);
            TotalCntAndRateItem totalIssuedRewards = TotalCntAndRateItem.builder()
                    .value(rewardTotalCntAndRateItem.getValue())
                    .changeRate(rewardTotalCntAndRateItem.getChangeRate())
                    .build();
            dashboardStatistics.put("totalIssuedRewards", totalIssuedRewards);
        } catch (Exception e) {
            log.error("Failed to fetch reward statistics: {}", e.getMessage(), e);
            dashboardStatistics.put("totalIssuedRewards", TotalCntAndRateItem.builder()
                    .value(0L)
                    .changeRate(0.0)
                    .build());
        }

        return dashboardStatistics;
    }

    /**
     * 월별 운전 횟수 추이 조회
     */
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

    /**
     * 월별 사용자 추이 조회
     */
    @Override
    public Map<String, Object> getMonthlyUserStatistics() {
        Map<String, Object> resData = userFetchService.fetchUserStatistics();

        UCUserStatisticsSummaryItem summaryRes = (UCUserStatisticsSummaryItem) resData.get("summary");
        UserStatisticsSummaryItem summary = UserStatisticsSummaryItem.builder()
                .lastWeekNewUsers(summaryRes.getLastWeekNewUsers())
                .monthlyUserGrowthRate(summaryRes.getMonthlyUserGrowthRate())
                .churnRate(summaryRes.getChurnRate())
                .build();

        List<UserMonthlyItem> userTrends = ((List<UCUserMonthlyItem>) resData.get("userTrend")).stream()
                .map(item -> UserMonthlyItem.builder()
                        .year(item.getYear())
                        .month(item.getMonth())
                        .newUsers(item.getNewUsers())
                        .activeUsers(item.getActiveUsers())
                        .churnedUsers(item.getChurnedUsers())
                        .build())
                .collect(Collectors.toList());

        Map result = new HashMap<>();
        result.put("userTrends", userTrends);
        result.put("summary", summary);

        return result;
    }
}
