package com.modive.adminservice.api.dashboard.service.impl;

import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.dto.res.UserMonthlyItem;
import com.modive.adminservice.api.dashboard.dto.res.UserStatisticsSummaryItem;
import com.modive.adminservice.external.dashboard.dto.res.DCMonthlyDriveItem;
import com.modive.adminservice.external.dashboard.service.DashboardFetchService;
import com.modive.adminservice.external.user.dto.res.UCUserMonthlyItem;
import com.modive.adminservice.external.user.dto.res.UCUserStatisticsSummaryItem;
import com.modive.adminservice.external.user.service.UserFetchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class AdminDashboardServiceImplTest {

    @Mock
    private UserFetchService userFetchService;

    @InjectMocks
    private AdminDashboardServiceImpl adminDashboardService;

    @Mock
    private DashboardFetchService dashboardFetchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * getMonthlyUserStatistics 테스트 - 정상 사용자 통계 및 트렌드 조회
     * 시나리오: 사용자 통계(summary)와 월별 사용자 트렌드(userTrend)를 정상적으로 조회하는 경우
     * 기대:
     *  - summary: 신규 유저 수, 증가율, 이탈률이 정확히 매핑됨
     *  - userTrends: 월별 유저 데이터가 변환되어 응답됨
     */
    @Test
    void testGetMonthlyUserStatistics() {
        // Mocking the data
        UCUserStatisticsSummaryItem summaryMock = new UCUserStatisticsSummaryItem();
        summaryMock.setLastWeekNewUsers(150L);
        summaryMock.setMonthlyUserGrowthRate(5.2);
        summaryMock.setChurnRate(2.3);

        List<UCUserMonthlyItem> userTrendMockData = new ArrayList<>();
        UCUserMonthlyItem monthlyData = new UCUserMonthlyItem();
        monthlyData.setYear(2025);
        monthlyData.setMonth(6);
        monthlyData.setNewUsers(200L);
        monthlyData.setActiveUsers(180L);
        monthlyData.setChurnedUsers(20L);
        userTrendMockData.add(monthlyData);

        Map<String, Object> serviceDataMock = new HashMap<>();
        serviceDataMock.put("summary", summaryMock);
        serviceDataMock.put("userTrend", userTrendMockData);

        when(userFetchService.fetchUserStatistics()).thenReturn(serviceDataMock);

        // Executing the method
        Map<String, Object> result = adminDashboardService.getMonthlyUserStatistics();

        // Assertions
        UserStatisticsSummaryItem summaryResult = (UserStatisticsSummaryItem) result.get("summary");
        assertEquals(150, summaryResult.getLastWeekNewUsers());
        assertEquals(5.2, summaryResult.getMonthlyUserGrowthRate());
        assertEquals(2.3, summaryResult.getChurnRate());

        List<UserMonthlyItem> userTrendsResult = (List<UserMonthlyItem>) result.get("userTrends");
        assertEquals(1, userTrendsResult.size());
        UserMonthlyItem firstTrend = userTrendsResult.get(0);
        assertEquals(2025, firstTrend.getYear());
        assertEquals(6, firstTrend.getMonth());
        assertEquals(200, firstTrend.getNewUsers());
        assertEquals(180, firstTrend.getActiveUsers());
        assertEquals(20, firstTrend.getChurnedUsers());
    }

    /**
     * getMonthlyDrivesStatistics 테스트 - 정상 월별 주행 수 통계 조회
     * 시나리오: Dashboard 서비스로부터 월별 주행 횟수 데이터를 받아올 수 있는 경우
     * 기대:
     *  - MonthlyDrivesItem 리스트로 매핑되고, 각 필드(year, month, count)가 정확히 변환됨
     */
    @Test
    void testGetMonthlyDrivesStatistics() {
        // Mocking the data
        List<DCMonthlyDriveItem> mockDriveData = new ArrayList<>();
        DCMonthlyDriveItem driveData = new DCMonthlyDriveItem();
        driveData.setYear(2025);
        driveData.setMonth(5);
        driveData.setCount(150L);
        mockDriveData.add(driveData);

        when(dashboardFetchService.fetchMonthlyDrivesStatistics()).thenReturn(mockDriveData);

        // Executing the method
        List<MonthlyDrivesItem> result = adminDashboardService.getMonthlyDrivesStatistics();

        // Assertions
        assertEquals(1, result.size());
        MonthlyDrivesItem firstDrive = result.get(0);
        assertEquals(2025, firstDrive.getYear());
        assertEquals(5, firstDrive.getMonth());
        assertEquals(150, firstDrive.getCount());
    }
}