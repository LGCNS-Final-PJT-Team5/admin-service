package com.modive.adminservice.external.dashboard.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.*;


import com.modive.adminservice.external.dashboard.client.DashBoardClient;
import com.modive.adminservice.external.dashboard.dto.res.*;
import com.modive.adminservice.external.dashboard.service.impl.DashboardFetchServiceImpl;
import com.modive.adminservice.global.dto.res.CommonRes;
import com.modive.adminservice.global.error.code.ErrorCode;
import com.modive.adminservice.global.error.exception.RestApiException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DashboardFetchServiceTest {

    @Mock
    private DashBoardClient dashBoardClient;

    @InjectMocks
    private DashboardFetchServiceImpl dashboardFetchService;

//    @BeforeEach
//    void setUp() {
//        // 테스트 준비 작업이 필요하다면 여기에 작성
//    }

    /**
     * fetchDriveCountMap(List<String> userIds) 테스트 - 정상 케이스
     * 시나리오: 사용자 ID 목록을 입력으로 각 사용자의 운전 횟수를 조회하는 경우
     * 기대:
     *  - DashBoardClient.getDriveCountByUser 메서드가 정확한 파라미터로 1회 호출됨
     *  - 사용자 ID를 키로, 운전 횟수를 값으로 하는 Map이 정확하게 반환됨
     */
    @Test
    @DisplayName("fetchDriveCountMap 메서드는 사용자 ID 목록을 받아 운전 횟수 맵을 반환해야 함")
    void fetchDriveCountMap_ShouldReturnDriveCountMap() {
        // Given
        List<String> userIds = Arrays.asList("user1", "user2", "user3");

        DCDriveCountItem item1 = new DCDriveCountItem("user1", 10);
        DCDriveCountItem item2 = new DCDriveCountItem("user2", 5);
        DCDriveCountItem item3 = new DCDriveCountItem("user3", 15);

        DCDriveCountResData resData = new DCDriveCountResData();
        resData.setDriveCountByUser(Arrays.asList(item1, item2, item3));

        CommonRes<DCDriveCountResData> response = new CommonRes<>();
        response.setData(resData);

        // When
        ArgumentCaptor<Map<String, List<String>>> bodyCaptor = ArgumentCaptor.forClass(Map.class);
        when(dashBoardClient.getDriveCountByUser(bodyCaptor.capture())).thenReturn(response);

        Map<String, Integer> result = dashboardFetchService.fetchDriveCountMap(userIds);

        // Then
        Map<String, List<String>> capturedBody = bodyCaptor.getValue();
        assertEquals(userIds, capturedBody.get("userIds"));

        assertEquals(3, result.size());
        assertEquals(10, result.get("user1"));
        assertEquals(5, result.get("user2"));
        assertEquals(15, result.get("user3"));
    }

    /**
     * fetchDriveCountMap(List<String> userIds) 테스트 - 응답이 null인 경우
     * 시나리오: DashBoardClient.getDriveCountByUser 메서드가 null을 반환하는 경우
     * 기대:
     *  - RestApiException이 발생하며 ErrorCode.FEIGN_DATA_MISSING 에러 코드가 포함됨
     */
    @Test
    @DisplayName("fetchDriveCountMap 메서드는 응답이 null일 때 예외를 던져야 함")
    void fetchDriveCountMap_ShouldThrowException_WhenResponseIsNull() {
        // Given
        List<String> userIds = Arrays.asList("user1", "user2");
        when(dashBoardClient.getDriveCountByUser(any())).thenReturn(null);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> dashboardFetchService.fetchDriveCountMap(userIds));
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }

    /**
     * fetchDriveCountMap(List<String> userIds) 테스트 - 응답의 data가 null인 경우
     * 시나리오: DashBoardClient.getDriveCountByUser 메서드의 응답에서 data 필드가 null인 경우
     * 기대:
     *  - RestApiException이 발생하며 ErrorCode.FEIGN_DATA_MISSING 에러 코드가 포함됨
     */
    @Test
    @DisplayName("fetchDriveCountMap 메서드는 응답의 data가 null일 때 예외를 던져야 함")
    void fetchDriveCountMap_ShouldThrowException_WhenDataIsNull() {
        // Given
        List<String> userIds = Arrays.asList("user1", "user2");
        CommonRes<DCDriveCountResData> response = new CommonRes<>();
        response.setData(null);

        when(dashBoardClient.getDriveCountByUser(any())).thenReturn(response);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> dashboardFetchService.fetchDriveCountMap(userIds));
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }

    /**
     * fetchDriveListByUserId(String userId, int pageSize, String startTime, String driveId) 테스트 - 정상 케이스
     * 시나리오: 특정 사용자의 운전 내역 리스트를 커서 기반으로 조회하는 경우
     * 기대:
     *  - DashBoardClient.getDrivesByUserId 메서드가 정확한 파라미터로 1회 호출됨
     *  - 클라이언트로부터 받은 응답 데이터가 그대로 반환됨
     */
    @Test
    @DisplayName("fetchDriveListByUserId 메서드는 유효한 응답을 반환해야 함")
    void fetchDriveListByUserId_ShouldReturnValidResponse() {
        // Given
        String userId = "user1";
        int pageSize = 10;
        String startTime = "2023-01-01";
        String driveId = "drive123";

        // 클래스 직접 생성 대신 모킹 사용
        DCDriveListResData resData = mock(DCDriveListResData.class);

        CommonRes<DCDriveListResData> response = new CommonRes<>();
        response.setData(resData);

        when(dashBoardClient.getDrivesByUserId(userId, pageSize, startTime, driveId)).thenReturn(response);

        // When
        DCDriveListResData result = dashboardFetchService.fetchDriveListByUserId(userId, pageSize, startTime, driveId);

        // Then
        assertSame(resData, result);
        verify(dashBoardClient, times(1)).getDrivesByUserId(userId, pageSize, startTime, driveId);
    }

    /**
     * fetchDriveListByUserId(String userId, int pageSize, String startTime, String driveId) 테스트 - 응답이 null인 경우
     * 시나리오: DashBoardClient.getDrivesByUserId 메서드가 null을 반환하는 경우
     * 기대:
     *  - RestApiException이 발생하며 ErrorCode.FEIGN_DATA_MISSING 에러 코드가 포함됨
     */
    @Test
    @DisplayName("fetchDriveListByUserId 메서드는 응답이 null일 때 예외를 던져야 함")
    void fetchDriveListByUserId_ShouldThrowException_WhenResponseIsNull() {
        // Given
        String userId = "user1";
        int pageSize = 10;
        String startTime = "2023-01-01";
        String driveId = "drive123";

        when(dashBoardClient.getDrivesByUserId(userId, pageSize, startTime, driveId)).thenReturn(null);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> dashboardFetchService.fetchDriveListByUserId(userId, pageSize, startTime, driveId));
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }

    /**
     * fetchDriveTotalCntAndRate() 테스트 - 정상 케이스
     * 시나리오: 누적 주행 횟수와 증감률을 조회하는 경우
     * 기대:
     *  - DashBoardClient.getTotalDriveCount 메서드가 1회 호출됨
     *  - 클라이언트로부터 받은 응답 데이터가 그대로 반환됨
     */
    @Test
    @DisplayName("fetchDriveTotalCntAndRate 메서드는 유효한 응답을 반환해야 함")
    void fetchDriveTotalCntAndRate_ShouldReturnValidResponse() {
        // Given
        DCTotalCntAndRateItem totalItem = new DCTotalCntAndRateItem();
        // totalItem 설정...

        DCTotalDriveResData resData = new DCTotalDriveResData();
        resData.setTotalDrives(totalItem);

        CommonRes<DCTotalDriveResData> response = new CommonRes<>();
        response.setData(resData);

        when(dashBoardClient.getTotalDriveCount()).thenReturn(response);

        // When
        DCTotalCntAndRateItem result = dashboardFetchService.fetchDriveTotalCntAndRate();

        // Then
        assertSame(totalItem, result);
        verify(dashBoardClient, times(1)).getTotalDriveCount();
    }

    /**
     * fetchDriveTotalCntAndRate() 테스트 - 응답이 null인 경우
     * 시나리오: DashBoardClient.getTotalDriveCount 메서드가 null을 반환하는 경우
     * 기대:
     *  - RestApiException이 발생하며 ErrorCode.FEIGN_DATA_MISSING 에러 코드가 포함됨
     */
    @Test
    @DisplayName("fetchDriveTotalCntAndRate 메서드는 응답이 null일 때 예외를 던져야 함")
    void fetchDriveTotalCntAndRate_ShouldThrowException_WhenResponseIsNull() {
        // Given
        when(dashBoardClient.getTotalDriveCount()).thenReturn(null);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> dashboardFetchService.fetchDriveTotalCntAndRate());
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }

    /**
     * fetchMonthlyDrivesStatistics() 테스트 - 정상 케이스
     * 시나리오: 12개월치 운전 횟수 통계를 조회하는 경우
     * 기대:
     *  - DashBoardClient.getMonthlyStats 메서드가 1회 호출됨
     *  - 클라이언트로부터 받은 월별 통계 리스트가 그대로 반환됨
     */
    @Test
    @DisplayName("fetchMonthlyDrivesStatistics 메서드는 유효한 응답을 반환해야 함")
    void fetchMonthlyDrivesStatistics_ShouldReturnValidResponse() {
        // Given
        List<DCMonthlyDriveItem> monthlyItems = Arrays.asList(
                new DCMonthlyDriveItem(),
                new DCMonthlyDriveItem()
        );

        DCMontlyDriveResData resData = new DCMontlyDriveResData();
        resData.setMonthlyDrivesStatistics(monthlyItems);

        CommonRes<DCMontlyDriveResData> response = new CommonRes<>();
        response.setData(resData);

        when(dashBoardClient.getMonthlyStats()).thenReturn(response);

        // When
        List<DCMonthlyDriveItem> result = dashboardFetchService.fetchMonthlyDrivesStatistics();

        // Then
        assertSame(monthlyItems, result);
        verify(dashBoardClient, times(1)).getMonthlyStats();
    }

    /**
     * fetchMonthlyDrivesStatistics() 테스트 - 응답이 null인 경우
     * 시나리오: DashBoardClient.getMonthlyStats 메서드가 null을 반환하는 경우
     * 기대:
     *  - RestApiException이 발생하며 ErrorCode.FEIGN_DATA_MISSING 에러 코드가 포함됨
     */
    @Test
    @DisplayName("fetchMonthlyDrivesStatistics 메서드는 응답이 null일 때 예외를 던져야 함")
    void fetchMonthlyDrivesStatistics_ShouldThrowException_WhenResponseIsNull() {
        // Given
        when(dashBoardClient.getMonthlyStats()).thenReturn(null);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> dashboardFetchService.fetchMonthlyDrivesStatistics());
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }

    /**
     * fetchMonthlyDrivesStatistics() 테스트 - 응답의 data가 null인 경우
     * 시나리오: DashBoardClient.getMonthlyStats 메서드의 응답에서 data 필드가 null인 경우
     * 기대:
     *  - RestApiException이 발생하며 ErrorCode.FEIGN_DATA_MISSING 에러 코드가 포함됨
     */
    @Test
    @DisplayName("fetchMonthlyDrivesStatistics 메서드는 응답의 data가 null일 때 예외를 던져야 함")
    void fetchMonthlyDrivesStatistics_ShouldThrowException_WhenDataIsNull() {
        // Given
        CommonRes<DCMontlyDriveResData> response = new CommonRes<>();
        response.setData(null);

        when(dashBoardClient.getMonthlyStats()).thenReturn(response);

        // When & Then
        RestApiException exception = assertThrows(RestApiException.class,
                () -> dashboardFetchService.fetchMonthlyDrivesStatistics());
        assertEquals(ErrorCode.FEIGN_DATA_MISSING, exception.getErrorCode());
    }
}