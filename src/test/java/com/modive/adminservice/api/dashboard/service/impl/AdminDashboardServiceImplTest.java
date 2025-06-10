package com.modive.adminservice.api.dashboard.service.impl;

import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;
import com.modive.adminservice.api.dashboard.dto.res.UserMonthlyItem;
import com.modive.adminservice.api.dashboard.dto.res.UserStatisticsSummaryItem;
import com.modive.adminservice.external.dashboard.dto.res.DCMonthlyDriveItem;
import com.modive.adminservice.external.dashboard.dto.res.DCTotalCntAndRateItem;
import com.modive.adminservice.external.dashboard.service.DashboardFetchService;
import com.modive.adminservice.external.reward.dto.res.RCRewardTotalCntAndRateItem;
import com.modive.adminservice.external.reward.service.impl.RewardFetchServiceImpl;
import com.modive.adminservice.external.user.dto.res.UCTotalCntAndRateItem;
import com.modive.adminservice.external.user.dto.res.UCUserMonthlyItem;
import com.modive.adminservice.external.user.dto.res.UCUserStatisticsSummaryItem;
import com.modive.adminservice.external.user.service.UserFetchService;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminDashboardServiceImplTest {

    @Mock
    private UserFetchService userFetchService;

    @InjectMocks
    private AdminDashboardServiceImpl adminDashboardService;

    @Mock
    private DashboardFetchService dashboardFetchService;

    @Mock
    private RewardFetchServiceImpl rewardFetchService;

    private static final String TEST_USER_ID = "test-user-123";

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

    /**
     * getDashboardStatistics 테스트 - 모든 서비스가 정상 응답할 때 대시보드 통계 조회
     * 시나리오: 모든 서비스(User, Dashboard, Reward)로부터 필요한 통계 데이터를 정상적으로 받아올 수 있는 경우
     * 기대:
     *  - 반환된 Map에 모든 필요한 통계 항목(totalUsers, totalDevices, totalDrives, totalIssuedRewards)이 포함됨
     *  - 각 통계 항목의 value와 changeRate가 원본 데이터와 일치함
     *  - 모든 서비스의 조회 메서드가 한 번씩 호출됨
     */
    @Test
    @DisplayName("대시보드 통계 조회 성공")
    void getDashboardStatistics_whenAllServicesReturnData_shouldReturnCorrectMap() {
        // given
        // 사용자 수 통계 Mock 데이터
        UCTotalCntAndRateItem userCountItem = new UCTotalCntAndRateItem();
        userCountItem.setValue(1000L);
        userCountItem.setChangeRate(5.5);
        when(userFetchService.fetchUserTotalCountAndChangeRate()).thenReturn(userCountItem);

        // 디바이스 수 통계 Mock 데이터
        UCTotalCntAndRateItem deviceCountItem = new UCTotalCntAndRateItem();
        deviceCountItem.setValue(1500L);
        deviceCountItem.setChangeRate(7.2);
        when(userFetchService.fetchDevicesTotalCountAndChangeRate()).thenReturn(deviceCountItem);

        // 주행 수 통계 Mock 데이터
        DCTotalCntAndRateItem driveCountItem = new DCTotalCntAndRateItem();
        driveCountItem.setValue(3000L);
        driveCountItem.setChangeRate(10.8);
        when(dashboardFetchService.fetchDriveTotalCntAndRate()).thenReturn(driveCountItem);

        // 리워드 통계 Mock 데이터
        RCRewardTotalCntAndRateItem rewardCountItem = new RCRewardTotalCntAndRateItem();
        rewardCountItem.setValue(5000L);
        rewardCountItem.setChangeRate(3.2);
        when(rewardFetchService.fetchTotalIssuedRewards(TEST_USER_ID)).thenReturn(rewardCountItem);

        // when
        Map<String, TotalCntAndRateItem> result = adminDashboardService.getDashboardStatistics(TEST_USER_ID);

        // then
        assertNotNull(result);
        assertEquals(4, result.size());

        // 사용자 수 검증
        TotalCntAndRateItem totalUsers = result.get("totalUsers");
        assertNotNull(totalUsers);
        assertEquals(1000L, totalUsers.getValue());
        assertEquals(5.5, totalUsers.getChangeRate(), 0.001);

        // 디바이스 수 검증
        TotalCntAndRateItem totalDevices = result.get("totalDevices");
        assertNotNull(totalDevices);
        assertEquals(1500L, totalDevices.getValue());
        assertEquals(7.2, totalDevices.getChangeRate(), 0.001);

        // 주행 수 검증
        TotalCntAndRateItem totalDrives = result.get("totalDrives");
        assertNotNull(totalDrives);
        assertEquals(3000L, totalDrives.getValue());
        assertEquals(10.8, totalDrives.getChangeRate(), 0.001);

        // 리워드 검증
        TotalCntAndRateItem totalIssuedRewards = result.get("totalIssuedRewards");
        assertNotNull(totalIssuedRewards);
        assertEquals(5000L, totalIssuedRewards.getValue());
        assertEquals(3.2, totalIssuedRewards.getChangeRate(), 0.001);

        // 각 서비스 메서드 호출 검증
        verify(userFetchService).fetchUserTotalCountAndChangeRate();
        verify(userFetchService).fetchDevicesTotalCountAndChangeRate();
        verify(dashboardFetchService).fetchDriveTotalCntAndRate();
        verify(rewardFetchService).fetchTotalIssuedRewards(TEST_USER_ID);
    }

    /**
     * getDashboardStatistics 테스트 - 일부 서비스 응답이 실패할 때 대시보드 통계 조회
     * 시나리오: User 서비스의 사용자 수 조회에서 예외가 발생하고, 다른 서비스는 정상 응답하는 경우
     * 기대:
     *  - 예외가 발생한 사용자 수 통계는 기본값(value: 0, changeRate: 0.0)으로 대체됨
     *  - 정상 응답한 다른 통계 항목(totalDevices, totalDrives, totalIssuedRewards)은 원본 데이터와 일치함
     *  - 반환된 Map에 모든 필요한 통계 항목이 포함되며, 일부 실패해도 전체 결과는 반환됨
     *  - 모든 서비스의 조회 메서드가 예외 발생과 관계없이 한 번씩 호출됨
     */
    @Test
    @DisplayName("일부 서비스 응답이 null일 때 대시보드 통계 조회")
    void getDashboardStatistics_whenUserServiceThrowsException_shouldHandleGracefully() {
        // given
        // 사용자 수 통계 Mock 데이터 (예외 발생)
        when(userFetchService.fetchUserTotalCountAndChangeRate())
                .thenThrow(new RestApiException(ErrorCode.FEIGN_DATA_MISSING));

        // 디바이스 수 통계 Mock 데이터
        UCTotalCntAndRateItem deviceCountItem = new UCTotalCntAndRateItem();
        deviceCountItem.setValue(1500L);
        deviceCountItem.setChangeRate(7.2);
        when(userFetchService.fetchDevicesTotalCountAndChangeRate()).thenReturn(deviceCountItem);

        // 주행 수 통계 Mock 데이터
        DCTotalCntAndRateItem driveCountItem = new DCTotalCntAndRateItem();
        driveCountItem.setValue(3000L);
        driveCountItem.setChangeRate(10.8);
        when(dashboardFetchService.fetchDriveTotalCntAndRate()).thenReturn(driveCountItem);

        // 리워드 통계 Mock 데이터
        RCRewardTotalCntAndRateItem rewardCountItem = new RCRewardTotalCntAndRateItem();
        rewardCountItem.setValue(5000L);
        rewardCountItem.setChangeRate(3.2);
        when(rewardFetchService.fetchTotalIssuedRewards(TEST_USER_ID)).thenReturn(rewardCountItem);

        // when
        Map<String, TotalCntAndRateItem> result = adminDashboardService.getDashboardStatistics(TEST_USER_ID);

        // then
        assertNotNull(result);
        assertEquals(4, result.size());

        // 예외 발생 항목에 대한 기본값 처리 검증
        TotalCntAndRateItem totalUsers = result.get("totalUsers");
        assertNotNull(totalUsers);
        assertEquals(0L, totalUsers.getValue());
        assertEquals(0.0, totalUsers.getChangeRate(), 0.001);

        // 정상 처리된 항목들 검증
        TotalCntAndRateItem totalDevices = result.get("totalDevices");
        assertNotNull(totalDevices);
        assertEquals(1500L, totalDevices.getValue());
        assertEquals(7.2, totalDevices.getChangeRate(), 0.001);

        TotalCntAndRateItem totalDrives = result.get("totalDrives");
        assertNotNull(totalDrives);
        assertEquals(3000L, totalDrives.getValue());
        assertEquals(10.8, totalDrives.getChangeRate(), 0.001);

        TotalCntAndRateItem totalIssuedRewards = result.get("totalIssuedRewards");
        assertNotNull(totalIssuedRewards);
        assertEquals(5000L, totalIssuedRewards.getValue());
        assertEquals(3.2, totalIssuedRewards.getChangeRate(), 0.001);

        // 각 서비스 메서드 호출 검증
        verify(userFetchService).fetchUserTotalCountAndChangeRate();
        verify(userFetchService).fetchDevicesTotalCountAndChangeRate();
        verify(dashboardFetchService).fetchDriveTotalCntAndRate();
        verify(rewardFetchService).fetchTotalIssuedRewards(TEST_USER_ID);
    }

    /**
     * getDashboardStatistics 테스트 - 디바이스 통계 조회 실패 시 대시보드 통계 조회
     * 시나리오: 디바이스 수 통계 조회에서 예외가 발생하고, 다른 서비스는 정상 응답하는 경우
     * 기대:
     *  - 예외가 발생한 디바이스 수 통계는 기본값(value: 0, changeRate: 0.0)으로 대체됨
     *  - 정상 응답한 다른 통계 항목(totalUsers, totalDrives, totalIssuedRewards)은 원본 데이터와 일치함
     *  - 반환된 Map에 모든 필요한 통계 항목이 포함되며, 일부 실패해도 전체 결과는 반환됨
     *  - 모든 서비스의 조회 메서드가 예외 발생과 관계없이 한 번씩 호출됨
     */
    @Test
    @DisplayName("디바이스 통계 조회 실패 시 대시보드 통계 조회")
    void getDashboardStatistics_whenDeviceServiceThrowsException_shouldHandleGracefully() {
        // given
        // 사용자 수 통계 Mock 데이터
        UCTotalCntAndRateItem userCountItem = new UCTotalCntAndRateItem();
        userCountItem.setValue(1000L);
        userCountItem.setChangeRate(5.5);
        when(userFetchService.fetchUserTotalCountAndChangeRate()).thenReturn(userCountItem);

        // 디바이스 수 통계 Mock 데이터 (예외 발생)
        when(userFetchService.fetchDevicesTotalCountAndChangeRate())
                .thenThrow(new RestApiException(ErrorCode.FEIGN_DATA_MISSING));

        // 주행 수 통계 Mock 데이터
        DCTotalCntAndRateItem driveCountItem = new DCTotalCntAndRateItem();
        driveCountItem.setValue(3000L);
        driveCountItem.setChangeRate(10.8);
        when(dashboardFetchService.fetchDriveTotalCntAndRate()).thenReturn(driveCountItem);

        // 리워드 통계 Mock 데이터
        RCRewardTotalCntAndRateItem rewardCountItem = new RCRewardTotalCntAndRateItem();
        rewardCountItem.setValue(5000L);
        rewardCountItem.setChangeRate(3.2);
        when(rewardFetchService.fetchTotalIssuedRewards(TEST_USER_ID)).thenReturn(rewardCountItem);

        // when
        Map<String, TotalCntAndRateItem> result = adminDashboardService.getDashboardStatistics(TEST_USER_ID);

        // then
        assertNotNull(result);
        assertEquals(4, result.size());

        // 정상 처리된 사용자 통계 검증
        TotalCntAndRateItem totalUsers = result.get("totalUsers");
        assertNotNull(totalUsers);
        assertEquals(1000L, totalUsers.getValue());
        assertEquals(5.5, totalUsers.getChangeRate(), 0.001);

        // 예외 발생 항목에 대한 기본값 처리 검증
        TotalCntAndRateItem totalDevices = result.get("totalDevices");
        assertNotNull(totalDevices);
        assertEquals(0L, totalDevices.getValue());
        assertEquals(0.0, totalDevices.getChangeRate(), 0.001);

        // 정상 처리된 나머지 항목들 검증
        TotalCntAndRateItem totalDrives = result.get("totalDrives");
        assertNotNull(totalDrives);
        assertEquals(3000L, totalDrives.getValue());
        assertEquals(10.8, totalDrives.getChangeRate(), 0.001);

        TotalCntAndRateItem totalIssuedRewards = result.get("totalIssuedRewards");
        assertNotNull(totalIssuedRewards);
        assertEquals(5000L, totalIssuedRewards.getValue());
        assertEquals(3.2, totalIssuedRewards.getChangeRate(), 0.001);

        // 각 서비스 메서드 호출 검증
        verify(userFetchService).fetchUserTotalCountAndChangeRate();
        verify(userFetchService).fetchDevicesTotalCountAndChangeRate();
        verify(dashboardFetchService).fetchDriveTotalCntAndRate();
        verify(rewardFetchService).fetchTotalIssuedRewards(TEST_USER_ID);
    }

    /**
     * getDashboardStatistics 테스트 - 주행 통계 조회 실패 시 대시보드 통계 조회
     * 시나리오: 주행 수 통계 조회에서 예외가 발생하고, 다른 서비스는 정상 응답하는 경우
     * 기대:
     *  - 예외가 발생한 주행 수 통계는 기본값(value: 0, changeRate: 0.0)으로 대체됨
     *  - 정상 응답한 다른 통계 항목(totalUsers, totalDevices, totalIssuedRewards)은 원본 데이터와 일치함
     *  - 반환된 Map에 모든 필요한 통계 항목이 포함되며, 일부 실패해도 전체 결과는 반환됨
     *  - 모든 서비스의 조회 메서드가 예외 발생과 관계없이 한 번씩 호출됨
     */
    @Test
    @DisplayName("주행 통계 조회 실패 시 대시보드 통계 조회")
    void getDashboardStatistics_whenDriveServiceThrowsException_shouldHandleGracefully() {
        // given
        // 사용자 수 통계 Mock 데이터
        UCTotalCntAndRateItem userCountItem = new UCTotalCntAndRateItem();
        userCountItem.setValue(1000L);
        userCountItem.setChangeRate(5.5);
        when(userFetchService.fetchUserTotalCountAndChangeRate()).thenReturn(userCountItem);

        // 디바이스 수 통계 Mock 데이터
        UCTotalCntAndRateItem deviceCountItem = new UCTotalCntAndRateItem();
        deviceCountItem.setValue(1500L);
        deviceCountItem.setChangeRate(7.2);
        when(userFetchService.fetchDevicesTotalCountAndChangeRate()).thenReturn(deviceCountItem);

        // 주행 수 통계 Mock 데이터 (예외 발생)
        when(dashboardFetchService.fetchDriveTotalCntAndRate())
                .thenThrow(new RestApiException(ErrorCode.FEIGN_DATA_MISSING));

        // 리워드 통계 Mock 데이터
        RCRewardTotalCntAndRateItem rewardCountItem = new RCRewardTotalCntAndRateItem();
        rewardCountItem.setValue(5000L);
        rewardCountItem.setChangeRate(3.2);
        when(rewardFetchService.fetchTotalIssuedRewards(TEST_USER_ID)).thenReturn(rewardCountItem);

        // when
        Map<String, TotalCntAndRateItem> result = adminDashboardService.getDashboardStatistics(TEST_USER_ID);

        // then
        assertNotNull(result);
        assertEquals(4, result.size());

        // 정상 처리된 항목들 검증
        TotalCntAndRateItem totalUsers = result.get("totalUsers");
        assertNotNull(totalUsers);
        assertEquals(1000L, totalUsers.getValue());
        assertEquals(5.5, totalUsers.getChangeRate(), 0.001);

        TotalCntAndRateItem totalDevices = result.get("totalDevices");
        assertNotNull(totalDevices);
        assertEquals(1500L, totalDevices.getValue());
        assertEquals(7.2, totalDevices.getChangeRate(), 0.001);

        // 예외 발생 항목에 대한 기본값 처리 검증
        TotalCntAndRateItem totalDrives = result.get("totalDrives");
        assertNotNull(totalDrives);
        assertEquals(0L, totalDrives.getValue());
        assertEquals(0.0, totalDrives.getChangeRate(), 0.001);

        // 정상 처리된 리워드 항목 검증
        TotalCntAndRateItem totalIssuedRewards = result.get("totalIssuedRewards");
        assertNotNull(totalIssuedRewards);
        assertEquals(5000L, totalIssuedRewards.getValue());
        assertEquals(3.2, totalIssuedRewards.getChangeRate(), 0.001);

        // 각 서비스 메서드 호출 검증
        verify(userFetchService).fetchUserTotalCountAndChangeRate();
        verify(userFetchService).fetchDevicesTotalCountAndChangeRate();
        verify(dashboardFetchService).fetchDriveTotalCntAndRate();
        verify(rewardFetchService).fetchTotalIssuedRewards(TEST_USER_ID);
    }

    /**
     * getDashboardStatistics 테스트 - 리워드 통계 조회 실패 시 대시보드 통계 조회
     * 시나리오: 리워드 통계 조회에서 예외가 발생하고, 다른 서비스는 정상 응답하는 경우
     * 기대:
     *  - 예외가 발생한 리워드 통계는 기본값(value: 0, changeRate: 0.0)으로 대체됨
     *  - 정상 응답한 다른 통계 항목(totalUsers, totalDevices, totalDrives)은 원본 데이터와 일치함
     *  - 반환된 Map에 모든 필요한 통계 항목이 포함되며, 일부 실패해도 전체 결과는 반환됨
     *  - 모든 서비스의 조회 메서드가 예외 발생과 관계없이 한 번씩 호출됨
     */
    @Test
    @DisplayName("리워드 통계 조회 실패 시 대시보드 통계 조회")
    void getDashboardStatistics_whenRewardServiceThrowsException_shouldHandleGracefully() {
        // given
        // 사용자 수 통계 Mock 데이터
        UCTotalCntAndRateItem userCountItem = new UCTotalCntAndRateItem();
        userCountItem.setValue(1000L);
        userCountItem.setChangeRate(5.5);
        when(userFetchService.fetchUserTotalCountAndChangeRate()).thenReturn(userCountItem);

        // 디바이스 수 통계 Mock 데이터
        UCTotalCntAndRateItem deviceCountItem = new UCTotalCntAndRateItem();
        deviceCountItem.setValue(1500L);
        deviceCountItem.setChangeRate(7.2);
        when(userFetchService.fetchDevicesTotalCountAndChangeRate()).thenReturn(deviceCountItem);

        // 주행 수 통계 Mock 데이터
        DCTotalCntAndRateItem driveCountItem = new DCTotalCntAndRateItem();
        driveCountItem.setValue(3000L);
        driveCountItem.setChangeRate(10.8);
        when(dashboardFetchService.fetchDriveTotalCntAndRate()).thenReturn(driveCountItem);

        // 리워드 통계 Mock 데이터 (예외 발생)
        when(rewardFetchService.fetchTotalIssuedRewards(TEST_USER_ID))
                .thenThrow(new RestApiException(ErrorCode.FEIGN_DATA_MISSING));

        // when
        Map<String, TotalCntAndRateItem> result = adminDashboardService.getDashboardStatistics(TEST_USER_ID);

        // then
        assertNotNull(result);
        assertEquals(4, result.size());

        // 정상 처리된 항목들 검증
        TotalCntAndRateItem totalUsers = result.get("totalUsers");
        assertNotNull(totalUsers);
        assertEquals(1000L, totalUsers.getValue());
        assertEquals(5.5, totalUsers.getChangeRate(), 0.001);

        TotalCntAndRateItem totalDevices = result.get("totalDevices");
        assertNotNull(totalDevices);
        assertEquals(1500L, totalDevices.getValue());
        assertEquals(7.2, totalDevices.getChangeRate(), 0.001);

        TotalCntAndRateItem totalDrives = result.get("totalDrives");
        assertNotNull(totalDrives);
        assertEquals(3000L, totalDrives.getValue());
        assertEquals(10.8, totalDrives.getChangeRate(), 0.001);

        // 예외 발생 항목에 대한 기본값 처리 검증
        TotalCntAndRateItem totalIssuedRewards = result.get("totalIssuedRewards");
        assertNotNull(totalIssuedRewards);
        assertEquals(0L, totalIssuedRewards.getValue());
        assertEquals(0.0, totalIssuedRewards.getChangeRate(), 0.001);

        // 각 서비스 메서드 호출 검증
        verify(userFetchService).fetchUserTotalCountAndChangeRate();
        verify(userFetchService).fetchDevicesTotalCountAndChangeRate();
        verify(dashboardFetchService).fetchDriveTotalCntAndRate();
        verify(rewardFetchService).fetchTotalIssuedRewards(TEST_USER_ID);
    }
}