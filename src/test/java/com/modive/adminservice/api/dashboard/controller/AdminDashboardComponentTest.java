package com.modive.adminservice.api.dashboard.controller;

import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.api.dashboard.service.AdminDashboardService;
import com.modive.adminservice.external.analysis.service.AnalysisFetchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import jakarta.servlet.ServletException;
import java.util.List;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
@AutoConfigureMockMvc
public class AdminDashboardComponentTest {

    @Autowired
    private MockMvc mockMvc;

//    @TestConfiguration
//    static class MockServiceConfig {
//        @Bean
//        public AdminDashboardService adminDashboardService() {
//            return Mockito.mock(AdminDashboardService.class);
//        }
//    }

    @MockitoBean
    private AdminDashboardService adminDashboardService;

    @MockitoBean
    private AnalysisFetchService analysisFetchService;

    private ResultActions performGet(String url) throws Exception {
        return mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON));
    }

    /**
     * getMonthlyUsersStatistics API 테스트 - 사용자 증감 통계 정상 응답
     * 시나리오: totalUsers와 monthlyGrowth 데이터가 포함된 정상 응답을 반환하는 경우
     * 기대: status 200, message 응답 확인, JSON 필드 구조 및 값 검증
     */
    @Test
    void getMonthlyUsersStatistics_Success() throws Exception {
        // Given
        Map<String, Object> mockUserStats = new HashMap<>();
        mockUserStats.put("totalUsers", 1000);
        mockUserStats.put("monthlyGrowth", Arrays.asList(
                Map.of("month", "2025-01", "count", 100),
                Map.of("month", "2025-02", "count", 150),
                Map.of("month", "2025-03", "count", 200)
        ));

        when(adminDashboardService.getMonthlyUserStatistics())
                .thenReturn(mockUserStats);

        // When & Then
        performGet("/admin/dashboard/users/monthly-stats")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("사용자 증감 추이 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.userStatistics.totalUsers").value(1000))
                .andExpect(jsonPath("$.data.userStatistics.monthlyGrowth").isArray())
                .andExpect(jsonPath("$.data.userStatistics.monthlyGrowth[0].month").value("2025-01"))
                .andExpect(jsonPath("$.data.userStatistics.monthlyGrowth[0].count").value(100));
    }

    /**
     * getMonthlyDrivesStatistics API 테스트 - 운전 통계 정상 응답
     * 시나리오: 월별 운전 횟수 데이터를 포함하는 리스트가 정상적으로 반환되는 경우
     * 기대: status 200, message 응답 확인, 각 연/월/주행수 값 검증
     */
    @Test
    void getMonthlyDrivesStatistics_Success() throws Exception {
        // Given
        List<MonthlyDrivesItem> mockStats = List.of(
                new MonthlyDrivesItem(2025, 5, 150L),
                new MonthlyDrivesItem(2025, 6, 200L)
        );

        when(adminDashboardService.getMonthlyDrivesStatistics())
                .thenReturn(mockStats);

        // When & Then
        mockMvc.perform(get("/admin/dashboard/drives/monthly-stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("월별 운전 횟수 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].month").value(5))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].count").value(150))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].month").value(6))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].count").value(200));
    }


    /**
     * getMonthlyDrivesStatistics API 테스트 - 내부 서버 오류 시 예외 처리
     * 시나리오: adminDashboardService 내부에서 RuntimeException 발생
     * 기대: ServletException으로 wrapping 되어 전달되고, 원인 메시지 검증 가능
     */
    @Test
    void getMonthlyDrivesStatistics_InternalServerError() throws Exception {
        // Given
        doThrow(new RuntimeException("내부 서버 오류"))
                .when(adminDashboardService).getMonthlyDrivesStatistics();

        // When & Then
        Exception exception = assertThrows(
                ServletException.class,
                () -> mockMvc.perform(get("/admin/dashboard/drives/monthly-stats"))
        );

        assertTrue(exception.getCause() instanceof RuntimeException);
        assertEquals("내부 서버 오류", exception.getCause().getMessage());
    }


    /**
     * getEventsByReason 통합 테스트 - 이벤트 유형별 추이 조회
     * 시나리오: 이벤트 유형별 발생 횟수를 mock 데이터로 주입
     * 기대: status 200, message 정상, 이벤트 유형별 발생 횟수가 정확히 반환됨
     */
    @Test
    @DisplayName("이벤트 유형별 추이 조회 통합 테스트")
    void getEventsByReason_Integration_ShouldReturnEventsStatisticsByReason() throws Exception {
        // given
        List<TotalEventCntByReasonItem> mockEventItems = Arrays.asList(
                TotalEventCntByReasonItem.builder()
                        .reason("SPEEDING")
                        .count(150L)
                        .build(),
                TotalEventCntByReasonItem.builder()
                        .reason("SUDDEN_ACCELERATION")
                        .count(80L)
                        .build(),
                TotalEventCntByReasonItem.builder()
                        .reason("SUDDEN_BRAKING")
                        .count(120L)
                        .build()
        );

        when(analysisFetchService.getTotalEventCntByType()).thenReturn(mockEventItems);

        // when & then
        mockMvc.perform(get("/admin/dashboard/events/by-reason/total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("이벤트 유형별 추이 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.eventsStatisticsByReason[0].reason").value("SPEEDING"))
                .andExpect(jsonPath("$.data.eventsStatisticsByReason[0].count").value(150))
                .andExpect(jsonPath("$.data.eventsStatisticsByReason[1].reason").value("SUDDEN_ACCELERATION"))
                .andExpect(jsonPath("$.data.eventsStatisticsByReason[1].count").value(80))
                .andExpect(jsonPath("$.data.eventsStatisticsByReason[2].reason").value("SUDDEN_BRAKING"))
                .andExpect(jsonPath("$.data.eventsStatisticsByReason[2].count").value(120));
    }

    /**
     * getSummary 통합 테스트 - 대시보드 통계 조회
     * 시나리오: 대시보드 상단 통계 데이터를 mock 데이터로 주입
     * 기대: status 200, message 정상, 대시보드 통계 데이터가 정확히 반환됨
     */
    @Test
    @DisplayName("대시보드 통계 조회 통합 테스트")
    void getSummary_Integration_ShouldReturnDashboardStatistics() throws Exception {
        // given
        String userId = "user123";

        Map<String, TotalCntAndRateItem> mockDashboardStats = new HashMap<>();
        mockDashboardStats.put("users", TotalCntAndRateItem.builder()
                .value(1500L)
                .changeRate(5.5)
                .build());
        mockDashboardStats.put("drives", TotalCntAndRateItem.builder()
                .value(3200L)
                .changeRate(12.8)
                .build());
        mockDashboardStats.put("events", TotalCntAndRateItem.builder()
                .value(850L)
                .changeRate(-2.3)
                .build());

        when(adminDashboardService.getDashboardStatistics(userId)).thenReturn(mockDashboardStats);

        // when & then
        mockMvc.perform(get("/admin/dashboard/summary")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-USER-ID", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("대시 보드 통계 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.dashboardStatistics.users.value").value(1500))
                .andExpect(jsonPath("$.data.dashboardStatistics.users.changeRate").value(5.5))
                .andExpect(jsonPath("$.data.dashboardStatistics.drives.value").value(3200))
                .andExpect(jsonPath("$.data.dashboardStatistics.drives.changeRate").value(12.8))
                .andExpect(jsonPath("$.data.dashboardStatistics.events.value").value(850))
                .andExpect(jsonPath("$.data.dashboardStatistics.events.changeRate").value(-2.3));
    }
}