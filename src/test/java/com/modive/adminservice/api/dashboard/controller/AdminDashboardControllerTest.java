package com.modive.adminservice.api.dashboard.controller;
import com.modive.adminservice.api.dashboard.dto.res.MonthlyDrivesItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalCntAndRateItem;
import com.modive.adminservice.api.dashboard.dto.res.TotalEventCntByReasonItem;
import com.modive.adminservice.api.dashboard.service.AdminDashboardService;
import com.modive.adminservice.external.analysis.service.AnalysisFetchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminDashboardControllerTest {

    @Mock
    private AdminDashboardService adminDashboardService;

    @Mock
    private AnalysisFetchService analysisFetchService;


    @InjectMocks
    private AdminDashboardController adminDashboardController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminDashboardController).build();
    }

    /**
     * getMonthlyDrivesStatistics 테스트 - 정상 응답
     * 시나리오: 월별 운전 통계를 2개월치 mock 데이터로 주입
     * 기대: status 200, message 정상, 각 필드(year, month, count) 정확히 반환됨
     */
    @Test
    void getMonthlyDrivesStatistics_ShouldReturnMonthlyDrivesStatistics() throws Exception {
        List<MonthlyDrivesItem> mockMonthlyDrivesStatistics = Arrays.asList(
                MonthlyDrivesItem.builder()
                        .year(2025)
                        .month(1)
                        .count(34L)
                        .build(),
                MonthlyDrivesItem.builder()
                        .year(2025)
                        .month(2)
                        .count(28L)
                        .build()
        );

        when(adminDashboardService.getMonthlyDrivesStatistics()).thenReturn(mockMonthlyDrivesStatistics);

        mockMvc.perform(get("/admin/dashboard/drives/monthly-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("월별 운전 횟수 조회에 성공했습니다."))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].month").value(1))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[0].count").value(34))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].year").value(2025))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].month").value(2))
                .andExpect(jsonPath("$.data.monthlyDrivesStatistics[1].count").value(28));
    }

    /**
     * getMonthlyUsersStatistics 테스트 - 정상 응답
     * 시나리오: 사용자 증가 추이 통계를 Map 형식으로 주입
     * 기대: status 200, message 정상, 각 키("2025-01", "2025-02")에 해당 값 정확히 반환됨
     */
    @Test
    void getMonthlyUsersStatistics_ShouldReturnMonthlyUsersStatistics() throws Exception {
        Map<String, Object> mockUserStatistics = Map.of(
                "2025-01", 120,
                "2025-02", 150
        );

        when(adminDashboardService.getMonthlyUserStatistics()).thenReturn(mockUserStatistics);

        mockMvc.perform(get("/admin/dashboard/users/monthly-stats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.message").value("사용자 증감 추이 조회에 성공하였습니다."))
                .andExpect(jsonPath("$.data.userStatistics['2025-01']").value(120))
                .andExpect(jsonPath("$.data.userStatistics['2025-02']").value(150));
    }

    /**
     * getEventsByReason 테스트 - 정상 응답
     * 시나리오: 이벤트 유형별 발생 횟수를 mock 데이터로 주입
     * 기대: status 200, message 정상, 이벤트 유형별 발생 횟수가 정확히 반환됨
     */
    @Test
    void getEventsByReason_ShouldReturnEventsStatisticsByReason() throws Exception {
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
     * getSummary 테스트 - 정상 응답
     * 시나리오: 대시보드 상단 통계 데이터를 mock 데이터로 주입
     * 기대: status 200, message 정상, 대시보드 통계 데이터가 정확히 반환됨
     */
    @Test
    void getSummary_ShouldReturnDashboardStatistics() throws Exception {
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